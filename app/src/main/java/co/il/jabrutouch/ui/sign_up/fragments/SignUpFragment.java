package co.il.jabrutouch.ui.sign_up.fragments;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.core.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import co.il.jabrutouch.R;




public class SignUpFragment extends Fragment implements View.OnClickListener, View.OnTouchListener {


    private static final String PHONE_NUMBER = "PHONE_NUMBER";
    private static final String USER_ID = "USER_ID";
    private OnSignUpFragmentListener mListener;
    private TextView mSignInTV;
    private EditText mFirstNameED;
    private EditText mLastNameED;
    private EditText mEmailOrPhoneED;
    private EditText mPasswordED;
    private Button mSignUpBTN;
    private String mUserId;
    private String mUserPhoneNumber;


    public SignUpFragment() {

    }

    public static SignUpFragment newInstance(String userId, String phoneNUmber) {
        SignUpFragment fragment = new SignUpFragment();
        Bundle args = new Bundle();
        args.putString(USER_ID, userId);
        args.putString(PHONE_NUMBER, phoneNUmber);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mUserId = getArguments().getString(USER_ID);
            mUserPhoneNumber = getArguments().getString(PHONE_NUMBER);
        }
    }




    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        findViews();
        initListeners();
    }





    /**
     * find views
     */
    private void findViews() {

        mFirstNameED = Objects.requireNonNull(getView()).findViewById(R.id.SUF_first_name_ET);
        mLastNameED = Objects.requireNonNull(getView()).findViewById(R.id.SUF_last_name_ET);
        mEmailOrPhoneED = Objects.requireNonNull(getView()).findViewById(R.id.SUF_email_or_phone_ET);
        mPasswordED = Objects.requireNonNull(getView()).findViewById(R.id.SUF_password_ET);
        mSignInTV = getView().findViewById(R.id.SUF_sign_in_TV);
        mSignUpBTN = getView().findViewById(R.id.SUF_sign_up_button_BTN);
    }




    /**
     * init listeners
     */
    @SuppressLint("ClickableViewAccessibility")
    private void initListeners() {

        mSignInTV.setOnClickListener(this);
        mSignUpBTN.setOnClickListener(this);

        mFirstNameED.setOnTouchListener(this);
        mLastNameED.setOnTouchListener(this);
        mEmailOrPhoneED.setOnTouchListener(this);
        mPasswordED.setOnTouchListener(this);

    }





    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_sign_up, container, false);
    }





    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        if (context instanceof OnSignUpFragmentListener) {
            mListener = (OnSignUpFragmentListener) context;
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

            case R.id.SUF_first_name_ET:
                mFirstNameED.setBackground(ContextCompat.getDrawable(Objects.requireNonNull(getContext()), R.drawable.rounded_edittext_top_shadow_padding));
                break;


            case R.id.SUF_last_name_ET:
                mLastNameED.setBackground(ContextCompat.getDrawable(Objects.requireNonNull(getContext()), R.drawable.rounded_edittext_top_shadow_padding));
                break;


            case R.id.SUF_email_or_phone_ET:
                mEmailOrPhoneED.setBackground(ContextCompat.getDrawable(Objects.requireNonNull(getContext()), R.drawable.rounded_edittext_top_shadow_padding));
                break;


            case R.id.SUF_password_ET:
                mPasswordED.setBackground(ContextCompat.getDrawable(Objects.requireNonNull(getContext()), R.drawable.rounded_edittext_top_shadow_padding));
                break;


            case R.id.SUF_sign_in_TV:

                setImageClickedAnimation(mSignInTV);

                mListener.OnSignInClicked();

                break;

            case R.id.SUF_sign_up_button_BTN:

                setButtonClickedAnimiation(mSignUpBTN);

                if (checkIfAllFieldsAreFilled(new EditText[]{mFirstNameED, mLastNameED, mEmailOrPhoneED, mPasswordED})) {


                    if (isEmailValid(mEmailOrPhoneED.getText().toString())){

                        mListener.OnSignUpClicked(mUserId,
                                mFirstNameED.getText().toString(),
                                mLastNameED.getText().toString(),
                                mEmailOrPhoneED.getText().toString(),
                                mPasswordED.getText().toString(),
                                mUserPhoneNumber);
                    }

                    else {

                        Toast.makeText(getContext(), getResources().getString(R.string.not_valid), Toast.LENGTH_SHORT).show();
                        mEmailOrPhoneED.setText("");
                        mEmailOrPhoneED.setBackground(ContextCompat.getDrawable(Objects.requireNonNull(getContext()), R.drawable.rounded_blue_edittext));


                    }

                }

                break;

        }
    }





    private void setImageClickedAnimation(TextView Icon) {

        Animation animation = AnimationUtils.loadAnimation(getContext(), R.anim.alpha);
        Icon.startAnimation(animation);

    }





    private void setButtonClickedAnimiation(Button Icon) {

        Animation animation = AnimationUtils.loadAnimation(getContext(), R.anim.alpha);
        Icon.startAnimation(animation);

    }





    /**
     * check if all fields are filled and if not change background color
     *
     * @param editTexts EditText[], list of all fields edit text
     * @return boolean allFilled
     */
    private boolean checkIfAllFieldsAreFilled(EditText[] editTexts) {

        boolean allFilled = true;

        for (EditText currentField : editTexts) {

            if (currentField.getText().toString().length() <= 0) {
                currentField.setBackground(ContextCompat.getDrawable(Objects.requireNonNull(getContext()), R.drawable.rounded_blue_edittext));
                allFilled = false;
            }

        }

        return allFilled;

    }




    /**
     * method is used for checking valid email id format.
     *
     * @param email String
     * @return boolean true for valid false for invalid
     */
    public static boolean isEmailValid(String email) {
        String expression = "^[\\w\\.-]+@([\\w\\-]+\\.)+[A-Z]{2,4}$";
        Pattern pattern = Pattern.compile(expression, Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(email);
        return matcher.matches();
    }





    @SuppressLint("ClickableViewAccessibility")
    @Override
    public boolean onTouch(View view, MotionEvent motionEvent) {
        EditText editText = (EditText) view;
        editText.setBackground(ContextCompat.getDrawable(Objects.requireNonNull(getContext()), R.drawable.rounded_edittext_top_shadow_padding));
        return false;
    }





    public interface OnSignUpFragmentListener {

        void OnSignInClicked();

        void OnSignUpClicked(String userId, String firstName, String lastName, String emailOrPhone, String password, String userPhoneNumber);
    }
}
