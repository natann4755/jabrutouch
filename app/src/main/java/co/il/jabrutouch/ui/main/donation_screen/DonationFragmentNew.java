package co.il.jabrutouch.ui.main.donation_screen;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Typeface;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.core.content.res.ResourcesCompat;
import androidx.fragment.app.Fragment;

import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.animation.Animation;
import android.view.animation.OvershootInterpolator;
import android.view.animation.TranslateAnimation;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.airbnb.lottie.LottieAnimationView;
import com.google.gson.Gson;
import com.jcminarro.roundkornerlayout.RoundKornerLinearLayout;
import com.robinhood.ticker.TickerUtils;
import com.robinhood.ticker.TickerView;

import java.util.Objects;

import co.il.jabrutouch.R;
import co.il.jabrutouch.server.RequestManager;
import co.il.jabrutouch.user_manager.UserManager;
import co.il.model.model.Result;
import co.il.model.model.SocketDonateMessage;
import co.il.model.model.User;
import co.il.model.model.UserDonationStatus;
import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;

import static co.il.jabrutouch.ui.main.donation_screen.DedicationFragment.PENDING;


public class DonationFragmentNew extends Fragment implements View.OnClickListener {


    public static final String TAG = DonationFragmentNew.class.getSimpleName();
    private static final String PAYMENT_STATUS = "PAYMENT_STATUS";
    private static final String PENDING_NOT_FROM_THANKES_MODE = "PENDING_NOT_FROM_THANKES_MODE";
    private Button mDonateBtnSubs;
    private Button mDonateBtnSingle;
    private Button mDonateBtnNODonate;
    private Button mDonateBtnThankes;
    private OnDonationFragmentNewListener mListener;
    private TickerView mTickerView1;
    private TickerView mTickerView2;
    private TickerView mTickerView3;
    private TickerView mTickerView4;
    private TickerView mTickerView5;
    private TickerView mTickerView6;
    private Handler handler = new Handler();
    private Runnable myRunnable;
    private int mHoursFromServer;
    private int mNewHoursFromServer = 165292;
    private LottieAnimationView mThumbSingle;
    private LottieAnimationView mThumbSubs;
    private Button mMyPlanBtn;
    private Button mHistoryBtn;
    private LinearLayout mSubLayoutLL;
    private LinearLayout mSingleLayoutLL;
    private LinearLayout mNoDonateLayoutLL;
    private RelativeLayout mThankesLayoutRL;
    private Handler layoutHandler = new Handler();
    private Runnable layoutRunnable;
    private RoundKornerLinearLayout mLottieViewLayoutLL;
    private RoundKornerLinearLayout mProgressAllLayoutLL;
    private RelativeLayout mAllViewRL;
    private TextView mKetarimLeftTV;
    private int mAllKetarim = 50;
    private LinearLayout mSubDetailsBtntLL;
    private UserDonationStatus mUserDonationStatus;
    private TextView mKetarimAllTV;
    private TextView mLikesNumberTV;
    private TextView mKetarimLeftSingleTV;
    private TextView mKetarimAllSingleTV;
    private TextView mLikesNumberSingleTV;
    private RoundKornerLinearLayout mLottieViewLayoutSingleLL;
    private RoundKornerLinearLayout mProgressAllLayoutSingleLL;
    private LinearLayout mNoDonateEverLL;
    private Button mDonateBtnNoDonateEver;
    private String mPaymentStatus;
    private RelativeLayout mEmptyLayoutRL;
    private ProgressBar mProgressBarPB;


    public DonationFragmentNew() {
        // Required empty public constructor
    }


