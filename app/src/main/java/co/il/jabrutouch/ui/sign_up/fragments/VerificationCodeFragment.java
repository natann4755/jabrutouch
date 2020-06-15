package co.il.jabrutouch.ui.sign_up.fragments;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatEditText;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.fragment.app.Fragment;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentManager;

import android.os.CountDownTimer;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;
import java.util.Objects;
import co.il.jabrutouch.R;




public class VerificationCodeFragment extends Fragment implements View.OnTouchListener, View.OnClickListener, CustomEditText.CustomEditTextListener {


    private static final String PHONE_NUMBER = "phoneNumber";
    private static final String POR_FAVOR = "Por favor espera ";
    private static final String SEGUNDOS = " segundos\npara recibir el código";

    private OnVerificationCodeFragmentListener mListener;
    private String mPhoneNumber;
    private CustomEditText mFirstPinET;
    private CustomEditText mSecondPinET;
    private CustomEditText mThirdPinET;
    private CustomEditText mFourthPinET;
    private TextView mPhoneNumberTV;
    private TextView mSendAgainBtn;
    private ProgressBar mProgressBar;
    private AppCompatTextView mWaitSecondsACTV;
    private LinearLayout mWaitSecondsLL;
    private LinearLayout mDidntCodeLL;
    private AppCompatTextView mRegisterByEmailACTV;


    public VerificationCodeFragment() {

    }




