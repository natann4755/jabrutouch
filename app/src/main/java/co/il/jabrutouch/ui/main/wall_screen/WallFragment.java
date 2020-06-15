package co.il.jabrutouch.ui.main.wall_screen;

import android.annotation.SuppressLint;
import android.content.Context;
import android.icu.util.Calendar;
import android.icu.util.HebrewCalendar;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.Collections;
import java.util.List;
import java.util.Locale;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

import co.il.jabrutouch.R;
import co.il.jabrutouch.ui.main.wall_screen.adapters.RecentGemaraAdapter;
import co.il.jabrutouch.ui.main.wall_screen.adapters.RecentMishnaAdapter;
import co.il.jabrutouch.user_manager.UserManager;
import co.il.model.model.TodayDafYomiDetailes;
import co.il.model.model.MishnayotItem;
import co.il.model.model.PagesItem;
import co.il.sqlcore.DBHandler;

import static co.il.sqlcore.DBKeys.GEMARA_TABLE;


public class WallFragment extends Fragment implements View.OnClickListener, RecentGemaraAdapter.RecentGemaraAdapterListener, RecentMishnaAdapter.RecentMishnaAdapterListener {

    public static final String TAG = WallFragment.class.getSimpleName();
    public static final String DAF_YOMI_DETAILES = "DAF_YOMI_DETAILES";
    private static final String RENDER_PAGE = "RENDER_PAGE";
    private OnWallFragmentListener mListener;
    private RecyclerView mGmaraRecyclerView;
    private RecyclerView mMishnaRecyclerView;
    private LinearLayoutManager mMishnaLayoutManager;
    private LinearLayoutManager mGmaraLayoutManager;
    private RecentGemaraAdapter mGemaraAdapter;
    private RecentMishnaAdapter mMishnaAdapter;
    private TextView mTodayDate;
    private TextView mTodayDaf;
    private TextView mWelcomeTV;
    private ImageView mWelcomeIV;
    private LinearLayout mRecentGemara;
    private LinearLayout mRecentMishna;
    private PagesItem mDafYomiDetailes;
    private ImageView mAudioIcon;
    private ImageView mVideoIcon;
    private LinearLayout mLeftBoxLinear;
    private ProgressBar mProgressLine;
    private View mWelcomeView;
    private View mWelcomeIvView;
    private View mRecentGemaraView;
    private View mDafView;


    public WallFragment() {


    }