    public static DonationFragmentNew newInstance(String payment) {
        DonationFragmentNew fragment = new DonationFragmentNew();
        Bundle args = new Bundle();
        args.putString(PAYMENT_STATUS, payment);
        fragment.setArguments(args);
        return fragment;
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            if (mPaymentStatus == null){

                mPaymentStatus =  getArguments().getString(PAYMENT_STATUS);
            }
        }
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_donation_fragment_new, container, false);
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        initViews();
        initListeners();
        startSocketForDonationHours();

        if (mPaymentStatus.equals(PENDING)){

            updatePayment();

        }else {

            getUserDonationFromServer();
        }

    }


    private void initViews() {

        mDonateBtnSubs = Objects.requireNonNull(getView()).findViewById(R.id.DFN_donate_BTN_subs);
        mDonateBtnSingle = Objects.requireNonNull(getView()).findViewById(R.id.DFN_donate_BTN_single);
        mDonateBtnNODonate = Objects.requireNonNull(getView()).findViewById(R.id.DFN_donate_BTN_no_donate);
        mDonateBtnThankes = Objects.requireNonNull(getView()).findViewById(R.id.DFN_donate_BTN_thankes);
        mDonateBtnNoDonateEver = Objects.requireNonNull(getView()).findViewById(R.id.DFN_donate_BTN_no_donate_ever);

        mTickerView1 = Objects.requireNonNull(getView()).findViewById(R.id.tickerView1);
        mTickerView2 = Objects.requireNonNull(getView()).findViewById(R.id.tickerView2);
        mTickerView3 = Objects.requireNonNull(getView()).findViewById(R.id.tickerView3);
        mTickerView4 = Objects.requireNonNull(getView()).findViewById(R.id.tickerView4);
        mTickerView5 = Objects.requireNonNull(getView()).findViewById(R.id.tickerView5);
        mTickerView6 = Objects.requireNonNull(getView()).findViewById(R.id.tickerView6);

        mThumbSingle = Objects.requireNonNull(getView()).findViewById(R.id.lav_thumbUp2_single);
        mThumbSubs = Objects.requireNonNull(getView()).findViewById(R.id.lav_thumbUp2_subs);
        mMyPlanBtn = Objects.requireNonNull(getView()).findViewById(R.id.NDF_my_plan_BTN);
        mHistoryBtn = Objects.requireNonNull(getView()).findViewById(R.id.NDF_history_BTN);

        mSubLayoutLL = Objects.requireNonNull(getView()).findViewById(R.id.FDN_subscription_layout_LL);
        mSingleLayoutLL = Objects.requireNonNull(getView()).findViewById(R.id.FDN_single_layout_LL);
        mNoDonateLayoutLL = Objects.requireNonNull(getView()).findViewById(R.id.FDN_no_donate_layout_LL);
        mThankesLayoutRL = Objects.requireNonNull(getView()).findViewById(R.id.FDN_thankes_layout_RL);
        mNoDonateEverLL = Objects.requireNonNull(getView()).findViewById(R.id.FDN_not_donate_ever_layout_LL);
        mEmptyLayoutRL = Objects.requireNonNull(getView()).findViewById(R.id.FDFN_empty_layout_RL);

        mKetarimLeftTV = Objects.requireNonNull(getView()).findViewById(R.id.DFN_ketarim_left_TV);
        mKetarimAllTV = Objects.requireNonNull(getView()).findViewById(R.id.DFN_ketarim_all_TV);
        mLikesNumberTV = Objects.requireNonNull(getView()).findViewById(R.id.DFN_likes_number_TV);

        mAllViewRL = Objects.requireNonNull(getView()).findViewById(R.id.DFN_all_view_RL);
        mLottieViewLayoutLL = Objects.requireNonNull(getView()).findViewById(R.id.DFN_lottie_view_linear_LL);
        mProgressAllLayoutLL = Objects.requireNonNull(getView()).findViewById(R.id.DFN_progress_all_layout_LL);

        mSubDetailsBtntLL = Objects.requireNonNull(getView()).findViewById(R.id.DFN_sub_details_Btn_LL);

        mKetarimLeftSingleTV = Objects.requireNonNull(getView()).findViewById(R.id.DFN_ketarim_left_single_TV);
        mKetarimAllSingleTV = Objects.requireNonNull(getView()).findViewById(R.id.DFN_ketarim_all_single_TV);
        mLikesNumberSingleTV = Objects.requireNonNull(getView()).findViewById(R.id.DFN_likes_number_single_TV);
        mLottieViewLayoutSingleLL = Objects.requireNonNull(getView()).findViewById(R.id.DFN_lottie_view_linear_single_LL);
        mProgressAllLayoutSingleLL = Objects.requireNonNull(getView()).findViewById(R.id.DFN_progress_all_layout_single_LL);

        mProgressBarPB = Objects.requireNonNull(getView()).findViewById(R.id.FDFN_progress_bar_PB);

    }


    private void startSocketForDonationHours() {

        mListener.startSocketForDonationHours();
    }


    private void initAnimationProgress(final boolean isSubscription) {


        mAllViewRL.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {

                if (isSubscription) {
                    ViewGroup.LayoutParams params = mLottieViewLayoutLL.getLayoutParams();
                    float percent = mUserDonationStatus.getAllCrowns() / (float) mUserDonationStatus.getUnusedCrowns();

                    if (mUserDonationStatus.getAllCrowns() == mUserDonationStatus.getUnusedCrowns()) {

                        params.width = (int) (mProgressAllLayoutLL.getWidth() / percent - 8);

                    } else {

                        params.width = (int) (mProgressAllLayoutLL.getWidth() / percent);
                    }

                    mLottieViewLayoutLL.setLayoutParams(params);

                    if (percent > 2) {

                        mProgressAllLayoutLL.setBackground(ContextCompat.getDrawable(Objects.requireNonNull(getContext()), R.drawable.round_donate));
                    } else {
                        mProgressAllLayoutLL.setBackgroundColor(getResources().getColor(R.color.round_donate_gray));

                    }

                    mAllViewRL.getViewTreeObserver().removeOnGlobalLayoutListener(this);

                } else {

                    ViewGroup.LayoutParams params = mLottieViewLayoutSingleLL.getLayoutParams();
                    float percent = mUserDonationStatus.getAllCrowns() / (float) mUserDonationStatus.getUnusedCrowns();

                    if (mUserDonationStatus.getAllCrowns() == mUserDonationStatus.getUnusedCrowns()) {

                        params.width = (int) (mProgressAllLayoutSingleLL.getWidth() / percent - 8);

                    } else {

                        params.width = (int) (mProgressAllLayoutSingleLL.getWidth() / percent);
                    }

                    mLottieViewLayoutSingleLL.setLayoutParams(params);

                    if (percent > 2) {

                        mProgressAllLayoutSingleLL.setBackground(ContextCompat.getDrawable(Objects.requireNonNull(getContext()), R.drawable.round_donate));
                    } else {
                        mProgressAllLayoutSingleLL.setBackgroundColor(getResources().getColor(R.color.round_donate_gray));

                    }

                    mAllViewRL.getViewTreeObserver().removeOnGlobalLayoutListener(this);


                }
            }

        });


    }


    @Override
    public void onDestroy() {

//        handler.removeCallbacks(myRunnable);
        layoutHandler.removeCallbacks(layoutRunnable);

        mListener.closeSocket();
        super.onDestroy();
    }


    private void initListeners() {

        mDonateBtnSubs.setOnClickListener(this);
        mDonateBtnSingle.setOnClickListener(this);
        mDonateBtnNODonate.setOnClickListener(this);
        mDonateBtnThankes.setOnClickListener(this);
        mHistoryBtn.setOnClickListener(this);
        mSubDetailsBtntLL.setOnClickListener(this);
        mDonateBtnNoDonateEver.setOnClickListener(this);

    }


    private void initTickerNumbersView(final int hoursFromServer) {

        mHoursFromServer = hoursFromServer;
        TickerView[] tickerList = new TickerView[]{mTickerView1, mTickerView2, mTickerView3, mTickerView4, mTickerView5, mTickerView6};


        String HoursFromServerPlusZero = intToString(hoursFromServer, 6);

        for (int i = 0; i < tickerList.length; i++) {

            tickerList[i].setCharacterLists(TickerUtils.provideNumberList());
            tickerList[i].setAnimationDuration(1100);
            tickerList[i].setAnimationInterpolator(new OvershootInterpolator());
            tickerList[i].setPreferredScrollingDirection(TickerView.ScrollingDirection.ANY);

//            Typeface typeface = ResourcesCompat.getFont(Objects.requireNonNull(getContext()), R.font.san_francisco_bold);
            Typeface typeface = Typeface.create("sans-serif", Typeface.BOLD);
            tickerList[i].setTypeface(typeface);

            int firstDigit = Integer.parseInt(HoursFromServerPlusZero.substring(i, i + 1));

            tickerList[i].setText(String.valueOf(firstDigit));
        }


    }


    private String intToString(int num, int digits) {
        String output = Integer.toString(num);
        while (output.length() < digits) output = "0" + output;
        return output;
    }


    private void getUserDonationFromServer() {


        RequestManager.getUserDonationStatus(UserManager.getToken(getContext())).subscribe(new Observer<Result<UserDonationStatus>>() {
            @Override
            public void onSubscribe(Disposable d) {

            }

            @Override
            public void onNext(Result<UserDonationStatus> userDonationStatusResult) {

//                mProgressBarPB.setVisibility(View.GONE);

                mUserDonationStatus = userDonationStatusResult.getData();

                if (mUserDonationStatus.getDonatePerMonth() > 0 && mUserDonationStatus.getUnusedCrowns() > 0) {

                    mEmptyLayoutRL.setVisibility(View.GONE);
                    mNoDonateLayoutLL.setVisibility(View.GONE);
                    mNoDonateEverLL.setVisibility(View.GONE);
                    mSingleLayoutLL.setVisibility(View.GONE);
                    mSubLayoutLL.setVisibility(View.VISIBLE);
                    setUserDonationDetails(mUserDonationStatus);
                    mPaymentStatus = PENDING_NOT_FROM_THANKES_MODE; // for next time to not start thanks linear again


                }else if (mUserDonationStatus.getAllCrowns() > 0 && mUserDonationStatus.getUnusedCrowns() == 0){

                    mEmptyLayoutRL.setVisibility(View.GONE);
                    mSubLayoutLL.setVisibility(View.GONE);
                    mNoDonateEverLL.setVisibility(View.GONE);
                    mSingleLayoutLL.setVisibility(View.GONE);
                    mNoDonateLayoutLL.setVisibility(View.VISIBLE);

                    mHoursFromServer = mUserDonationStatus.getWatchCount();
                    initTickerNumbersView(mHoursFromServer);

//                    setUserDonationDetails(mUserDonationStatus);
                    mPaymentStatus = PENDING_NOT_FROM_THANKES_MODE; // for next time to not start thanks linear again

                }


                else if (mUserDonationStatus.getDonatePerMonth() == 0 && mUserDonationStatus.getAllCrowns() == 0 && mUserDonationStatus.getLikes() == 0) {

                    mEmptyLayoutRL.setVisibility(View.GONE);
                    mSubLayoutLL.setVisibility(View.GONE);
                    mSingleLayoutLL.setVisibility(View.GONE);
                    mNoDonateLayoutLL.setVisibility(View.GONE);
                    mNoDonateEverLL.setVisibility(View.VISIBLE);

                    mHoursFromServer = mUserDonationStatus.getWatchCount();
                    initTickerNumbersView(mHoursFromServer);

                    UserManager.setInPendingMode(true, getContext());
                    mPaymentStatus = PENDING_NOT_FROM_THANKES_MODE; // for next time to not start thanks linear again

                } else {

                    mEmptyLayoutRL.setVisibility(View.GONE);
                    mNoDonateLayoutLL.setVisibility(View.GONE);
                    mSubLayoutLL.setVisibility(View.GONE);
                    mNoDonateEverLL.setVisibility(View.GONE);
                    mSingleLayoutLL.setVisibility(View.VISIBLE);
                    setUserDonationDetailsForSingle(mUserDonationStatus);
                    mPaymentStatus = PENDING_NOT_FROM_THANKES_MODE; // for next time to not start thanks linear again

                }

            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onComplete() {

            }
        });


    }


    @SuppressLint("SetTextI18n")
    private void setUserDonationDetailsForSingle(UserDonationStatus userDonationStatus) {

        mKetarimAllSingleTV.setText(getResources().getString(R.string.out_of) + " " + userDonationStatus.getAllCrowns() + " " + getResources().getString(R.string.Ketarim_lower));
        mKetarimLeftSingleTV.setText(String.valueOf(userDonationStatus.getUnusedCrowns()));
        mLikesNumberSingleTV.setText(String.valueOf(userDonationStatus.getLikes()));
        initAnimationProgress(false);
        mHoursFromServer = userDonationStatus.getWatchCount();
        initTickerNumbersView(mHoursFromServer);

    }


    @SuppressLint("SetTextI18n")
    private void setUserDonationDetails(UserDonationStatus userDonationStatus) {


        mKetarimAllTV.setText(getResources().getString(R.string.out_of) + " " + userDonationStatus.getAllCrowns() + " " + getResources().getString(R.string.Ketarim_lower));
        mKetarimLeftTV.setText(String.valueOf(userDonationStatus.getUnusedCrowns()));
        mLikesNumberTV.setText(String.valueOf(userDonationStatus.getLikes()));
        initAnimationProgress(true);
        mHoursFromServer = userDonationStatus.getWatchCount();
        initTickerNumbersView(mHoursFromServer);

    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnDonationFragmentNewListener) {
            mListener = (OnDonationFragmentNewListener) context;
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
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        mListener.hideToolBar();

    }


    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onClick(View view) {

        switch (view.getId()) {

            case R.id.DFN_donate_BTN_single:
            case R.id.DFN_donate_BTN_subs:
            case R.id.DFN_donate_BTN_no_donate:
            case R.id.DFN_donate_BTN_thankes:
            case R.id.DFN_donate_BTN_no_donate_ever:

                mListener.openDonationActivity();
                layoutHandler.removeCallbacks(layoutRunnable);

                break;

            case R.id.NDF_history_BTN:

                Toast.makeText(getContext(), getResources().getString(R.string.coming_soon), Toast.LENGTH_LONG).show();
                break;

            case R.id.DFN_sub_details_Btn_LL:

                mListener.openDetailsDonationActivity(mUserDonationStatus);

                break;
        }


    }


    public void updatePayment() { // ********  client changed version That's why it looks like this ;>)...  ********

        ThanksDialog thanksDialog = new ThanksDialog();
        thanksDialog.showDialog(getContext());

        getUserDonationFromServer();

//        mSubLayoutLL.setVisibility(View.GONE);
//        mSingleLayoutLL.setVisibility(View.GONE);
//        mNoDonateLayoutLL.setVisibility(View.GONE);
//        mNoDonateEverLL.setVisibility(View.GONE);
//        mEmptyLayoutRL.setVisibility(View.GONE);
//        mThankesLayoutRL.setVisibility(View.VISIBLE);
//
//        RequestManager.getUserDonationStatus(UserManager.getToken(getContext())).subscribe(new Observer<Result<UserDonationStatus>>() {
//            @Override
//            public void onSubscribe(Disposable d) {
//
//            }
//
//            @Override
//            public void onNext(Result<UserDonationStatus> userDonationStatusResult) {
//
//
//
//
//                initTickerNumbersView(userDonationStatusResult.getData().getWatchCount());
//
//                layoutRunnable = new Runnable() {
//                    @Override
//                    public void run() {
//                        TranslateAnimation animate = new TranslateAnimation(0, mThankesLayoutRL.getWidth(), 0, 0);
//                        animate.setDuration(500);
//                        animate.setFillAfter(true);
//                        mThankesLayoutRL.startAnimation(animate);
//
//
//                        animate.setAnimationListener(new Animation.AnimationListener() {
//                            @Override
//                            public void onAnimationStart(Animation animation) {
//
//                            }
//
//                            @Override
//                            public void onAnimationEnd(Animation animation) {
//                                mThankesLayoutRL.setVisibility(View.GONE);
//                                mEmptyLayoutRL.setVisibility(View.VISIBLE);
//                                getUserDonationFromServer();
//                            }
//
//                            @Override
//                            public void onAnimationRepeat(Animation animation) {
//
//                            }
//                        });
//
//                        animate.setFillAfter(false); // to clear animation for next time
//                    }
//                };
//
//                layoutHandler.postDelayed(layoutRunnable, 5000);
//
//            }
//
//            @Override
//            public void onError(Throwable e) {
//
//            }
//
//            @Override
//            public void onComplete() {
//
//            }
//        });


    }


    public void updateHoursGlobalFromSocket(SocketDonateMessage socketDonateMessage) {


        initTickerNumbersView(socketDonateMessage.getWatchCount());

        Gson gson = new Gson();
        User userFromJson = gson.fromJson(UserManager.getUser(getContext()), User.class);

        for (int i = 0; i < socketDonateMessage.getDonatedBy().size(); i++) {

            if (socketDonateMessage.getDonatedBy().get(i) == userFromJson.getId()) {

                mUserDonationStatus.setUnusedCrowns(mUserDonationStatus.getUnusedCrowns() - 1);
                mUserDonationStatus.setLikes(mUserDonationStatus.getLikes() + 1);

                if (mUserDonationStatus.getDonatePerMonth() == 0){

                    mKetarimLeftSingleTV.setText(String.valueOf(mUserDonationStatus.getUnusedCrowns()));
                    mLikesNumberSingleTV.setText(String.valueOf(mUserDonationStatus.getLikes()));

                }else {

                    mKetarimLeftTV.setText(String.valueOf(mUserDonationStatus.getUnusedCrowns()));
                    mLikesNumberTV.setText(String.valueOf(mUserDonationStatus.getLikes()));
                }


                initAnimationProgress(true);

                if (mUserDonationStatus.getUnusedCrowns() == 0){

                    mNoDonateLayoutLL.setVisibility(View.VISIBLE);
                    mThankesLayoutRL.setVisibility(View.GONE);
                    mSubLayoutLL.setVisibility(View.GONE);
                    mSingleLayoutLL.setVisibility(View.GONE);
                }
            }


        }


    }


    public interface OnDonationFragmentNewListener {
        void hideToolBar();

        void openDonationActivity();

        void openDetailsDonationActivity(UserDonationStatus userDonationStatus);

        void startSocketForDonationHours();

        void closeSocket();
    }


}
