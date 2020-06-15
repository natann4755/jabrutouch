package co.il.jabrutouch.ui.main.mishna_screen.fragments;

import android.content.Context;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import java.util.Objects;
import co.il.jabrutouch.R;
import co.il.model.model.masechet_list_model.ChaptersItem;
import co.il.model.model.masechet_list_model.MasechetItem;
import co.il.jabrutouch.ui.main.mishna_screen.adapters.MishnaChapterAdapter;



public class MishnaChapterFragment extends Fragment implements MishnaChapterAdapter.MishnaChapterAdapterListener {

    public static final String TAG = MishnaChapterFragment.class.getSimpleName();
    private static final String MASECHET = "MASECHET";
    private static final String SEDER_ORDER = "SEDER_ORDER";
    private OnMishnaChapterFragmentListener mListener;
    private TextView mChapterTitle;
    private RecyclerView mChapterRecyclerView;
    private MasechetItem mMasechet;
    private MishnaChapterAdapter mMishnaChapterAdapter;
    private int mSederOrder;

    public MishnaChapterFragment() {

    }


    public static MishnaChapterFragment newInstance(MasechetItem masechet, int sederOrder) {
        MishnaChapterFragment fragment = new MishnaChapterFragment();
        Bundle args = new Bundle();
        args.putSerializable(MASECHET, masechet);
        args.putInt(SEDER_ORDER, sederOrder);
        fragment.setArguments(args);
        return fragment;
    }



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mMasechet = (MasechetItem) getArguments().getSerializable(MASECHET);
            mSederOrder =  getArguments().getInt(SEDER_ORDER);

        }
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        
        mListener.hideToolBar();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        return inflater.inflate(R.layout.fragment_mishna_chapter, container, false);
    }




    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        initViews();
        setTitle();
        setChapterRecylcer();

    }

    /**
     * init views
     */
    private void initViews() {

        mChapterRecyclerView = Objects.requireNonNull(getView()).findViewById(R.id.MCF_recycler_RV);
        mChapterTitle = getView().findViewById(R.id.MCF_title_TV);

    }



    /**
     * set title
     */
    private void setTitle() {

        mChapterTitle.setText(mMasechet.getName());

    }



    /**
     * set mishna chapter recycler view
     */
    private void setChapterRecylcer() {

        mChapterRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        mMishnaChapterAdapter = new MishnaChapterAdapter(getContext(), mMasechet,mSederOrder, this);
        mChapterRecyclerView.setAdapter(mMishnaChapterAdapter);

    }



    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnMishnaChapterFragmentListener) {
            mListener = (OnMishnaChapterFragmentListener) context;
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
    public void onMishnaChapterClicked(ChaptersItem chaptersItem, MasechetItem masechetItem, int sederOrder) {

        mListener.onMishnaChapterClicked(chaptersItem, masechetItem, sederOrder);

    }




    public interface OnMishnaChapterFragmentListener {

        void onMishnaChapterClicked(ChaptersItem chaptersItem, MasechetItem masechetItem, int sederOrder);

        void hideToolBar();
    }
}
