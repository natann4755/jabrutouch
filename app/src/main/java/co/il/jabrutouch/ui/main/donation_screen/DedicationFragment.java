package co.il.jabrutouch.ui.main.donation_screen;

import android.content.Context;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.google.gson.Gson;
import com.viewpagerindicator.LinePageIndicator;
import java.util.Objects;
import co.il.jabrutouch.R;
import co.il.jabrutouch.user_manager.UserManager;
import co.il.model.model.DonationData;
import co.il.model.model.Payment;
import co.il.model.model.User;


public class DedicationFragment extends Fragment {

    public static final String TAG = DedicationFragment.class.getSimpleName();
    public static final String PAYMENT = "PAYMENT";
    public static final String DONATION_DATA = "DONATION_DATA";
    private static final String DONATED = "donated";
    public static final String PENDING = "pending";
    private OnDedicationFragmentListener mListener;
    private Payment mPaymentObject;
    private ViewPager mViewPager;
    private MyPagerAdapter mSlideScreensAdapter;
    private LinePageIndicator mLinePageIndicator;
    private Button mContinueBtnBTN;
    private User userFromJson;
    private DonationData mDonationData;
    private String mDedicationName;
    private String mDonationName;
    private TextView mTopViewTV;

    public DedicationFragment() {
        // Required empty public constructor
    }


