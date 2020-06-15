package co.il.jabrutouch.ui.main.donation_screen;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.Objects;

import co.il.jabrutouch.R;
import co.il.model.model.User;


public class FirstDedicationViewPagerFragment extends Fragment implements View.OnClickListener {


    private static final String USER = "USER";
    private OnFirstDedicationViewPagerFragmentListener mListener;
    private User mUser;
    private AppCompatTextView mUserName;
    private AppCompatTextView mUserCountry;
    private ImageView mEditEditTextIV;
    private RelativeLayout mLinearEditNameLL;
    private RelativeLayout mLinearNameLL;
    private RelativeLayout mVNameRL;
    private EditText mEditNameET;
    private String mEditName = null;
    private TextView mUserCountryTV;

    public FirstDedicationViewPagerFragment() {
        // Required empty public constructor
    }


    public static FirstDedicationViewPagerFragment newInstance(User userFromJson) {
        FirstDedicationViewPagerFragment fragment = new FirstDedicationViewPagerFragment();
        Bundle args = new Bundle();
        args.putSerializable(USER, userFromJson);
        fragment.setArguments(args);
        return fragment;
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mUser = (User) getArguments().getSerializable(USER);

        }
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_first_dedication_view_pager, container, false);
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        initViews();
        setUserDetailes();
        initListeners();

    }


    private void initViews() {

        mUserName = Objects.requireNonNull(getView()).findViewById(R.id.FDVPF_name_ACTV);
//        mUserCountry = Objects.requireNonNull(getView()).findViewById(R.id.FDVPF_Donation_location_TV);
        mEditEditTextIV = Objects.requireNonNull(getView()).findViewById(R.id.FDVPF_edit_edittext_IV);
        mLinearEditNameLL = Objects.requireNonNull(getView()).findViewById(R.id.FFDVP_linear_edit_name_LL);
        mLinearNameLL = Objects.requireNonNull(getView()).findViewById(R.id.FFDVP_linear_name_LL);
        mVNameRL = Objects.requireNonNull(getView()).findViewById(R.id.FFDVP_v_name_RL);
        mEditNameET = Objects.requireNonNull(getView()).findViewById(R.id.FFDVP_edittext_ET);
        mUserCountryTV = Objects.requireNonNull(getView()).findViewById(R.id.FFDVP_User_location_CCP);


    }


    @SuppressLint("SetTextI18n")
    private void setUserDetailes() {

        if (mEditName == null){

            mUserName.setText(mUser.getFirstName() + " " + mUser.getLastName());
//            mUserCountry.setText(mUser.getCountry());
            mUserCountryTV.setText(mUser.getCountry());

        }else {
            mUserName.setText(mEditName);
            mUserCountryTV.setText(mUser.getCountry());


        }


    }


    private void initListeners() {

        mEditEditTextIV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                mLinearNameLL.setVisibility(View.GONE);
                mLinearEditNameLL.setVisibility(View.VISIBLE);


            }
        });


        mVNameRL.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                mLinearNameLL.setVisibility(View.VISIBLE);
                mLinearEditNameLL.setVisibility(View.GONE);

                mUserName.setText(mEditNameET.getText().toString());
                mEditName = mEditNameET.getText().toString();
                mUser.setFirstName(mEditNameET.getText().toString());
                mUser.setLastName("");

                mListener.saveEditNameForAllPager(mEditNameET.getText().toString());
            }
        });



    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFirstDedicationViewPagerFragmentListener) {
            mListener = (OnFirstDedicationViewPagerFragmentListener) context;
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
    public void onClick(View view) {

        switch (view.getId()) {



        }


    }


    public interface OnFirstDedicationViewPagerFragmentListener {

        void saveEditNameForAllPager(String s);
    }
}
