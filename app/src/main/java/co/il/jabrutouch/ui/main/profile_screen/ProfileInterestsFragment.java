package co.il.jabrutouch.ui.main.profile_screen;

import android.content.Context;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.os.Parcelable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import co.il.jabrutouch.R;
import co.il.model.model.IdAndNameDetailed;




public class ProfileInterestsFragment extends Fragment implements InterestsAdapter.InterestsListener {
    public static final String TAG = ProfileInterestsFragment.class.getSimpleName();
    private static final String INTEREST_LIST = "INTEREST_LIST";
    private static final String YOUR_INTEREST_LIST = "YOUR_INTEREST_LIST";


    private ProfileInterestsFragmentListener mListener;
    private RecyclerView mRecyclerView;
    private InterestsAdapter mAdapter;
    private List<IdAndNameDetailed> mInterestList = new ArrayList<>();
    private List<IdAndNameDetailed> mYourInterestList = new ArrayList<>();
    private AppCompatTextView mSaveBtnACTV;
    private ImageView mArrowBackIV;



    public ProfileInterestsFragment() {
    }



    public static ProfileInterestsFragment newInstance(List<IdAndNameDetailed> interestList, List<IdAndNameDetailed> yourInterestList) {
        ProfileInterestsFragment fragment = new ProfileInterestsFragment();
        Bundle args = new Bundle();
        args.putParcelableArrayList(INTEREST_LIST, (ArrayList<? extends Parcelable>) interestList);
        args.putParcelableArrayList(YOUR_INTEREST_LIST, (ArrayList<? extends Parcelable>) yourInterestList);
        fragment.setArguments(args);
        return fragment;
    }



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mInterestList = getArguments().getParcelableArrayList(INTEREST_LIST);
            mYourInterestList.clear();
            mYourInterestList.addAll(Objects.requireNonNull(getArguments().<IdAndNameDetailed>getParcelableArrayList(YOUR_INTEREST_LIST)));
        }
    }




    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_profile_interests, container, false);
    }



    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        initViews();
        intListeners();
        setTheAdapter();


    }




    private void initViews() {

        mRecyclerView = getView().findViewById(R.id.PIF_recycler_RV);
        mSaveBtnACTV = getView().findViewById(R.id.FPI_save_ACTV);
        mArrowBackIV = getView().findViewById(R.id.FPI_back_arrow_IV);

    }




    private void intListeners() {

        mArrowBackIV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setViewClickedAnimation(view);
                mListener.onArrowBackClicked();
            }
        });

        mSaveBtnACTV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setViewClickedAnimation(view);
                mListener.onSaveClicked(mYourInterestList);

            }
        });

    }





    private void setViewClickedAnimation(View mView) {

        Animation animation = AnimationUtils.loadAnimation(getContext(), R.anim.alpha);
        mView.startAnimation(animation);

    }





    private void setTheAdapter() {

        for (int i = 0; i < mInterestList.size(); i++) {

            for (int j = 0; j < mYourInterestList.size(); j++) {

                if (mInterestList.get(i).getId() == mYourInterestList.get(j).getId()){
                    mInterestList.get(i).setIsSelected(true);
                }
            }
        }

        mRecyclerView.setLayoutManager(new GridLayoutManager(getContext(), 3));
        mAdapter = new InterestsAdapter(getContext(), mInterestList, this, true);
        mRecyclerView.setAdapter(mAdapter);
    }





    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof ProfileInterestsFragmentListener) {
            mListener = (ProfileInterestsFragmentListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }




    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }




    @Override
    public void onInterestAdd(IdAndNameDetailed interest) {
        mYourInterestList.add(interest);

    }




    @Override
    public void onInterestRemoved(IdAndNameDetailed interest) {

        for (int i = 0; i < mYourInterestList.size(); i++) {

            if (mYourInterestList.get(i).getId() == interest.getId()){

                mYourInterestList.remove(mYourInterestList.get(i));
            }
        }
    }




    public interface ProfileInterestsFragmentListener {

        void onSaveClicked(List<IdAndNameDetailed> yourList);

        void onArrowBackClicked();

    }

}
