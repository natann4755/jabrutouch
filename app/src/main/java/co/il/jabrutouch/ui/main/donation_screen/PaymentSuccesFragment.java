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
import android.widget.Button;

import com.airbnb.lottie.LottieAnimationView;

import java.util.Objects;

import co.il.jabrutouch.R;


public class PaymentSuccesFragment extends Fragment {


    public static final String TAG = PaymentSuccesFragment.class.getSimpleName();
    private OnPaymentSuccesFragmentListener mListener;
    private LottieAnimationView mLottieAnimationView;
    private Button mDoneButtonBTN;

    public PaymentSuccesFragment() {
        // Required empty public constructor
    }


    public static PaymentSuccesFragment newInstance() {
        PaymentSuccesFragment fragment = new PaymentSuccesFragment();
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
        return inflater.inflate(R.layout.fragment_payment_succes, container, false);
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        initAmimationSuccesIcon();
        initDoneBtn();
    }

    private void initDoneBtn() {
        
        mDoneButtonBTN = Objects.requireNonNull(getView()).findViewById(R.id.PSF_done_btn_BTN);
        mDoneButtonBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mListener.onDoneSuccesBtnClicked();
            }
        });
        
    }

    private void initAmimationSuccesIcon() {

        mLottieAnimationView = Objects.requireNonNull(getView()).findViewById(R.id.lav_thumbUp);
        mLottieAnimationView.playAnimation();

    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnPaymentSuccesFragmentListener) {
            mListener = (OnPaymentSuccesFragmentListener) context;
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


    public interface OnPaymentSuccesFragmentListener {

        void onDoneSuccesBtnClicked();
    }
}