    public static DedicationFragment newInstance(Payment payment, DonationData mDonationData) {
        DedicationFragment fragment = new DedicationFragment();
        Bundle args = new Bundle();
        args.putSerializable(PAYMENT, payment);
        args.putSerializable(DONATION_DATA, mDonationData);
        fragment.setArguments(args);
        return fragment;
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mPaymentObject = (Payment) getArguments().getSerializable(PAYMENT);
            mDonationData = (DonationData) getArguments().getSerializable(DONATION_DATA);
        }
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_dedication, container, false);
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        initViews();
        setTheViewPagerAndInIndicator();
        initListeners();
        getUser();
    }




    private void initViews() {

        mViewPager = Objects.requireNonNull(getView()).findViewById(R.id.DF_view_pager_VP);
        mSlideScreensAdapter = new DedicationFragment.MyPagerAdapter(getChildFragmentManager());
        mLinePageIndicator = getView().findViewById(R.id.activity_view_pager_indicator_underline);
        mContinueBtnBTN = getView().findViewById(R.id.DEDF_continue_btn_BTN);
        mTopViewTV = getView().findViewById(R.id.FD_top_view_TV);

    }


    private void setTheViewPagerAndInIndicator() {

        int pagerPadding = 150; // set margin between pages
        mViewPager.setClipToPadding(false);
        mViewPager.setPadding(pagerPadding, 0, pagerPadding, 0);
        mViewPager.setPageMargin(50);


        mViewPager.setPageTransformer(false, new ViewPager.PageTransformer() {
            @Override
            public void transformPage(@NonNull View page, float position) {

                float absPosition = Math.abs(position);

                int pageWidth = mViewPager.getMeasuredWidth() -
                        mViewPager.getPaddingLeft() - mViewPager.getPaddingRight();
                int paddingLeft = mViewPager.getPaddingLeft();
                float transformPos = (float) (page.getLeft() -
                        (mViewPager.getScrollX() + paddingLeft)) / pageWidth;

                if (transformPos < -1) {

                    page.setScaleY(0.8f);
                    page.findViewById(R.id.FDVPF_left_arrow_IV).setVisibility(View.VISIBLE);
                    page.findViewById(R.id.FDVPF_right_arrow_IV).setVisibility(View.VISIBLE);

                } else if (transformPos <= 1) {

                    if (((0.8f - 1) * absPosition + 1) > 0.8){

                        page.setScaleY((0.8f - 1) * absPosition + 1);
                        page.findViewById(R.id.FDVPF_left_arrow_IV).setVisibility(View.GONE);
                        page.findViewById(R.id.FDVPF_right_arrow_IV).setVisibility(View.GONE);
                    }
                } else {

                    page.setScaleY(0.8f);
                    page.findViewById(R.id.FDVPF_left_arrow_IV).setVisibility(View.VISIBLE);
                    page.findViewById(R.id.FDVPF_right_arrow_IV).setVisibility(View.VISIBLE);
                }


            }
        });


        mViewPager.setAdapter(mSlideScreensAdapter);
        mLinePageIndicator.setViewPager(mViewPager);

    }






    private void initListeners() {


        mContinueBtnBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                mPaymentObject.setDedicationTemplate(mViewPager.getCurrentItem() + 1);
                if (mDonationName != null){

                    mPaymentObject.setNameToRepresent(mDonationName);

                }else {

                    mPaymentObject.setNameToRepresent(userFromJson.getFirstName() + " " + userFromJson.getLastName());
                }
                if (mDedicationName != null){

                    mPaymentObject.setDedicationText(mDedicationName);
                }else {

                    mPaymentObject.setDedicationText("");

                }

                mPaymentObject.setStatus(PENDING);
                mPaymentObject.setCountry(userFromJson.getCountry());
//                mPaymentObject.setStatus(DONATED);
                mListener.onDedicationContinueBtnClicked(mPaymentObject, mDonationData);

            }
        });



    }






    private void getUser() {

        Gson gson = new Gson();
        userFromJson = gson.fromJson(UserManager.getUser(getContext()), User.class);


    }








    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnDedicationFragmentListener) {
            mListener = (OnDedicationFragmentListener) context;
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





    public void saveEditNameForAllPager() {


        mSlideScreensAdapter.saveEditNameForAllPager();



    }

    public void onDedicationNameChanges(String dedicationName) {

        mDedicationName = dedicationName;

    }

    public void onDonationNameChanges(String donationName) {


        mDonationName = donationName;


    }

    public void hideTopView() {

        mTopViewTV.setVisibility(View.GONE);
    }

    public void showTopView() {
        mTopViewTV.setVisibility(View.VISIBLE);

    }


    public interface OnDedicationFragmentListener {

        void onDedicationContinueBtnClicked(Payment mPaymentObject, DonationData mDonationData);

    }






    /**
     * class adapter for the slide screens
     */
    private class MyPagerAdapter extends FragmentPagerAdapter {

        private FirstDedicationViewPagerFragment mFirstDedicationViewPagerFragment;
        private OthersDedicationViewPagerFragment mOthersDedicationViewPagerFragment;

        MyPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        /**
         * get the item of the screen position to call the slide screens
         *
         * @param position int, position of the view pager
         * @return slide screen fragment
         */
        @NonNull
        @Override
        public Fragment getItem(int position) {
            switch (position) {

                case 0:
                    return mFirstDedicationViewPagerFragment = FirstDedicationViewPagerFragment.newInstance(userFromJson);
                case 1:
                    return mOthersDedicationViewPagerFragment = OthersDedicationViewPagerFragment.newInstance(mDonationData.getDedication().get(1), userFromJson);
                case 2:
                    return mOthersDedicationViewPagerFragment = OthersDedicationViewPagerFragment.newInstance(mDonationData.getDedication().get(2), userFromJson);
                case 3:
                    return mOthersDedicationViewPagerFragment = OthersDedicationViewPagerFragment.newInstance(mDonationData.getDedication().get(3), userFromJson);

                default:
                    return mFirstDedicationViewPagerFragment = FirstDedicationViewPagerFragment.newInstance(userFromJson);
            }
        }



        @Override
        public int getItemPosition(@NonNull Object object) {

            return POSITION_NONE;
        }




        @Override
        public int getCount() {
            return 4;
        }

        void saveEditNameForAllPager() {

                mSlideScreensAdapter.notifyDataSetChanged();
                int currentItem = mViewPager.getCurrentItem();
                setTheViewPagerAndInIndicator();
                mViewPager.setCurrentItem(currentItem);

        }


    }


}
