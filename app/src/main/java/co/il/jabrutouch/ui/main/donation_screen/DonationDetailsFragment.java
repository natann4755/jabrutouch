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

import java.io.Serializable;

import co.il.jabrutouch.R;
import co.il.model.model.UserDonationStatus;

public class DonationDetailsFragment extends Fragment {


    public static final String TAG = DonationDetailsFragment.class.getSimpleName();
    private static final String USER_STATUS = "USER_STATUS";
    private OnDonationDetailsFragmentListener mListener;
    private UserDonationStatus mUserStatus;
    private TextView mDonatePerMonthTV;

    public DonationDetailsFragment() {
        // Required empty public constructor
    }


    public static DonationDetailsFragment newInstance(UserDonationStatus userDonationStatus) {
        DonationDetailsFragment fragment = new DonationDetailsFragment();
        Bundle args = new Bundle();
        args.putSerializable(USER_STATUS, userDonationStatus);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {

            mUserStatus = (UserDonationStatus) getArguments().getSerializable(USER_STATUS);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_donation_details, container, false);
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        initViews();
        setUserDetails();
    }



    private void initViews() {

        mDonatePerMonthTV = getView().findViewById(R.id.FDD_donate_per_month_TV);


    }




    private void setUserDetails() {

        mDonatePerMonthTV.setText(String.format( "%s%s %s", getResources().getString(R.string.dolar), mUserStatus.getDonatePerMonth(), getResources().getString(R.string.monthly)));
    }
    

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnDonationDetailsFragmentListener) {
            mListener = (OnDonationDetailsFragmentListener) context;
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


    public interface OnDonationDetailsFragmentListener {

    }
}
