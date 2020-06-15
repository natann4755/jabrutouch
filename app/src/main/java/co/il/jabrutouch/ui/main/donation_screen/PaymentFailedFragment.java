package co.il.jabrutouch.ui.main.donation_screen;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import co.il.jabrutouch.R;


public class PaymentFailedFragment extends Fragment {


    public static final String TAG = PaymentFailedFragment.class.getSimpleName();
    private OnPaymentFailedFragmentListener mListener;
    private TextView mTryAgainBtnTV;
    private TextView mContactUsBtnTV;

    public PaymentFailedFragment() {
        // Required empty public constructor
    }

    public static PaymentFailedFragment newInstance() {
        PaymentFailedFragment fragment = new PaymentFailedFragment();
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
        return inflater.inflate(R.layout.fragment_payment_failed, container, false);
    }






    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        initViews();
        initListeners();
    }

   


    private void initViews() {

        mTryAgainBtnTV = getView().findViewById(R.id.PFF_try_again_Btn_TV);
        mContactUsBtnTV = getView().findViewById(R.id.PFF_contect_us_Btn_TV);


    }




    private void initListeners() {
        
        mTryAgainBtnTV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                
                mListener.onTryAgainClicked();
            }
        });
        
        
        
        mContactUsBtnTV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                
                mListener.onContactUsClicked();
            }
        });
    }




    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnPaymentFailedFragmentListener) {
            mListener = (OnPaymentFailedFragmentListener) context;
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


    public interface OnPaymentFailedFragmentListener {

        void onTryAgainClicked();

        void onContactUsClicked();
    }
}
