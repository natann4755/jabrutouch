package co.il.jabrutouch.ui.main.donation_screen;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import co.il.jabrutouch.R;


public class CreditCardPaymentFragment extends Fragment {


    public static final String TAG = CreditCardPaymentFragment.class.getSimpleName();
    private OnCreditCardPaymentFragmentListener mListener;
    private RelativeLayout mRelativeForProgress;
    private ProgressBar mProgressBar;
    private Button mSendPaymentButton;
    private TextView mReplaceCardTV;

    public CreditCardPaymentFragment() {
        // Required empty public constructor
    }

    public static CreditCardPaymentFragment newInstance() {
        CreditCardPaymentFragment fragment = new CreditCardPaymentFragment();
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
        return inflater.inflate(R.layout.fragment_credit_card_payment, container, false);
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);


        initViews();
        initListeners();
    }




    private void initViews() {


        mRelativeForProgress = getView().findViewById(R.id.MUAI_relative_for_progress_RL);
        mProgressBar = getView().findViewById(R.id.MUAI_progress_bar_PB);
        mSendPaymentButton = getView().findViewById(R.id.CCPF_send_payment_btn_BTN);
        mReplaceCardTV = getView().findViewById(R.id.CCPF_replace_card_TV);

    }






    private void initListeners() {
        mSendPaymentButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                mProgressBar.setVisibility(View.VISIBLE);
                mRelativeForProgress.setVisibility(View.VISIBLE);


                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {

//                        mListener.onPaymentSucced();
                        mListener.onPaymentField();
                        
                    }
                }, 5000);

            }
        });



        mReplaceCardTV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                mListener.onReplaceCardClicked();


            }
        });



    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnCreditCardPaymentFragmentListener) {
            mListener = (OnCreditCardPaymentFragmentListener) context;
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



    public interface OnCreditCardPaymentFragmentListener {

        void onReplaceCardClicked();

        void onPaymentSucced();

        void onPaymentField();
    }
}
