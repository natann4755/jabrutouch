package co.il.jabrutouch.ui.main.donation_screen;

import android.content.Context;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RelativeLayout;

import java.util.Objects;

import co.il.jabrutouch.R;


public class PaymentFragment extends Fragment implements View.OnClickListener {


    public static final String TAG = PaymentFragment.class.getSimpleName();

    private OnPaymentFragmentListener mListener;
    private RelativeLayout mGooglePayBtnRL;
    private RelativeLayout mCreditCardRL;
    private RelativeLayout mPayPalRL;
    private Button mGooglePayBTN;
    private Button mCreditCardBTN;
    private Button mPayPalBTN;
    private boolean mCreditCardPressed;
    private Button mContinueBTN;


    public PaymentFragment() {
        // Required empty public constructor
    }


    public static PaymentFragment newInstance() {
        PaymentFragment fragment = new PaymentFragment();
        Bundle args = new Bundle();

        fragment.setArguments(args);
        return fragment;
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {

        }
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_payment, container, false);
    }


    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        initViews();
        initListeners();
    }




    private void initViews() {

        mGooglePayBtnRL = Objects.requireNonNull(getView()).findViewById(R.id.PF_google_pay_btn_RL);
        mCreditCardRL = getView().findViewById(R.id.PF_credit_card_btn_RL);
        mPayPalRL = getView().findViewById(R.id.PF_paypal_btn_RL);
        mGooglePayBTN = getView().findViewById(R.id.PF_google_pay_btn_BTN);
        mCreditCardBTN = getView().findViewById(R.id.PF_credit_card_btn_BTN);
        mPayPalBTN = getView().findViewById(R.id.PF_paypal_btn_BTN);
        mContinueBTN = getView().findViewById(R.id.PF_continue_to_payment_btn_BTN);



    }


    private void initListeners() {


        mGooglePayBtnRL.setOnClickListener(this);
        mCreditCardRL.setOnClickListener(this);
        mPayPalRL.setOnClickListener(this);
        mContinueBTN.setOnClickListener(this);


    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnPaymentFragmentListener) {
            mListener = (OnPaymentFragmentListener) context;
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


    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public void onClick(View view) {

        switch (view.getId()) {


            case R.id.PF_google_pay_btn_RL:

                mCreditCardPressed = false;

                mGooglePayBtnRL.setBackground(ContextCompat.getDrawable(Objects.requireNonNull(getContext()), R.drawable.blue_button));
                mCreditCardRL.setBackground(ContextCompat.getDrawable(Objects.requireNonNull(getContext()), R.drawable.white_button));
                mPayPalRL.setBackground(ContextCompat.getDrawable(Objects.requireNonNull(getContext()), R.drawable.white_button));

                mGooglePayBTN.setTextColor(getResources().getColor(R.color.color_white));
                mCreditCardBTN.setTextColor(getResources().getColor(R.color.color_blue_gray));
                mPayPalBTN.setTextColor(getResources().getColor(R.color.color_blue_gray));

                mListener.requestGooglePayPayment(view);

                break;

            case R.id.PF_credit_card_btn_RL:

                mCreditCardPressed = true;

                mCreditCardRL.setBackground(ContextCompat.getDrawable(Objects.requireNonNull(getContext()), R.drawable.blue_button));
                mGooglePayBtnRL.setBackground(ContextCompat.getDrawable(Objects.requireNonNull(getContext()), R.drawable.white_button));
                mPayPalRL.setBackground(ContextCompat.getDrawable(Objects.requireNonNull(getContext()), R.drawable.white_button));


                mCreditCardBTN.setTextColor(getResources().getColor(R.color.color_white));
                mGooglePayBTN.setTextColor(getResources().getColor(R.color.color_blue_gray));
                mPayPalBTN.setTextColor(getResources().getColor(R.color.color_blue_gray));


                break;

            case R.id.PF_paypal_btn_RL:

                mCreditCardPressed = false;


                mPayPalRL.setBackground(ContextCompat.getDrawable(Objects.requireNonNull(getContext()), R.drawable.blue_button));
                mGooglePayBtnRL.setBackground(ContextCompat.getDrawable(Objects.requireNonNull(getContext()), R.drawable.white_button));
                mCreditCardRL.setBackground(ContextCompat.getDrawable(Objects.requireNonNull(getContext()), R.drawable.white_button));


                mPayPalBTN.setTextColor(getResources().getColor(R.color.color_white));
                mGooglePayBTN.setTextColor(getResources().getColor(R.color.color_blue_gray));
                mCreditCardBTN.setTextColor(getResources().getColor(R.color.color_blue_gray));


                mListener.requestPayPalPayment(view);

                break;


            case R.id.PF_continue_to_payment_btn_BTN:


                if (mCreditCardPressed){

                    mListener.onCreditCardPressed();

                }


                break;


        }


    }


    public interface OnPaymentFragmentListener {


        void requestGooglePayPayment(View view);

        void requestPayPalPayment(View view);

        void onCreditCardPressed();
    }
}
