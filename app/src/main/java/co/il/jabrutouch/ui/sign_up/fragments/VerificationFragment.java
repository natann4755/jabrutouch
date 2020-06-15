package co.il.jabrutouch.ui.sign_up.fragments;

import android.content.Context;
import android.location.LocationManager;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import com.hbb20.CountryCodePicker;
import java.util.Objects;
import co.il.jabrutouch.R;



public class VerificationFragment extends Fragment implements View.OnClickListener {


    private OnVerificationFragmentListener mListener;
    private EditText mPhoneNumberET;
    private CountryCodePicker mCountryPickerCCP;
    private Button mSendBtn;

    public VerificationFragment() {
    }


    public static VerificationFragment newInstance() {
        VerificationFragment fragment = new VerificationFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


    }


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_verification, container, false);
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);


        initViews();
        initListeners();


    }




    /**
     * init views
     */
    public void initViews() {

        if (getView() != null) {

            mCountryPickerCCP = getView().findViewById(R.id.VF_countryPicker_CCP);
            mPhoneNumberET = getView().findViewById(R.id.VF_phone_ET);
            mSendBtn = getView().findViewById(R.id.VF_send_BTN);
        }

    }




    /**
     * init VerificationFragment listeners
     */
    private void initListeners() {

        mSendBtn.setOnClickListener(this);

    }




    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        if (context instanceof OnVerificationFragmentListener) {
            mListener = (OnVerificationFragmentListener) context;
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

            case R.id.VF_send_BTN:

                setButtonClickedAnimiation(mSendBtn);

                mListener.OnSendVerificationBtnClicked("+" + mCountryPickerCCP.getSelectedCountryCode() + mPhoneNumberET.getText().toString());


        }

    }





    private void setButtonClickedAnimiation(Button Icon) {

        Animation animation = AnimationUtils.loadAnimation(getContext(), R.anim.alpha);
        Icon.startAnimation(animation);

    }





    public interface OnVerificationFragmentListener {

        void OnSendVerificationBtnClicked(String phoneNumber);

    }
}