    public static VerificationCodeFragment newInstance(String phoneNumber) {
        VerificationCodeFragment fragment = new VerificationCodeFragment();
        Bundle args = new Bundle();
        args.putString(PHONE_NUMBER, phoneNumber);
        fragment.setArguments(args);
        return fragment;
    }



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null) {
            mPhoneNumber = getArguments().getString(PHONE_NUMBER);
        }
    }




    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_verification_code, container, false);
    }




    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initViews();
        initListeners();
        setKeyboardListeners();
        setPinCodeEntry();
        setPhoneNumberET();
        initCountDownTimer();



    }



    public Fragment getVisibleFragment() {
        Fragment f = null;
        FragmentManager fragmentManager = getFragmentManager();
        List<Fragment> fragments = Objects.requireNonNull(fragmentManager).getFragments();
        for (Fragment fragment : fragments) {
            if (fragment != null && fragment.isVisible()) {
                f = fragment;
            }
        }
        return f;
    }




    /**
     * close the fragment when on back pressed
     */
    private void setKeyboardListeners() {

        mFirstPinET.addListener(this);
        mSecondPinET.addListener(this);
        mThirdPinET.addListener(this);
        mFourthPinET.addListener(this);

    }





    /**
     * init views
     */
    private void initViews() {
        if (getView() != null) {

            mFirstPinET = getView().findViewById(R.id.VCF_first_pin_ET);
            mSecondPinET = getView().findViewById(R.id.VCF_second_pin_ET);
            mThirdPinET = getView().findViewById(R.id.VCF_third_pin_ET);
            mFourthPinET = getView().findViewById(R.id.VCF_fourth_pin_ET);
            mPhoneNumberTV = getView().findViewById(R.id.VCF_phone_number_TV);
            mSendAgainBtn = getView().findViewById(R.id.VCF_send_again_TV);
            mProgressBar = getView().findViewById(R.id.VCF_progress_bar_PB);
            mWaitSecondsACTV = getView().findViewById(R.id.VCF_wait_seconds_ACTV);
            mWaitSecondsLL = getView().findViewById(R.id.VCF_wait_LL);
            mDidntCodeLL = getView().findViewById(R.id.VCF_didnt_code_LL);
            mRegisterByEmailACTV = getView().findViewById(R.id.VCF_register_by_mail_ACTV);
        }


    }




    /**
     * init listeners
     */
    private void initListeners() {

        mSendAgainBtn.setOnClickListener(this);
        mRegisterByEmailACTV.setOnClickListener(this);
    }





    /**
     * set phone number from verificationFragment
     */
    @SuppressLint("SetTextI18n")
    private void setPhoneNumberET() {

        mPhoneNumberTV.setText(mPhoneNumber);

    }






    private void initCountDownTimer() {


        new CountDownTimer(30000, 1000) {

            @SuppressLint("SetTextI18n")
            public void onTick(long millisUntilFinished) {

                mWaitSecondsACTV.setText(POR_FAVOR + millisUntilFinished/ 1000 + SEGUNDOS);

            }

            public void onFinish() {

                mWaitSecondsLL.setVisibility(View.GONE);
                mDidntCodeLL.setVisibility(View.VISIBLE);

            }

        }.start();



    }








    /**
     * set the pin code listeners
     */
    @SuppressLint("ClickableViewAccessibility")
    private void setPinCodeEntry() {


        mFirstPinET.setOnTouchListener(this);
        mSecondPinET.setOnTouchListener(this);
        mThirdPinET.setOnTouchListener(this);
        mFourthPinET.setOnTouchListener(this);

        openKeyboardWhenFragmentStart(mFirstPinET);


        mFirstPinET.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (charSequence.length() == 1) {
                    mFirstPinET.setBackground(null);
                    mSecondPinET.requestFocus(View.FOCUS_DOWN);
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });


        mSecondPinET.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {

                if (event.getAction() != KeyEvent.ACTION_DOWN)
                    return true;

                if (keyCode == KeyEvent.KEYCODE_DEL) {
                    mFirstPinET.requestFocus(View.FOCUS_DOWN);
                    mFirstPinET.setText(null);
                    mFirstPinET.setBackground(ContextCompat.getDrawable(Objects.requireNonNull(getContext()), R.drawable.dote));
                    return true;
                }
                return false;
            }
        });


        mSecondPinET.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {


                if (charSequence.length() == 1) {
                    mSecondPinET.setBackground(null);
                    mThirdPinET.requestFocus(View.FOCUS_DOWN);

                }

            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });


        mThirdPinET.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {

                if (event.getAction() != KeyEvent.ACTION_DOWN)
                    return true;

                if (keyCode == KeyEvent.KEYCODE_DEL) {
                    mSecondPinET.requestFocus(View.FOCUS_DOWN);
                    mSecondPinET.setText(null);
                    mSecondPinET.setBackground(ContextCompat.getDrawable(Objects.requireNonNull(getContext()), R.drawable.dote));
                    return true;
                }
                return false;
            }
        });


        mThirdPinET.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                if (charSequence.length() == 1) {
                    mThirdPinET.setBackground(null);
                    mFourthPinET.requestFocus(View.FOCUS_DOWN);
                }


            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });


        mFourthPinET.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {

                if (event.getAction() != KeyEvent.ACTION_DOWN)
                    return true;

                if (keyCode == KeyEvent.KEYCODE_DEL) {

                    if (mFourthPinET.getText().toString().length() == 0) {

                        mThirdPinET.requestFocus(View.FOCUS_DOWN);
                        mThirdPinET.setText(null);
                        mThirdPinET.setBackground(ContextCompat.getDrawable(Objects.requireNonNull(getContext()), R.drawable.dote));
                    } else {
                        mFourthPinET.setBackground(ContextCompat.getDrawable(Objects.requireNonNull(getContext()), R.drawable.dote));
                        mFourthPinET.setText("");
                        return true;

                    }

                }
                return false;
            }
        });


        mFourthPinET.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (i2 == 0) {
                    mFourthPinET.setBackground(ContextCompat.getDrawable(Objects.requireNonNull(getContext()), R.drawable.dote));
                } else {
                    if (charSequence.length() == 1) {
                        mFourthPinET.setBackground(null);
                        mListener.sendTheCodeAndMoveToSignUpFragment(mFirstPinET.getText().toString()
                                + mSecondPinET.getText().toString() + mThirdPinET.getText().toString() + mFourthPinET.getText().toString(), mPhoneNumber);
                    }
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });


    }




    /**
     * open the Keyboard When Fragment Start
     */
    private void openKeyboardWhenFragmentStart(EditText editText) {

        InputMethodManager inputMethodManager = (InputMethodManager) Objects.requireNonNull(getActivity()).getSystemService(getActivity().INPUT_METHOD_SERVICE);
        editText.requestFocus();
        Objects.requireNonNull(inputMethodManager).showSoftInput(editText, 0);

    }




    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnVerificationCodeFragmentListener) {
            mListener = (OnVerificationCodeFragmentListener) context;
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
    public boolean onTouch(View view, MotionEvent motionEvent) {
        return true;
    }





    /**
     * reset the code when the code is incorrect
     */
    public void resetTheCode() {

        mFirstPinET.setText(null);
        mSecondPinET.setText(null);
        mThirdPinET.setText(null);
        mFourthPinET.setText(null);

        mFirstPinET.setBackground(ContextCompat.getDrawable(Objects.requireNonNull(getContext()), R.drawable.dote));
        mSecondPinET.setBackground(ContextCompat.getDrawable(Objects.requireNonNull(getContext()), R.drawable.dote));
        mThirdPinET.setBackground(ContextCompat.getDrawable(Objects.requireNonNull(getContext()), R.drawable.dote));
        mFourthPinET.setBackground(ContextCompat.getDrawable(Objects.requireNonNull(getContext()), R.drawable.dote));

        mFirstPinET.requestFocus(View.FOCUS_DOWN);

    }





    @Override
    public void onClick(View view) {
        switch (view.getId()) {

            case R.id.VCF_send_again_TV:

                setImageClickedAnimation(mSendAgainBtn);

                mProgressBar.setVisibility(View.VISIBLE);

                resetTheCode();
                mWaitSecondsLL.setVisibility(View.VISIBLE);
                mDidntCodeLL.setVisibility(View.GONE);
                initCountDownTimer();
                mListener.onSendAgainClicked(mPhoneNumber);

                mProgressBar.setVisibility(View.GONE);
                break;


            case R.id.VCF_register_by_mail_ACTV:

                setImageClickedAnimation(mRegisterByEmailACTV);


                registerByMail();


                break;
        }
    }




    private void registerByMail() {


        Intent i = new Intent(Intent.ACTION_SEND);
        i.setType("message/rfc822");
        i.putExtra(Intent.EXTRA_EMAIL  , new String[]{getResources().getString(R.string.jabru_mail)});
        i.putExtra(Intent.EXTRA_SUBJECT, getResources().getString(R.string.no_he));
        i.putExtra(Intent.EXTRA_TEXT   , getResources().getString(R.string.hola_mail_body));
        try {
            startActivity(Intent.createChooser(i, "Send mail..."));
        } catch (android.content.ActivityNotFoundException ex) {
            Toast.makeText(getContext(), "There are no email clients installed.", Toast.LENGTH_SHORT).show();
        }


        mListener.onOnBackPressedFromMail();

    }


    private void setImageClickedAnimation(TextView Icon) {

        Animation animation = AnimationUtils.loadAnimation(getContext(), R.anim.alpha);
        Icon.startAnimation(animation);

    }




    @Override
    public void onKeyPrime() {

        resetTheCode();
        mListener.onOnBackPressed();
    }



    public interface OnVerificationCodeFragmentListener {

        void sendTheCodeAndMoveToSignUpFragment(String code, String phoneNumber);

        void onSendAgainClicked(String phoneNumber);

        void onOnBackPressed();

        void onOnBackPressedFromMail();
    }
}