    public static WallFragment newInstance(PagesItem mDafYomiDetailes) {
        WallFragment fragment = new WallFragment();
        Bundle args = new Bundle();
        args.putSerializable(DAF_YOMI_DETAILES, mDafYomiDetailes);
        fragment.setArguments(args);
        return fragment;
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mDafYomiDetailes = (PagesItem) getArguments().getSerializable(DAF_YOMI_DETAILES);
        }
    }


    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        mListener.showToolBar();
    }


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        return inflater.inflate(R.layout.fragment_wall, container, false);
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        initViews();
        initListeners();
        getHebrewCalender();
        setTheTodayDafYomi();
        chackIfUserIsFirstTimeInTheApp();
        initProgressLine();
        setGmaraRecyclerView();
        setMishnaRecyclerView();

    }


    /**
     * set the daf tomi that loaded in the splash screen
     */
    @SuppressLint("SetTextI18n")
    private void setTheTodayDafYomi() {

        Gson gson = new Gson();
        final TodayDafYomiDetailes dafYomi = gson.fromJson(UserManager.getTodayDafYomi(getActivity()), TodayDafYomiDetailes.class);

        mTodayDaf.setText(dafYomi.getMasechetName() + " " + dafYomi.getMasechetPage());

        if (mDafYomiDetailes != null) {


            if (mDafYomiDetailes.getVideo().equals("")) {

                mVideoIcon.setVisibility(View.GONE);

            }
            if (mDafYomiDetailes.getAudio().equals("")) {

                mAudioIcon.setVisibility(View.GONE);
            }

        }


    }


    /**
     * set the hebrew date
     */
    @SuppressLint({"SetTextI18n"})
    private void getHebrewCalender() {

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
            HebrewCalendar hebrewCalendar = new HebrewCalendar(Locale.getDefault());
            int year = hebrewCalendar.get(Calendar.YEAR);
            int month = hebrewCalendar.get(Calendar.MONTH);
            int day = hebrewCalendar.get(Calendar.DAY_OF_MONTH);
            mTodayDate.setText(theMonth(month) + " " + day + ", " + year);
        }


    }


    /**
     * convert number month to hebrew month
     *
     * @param month, int
     * @return hebrew month
     */
    private static String theMonth(int month) {
        String[] monthNames = {"TISHRI", "HESHVAN", "KISLEV", "TEVET", "SEHVAT", "ADAR_1", "ADAR", "NISSAN", "IYER", "SIVAN", "TAMUZ", "AV", "ELUL"};
        return monthNames[month];
    }


    /**
     * init views
     */
    private void initViews() {

        mWelcomeTV = Objects.requireNonNull(getView()).findViewById(R.id.WF_welcome_TV);
        mWelcomeView = Objects.requireNonNull(getView()).findViewById(R.id.WF_welcome_TV_view);
        mWelcomeIV = getView().findViewById(R.id.WF_welcome_IV);
        mWelcomeIvView = getView().findViewById(R.id.WF_welcome_IV_view2);
        mRecentGemara = getView().findViewById(R.id.WF_recent_gemara_LL);
        mRecentGemaraView = getView().findViewById(R.id.WF_recent_gemara_view);
        mDafView = getView().findViewById(R.id.WF_daf_view);
        mRecentMishna = getView().findViewById(R.id.WF_recent_mishna_LL);
        mGmaraRecyclerView = Objects.requireNonNull(getView()).findViewById(R.id.WF_recycler_gmara_RV);
        mMishnaRecyclerView = getView().findViewById(R.id.WF_recycler_mishna_RV);
        mTodayDate = getView().findViewById(R.id.WF_today_date_TV);
        mTodayDaf = getView().findViewById(R.id.WF_today_daf_TV);
        mAudioIcon = getView().findViewById(R.id.WF_audio_icon_IV);
        mVideoIcon = getView().findViewById(R.id.WF_video_icon_IV);
        mLeftBoxLinear = getView().findViewById(R.id.WF_left_box);
        mProgressLine = getView().findViewById(R.id.WF_progressbar);


    }


    private void initListeners() {

        mAudioIcon.setOnClickListener(this);
        mVideoIcon.setOnClickListener(this);
        mLeftBoxLinear.setOnClickListener(this);
    }


    private void initProgressLine() {

        final DBHandler dbHandler = new DBHandler(getContext());

        mProgressLine.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {


                if (mDafYomiDetailes != null) {

                    PagesItem dbPageItem = dbHandler.findGemaraById(String.valueOf(mDafYomiDetailes.getId()), GEMARA_TABLE);

                    if (dbPageItem != null) {

                        int currentProgress = (int) (TimeUnit.MILLISECONDS.toSeconds(dbPageItem.getTimeLine()) * 100 / dbPageItem.getDuration());
                        mProgressLine.setProgress(currentProgress);

                    }

                }

                mProgressLine.getViewTreeObserver().removeOnGlobalLayoutListener(this);
            }
        });


    }


    private void chackIfUserIsFirstTimeInTheApp() {


        if (UserManager.getRecentGemaraPlayed(getContext()) != null || UserManager.getRecentMishnaPlayed(getContext()) != null) {

            mWelcomeTV.setVisibility(View.GONE);
            mWelcomeView.setVisibility(View.GONE);
            mWelcomeIV.setVisibility(View.GONE);
            mWelcomeIvView.setVisibility(View.GONE);

            mRecentGemara.setVisibility(View.VISIBLE);
            mDafView.setVisibility(View.VISIBLE);
            mRecentGemaraView.setVisibility(View.VISIBLE);
            mRecentMishna.setVisibility(View.VISIBLE);


        }


    }


    /**
     * set gmara recycler view
     */
    private void setGmaraRecyclerView() {

        Gson gson = new Gson();

        Type type = new TypeToken<List<PagesItem>>() {
        }.getType();
        List<PagesItem> recentPagesItemList = gson.fromJson(UserManager.getRecentGemaraPlayed(getContext()), type);

        if (recentPagesItemList != null) {

            Collections.reverse(recentPagesItemList);
        }


        mGmaraLayoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false);
        mGmaraRecyclerView.setLayoutManager(mGmaraLayoutManager);
        mGemaraAdapter = new RecentGemaraAdapter(getContext(), recentPagesItemList, this);
        mGmaraRecyclerView.setAdapter(mGemaraAdapter);

    }


    /**
     * set mishna recycler view
     */
    private void setMishnaRecyclerView() {

        Gson gson = new Gson();

        Type type = new TypeToken<List<MishnayotItem>>() {
        }.getType();
        List<MishnayotItem> recentMishnaItemList = gson.fromJson(UserManager.getRecentMishnaPlayed(getContext()), type);

        if (recentMishnaItemList != null) {

            Collections.reverse(recentMishnaItemList);
        }


        mMishnaLayoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false);
        mMishnaRecyclerView.setLayoutManager(mMishnaLayoutManager);
        mMishnaAdapter = new RecentMishnaAdapter(getContext(), recentMishnaItemList, this);
        mMishnaRecyclerView.setAdapter(mMishnaAdapter);

    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnWallFragmentListener) {
            mListener = (OnWallFragmentListener) context;
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


            case R.id.WF_audio_icon_IV:

                setImageClickedAnimation(mAudioIcon);

                if (isNetworkAvailable()) {

                    mListener.onAudioClicked(mDafYomiDetailes);
                } else {

                    NoInternetDialog noInternetDialog = new NoInternetDialog();
                    noInternetDialog.showDialog(getContext());
                }

                break;


            case R.id.WF_video_icon_IV:

                setImageClickedAnimation(mVideoIcon);

                if (isNetworkAvailable()) {

                    mListener.onVideoClicked(mDafYomiDetailes);
                } else {

                    NoInternetDialog noInternetDialog = new NoInternetDialog();
                    noInternetDialog.showDialog(getContext());
                }

                break;


            case R.id.WF_left_box:

                setViewClickedAnimation(mLeftBoxLinear);

                if (!isNetworkAvailable()) {

                    NoInternetDialog noInternetDialog = new NoInternetDialog();
                    noInternetDialog.showDialog(getContext());

                } else {

                    if (!mDafYomiDetailes.getVideo().equals("")) {

                        mListener.onVideoClicked(mDafYomiDetailes);
                    } else {

                        if (!mDafYomiDetailes.getAudio().equals("")) {
                            mListener.onAudioClicked(mDafYomiDetailes);

                        } else {
                            Toast.makeText(getContext(), getResources().getString(R.string.no_sheurim), Toast.LENGTH_SHORT).show();
                        }
                    }
                }

                break;


        }

    }


    private void setViewClickedAnimation(View mView) {

        Animation animation = AnimationUtils.loadAnimation(getContext(), R.anim.alpha);
        mView.startAnimation(animation);

    }


    private void setImageClickedAnimation(ImageView Icon) {

        Animation animation = AnimationUtils.loadAnimation(getActivity(), R.anim.alpha);
        Icon.startAnimation(animation);

    }


    @Override
    public void onVideoClicked(PagesItem pagesItem) {

        if (isNetworkAvailable()) {

            mListener.onVideoClicked(pagesItem);
        } else {

            NoInternetDialog noInternetDialog = new NoInternetDialog();
            noInternetDialog.showDialog(getContext());
        }
    }


    @Override
    public void onAudioClicked(PagesItem pagesItem) {

        if (isNetworkAvailable()) {

            mListener.onAudioClicked(pagesItem);
        } else {

            NoInternetDialog noInternetDialog = new NoInternetDialog();
            noInternetDialog.showDialog(getContext());
        }

    }


    @Override
    public void onVideoMishnaClicked(MishnayotItem mishnayotItem) {

        if (isNetworkAvailable()) {

            mListener.onVideoMishnaClicked(mishnayotItem);
        } else {

            NoInternetDialog noInternetDialog = new NoInternetDialog();
            noInternetDialog.showDialog(getContext());
        }
    }


    @Override
    public void onAudioMishnaClicked(MishnayotItem mishnayotItem) {

        if (isNetworkAvailable()) {

            mListener.onAudioMishnaClicked(mishnayotItem);
        } else {

            NoInternetDialog noInternetDialog = new NoInternetDialog();
            noInternetDialog.showDialog(getContext());
        }
    }


    /**
     * check if network available
     *
     * @return boolean
     */
    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) Objects.requireNonNull(getContext()).getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = Objects.requireNonNull(connectivityManager).getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }


    public void notifyData(long currentPosition, int currentPageId) {

        DBHandler dbHandler = new DBHandler(getContext());
        PagesItem dbPageItem = dbHandler.findGemaraById(String.valueOf(mDafYomiDetailes.getId()), GEMARA_TABLE);

        if (dbPageItem != null && dbPageItem.getId() == currentPageId) {

            int currentProgress = (int) (TimeUnit.MILLISECONDS.toSeconds(currentPosition) * 100 / dbPageItem.getDuration());
            mProgressLine.setProgress(currentProgress);
            setGmaraRecyclerViewForNewProgress(Objects.requireNonNull(dbPageItem).getId(), currentPosition);
            setMishnaRecyclerViewForNewProgress(Objects.requireNonNull(dbPageItem).getId(), currentPosition);

        } else {

            setGmaraRecyclerViewForNewProgress(currentPageId, currentPosition);
            setMishnaRecyclerViewForNewProgress(currentPageId, currentPosition);

        }


    }


    private void setMishnaRecyclerViewForNewProgress(int id, long currentPosition) {


        Gson gson = new Gson();

        Type type = new TypeToken<List<MishnayotItem>>() {
        }.getType();
        List<MishnayotItem> recentMishnaItemList = gson.fromJson(UserManager.getRecentMishnaPlayed(getContext()), type);

        if (recentMishnaItemList != null) {

            Collections.reverse(recentMishnaItemList);


            for (int i = 0; i < Objects.requireNonNull(recentMishnaItemList).size(); i++) {

                if (recentMishnaItemList.get(i).getId() == id) {
                    recentMishnaItemList.get(i).setTimeLine(currentPosition);
                }

            }


            mMishnaLayoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false);
            mMishnaRecyclerView.setLayoutManager(mMishnaLayoutManager);
            mMishnaAdapter = new RecentMishnaAdapter(getContext(), recentMishnaItemList, this);
            mMishnaRecyclerView.setAdapter(mMishnaAdapter);

        }
    }


    private void setGmaraRecyclerViewForNewProgress(int id, long currentPosition) {


        Gson gson = new Gson();

        Type type = new TypeToken<List<PagesItem>>() {
        }.getType();
        List<PagesItem> recentPagesItemList = gson.fromJson(UserManager.getRecentGemaraPlayed(getContext()), type);

        if (recentPagesItemList != null) {

            Collections.reverse(recentPagesItemList);


            for (int i = 0; i < Objects.requireNonNull(recentPagesItemList).size(); i++) {

                if (recentPagesItemList.get(i).getId() == id) {
                    recentPagesItemList.get(i).setTimeLine(currentPosition);
                }

            }

            mGmaraLayoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false);
            mGmaraRecyclerView.setLayoutManager(mGmaraLayoutManager);
            mGemaraAdapter = new RecentGemaraAdapter(getContext(), recentPagesItemList, this);
            mGmaraRecyclerView.setAdapter(mGemaraAdapter);


        }

    }

    public void refreshRecentPages() {

        if (mGemaraAdapter != null) {

            setGmaraRecyclerView();
        }

        if (mMishnaAdapter != null) {

            setMishnaRecyclerView();
        }

    }


    public interface OnWallFragmentListener {

        void showToolBar();

        void onVideoClicked(PagesItem pagesItem);

        void onAudioClicked(PagesItem pagesItem);

        void onVideoMishnaClicked(MishnayotItem mishnayotItem);

        void onAudioMishnaClicked(MishnayotItem mishnayotItem);
    }
}
