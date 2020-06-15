package co.il.jabrutouch.ui.main.video_screen;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.graphics.Rect;
import android.media.AudioManager;
import android.media.MediaRecorder;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.inputmethod.InputMethodManager;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.amazonaws.mobileconnectors.s3.transferutility.TransferState;
import com.devlomi.record_view.OnBasketAnimationEnd;
import com.devlomi.record_view.OnRecordClickListener;
import com.devlomi.record_view.OnRecordListener;
import com.devlomi.record_view.RecordButton;
import com.devlomi.record_view.RecordView;
import com.dinuscxj.progressbar.CircleProgressBar;
import com.github.barteksc.pdfviewer.PDFView;
import com.google.android.exoplayer2.ExoPlayer;
import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.PlaybackParameters;
import com.google.android.exoplayer2.Player;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.Timeline;
import com.google.android.exoplayer2.source.ExtractorMediaSource;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.source.TrackGroupArray;
import com.google.android.exoplayer2.trackselection.AdaptiveTrackSelection;
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;
import com.google.android.exoplayer2.trackselection.TrackSelection;
import com.google.android.exoplayer2.trackselection.TrackSelectionArray;
import com.google.android.exoplayer2.ui.PlayerView;
import com.google.android.exoplayer2.upstream.BandwidthMeter;
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory;
import com.google.android.exoplayer2.upstream.DefaultHttpDataSourceFactory;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.jcminarro.roundkornerlayout.RoundKornerLinearLayout;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;
import java.lang.reflect.Type;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

import co.il.jabrutouch.R;
import co.il.jabrutouch.data_base_manager.DataBaseManager;
import co.il.jabrutouch.server.RequestManager;
import co.il.jabrutouch.ui.main.audio_screen.AudioActivity;
import co.il.jabrutouch.ui.main.audio_screen.CustomEditText2;
import co.il.jabrutouch.ui.main.donation_screen.DonationActivity;
import co.il.jabrutouch.ui.main.donation_screen.DonationDialogNoDonate;
import co.il.model.model.ChatObject;
import co.il.model.model.ErrorResponse;
import co.il.model.model.LessonDonationBy;
import co.il.model.model.LessonLike;
import co.il.model.model.MessageObject;
import co.il.model.model.Result;
import co.il.jabrutouch.ui.main.audio_screen.AudioReceiver;
import co.il.jabrutouch.ui.main.audio_screen.NotificationPanel;
import co.il.jabrutouch.ui.main.donation_screen.DonationDialog;
import co.il.jabrutouch.user_manager.UserManager;
import co.il.model.model.AnalyticsData;
import co.il.model.model.MishnayotItem;
import co.il.model.model.PagesItem;
import co.il.model.model.User;
import co.il.s3.interfaces.DownloadListener;
import co.il.s3.interfaces.UploadListener;
import co.il.s3.utils.Config;
import co.il.s3.utils.S3Helper;
import co.il.sqlcore.DBHandler;
import de.hdodenhof.circleimageview.CircleImageView;
import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;
import retrofit2.HttpException;

import static co.il.jabrutouch.MainActivity.PAGE_ITEM;
import static co.il.jabrutouch.MainActivity.TALMUD_TYPE;
import static co.il.jabrutouch.MyFireBaseMessagingService.AUDIO_MESSAGE;
import static co.il.jabrutouch.ui.main.audio_screen.AudioActivity.hideKeyboard;
import static co.il.jabrutouch.ui.main.audio_screen.AudioReceiver.ACTION_AUDIO;
import static co.il.jabrutouch.ui.main.message_screen.MessageActivity.AUDIO;
import static co.il.jabrutouch.ui.main.message_screen.MessageActivity.AUDIO_RECORD;
import static co.il.jabrutouch.ui.main.message_screen.MessageActivity.S3_SUB_FOLDER;
import static co.il.jabrutouch.ui.main.message_screen.MessageActivity.TEXT;
import static co.il.model.model.AnalyticsData.GEMARA;
import static co.il.model.model.AnalyticsData.MISHNA;
import static co.il.model.model.AnalyticsData.WATCH;
import static co.il.sqlcore.DBKeys.GEMARA_GALLERY_TABLE;
import static co.il.sqlcore.DBKeys.GEMARA_TABLE;
import static co.il.sqlcore.DBKeys.GEMARA_VIDEO_PARTS_TABLE;
import static co.il.sqlcore.DBKeys.MISHNA_GALLERY_TABLE;
import static co.il.sqlcore.DBKeys.MISHNA_TABLE;
import static co.il.sqlcore.DBKeys.MISHNA_VIDEO_PARTS_TABLE;


public class VideoActivity extends AppCompatActivity implements View.OnClickListener,
        AudioReceiver.audioReceiverListener, DonationDialog.DonationDialogListener, CustomEditText2.CustomEditText2Listener, DonationDialogNoDonate.DonationDialogNoDonateListener {


    private static final int VIDEO_PERMISSIONS_REQUEST_WRITE_EXTERNAL_STORAGE = 1235;
    private static final String POSITION = "POSITION";
    private static final String VIDEO_FILE = "video/";
    private static final String VIDEO = "video";
    private static final String TAG = VideoActivity.class.getSimpleName();
    public static final String DOWNLOAD_VIDEO_PROGRESS = "DOWNLOAD_VIDEO_PROGRESS";
    public static final String VIDEO_PROGRESS = "VIDEO_PROGRESS";
    public static final String VIDEO_PROGRESS_PAGE_ITEN_PARS = "VIDEO_PROGRESS_PAGE_ITEN_PARS";
    public static final String VIDEO_PROGRESS_PAGE_ITEN = "VIDEO_PROGRESS_PAGE_ITEN";
    public static final String IMAGES_LIST = "IMAGES_LIST";
    public static final String CURRENT_PROGRESS = "CURRENT_PROGRESS";
    public static final String CURRENT_PAGE_ID = "CURRENT_PAGE_ID";
    private static final String ASK_THE_RABBI = "Ask the Rabbi: ";
    private static final String M4A = ".m4a";
    private static final int MY_PERMISSIONS_RECORD_AUDIO = 3422;
    private PDFView mPfdView;
    private ProgressBar mProgress;
    private PlayerView mVideoView;
    private static final String PAGES_FILE = "pages/";
    private PagesItem mPageItem;
    private ImageView mPlayBtn;
    private ImageView mFullScreenBtn;
    private Dialog mFullScreenDialog;
    private boolean mExoPlayerFullscreen = false;
    private long position = 0;
    private boolean mFullScreenMode;
    private ImageView mDownArrowBtn;
    private RoundKornerLinearLayout mdownLL;
    private ImageView mSpeedIV;
    private ImageView mBackIV;
    private ImageView mPauseIV;
    private ImageView mForwardIV;
    private ImageView mBackArrowIV;
    private FrameLayout mVideoFrameView;
    private Runnable runnable;
    private Handler handler = new Handler();
    private boolean durationSet;
    private long realDurationMillis;
    private ProgressBar mVideoProgresBar;
    private boolean videoStatus;
    private RelativeLayout mVideoRelative;
    private PagesItem dbPageItem;
    private DBHandler dbHandler;
    private String mTalmudType;
    private TextView mMasechetName;
    private ImageView mDownloadBtnIV;
    private ImageView mMessageBtnIV;
    private ImageView mImageBtnIV;
    private CircleProgressBar mVideoProgressDownloadCPB;
    private boolean downloadPageFinished;
    private NotificationPanel nPanel;
    private AudioReceiver audioReciver;
    private CircleImageView mSpeedBtn;
    private long startTime = 0;
    private long pauseTime = 0;
    private long sumTimeUserUsed = 0;
    private int onlineForAnalytics = 1;
    private RelativeLayout pdfRelative;
    private LinearLayout allViewLL;
    private LinearLayout allButtonsLL;
    private RelativeLayout topViewRL;
    private boolean bottomFrameMode;
    private LinearLayout mLinearTopLL;
    private LinearLayout landButtons;
    private RoundKornerLinearLayout mRoundedShadowRLL;
    private boolean firstTime = true;
    private AnalyticsData analyticsData;
    private Gson gson;
    private LinearLayout mImageLinearLL;
    private AudioManager.OnAudioFocusChangeListener mVideoListsner;
    private DonationDialog donationDialog;
    private ImageView mJumpBach;
    private ImageView mJumpFrd;
    private DottedSeekBar seekBar;
    private LinearLayout linearForSeekBar;
    private LinearLayout mSeekBaeView;
    private CustomEditText2 mMessageET;
    private LinearLayout mMessageBoxLL;
    private View mViewForSeekBar;
    private LinearLayout mLinearForMessageLL;
    private RecordButton mRecordButtonRB;
    private String pathName;
    private RecordView mRecordViewRV;
    private MediaRecorder recorder;
    private TextView mCancelMessageModeTV;
    private Intent serviceIntent;
//    private ImageView mImageBtnForMessageModeIV;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        overridePendingTransition(R.anim.slide_in, R.anim.slide_out);  // set animation to screen to start from the side

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN); // set full screen

        setContentView(R.layout.activity_video);

        serviceIntent = new Intent(this, ClosingService.class);
        startService(serviceIntent);

        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);


        initViews();
        checkIfPageExistInDB();
        showDonationDialog();
        initListeners();
        initPictureIcon();
        initFullScreen();
        downloadPdf(dbPageItem);
        startFromLastState();
        registerReciver();
        initForwardButton();

        if (mVideoView.getPlayer() == null) {

            setVideoPlayer(dbPageItem);

        }


        addVideoPlayerListener();

        analyticsData = new AnalyticsData();
        gson = new Gson();


        AudioManager audioManager = (AudioManager) getSystemService(AUDIO_SERVICE);

        mVideoListsner = initAudioListener();


        int result = Objects.requireNonNull(audioManager).requestAudioFocus(mVideoListsner, AudioManager.STREAM_MUSIC,
                AudioManager.AUDIOFOCUS_GAIN);

        if (result != AudioManager.AUDIOFOCUS_REQUEST_GRANTED) {
            // could not get audio focus.
        }

    }


    private void startFromLastState() {

        if (dbPageItem != null) {

            position = dbPageItem.getTimeLine();
        }


    }


    /**
     * show donation dialog only if user didn't download the page
     */
    private void showDonationDialog() {

        if (dbPageItem == null || dbPageItem.getVideo().equals("")) {




            RequestManager.getLessonDonations(UserManager.getToken(this)).subscribe(new Observer<Result<LessonDonationBy>>() {
                @Override
                public void onSubscribe(Disposable d) {

                }

                @Override
                public void onNext(Result<LessonDonationBy> lessonDonationByResult) {

                    setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

                    if (lessonDonationByResult.getData().getDonatedBy().size() == 0){

                        DonationDialogNoDonate donationDialogNoDonate = new DonationDialogNoDonate();
                        donationDialogNoDonate.showDialog(VideoActivity.this, VideoActivity.this);
                    }
                    else {

                        DonationDialog donationDialog = new DonationDialog();
                        donationDialog.showDialog(VideoActivity.this, VideoActivity.this, lessonDonationByResult.getData());

                    }


                }

                @Override
                public void onError(Throwable e) {

                }

                @Override
                public void onComplete() {

                }
            });


//            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
//            donationDialog = new DonationDialog();
//            donationDialog.showDialog(this, this, lessonDonationByResult.getData().getDonatedBy());
        }

    }


    @Override
    protected void onResume() {
        super.onResume();

        if (!firstTime) {

            startTime = System.currentTimeMillis();
        }


    }


    @Override
    protected void onPause() {
        super.onPause();

    }


    private void initPictureIcon() {

        if (mPageItem != null) {

            if (mPageItem.getGallery() == null || mPageItem.getGallery().size() == 0) {

                mImageBtnIV.setBackground(ContextCompat.getDrawable(this, R.drawable.ic_image_gray));


            }
        }

        if (dbPageItem != null) {

            if (dbPageItem.getGallery() == null || dbPageItem.getGallery().size() == 0) {

                mImageBtnIV.setBackground(ContextCompat.getDrawable(this, R.drawable.ic_image_gray));
//                mImageBtnForMessageModeIV.setBackground(ContextCompat.getDrawable(this, R.drawable.ic_image_gray));


            }
        }


    }


    private void addVideoPlayerListener() {

        mVideoView.getPlayer().addListener(new Player.DefaultEventListener() {
            @Override
            public void onPlayerStateChanged(boolean playWhenReady, int playbackState) {
                if (playWhenReady && playbackState == Player.STATE_READY) {
                    // media actually playing
                    firstTime = false;

                    if (mTalmudType.equals(GEMARA_TABLE)) {

                        nPanel = new NotificationPanel(VideoActivity.this, mPageItem.getMasechetName() + " " + mPageItem.getPageNumber(), mVideoView.getPlayer().getPlayWhenReady(), true);

                    } else if (mTalmudType.equals(MISHNA_TABLE)) {

                        nPanel = new NotificationPanel(VideoActivity.this, mPageItem.getMasechetName() + " " + mPageItem.getChapter() + "-" + mPageItem.getPageNumber(), mVideoView.getPlayer().getPlayWhenReady(), false);


                    }

                    startTime = System.currentTimeMillis();
                    pauseTime = 0;


                    analyticsData.setEvent(WATCH);

                    if (mTalmudType.equals(GEMARA_TABLE)) {

                        analyticsData.setCategory(GEMARA);

                    } else if (mTalmudType.equals(MISHNA_TABLE)) {

                        analyticsData.setCategory(MISHNA);
                    }

                    analyticsData.setMediaType(AnalyticsData.VIDEO);

                    analyticsData.setPageId(String.valueOf(mPageItem.getId()));
                    analyticsData.setTimeStart(System.currentTimeMillis());

                    analyticsData.setOnline(1);
                    analyticsData.setPlaying(true);
                    String analyticsDataString = gson.toJson(analyticsData);
                    UserManager.setAnalyticsData(analyticsDataString, VideoActivity.this);


                } else if (playWhenReady) {
                    // might be idle (plays after prepare()),
                    // buffering (plays when data available)
                    // or ended (plays when seek away from end)

                    if (mTalmudType.equals(GEMARA_TABLE)) {

                        nPanel = new NotificationPanel(VideoActivity.this, mPageItem.getMasechetName() + " " + mPageItem.getPageNumber(), mVideoView.getPlayer().getPlayWhenReady(), true);

                    } else if (mTalmudType.equals(MISHNA_TABLE)) {

                        nPanel = new NotificationPanel(VideoActivity.this, mPageItem.getMasechetName() + " " + mPageItem.getChapter() + "-" + mPageItem.getPageNumber(), mVideoView.getPlayer().getPlayWhenReady(), false);


                    }


                    pauseTime = System.currentTimeMillis();
                    if (startTime != 0) {

                        sumTimeUserUsed += (pauseTime - startTime);
                        startTime = 0;
                    }

                } else {


                    pauseTime = System.currentTimeMillis();
                    if (startTime != 0) {

                        if (mTalmudType.equals(GEMARA_TABLE)) {

                            nPanel = new NotificationPanel(VideoActivity.this, mPageItem.getMasechetName() + " " + mPageItem.getPageNumber(), mVideoView.getPlayer().getPlayWhenReady(), true);

                        } else if (mTalmudType.equals(MISHNA_TABLE)) {

                            nPanel = new NotificationPanel(VideoActivity.this, mPageItem.getMasechetName() + " " + mPageItem.getChapter() + "-" + mPageItem.getPageNumber(), mVideoView.getPlayer().getPlayWhenReady(), false);


                        }


                        sumTimeUserUsed += (pauseTime - startTime);

                        analyticsData.setDuration(sumTimeUserUsed);
                        analyticsData.setPlaying(false);

                        String analyticsDataString = gson.toJson(analyticsData);
                        UserManager.setAnalyticsData(analyticsDataString, VideoActivity.this);

                        startTime = 0;
                    }
//                     player paused in any state
                }
            }
        });
    }


    private void registerReciver() {

        audioReciver = new AudioReceiver(this);
        IntentFilter filter = new IntentFilter(ACTION_AUDIO);
        registerReceiver(audioReciver, filter);

    }


    @Override
    protected void onDestroy() {
        unregisterReceiver(audioReciver);

        mVideoListsner = null;
        if (mVideoView != null && mVideoView.getPlayer() != null) {

            mVideoView.getPlayer().stop();

            if (nPanel != null) {

                nPanel.notificationCancel();
            }

            saveUserState(mVideoView.getPlayer().getCurrentPosition());
            mVideoView = null;

        }

        stopService(serviceIntent);

        super.onDestroy();
    }


    private void saveUserState(long currentPosition) {

        dbHandler.updateTimeLineInTable(String.valueOf(mPageItem.getId()), currentPosition, mTalmudType);
        if (donationDialog != null) {
            donationDialog.dismiss();
            donationDialog = null;
        }

    }


    private void checkIfPageExistInDB() {

        if (mTalmudType.equals(GEMARA_TABLE)) {

            dbPageItem = dbHandler.findGemaraById(String.valueOf(mPageItem.getId()), GEMARA_TABLE);

            if (dbPageItem != null) {

                dbPageItem.setVideoPart(dbHandler.getVideoPart(GEMARA_VIDEO_PARTS_TABLE, String.valueOf(mPageItem.getId())));
            }

            if (dbPageItem != null) {

                dbPageItem.setGallery(dbHandler.getGallery(GEMARA_GALLERY_TABLE, String.valueOf(mPageItem.getId())));
            }


        } else if (mTalmudType.equals(MISHNA_TABLE)) {

            MishnayotItem dbMishnaItem = dbHandler.findMishnaById(String.valueOf(mPageItem.getId()), MISHNA_TABLE);
            if (dbMishnaItem != null) {

                dbPageItem = genaretMishnaItemToPageItem(dbMishnaItem);
            }


            if (dbPageItem != null) {

                dbPageItem.setVideoPart(dbHandler.getVideoPartForMishna(MISHNA_VIDEO_PARTS_TABLE, String.valueOf(mPageItem.getId())));
            }

            if (dbPageItem != null) {

                dbPageItem.setGallery(dbHandler.getMishnaGallery(MISHNA_GALLERY_TABLE, String.valueOf(mPageItem.getId())));
            }

        }


    }


    private PagesItem genaretMishnaItemToPageItem(MishnayotItem mishnayotItem) {

        PagesItem pagesItem = new PagesItem();
        pagesItem.setDuration(mishnayotItem.getDuration());
        pagesItem.setChapter(mishnayotItem.getChapter());
        pagesItem.setPageNumber(mishnayotItem.getMishna());
        pagesItem.setPresenter(mishnayotItem.getPresenter());
        pagesItem.setVideoPart(mishnayotItem.getVideoPart());
        pagesItem.setId(mishnayotItem.getId());
        pagesItem.setAudio(mishnayotItem.getAudio());
        pagesItem.setVideo(mishnayotItem.getVideo());
        pagesItem.setPage(mishnayotItem.getPage());
        pagesItem.setGallery(mishnayotItem.getGallery());
        pagesItem.setMasechetOrder(mishnayotItem.getMasechetOrder());
        pagesItem.setSederOrder(mishnayotItem.getSederOrder());
        pagesItem.setMasechetName(mishnayotItem.getMasechetName());
        pagesItem.setTimeLine(mishnayotItem.getTimeLine());
        pagesItem.setMediaType(mishnayotItem.getMediaType());

        return pagesItem;


    }


    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);

        int newOrientation = newConfig.orientation;

        if (!mFullScreenMode) {

            if (newOrientation == Configuration.ORIENTATION_LANDSCAPE) {

                int width = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 295, getResources().getDisplayMetrics());

                int Height = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 75, getResources().getDisplayMetrics());

                RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(
                        width,
                        Height
                );

                params.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
                params.addRule(RelativeLayout.CENTER_HORIZONTAL);

                mRoundedShadowRLL.setLayoutParams(params);

                if (!bottomFrameMode) {


                    RelativeLayout.LayoutParams imParams =
                            new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);

                    topViewRL.removeView(mVideoRelative);
                    topViewRL.setVisibility(View.GONE);
                    mdownLL.addView(mVideoRelative, imParams);


                    mdownLL.setVisibility(View.VISIBLE);
                    mRoundedShadowRLL.setVisibility(View.VISIBLE);
                    mVideoView.setUseController(false);
                    allButtonsLL.setVisibility(View.VISIBLE);
                }

                mLinearTopLL.setVisibility(View.GONE);
                landButtons.setVisibility(View.VISIBLE);

                RelativeLayout.LayoutParams params2 = new RelativeLayout.LayoutParams(
                        RelativeLayout.LayoutParams.MATCH_PARENT,
                        RelativeLayout.LayoutParams.MATCH_PARENT
                );
                params2.setMargins(0, 0, 0, 0);
                linearForSeekBar.setLayoutParams(params2);

                seekBar.setThumb(null);
//                seekBar.setDots(new int[]{Integer.parseInt(mPageItem.getVideoPart().get(0).getVideoPartTimeLine()), Integer.parseInt(mPageItem.getVideoPart().get(1).getVideoPartTimeLine())});
                seekBar.setDotsDrawable(R.drawable.ic_dote_yellow, -5);

                int Height2 = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 5, getResources().getDisplayMetrics());

                RelativeLayout.LayoutParams paramsForSeekBar = new RelativeLayout.LayoutParams(
                        RelativeLayout.LayoutParams.FILL_PARENT,
                        Height2
                );
                paramsForSeekBar.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
                seekBar.setLayoutParams(paramsForSeekBar);

                mImageBtnIV = findViewById(R.id.VA_image_btn_IV_land);
                mDownloadBtnIV = findViewById(R.id.VA_download_btn_IV_land);
                mMessageBtnIV = findViewById(R.id.VA_message_btn_IV_land);
                mBackArrowIV = findViewById(R.id.VA_arrow_back_IV_land);

                initPictureIcon();
                initListeners();
                checkStatusForPlayIcon();


                float speedStstus = mVideoView.getPlayer().getPlaybackParameters().speed;


                if (speedStstus == 1.0) {

                    mSpeedIV.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.ic_jabru_speed_selector));

                } else if (speedStstus == 1.5) {

                    mSpeedIV.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.ic_audio_speed_x2_selector));


                } else {

                    mSpeedIV.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.ic_audio_speed_x3_selector));

                }


            } else {


                mLinearTopLL.setVisibility(View.VISIBLE);
                landButtons.setVisibility(View.GONE);

                RelativeLayout.LayoutParams paramsForTopLinear = new RelativeLayout.LayoutParams(
                        RelativeLayout.LayoutParams.MATCH_PARENT,
                        RelativeLayout.LayoutParams.MATCH_PARENT
                );

                int dpValue = 12; // margin in dips
                float d = this.getResources().getDisplayMetrics().density;
                int margin = (int) (dpValue * d); // margin in pixels

                paramsForTopLinear.setMargins(0, 0, 0, margin);
                linearForSeekBar.setLayoutParams(paramsForTopLinear);

                RelativeLayout.LayoutParams imParams2 =
                        new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
                mdownLL.removeView(mVideoRelative);
                topViewRL.addView(mVideoRelative, imParams2);
                topViewRL.setVisibility(View.VISIBLE);
                mdownLL.setVisibility(View.GONE);
                mRoundedShadowRLL.setVisibility(View.GONE);
                mVideoView.setUseController(true);
                allButtonsLL.setVisibility(View.GONE);
                bottomFrameMode = false;

                mImageBtnIV = findViewById(R.id.VA_image_btn_IV);
                mDownloadBtnIV = findViewById(R.id.VA_download_btn_IV);
                mMessageBtnIV = findViewById(R.id.VA_message_btn_IV);
                mBackArrowIV = findViewById(R.id.VA_arrow_back_IV);

                seekBar.setThumb(ContextCompat.getDrawable(this, R.drawable.custom_thumb));
//                seekBar.setDots(new int[]{Integer.parseInt(mPageItem.getVideoPart().get(0).getVideoPartTimeLine()), Integer.parseInt(mPageItem.getVideoPart().get(1).getVideoPartTimeLine())});
                seekBar.setDotsDrawable(R.drawable.ic_dote, 5);

                RelativeLayout.LayoutParams paramsForTopSeekBar = new RelativeLayout.LayoutParams(
                        RelativeLayout.LayoutParams.FILL_PARENT,
                        RelativeLayout.LayoutParams.WRAP_CONTENT
                );
                paramsForTopSeekBar.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
                seekBar.setLayoutParams(paramsForTopSeekBar);

                initPictureIcon();
                checkStatusForPlayIcon();


                float speedStstus = mVideoView.getPlayer().getPlaybackParameters().speed;


                if (speedStstus == 1.0) {

                    mSpeedBtn.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.ic_exo_speed_x1));

                } else if (speedStstus == 1.5) {

                    mSpeedBtn.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.ic_exo_speed_x2));


                } else {

                    mSpeedBtn.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.ic_exo_speed_x3));

                }


            }

        }


    }


    private void checkStatusForPlayIcon() {

        if (mVideoView.getPlayer().getPlaybackState() == Player.STATE_READY && mVideoView.getPlayer().getPlayWhenReady()) {

            mPauseIV.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.ic_jabru_pause_selector));
            int paddingDp = 10;
            float density = this.getResources().getDisplayMetrics().density;
            int paddingPixel = (int) (paddingDp * density);
            mPauseIV.setPadding(paddingPixel, paddingPixel, paddingPixel, paddingPixel);

        } else {

            mPauseIV.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.ic_play_selector));
        }

    }


    private void initViews() {

        dbHandler = new DBHandler(this);

        mPageItem = (PagesItem) getIntent().getSerializableExtra(PAGE_ITEM);
        mTalmudType = getIntent().getStringExtra(TALMUD_TYPE);
        mPfdView = findViewById(R.id.VA_pdfView_PDFV);
        mProgress = findViewById(R.id.VA_progress_bar);
        mVideoView = findViewById(R.id.VA_video_view);
        mPlayBtn = findViewById(R.id.exo_play);
        mFullScreenBtn = findViewById(R.id.exo_fullscreen_icon);
        mDownArrowBtn = findViewById(R.id.exo_down_arrow_IV);
        mSpeedBtn = findViewById(R.id.exo_speed_IV);
        mdownLL = findViewById(R.id.VA_buttom_view_RKLL);
        mMasechetName = findViewById(R.id.VA_name_TV);
        mSpeedIV = findViewById(R.id.VA_speed_IV);
        mBackIV = findViewById(R.id.VA_back30_IV);
        mPauseIV = findViewById(R.id.VA_pause_IV);
        mForwardIV = findViewById(R.id.VA_forward10_IV);
        mBackArrowIV = findViewById(R.id.VA_arrow_back_IV);
        mVideoFrameView = findViewById(R.id.VA_media_frame);
        mVideoProgresBar = findViewById(R.id.VA_video_progress_bar);
        mVideoRelative = findViewById(R.id.VA_video_relativ_RL);
        mDownloadBtnIV = findViewById(R.id.VA_download_btn_IV);
        mMessageBtnIV = findViewById(R.id.VA_message_btn_IV);
        mImageBtnIV = findViewById(R.id.VA_image_btn_IV);
        mVideoProgressDownloadCPB = findViewById(R.id.VA_progress_bar_PB);
        pdfRelative = findViewById(R.id.VA_pdf_RL);
        allViewLL = findViewById(R.id.VA_all_View_LL);
        allButtonsLL = findViewById(R.id.VA_all_buttons_LL);
        topViewRL = findViewById(R.id.VA_top_video_RL);
        landButtons = findViewById(R.id.VA_land_buttons_LL);
        mRoundedShadowRLL = findViewById(R.id.VA_rounded_shadow_RLL);
        mImageLinearLL = findViewById(R.id.VA_image_linear_LL);
        mJumpBach = findViewById(R.id.exo_video_back);
        mJumpFrd = findViewById(R.id.exo_video_frd);
        seekBar = findViewById(R.id.VA_seekbar_SB);
        linearForSeekBar = findViewById(R.id.VA_linear_for_seekbar_LL);
        mSeekBaeView = findViewById(R.id.VA_view_for_seekbar_V);
        mMessageET = findViewById(R.id.VA_message_ET);
        mMessageBoxLL = findViewById(R.id.VA_message_box_LL);
        mViewForSeekBar = findViewById(R.id.VA_view_for_keyboard);
        mLinearTopLL = findViewById(R.id.VA_top_buttons_LL);
        mLinearForMessageLL = findViewById(R.id.VA_linear_for_message);
        mCancelMessageModeTV = findViewById(R.id.VA_cancel_TV);
//        mImageBtnForMessageModeIV = findViewById(R.id.VA_image_btn_in_message_mode_IV);
        mRecordButtonRB = findViewById(R.id.VA_record_button);
        mRecordViewRV = findViewById(R.id.VA_record_view);

        initTopBar();

    }


    @SuppressLint("SetTextI18n")
    private void initTopBar() {

        if (mTalmudType.equals(GEMARA_TABLE)) {

            mMasechetName.setText(mPageItem.getMasechetName() + " " + mPageItem.getPageNumber());

        } else if (mTalmudType.equals(MISHNA_TABLE)) {

            mMasechetName.setText(mPageItem.getMasechetName() + " " + mPageItem.getChapter() + "-" + mPageItem.getPageNumber());


        }
    }


    private void initListeners() {

        mDownArrowBtn.setOnClickListener(this);
        mSpeedBtn.setOnClickListener(this);
        mSpeedIV.setOnClickListener(this);
        mBackIV.setOnClickListener(this);
        mPauseIV.setOnClickListener(this);
        mForwardIV.setOnClickListener(this);
        mBackArrowIV.setOnClickListener(this);
        mVideoFrameView.setOnClickListener(this);
        mDownloadBtnIV.setOnClickListener(this);
        mMessageBtnIV.setOnClickListener(this);
        mImageBtnIV.setOnClickListener(this);
        mJumpBach.setOnClickListener(this);
        mJumpFrd.setOnClickListener(this);

        if (mCancelMessageModeTV != null) {

            mCancelMessageModeTV.setOnClickListener(this);
        }

//        if (mImageBtnForMessageModeIV != null) {
//
//            mImageBtnForMessageModeIV.setOnClickListener(this);
//        }


    }


    private void initFullScreen() {


        initFullscreenDialog();

        mFullScreenBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                if (!mExoPlayerFullscreen) {
                    mFullScreenMode = true;
                    openFullscreenDialog();
                    mDownArrowBtn.setVisibility(View.GONE);
                    setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
                } else {
                    mFullScreenMode = false;

                    closeFullscreenDialog();
                    mDownArrowBtn.setVisibility(View.VISIBLE);
                    setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED);

                    final Handler handler = new Handler();
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            findIfLandScape();
                        }
                    }, 100);

                }
            }
        });

    }


    /**
     * find if the screen is land after closed full screen
     */
    private void findIfLandScape() {


        if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {


            int width = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 295, getResources().getDisplayMetrics());

            int Height = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 75, getResources().getDisplayMetrics());

            RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(
                    width,
                    Height
            );

            params.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
            params.addRule(RelativeLayout.CENTER_HORIZONTAL);

            mRoundedShadowRLL.setLayoutParams(params);


            RelativeLayout.LayoutParams imParams =
                    new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);

            topViewRL.removeView(mVideoRelative);
            topViewRL.setVisibility(View.GONE);
            mdownLL.addView(mVideoRelative, imParams);


            mRoundedShadowRLL.setVisibility(View.VISIBLE);
            mdownLL.setVisibility(View.VISIBLE);
            mVideoView.setUseController(false);
            allButtonsLL.setVisibility(View.VISIBLE);

            RelativeLayout.LayoutParams params2 = new RelativeLayout.LayoutParams(
                    RelativeLayout.LayoutParams.MATCH_PARENT,
                    RelativeLayout.LayoutParams.MATCH_PARENT
            );
            params2.setMargins(0, 0, 0, 0);
            linearForSeekBar.setLayoutParams(params2);


            seekBar.setThumb(null);
//            seekBar.setDots(new int[]{Integer.parseInt(mPageItem.getVideoPart().get(0).getVideoPartTimeLine()), Integer.parseInt(mPageItem.getVideoPart().get(1).getVideoPartTimeLine())});
            seekBar.setDotsDrawable(R.drawable.ic_dote_yellow, -5);


            int Height2 = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 5, getResources().getDisplayMetrics());

            RelativeLayout.LayoutParams paramsForSeekBar = new RelativeLayout.LayoutParams(
                    RelativeLayout.LayoutParams.FILL_PARENT,
                    Height2
            );
            paramsForSeekBar.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
            seekBar.setLayoutParams(paramsForSeekBar);


            mLinearTopLL.setVisibility(View.GONE);
            landButtons.setVisibility(View.VISIBLE);

            mImageBtnIV = findViewById(R.id.VA_image_btn_IV_land);
            mDownloadBtnIV = findViewById(R.id.VA_download_btn_IV_land);
            mMessageBtnIV = findViewById(R.id.VA_message_btn_IV_land);
            mBackArrowIV = findViewById(R.id.VA_arrow_back_IV_land);

            initPictureIcon();
            initListeners();
            checkStatusForPlayIcon();


            float speedStstus = mVideoView.getPlayer().getPlaybackParameters().speed;


            if (speedStstus == 1.0) {

                mSpeedIV.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.ic_jabru_speed_selector));

            } else if (speedStstus == 1.5) {

                mSpeedIV.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.ic_audio_speed_x2_selector));


            } else {

                mSpeedIV.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.ic_audio_speed_x3_selector));

            }
        }


    }


    private void initFullscreenDialog() {

        mFullScreenDialog = new Dialog(this, android.R.style.Theme_Black_NoTitleBar_Fullscreen) {

            public void onBackPressed() {
                if (mExoPlayerFullscreen) {
                    mFullScreenMode = false;
                    closeFullscreenDialog();
                    setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED);

                    final Handler handler = new Handler();
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            findIfLandScape();
                        }
                    }, 100);

                }
                super.onBackPressed();
            }
        };
    }


    private void openFullscreenDialog() {

        RelativeLayout.LayoutParams paramsForTopLinear = new RelativeLayout.LayoutParams(
                RelativeLayout.LayoutParams.MATCH_PARENT,
                RelativeLayout.LayoutParams.MATCH_PARENT
        );


        paramsForTopLinear.setMargins(0, 0, 0, 0);
        linearForSeekBar.setLayoutParams(paramsForTopLinear);


        RelativeLayout.LayoutParams imParams = new
                RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        mVideoRelative.removeView(seekBar);
        mSeekBaeView.addView(seekBar, imParams);


        seekBar.setProgressDrawable(ContextCompat.getDrawable(Objects.requireNonNull(this), R.drawable.custom_seekbar_bar_for_record));
        seekBar.setDotsDrawable(R.drawable.ic_dote_yellow, 10);

        ((ViewGroup) mVideoRelative.getParent()).removeView(mVideoRelative);
        mFullScreenDialog.addContentView(mVideoRelative, new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        mFullScreenBtn.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.ic_video_minimize_screen));
        mExoPlayerFullscreen = true;
        mFullScreenDialog.show();
    }


    private void closeFullscreenDialog() {

        RelativeLayout.LayoutParams imParams = new
                RelativeLayout.LayoutParams(ViewGroup.LayoutParams.FILL_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);

        imParams.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
        seekBar.setLayoutParams(imParams);

        seekBar.setProgressDrawable(ContextCompat.getDrawable(Objects.requireNonNull(this), R.drawable.custom_seekbar_bar));

        mSeekBaeView.removeView(seekBar);
        mVideoRelative.addView(seekBar, imParams);

        ((ViewGroup) mVideoRelative.getParent()).removeView(mVideoRelative);

        mExoPlayerFullscreen = false;
        mFullScreenDialog.dismiss();
        mFullScreenBtn.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.ic_video_full));
    }


    private void downloadPdf(PagesItem dbPageItem) {


        if (dbPageItem != null && dbPageItem.getPage() != null && !dbPageItem.getPage().equals("")) {

            mProgress.setVisibility(View.GONE);
            mPfdView.fromFile(new File(dbPageItem.getPage())).load();

        } else if (!mPageItem.getPage().equals("")) {


            if (checkPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE, VIDEO_PERMISSIONS_REQUEST_WRITE_EXTERNAL_STORAGE)) {

                mProgress.setVisibility(View.VISIBLE);
                S3Helper s3Helper = new S3Helper(this);
                s3Helper.downloadFile(this, mPageItem.getPage(), mPageItem.getPage(), PAGES_FILE, new DownloadListener() {
                    @Override
                    public void onDownloadStart(int id, File fileLocation) {
                    }

                    @Override
                    public void onProgressChanged(int percentDone) {

                    }

                    @Override
                    public void onDownloadWaiting(int id, TransferState state, File fileLocation) {

                    }

                    @Override
                    public void onDownloadReceivedStop(int id, TransferState state, File fileLocation) {

                    }

                    @Override
                    public void onDownloadFinish(int id, String link, String pathName, String pagePathName) {
                        mPfdView.fromFile(new File(pagePathName)).load();
                        mPageItem.setPage(pagePathName);
                        addDownloadPageToDB();
                        downloadPageFinished = true;
                        mProgress.setVisibility(View.GONE);
//                        mPageItem = (PagesItem) getIntent().getSerializableExtra(PAGE_ITEM);


                    }

                    @Override
                    public void onDownloadError() {

                    }
                });

            }
        }


    }


    /**
     * check if request granted or denied
     *
     * @param requestCode  int
     * @param permissions  String[]
     * @param grantResults int[]
     */
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {

            case VIDEO_PERMISSIONS_REQUEST_WRITE_EXTERNAL_STORAGE:

                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    mProgress.setVisibility(View.VISIBLE);
                    S3Helper s3Helper = new S3Helper(this);
                    s3Helper.downloadFile(this, mPageItem.getPage(), mPageItem.getPage(), PAGES_FILE, new DownloadListener() {
                        @Override
                        public void onDownloadStart(int id, File fileLocation) {
                        }

                        @Override
                        public void onProgressChanged(int percentDone) {

                        }

                        @Override
                        public void onDownloadWaiting(int id, TransferState state, File fileLocation) {

                        }

                        @Override
                        public void onDownloadReceivedStop(int id, TransferState state, File fileLocation) {

                        }

                        @Override
                        public void onDownloadFinish(int id, String link, String pathName, String pagePathName) {
                            mPfdView.fromFile(new File(pagePathName)).load();
                            mPageItem.setPage(pagePathName);
                            addDownloadPageToDB();
                            downloadPageFinished = true;
                            mProgress.setVisibility(View.GONE);
//                        mPageItem = (PagesItem) getIntent().getSerializableExtra(PAGE_ITEM);


                        }

                        @Override
                        public void onDownloadError() {

                        }
                    });

                }

                break;


        }

    }


    private void addDownloadPageToDB() {

        DataBaseManager dataBaseManager = new DataBaseManager();
        dataBaseManager.addPageToDB(this, mPageItem, mTalmudType);

    }


    private void setVideoPlayer(PagesItem dbPageItem) {

        TrackSelection.Factory videoTrackSelectionFactory =
                new AdaptiveTrackSelection.Factory(new BandwidthMeter() {
                    @Override
                    public long getBitrateEstimate() {
                        return 0;
                    }
                });
        DefaultTrackSelector trackSelector = new DefaultTrackSelector(videoTrackSelectionFactory);
        SimpleExoPlayer player = ExoPlayerFactory.newSimpleInstance(VideoActivity.this, trackSelector);
        mVideoView.setPlayer(player);
        player.setPlayWhenReady(videoStatus);

        Uri uri;
        MediaSource mediaSource;

        if (dbPageItem != null && !dbPageItem.getVideo().equals("")) {

            uri = Uri.parse(dbPageItem.getVideo());
            onlineForAnalytics = 0;
            mediaSource = buildLocalMediaSource(uri);
            mDownloadBtnIV.setVisibility(View.GONE);

        } else {

            uri = Uri.parse(mPageItem.getVideo());
            mediaSource = buildMediaSource(uri);
        }


        player.prepare(mediaSource);

        if (position != 0) {

            player.seekTo(position);

        }


        mVideoView.getPlayer().addListener(new ExoPlayer.DefaultEventListener() {
            @Override
            public void onTimelineChanged(Timeline timeline, Object manifest, int reason) {

            }

            @Override
            public void onTracksChanged(TrackGroupArray trackGroups, TrackSelectionArray trackSelections) {

            }

            @Override
            public void onLoadingChanged(boolean isLoading) {

            }

            @Override
            public void onPlayerStateChanged(boolean playWhenReady, int playbackState) {
                if (playbackState == Player.STATE_READY && !durationSet) {

                    mVideoProgresBar.setVisibility(View.GONE);

//                                realDurationMillis = mVideoView.getPlayer().getDuration();
//                                durationSet = true;
                    addSeekBar(mVideoView.getPlayer().getDuration());

                }

            }

        });


    }


    private MediaSource buildLocalMediaSource(Uri uri) {
        return new ExtractorMediaSource.Factory(
                new DefaultDataSourceFactory(this, "Exoplayer-local")).
                createMediaSource(uri);
    }


    private void addSeekBar(long duration) {

        seekBar.setMax((int) duration);
        seekBar.setProgress(0);

        if (!mFullScreenMode) {

            List<Integer> partTimeListInt = new ArrayList<>();

            if (mPageItem.getVideoPart() != null) {

                for (int i = 0; i < mPageItem.getVideoPart().size(); i++) {

                    partTimeListInt.add(Integer.valueOf(mPageItem.getVideoPart().get(i).getVideoPartTimeLine()));
                }
            } else if (dbPageItem.getVideoPart() != null) {

                for (int i = 0; i < dbPageItem.getVideoPart().size(); i++) {

                    partTimeListInt.add(Integer.valueOf(dbPageItem.getVideoPart().get(i).getVideoPartTimeLine()));
                }


            }


            seekBar.setDots(partTimeListInt);
//            seekBar.setDots(new int[]{Integer.parseInt(mPageItem.getVideoPart().get(0).getVideoPartTimeLine()), Integer.parseInt(mPageItem.getVideoPart().get(1).getVideoPartTimeLine())});
            seekBar.setDotsDrawable(R.drawable.ic_dote, 5);

        }


        changeSeekBar(seekBar, mVideoView.getPlayer());

        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if (fromUser) {
                    mVideoView.getPlayer().seekTo(progress);
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });


    }


    private void changeSeekBar(final SeekBar mSeekBar, final Player player) {

        mSeekBar.setProgress((int) player.getCurrentPosition());

        if (mVideoView != null) {
            if (mVideoView.getPlayer() != null) {

                if (mVideoView.getPlayer().getPlaybackState() == Player.STATE_READY && mVideoView.getPlayer().getPlayWhenReady()) {
                    runnable = new Runnable() {
                        @Override
                        public void run() {

                            changeSeekBar(mSeekBar, player);

                            if (mVideoView != null && mVideoView.getPlayer() != null) {

                                if (mVideoView.getPlayer().getPlaybackState() == Player.STATE_READY && mVideoView.getPlayer().getPlayWhenReady()) {


                                    int duration = (int) player.getCurrentPosition();
                                    @SuppressLint("DefaultLocale") String time = String.format("%02d:%02d ",
                                            TimeUnit.MILLISECONDS.toMinutes(duration),
                                            TimeUnit.MILLISECONDS.toSeconds(duration) -
                                                    TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(duration))
                                    );


                                }
                            }
                        }
                    };

                    handler.postDelayed(runnable, 1000);
                }
            }
        }

    }


    private MediaSource buildMediaSource(Uri uri) {
        return new ExtractorMediaSource.Factory(
                new DefaultHttpDataSourceFactory("exoplayer-codelab")).
                createMediaSource(uri);
    }


    @Override
    public void onBackPressed() {

        if (mVideoView != null && mVideoView.getPlayer() != null) {

            mVideoView.getPlayer().stop();
            if (nPanel != null) {

                nPanel.notificationCancel();
            }

            if (startTime != 0 && mVideoView.getPlayer().getPlaybackState() == Player.STATE_READY) {
//
                pauseTime = System.currentTimeMillis();

                sumTimeUserUsed += (pauseTime - startTime);
                startTime = 0;
            }

            sendAnalyticsData(sumTimeUserUsed);


        }

        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED);

        Intent returnIntent = new Intent();
        returnIntent.putExtra(CURRENT_PROGRESS, mVideoView.getPlayer().getCurrentPosition());
        returnIntent.putExtra(CURRENT_PAGE_ID, mPageItem.getId());
        setResult(Activity.RESULT_OK, returnIntent);
        finish();

    }


    private void sendAnalyticsData(long sumTimeUserUsed) {

        AnalyticsData analyticsData = new AnalyticsData();
        analyticsData.setEvent(WATCH);

        if (mTalmudType.equals(GEMARA_TABLE)) {

            analyticsData.setCategory(GEMARA);

        } else if (mTalmudType.equals(MISHNA_TABLE)) {

            analyticsData.setCategory(MISHNA);
        }

        analyticsData.setMediaType(AnalyticsData.VIDEO);
        analyticsData.setPageId(String.valueOf(mPageItem.getId()));
        analyticsData.setDuration(sumTimeUserUsed);
        analyticsData.setOnline(onlineForAnalytics);


        RequestManager.sendAnalytics(UserManager.getToken(this), analyticsData).subscribe(new Observer<Result>() {
            @Override
            public void onSubscribe(Disposable d) {

            }

            @Override
            public void onNext(Result result) {
                UserManager.setAnalyticsData(null, VideoActivity.this);

            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onComplete() {

            }
        });


    }


    /**
     * Check if permission "Manifest.permission.READ_EXTERNAL_STORAGE" or
     * "Manifest.permission.WRITE_EXTERNAL_STORAGE" was granted.
     * If the permission wasn't granted, the app request it and the result is handled at:
     */
    private static boolean checkPermission(Activity activity, String permission,
                                           int permissionCode) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (activity.checkSelfPermission(permission)
                    != PackageManager.PERMISSION_GRANTED) {

                // Should we show an explanation?
                if (activity.shouldShowRequestPermissionRationale(permission)) {
                    // Explain to the user why we need to read the contacts
                }

                activity.requestPermissions(new String[]{permission}, permissionCode);
                // MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE is an
                // app-defined int constant that should be quite unique
                return false;
            }
        }

        return true;
    }


    @Override
    public void onClick(View view) {

        switch (view.getId()) {

            case R.id.exo_down_arrow_IV:

                bottomFrameMode = true;

                RelativeLayout.LayoutParams params2 = new RelativeLayout.LayoutParams(
                        RelativeLayout.LayoutParams.MATCH_PARENT,
                        RelativeLayout.LayoutParams.MATCH_PARENT
                );
                params2.setMargins(0, 0, 0, 0);
                linearForSeekBar.setLayoutParams(params2);


                int width = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 337, getResources().getDisplayMetrics());

                int Height = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 70, getResources().getDisplayMetrics());

                RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(
                        width,
                        Height
                );

                params.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
                params.addRule(RelativeLayout.CENTER_HORIZONTAL);
                mRoundedShadowRLL.setLayoutParams(params);


                RelativeLayout.LayoutParams imParams = new
                        RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);

                topViewRL.removeView(mVideoRelative);
                topViewRL.setVisibility(View.GONE);
                mdownLL.addView(mVideoRelative, imParams);
                mRoundedShadowRLL.setVisibility(View.VISIBLE);
                mdownLL.setVisibility(View.VISIBLE);
                mVideoView.setUseController(false);
                allButtonsLL.setVisibility(View.VISIBLE);
                checkStatusForPlayIcon();

                setImageClickedAnimation(mDownArrowBtn);

                seekBar.setThumb(null);
//                seekBar.setDots(new int[]{Integer.parseInt(mPageItem.getVideoPart().get(0).getVideoPartTimeLine()), Integer.parseInt(mPageItem.getVideoPart().get(1).getVideoPartTimeLine())});
                seekBar.setDotsDrawable(R.drawable.ic_dote_yellow, -5);

                int Height2 = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 5, getResources().getDisplayMetrics());

                RelativeLayout.LayoutParams paramsForSeekBar = new RelativeLayout.LayoutParams(
                        RelativeLayout.LayoutParams.FILL_PARENT,
                        Height2
                );
                paramsForSeekBar.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
                seekBar.setLayoutParams(paramsForSeekBar);


//                seekBar.setVisibility(View.GONE);

                float speedStstus = mVideoView.getPlayer().getPlaybackParameters().speed;

                if (speedStstus == 1.0) {

                    mSpeedIV.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.ic_jabru_speed_selector));

                } else if (mVideoView.getPlayer().getPlaybackParameters().speed == 1.5) {

                    mSpeedIV.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.ic_audio_speed_x2_selector));


                } else {

                    mSpeedIV.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.ic_audio_speed_x3_selector));


                }

                break;


            case R.id.exo_speed_IV:


                if (mVideoView.getPlayer().getPlaybackState() == Player.STATE_READY && mVideoView.getPlayer().getPlayWhenReady()) {

                    if (mVideoView.getPlayer().getPlaybackParameters().speed == 1.0) {


                        float speed = 1.5f;
                        PlaybackParameters param = new PlaybackParameters(speed, 1.0f);
                        mVideoView.getPlayer().setPlaybackParameters(param);
                        mSpeedBtn.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.ic_exo_speed_x2));

                    } else if (mVideoView.getPlayer().getPlaybackParameters().speed == 1.5) {

                        float speed = 2f;
                        PlaybackParameters param = new PlaybackParameters(speed, 1.0f);
                        mVideoView.getPlayer().setPlaybackParameters(param);
                        mSpeedBtn.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.ic_wh_x3));


                    } else {

                        float speed = 1f;
                        PlaybackParameters param = new PlaybackParameters(speed, speed);
                        mVideoView.getPlayer().setPlaybackParameters(param);
                        mSpeedBtn.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.ic_exo_speed_x1));


                    }
                }

                break;


            case R.id.VA_speed_IV:


                if (mVideoView.getPlayer().getPlaybackState() == Player.STATE_READY && mVideoView.getPlayer().getPlayWhenReady()) {


                    if (mVideoView.getPlayer().getPlaybackParameters().speed == 1.0) {

                        float speed = 1.5f;
                        PlaybackParameters param = new PlaybackParameters(speed, 1.0f);
                        mVideoView.getPlayer().setPlaybackParameters(param);
                        mSpeedIV.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.ic_audio_speed_x2_selector));

                    } else if (mVideoView.getPlayer().getPlaybackParameters().speed == 1.5) {

                        float speed = 2f;
                        PlaybackParameters param = new PlaybackParameters(speed, 1.0f);
                        mVideoView.getPlayer().setPlaybackParameters(param);
                        mSpeedIV.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.ic_audio_speed_x3_selector));


                    } else {

                        float speed = 1f;
                        PlaybackParameters param = new PlaybackParameters(speed, speed);
                        mVideoView.getPlayer().setPlaybackParameters(param);
                        mSpeedIV.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.ic_jabru_speed_selector));


                    }
                }

                break;


            case R.id.VA_back30_IV:


                if (mVideoView.getPlayer().getCurrentPosition() - 10000 >= 0) {

                    mVideoView.getPlayer().seekTo(mVideoView.getPlayer().getCurrentPosition() - 10000);

                } else {
                    mVideoView.getPlayer().seekTo(0);
                }


                break;


            case R.id.VA_forward10_IV:


                if (mVideoView.getPlayer().getCurrentPosition() + 10000 <= mVideoView.getPlayer().getDuration()) {

                    mVideoView.getPlayer().seekTo(mVideoView.getPlayer().getCurrentPosition() + 10000);

                } else {
                    mVideoView.getPlayer().seekTo(mVideoView.getPlayer().getDuration());
                }


                break;


            case R.id.VA_pause_IV:


                if (mVideoView.getPlayer().getPlaybackState() == Player.STATE_READY && mVideoView.getPlayer().getPlayWhenReady()) {

                    mVideoView.getPlayer().setPlayWhenReady(false);
                    mVideoView.getPlayer().getPlaybackState();
                    mPauseIV.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.ic_play_selector));

                    if (mTalmudType.equals(GEMARA_TABLE)) {

                        nPanel = new NotificationPanel(this, mPageItem.getMasechetName() + " " + mPageItem.getPageNumber(), mVideoView.getPlayer().getPlayWhenReady(), true);

                    } else if (mTalmudType.equals(MISHNA_TABLE)) {

                        nPanel = new NotificationPanel(this, mPageItem.getMasechetName() + " " + mPageItem.getChapter() + "-" + mPageItem.getPageNumber(), mVideoView.getPlayer().getPlayWhenReady(), false);


                    }


                } else {
                    mVideoView.getPlayer().setPlayWhenReady(true);
                    mVideoView.getPlayer().getPlaybackState();
                    mPauseIV.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.ic_jabru_pause_selector));

                    int paddingDp = 10;
                    float density = this.getResources().getDisplayMetrics().density;
                    int paddingPixel = (int) (paddingDp * density);
                    mPauseIV.setPadding(paddingPixel, paddingPixel, paddingPixel, paddingPixel);


                    if (mTalmudType.equals(GEMARA_TABLE)) {

                        nPanel = new NotificationPanel(this, mPageItem.getMasechetName() + " " + mPageItem.getPageNumber(), mVideoView.getPlayer().getPlayWhenReady(), true);

                    } else if (mTalmudType.equals(MISHNA_TABLE)) {

                        nPanel = new NotificationPanel(this, mPageItem.getMasechetName() + " " + mPageItem.getChapter() + "-" + mPageItem.getPageNumber(), mVideoView.getPlayer().getPlayWhenReady(), false);


                    }


                }


                break;


            case R.id.VA_arrow_back_IV:
            case R.id.VA_arrow_back_IV_land:

                setImageClickedAnimation(mBackArrowIV);

                mVideoView.getPlayer().stop();

                if (nPanel != null) {

                    nPanel.notificationCancel();
                }
                setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED);

                if (startTime != 0 && mVideoView.getPlayer().getPlaybackState() == Player.STATE_READY) {
//
                    pauseTime = System.currentTimeMillis();

                    sumTimeUserUsed += (pauseTime - startTime);
                    startTime = 0;
                }

                sendAnalyticsData(sumTimeUserUsed);

                Intent returnIntent = new Intent();
                returnIntent.putExtra(CURRENT_PROGRESS, mVideoView.getPlayer().getCurrentPosition());
                returnIntent.putExtra(CURRENT_PAGE_ID, mPageItem.getId());
                setResult(Activity.RESULT_OK, returnIntent);

                finish();
                break;


            case R.id.VA_media_frame:

                if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {


                    RelativeLayout.LayoutParams paramsForTopLinear = new RelativeLayout.LayoutParams(
                            RelativeLayout.LayoutParams.MATCH_PARENT,
                            RelativeLayout.LayoutParams.MATCH_PARENT
                    );

                    int dpValue = 12; // margin in dips
                    float d = this.getResources().getDisplayMetrics().density;
                    int margin = (int) (dpValue * d); // margin in pixels

                    paramsForTopLinear.setMargins(0, 0, 0, margin);
                    linearForSeekBar.setLayoutParams(paramsForTopLinear);


                    RelativeLayout.LayoutParams imParams2 =
                            new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
                    mdownLL.removeView(mVideoRelative);
                    topViewRL.addView(mVideoRelative, imParams2);
                    topViewRL.setVisibility(View.VISIBLE);
                    mdownLL.setVisibility(View.GONE);
                    mRoundedShadowRLL.setVisibility(View.GONE);
                    mVideoView.setUseController(true);
                    allButtonsLL.setVisibility(View.GONE);
                    bottomFrameMode = false;

                    seekBar.setThumb(ContextCompat.getDrawable(this, R.drawable.custom_thumb));
//                    seekBar.setDots(new int[]{Integer.parseInt(mPageItem.getVideoPart().get(0).getVideoPartTimeLine()), Integer.parseInt(mPageItem.getVideoPart().get(1).getVideoPartTimeLine())});
                    seekBar.setDotsDrawable(R.drawable.ic_dote, 5);

                    RelativeLayout.LayoutParams paramsForTopSeekBar = new RelativeLayout.LayoutParams(
                            RelativeLayout.LayoutParams.FILL_PARENT,
                            RelativeLayout.LayoutParams.WRAP_CONTENT
                    );
                    paramsForTopSeekBar.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
                    seekBar.setLayoutParams(paramsForTopSeekBar);


                    float speedStstus2 = mVideoView.getPlayer().getPlaybackParameters().speed;

                    if (speedStstus2 == 1.0) {

                        mSpeedBtn.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.ic_exo_speed_x1));

                    } else if (speedStstus2 == 1.5) {

                        mSpeedBtn.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.ic_exo_speed_x2));


                    } else {

                        mSpeedBtn.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.ic_exo_speed_x3));

                    }


                }

                break;


            case R.id.VA_download_btn_IV:

                setImageClickedAnimation(mDownloadBtnIV);

                downloadVideoAndRefreshPage();


                break;


            case R.id.VA_image_btn_IV:
            case R.id.VA_image_btn_IV_land:
//            case R.id.VA_image_btn_in_message_mode_IV:


                setImageClickedAnimation(mImageBtnIV);

//                Toast.makeText(this, getResources().getString(R.string.in_development), Toast.LENGTH_LONG).show();
                if ((mPageItem != null && mPageItem.getGallery() != null && mPageItem.getGallery().size() > 0) ||
                        (dbPageItem != null && dbPageItem.getGallery() != null && dbPageItem.getGallery().size() > 0)
                ) {

                    Intent intent = new Intent(this, GallerySlideActivity.class);
                    intent.putExtra(IMAGES_LIST, (Serializable) mPageItem.getGallery());
                    startActivity(intent);

                }

                break;


            case R.id.VA_message_btn_IV:
            case R.id.VA_message_btn_IV_land:

                setImageClickedAnimation(mMessageBtnIV);

//                Toast.makeText(this, getResources().getString(R.string.in_development), Toast.LENGTH_LONG).show();

                openMessageBar();

                break;


            case R.id.exo_video_back:

                jumpToPreviousPosition();

                break;

            case R.id.exo_video_frd:

                jumpToNextPosition();

                break;


            case R.id.VA_cancel_TV:

                closeMessageMode();
                startVideoAgain();

                break;


        }

    }


    private void jumpToPreviousPosition() {

        if (mPageItem.getVideoPart() != null) {

            if (mPageItem.getVideoPart().size() > 0 && mVideoView.getPlayer().getCurrentPosition() <= TimeUnit.SECONDS.toMillis(Long.parseLong(mPageItem.getVideoPart().get(0).getVideoPartTimeLine()))) {

                mVideoView.getPlayer().seekTo(0);

            } else if (mPageItem.getVideoPart().size() == 0) {

                mVideoView.getPlayer().seekTo(0);


            } else {

                for (int i = mPageItem.getVideoPart().size() - 1; i >= 0; i--) {

                    if (mVideoView.getPlayer().getCurrentPosition() > TimeUnit.SECONDS.toMillis(Long.parseLong(mPageItem.getVideoPart().get(i).getVideoPartTimeLine()))) {

                        mVideoView.getPlayer().seekTo(TimeUnit.SECONDS.toMillis(Long.parseLong(mPageItem.getVideoPart().get(i).getVideoPartTimeLine())));
                        break;
                    }

                }
            }


        } else if (dbPageItem.getVideoPart() != null) {

            if (dbPageItem.getVideoPart().size() > 0 && mVideoView.getPlayer().getCurrentPosition() <= TimeUnit.SECONDS.toMillis(Long.parseLong(dbPageItem.getVideoPart().get(0).getVideoPartTimeLine()))) {

                mVideoView.getPlayer().seekTo(0);

            } else {

                for (int i = dbPageItem.getVideoPart().size() - 1; i >= 0; i--) {

                    if (mVideoView.getPlayer().getCurrentPosition() > TimeUnit.SECONDS.toMillis(Long.parseLong(dbPageItem.getVideoPart().get(i).getVideoPartTimeLine()))) {

                        mVideoView.getPlayer().seekTo(TimeUnit.SECONDS.toMillis(Long.parseLong(dbPageItem.getVideoPart().get(i).getVideoPartTimeLine())));
                        break;
                    }

                }
            }


        }

    }


    private void jumpToNextPosition() {


        if (mPageItem.getVideoPart() != null) {


            for (int i = 0; i < mPageItem.getVideoPart().size(); i++) {

                if (mVideoView.getPlayer().getContentPosition() < TimeUnit.SECONDS.toMillis(Long.parseLong(mPageItem.getVideoPart().get(i).getVideoPartTimeLine()))) {

                    mVideoView.getPlayer().seekTo(TimeUnit.SECONDS.toMillis(Long.parseLong(mPageItem.getVideoPart().get(i).getVideoPartTimeLine())));
                    break;
                }

            }
        } else if (dbPageItem.getVideoPart() != null) {

            for (int i = 0; i < dbPageItem.getVideoPart().size(); i++) {

                if (mVideoView.getPlayer().getContentPosition() < TimeUnit.SECONDS.toMillis(Long.parseLong(dbPageItem.getVideoPart().get(i).getVideoPartTimeLine()))) {

                    mVideoView.getPlayer().seekTo(TimeUnit.SECONDS.toMillis(Long.parseLong(dbPageItem.getVideoPart().get(i).getVideoPartTimeLine())));
                    break;
                }

            }

        }


    }


    private void initForwardButton() {

        if (mPageItem != null && mPageItem.getVideoPart() != null && mPageItem.getVideoPart().size() == 0) {

            // TODO waiting for new image

        }

    }


    private void setImageClickedAnimation(ImageView Icon) {

        Animation animation = AnimationUtils.loadAnimation(this, R.anim.alpha);
        Icon.startAnimation(animation);

    }


    private void downloadVideoAndRefreshPage() {

        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        position = mVideoView.getPlayer().getCurrentPosition();

        mVideoView.getPlayer().setPlayWhenReady(false);

        mDownloadBtnIV.setVisibility(View.GONE);
        mVideoProgressDownloadCPB.setVisibility(View.VISIBLE);


        String pagePathName = "";

        if (!mPageItem.getPage().equals("") && !downloadPageFinished) {

            S3Helper s3Helper = new S3Helper(this);
            s3Helper.downloadFilePagesWithoutListener(mPageItem.getPage(), PAGES_FILE, this);

            pagePathName = Config.getPathName(this) + PAGES_FILE + getPageFIleNameFromFileKEy(mPageItem.getPage());
        }

        File folder = new File(Config.getPathName(this) + VIDEO);
        boolean success = true;
        if (!folder.exists()) {
            success = folder.mkdirs();
        }
        if (success) {
            // Do something on success
        } else {
            // Do something else on failure
        }

        final String finalPagePathName = pagePathName;

        Thread thread = new Thread(new Runnable() {

            @Override
            public void run() {

                try {

                    URL url = new URL(mPageItem.getVideo());
                    HttpURLConnection con = (HttpURLConnection) url.openConnection();
                    int lenghtOfFile = con.getContentLength();

                    con.setRequestMethod("GET");
                    con.connect();

                    final String pathName = Config.getPathName(VideoActivity.this) + VIDEO_FILE;

                    final String fileName = getFIleNameFromFileKEy(mPageItem.getPage());

                    File outputFile = new File(pathName, fileName);

                    if (!outputFile.exists()) {
                        outputFile.createNewFile();
                    }

                    FileOutputStream fos = new FileOutputStream(outputFile);

//                    int status = con.getResponseCode();

                    InputStream is = con.getInputStream();

                    byte[] buffer = new byte[1024];
                    int len1 = 0;

                    int total = 0;
                    long status = 0;

                    while ((len1 = is.read(buffer)) != -1) {
                        total += len1;

                        fos.write(buffer, 0, len1);

                        if (status != total * 100L / lenghtOfFile) {

                            final long finalStatus = status;
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    // change UI elements here
                                    mVideoProgressDownloadCPB.setProgress((int) finalStatus);

                                    Intent resultsIntent = new Intent(DOWNLOAD_VIDEO_PROGRESS);
                                    resultsIntent.putExtra(VIDEO_PROGRESS, String.valueOf(finalStatus));
                                    Bundle bundle = new Bundle();
                                    bundle.putSerializable(VIDEO_PROGRESS_PAGE_ITEN_PARS, mPageItem);
                                    resultsIntent.putExtra(VIDEO_PROGRESS_PAGE_ITEN, bundle);
                                    resultsIntent.putExtra(TALMUD_TYPE, mTalmudType);
                                    sendBroadcast(resultsIntent);
                                }
                            });

                        }
                        status = total * 100L / lenghtOfFile;

                    }

                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            // change UI elements here
                            mVideoProgressDownloadCPB.setVisibility(View.GONE);

                            Intent resultsIntent = new Intent(DOWNLOAD_VIDEO_PROGRESS);
                            resultsIntent.putExtra(VIDEO_PROGRESS, String.valueOf(100));
                            Bundle bundle = new Bundle();
                            bundle.putSerializable(VIDEO_PROGRESS_PAGE_ITEN_PARS, mPageItem);
                            resultsIntent.putExtra(VIDEO_PROGRESS_PAGE_ITEN, bundle);
                            resultsIntent.putExtra(TALMUD_TYPE, mTalmudType);
                            sendBroadcast(resultsIntent);

                            DataBaseManager dataBaseManager = new DataBaseManager();

                            if (mTalmudType.equals(GEMARA_TABLE)) {

                                dataBaseManager.onVideoGemaraDownloadFinished(VideoActivity.this, mPageItem, pathName + fileName, finalPagePathName);

                            } else if (mTalmudType.equals(MISHNA_TABLE)) {

                                dataBaseManager.onVideoMishnaDownloadFinished(VideoActivity.this, generateMishnaObject(mPageItem), pathName + fileName, finalPagePathName);

                            }


                            if (mVideoView != null && mVideoView.getPlayer() != null) {

                                mVideoView.getPlayer().stop();
                            }
                            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED);

                            checkIfPageExistInDB();

                            if (mVideoView != null) {

                                setVideoPlayer(dbPageItem);
                            }


                        }
                    });

                    fos.close();
                    is.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }


            }
        });
        thread.start();


    }


    /**
     * generate gemara to mishna
     *
     * @param pagesItem PagesItem
     * @return pagesItem
     */
    private MishnayotItem generateMishnaObject(PagesItem pagesItem) {

        MishnayotItem mishnayotItem = new MishnayotItem();
        mishnayotItem.setSederOrder(pagesItem.getSederOrder());
        mishnayotItem.setMasechetOrder(pagesItem.getMasechetOrder());
        mishnayotItem.setMasechetName(pagesItem.getMasechetName());
        mishnayotItem.setId(pagesItem.getId());
        mishnayotItem.setPosition(pagesItem.getPosition());
        mishnayotItem.setVideo(pagesItem.getVideo());
        mishnayotItem.setAudio(pagesItem.getAudio());
        mishnayotItem.setMishna(pagesItem.getPageNumber());

        return mishnayotItem;


    }


    private String getFIleNameFromFileKEy(String fileKey) {

        String fileName = fileKey.substring(fileKey.lastIndexOf("/") + 1);
        return fileName.substring(0, fileName.indexOf(".pdf")) + ".mp4";

    }


    private String getPageFIleNameFromFileKEy(String page) {

        return page.substring(page.lastIndexOf("/") + 1);
    }


    /**
     * on user press the pause button in the notification bar
     *
     * @param stringExtra String
     */
    @Override
    public void onStopClicked(String stringExtra) {


        if (stringExtra.equals("AUDIO_STOP")) {

            if (mVideoView.getPlayer().getPlaybackState() == Player.STATE_READY && mVideoView.getPlayer().getPlayWhenReady()) {

                mVideoView.getPlayer().setPlayWhenReady(false);

                pauseTime = System.currentTimeMillis();
                if (startTime != 0) {

                    sumTimeUserUsed += (pauseTime - startTime);
                    startTime = 0;
                }

                mVideoView.getPlayer().getPlaybackState();
                mPauseIV.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.ic_play_selector));

                if (mTalmudType.equals(GEMARA_TABLE)) {

                    nPanel = new NotificationPanel(this, mPageItem.getMasechetName() + " " + mPageItem.getPageNumber(), mVideoView.getPlayer().getPlayWhenReady(), true);

                } else if (mTalmudType.equals(MISHNA_TABLE)) {

                    nPanel = new NotificationPanel(this, mPageItem.getMasechetName()
                            + " " + mPageItem.getChapter() + "-" + mPageItem.getPageNumber(), mVideoView.getPlayer().getPlayWhenReady(), false);


                }


            }

        } else if (stringExtra.equals("AUDIO_PLAY")) {


            mVideoView.getPlayer().setPlayWhenReady(true);
            startTime = System.currentTimeMillis();
            mVideoView.getPlayer().getPlaybackState();
            mPauseIV.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.ic_jabru_pause_selector));

            int paddingDp = 10;
            float density = this.getResources().getDisplayMetrics().density;
            int paddingPixel = (int) (paddingDp * density);
            mPauseIV.setPadding(paddingPixel, paddingPixel, paddingPixel, paddingPixel);


            if (mTalmudType.equals(GEMARA_TABLE)) {

                nPanel = new NotificationPanel(this, mPageItem.getMasechetName() + " " + mPageItem.getPageNumber(), mVideoView.getPlayer().getPlayWhenReady(), true);

            } else if (mTalmudType.equals(MISHNA_TABLE)) {

                nPanel = new NotificationPanel(this, mPageItem.getMasechetName()
                        + " " + mPageItem.getChapter() + "-" + mPageItem.getPageNumber(), mVideoView.getPlayer().getPlayWhenReady(), false);


            }


        }


    }


    /**
     * open message bar
     */
    private void openMessageBar() {

        if (mVideoView.getPlayer().getPlaybackState() == Player.STATE_READY && mVideoView.getPlayer().getPlayWhenReady()) {

            mVideoView.getPlayer().setPlayWhenReady(false);
            mVideoView.getPlayer().getPlaybackState();
            mPauseIV.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.ic_play_selector));

            if (mTalmudType.equals(GEMARA_TABLE)) {

                nPanel = new NotificationPanel(this, mPageItem.getMasechetName() + " " + mPageItem.getPageNumber(), mVideoView.getPlayer().getPlayWhenReady(), true);

            } else if (mTalmudType.equals(MISHNA_TABLE)) {

                nPanel = new NotificationPanel(this, mPageItem.getMasechetName() + " " + mPageItem.getChapter() + "-" + mPageItem.getPageNumber(), mVideoView.getPlayer().getPlayWhenReady(), false);


            }


        }


        InputMethodManager inputMethodManager = (InputMethodManager) Objects.requireNonNull(this).getSystemService(this.INPUT_METHOD_SERVICE);
        mMessageET.requestFocus();
        Objects.requireNonNull(inputMethodManager).showSoftInput(mMessageET, 0);

        final Window mRootWindow = getWindow();

        allViewLL.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            public void onGlobalLayout() {

                setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);


                int screenHeight = (allViewLL.getRootView().getHeight());
                Rect r = new Rect();
                View view = mRootWindow.getDecorView();
                view.getWindowVisibleDisplayFrame(r);

                int keyBoardHeight = (int) ((screenHeight - (r.bottom + navigationBarHeight())) / getResources().getDisplayMetrics().density);

                if (keyBoardHeight > 50) {

                    int Height = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, keyBoardHeight, getResources().getDisplayMetrics());

                    LinearLayout.LayoutParams params2 = new LinearLayout.LayoutParams(
                            LinearLayout.LayoutParams.MATCH_PARENT, Height
                    );


                    mMessageBoxLL.setVisibility(View.VISIBLE);
                    mViewForSeekBar.setVisibility(View.VISIBLE);

                    mLinearForMessageLL.setVisibility(View.VISIBLE);
                    mLinearTopLL.setVisibility(View.GONE);

                    mMessageET.addListener(VideoActivity.this);

                    initMessageTextChangedListener();
                    setRecordView();

                    mViewForSeekBar.setLayoutParams(params2);
                    allViewLL.getViewTreeObserver().removeOnGlobalLayoutListener(this);

                }
            }
        });

    }


    /**
     * init message listener and send button pressed
     */
    private void initMessageTextChangedListener() {

        mMessageET.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                if (i2 > 0) {

                    mRecordButtonRB.setBackground(ContextCompat.getDrawable(VideoActivity.this, R.drawable.ic_arrow_righte));


                    mRecordButtonRB.setListenForRecord(false);

                    //ListenForRecord must be false ,otherwise onClick will not be called
                    mRecordButtonRB.setOnRecordClickListener(new OnRecordClickListener() {
                        @Override
                        public void onClick(View v) {

                            mProgress.setVisibility(View.VISIBLE);
                            MessageObject messagesObject = new MessageObject();
                            messagesObject.setMessage(mMessageET.getText().toString());

                            String title = "";
                            if (mTalmudType.equals(GEMARA_TABLE)) {

                                title = mPageItem.getMasechetName() + " " + mPageItem.getPageNumber();
                                messagesObject.setIsGemara(true);

                            } else if (mTalmudType.equals(MISHNA_TABLE)) {

                                title = mPageItem.getMasechetName() + " " + mPageItem.getChapter() + "-" + mPageItem.getPageNumber();
                                messagesObject.setIsGemara(false);


                            }

                            messagesObject.setLessonId(mPageItem.getId());
                            messagesObject.setTitle(ASK_THE_RABBI + title);
                            messagesObject.setIsMine(true);
                            messagesObject.setMessageType(TEXT);
                            if (mPageItem.getPresenter() != null) {

                                messagesObject.setToUser(mPageItem.getPresenter().getId());
                            }


                            Gson gson = new Gson();
                            User userFromJson = gson.fromJson(UserManager.getUser(VideoActivity.this), User.class);
                            messagesObject.setFromUser(userFromJson.getId());

                            messagesObject.setSentAt(System.currentTimeMillis());

                            sendMessageToServerAndSaveInLocalStorage(messagesObject);

                            mMessageET.setText("");

                            mMessageET.setText("");

                        }
                    });


                } else if (charSequence.length() == 0) {

                    mRecordButtonRB.setBackground(ContextCompat.getDrawable(VideoActivity.this, R.drawable.ic_mic));
                    if (ContextCompat.checkSelfPermission(VideoActivity.this, Manifest.permission.RECORD_AUDIO) == PackageManager.PERMISSION_GRANTED) {
                        {

                            mRecordButtonRB.setListenForRecord(true);
                        }
                    } else {

                        checkItHasAudioRecordPermission();
                    }

                }

            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });


    }


    private void sendMessageToServerAndSaveInLocalStorage(final MessageObject messageObject) {

        if (isNetworkAvailable()) {

            RequestManager.sendMessage(UserManager.getToken(this), messageObject).subscribe(new Observer<Result<MessageObject>>() {
                @Override
                public void onSubscribe(Disposable d) {

                }

                @Override
                public void onNext(Result<MessageObject> messagesObject2Result) {

                    messageObject.setChatId(messagesObject2Result.getData().getChatId());

                    saveMessageToDataBase(messageObject);
                    mProgress.setVisibility(View.GONE);


                    LayoutInflater inflater = getLayoutInflater();
                    View layout = inflater.inflate(R.layout.dialog_message_sent,
                            (ViewGroup) findViewById(R.id.toast_layout_root));

                    Toast toast = new Toast(getApplicationContext());
                    toast.setGravity(Gravity.TOP, 0, 0);
                    toast.setDuration(Toast.LENGTH_SHORT);
                    toast.setView(layout);
                    toast.show();

                    closeMessageMode();
                    startVideoAgain();

                }

                @Override
                public void onError(Throwable e) {

                    mProgress.setVisibility(View.GONE);

                    if (e instanceof HttpException) {
                        HttpException exception = (HttpException) e;
                        ErrorResponse response = null;
                        try {
                            response = new Gson().fromJson(Objects.requireNonNull(exception.response().errorBody()).string(), ErrorResponse.class);
                            Toast.makeText(VideoActivity.this, Objects.requireNonNull(response).getErrors().get(0).getMessage(), Toast.LENGTH_LONG).show();
                        } catch (Exception e1) {
                            e1.printStackTrace();
                        }
                    }
                }

                @Override
                public void onComplete() {

                }
            });
        } else {

            Toast.makeText(this, this.getResources().getString(R.string.offline_for_messages), Toast.LENGTH_LONG).show();

            Gson gson = new Gson();

            Type type = new TypeToken<List<MessageObject>>() {
            }.getType();
            List<MessageObject> recentMessagesList = gson.fromJson(UserManager.getMessageForOffline(this), type);

            if (recentMessagesList == null) {
                recentMessagesList = new ArrayList<>();
            }

            recentMessagesList.add(messageObject);

            String jsonMessage = gson.toJson(recentMessagesList);
            UserManager.setMessageForOffline(jsonMessage, VideoActivity.this);

        }


    }

    private void saveMessageToDataBase(MessageObject messageObject) {

        DataBaseManager manager = new DataBaseManager();

        ChatObject newChatObject = new ChatObject();
        newChatObject.setChatId(messageObject.getChatId());
        newChatObject.setCreatedAt(messageObject.getSentAt());
        newChatObject.setTitle(messageObject.getTitle());
        newChatObject.setFromUser(messageObject.getToUser());
        newChatObject.setToUser(messageObject.getFromUser());
        newChatObject.setChatType(2);
        newChatObject.setLessonId(mPageItem.getId());


        if (mTalmudType.equals(GEMARA_TABLE)) {

            newChatObject.setIsGemara(true);

        } else if (mTalmudType.equals(MISHNA_TABLE)) {

            newChatObject.setIsGemara(false);

        }

        if (messageObject.getMessageType() == AUDIO) {

            newChatObject.setLastMessage(AUDIO_MESSAGE);

        } else {

            newChatObject.setLastMessage(messageObject.getMessage());
        }
        newChatObject.setLastMessageTime(messageObject.getSentAt());

        manager.addChatToChatTable(getApplicationContext(), newChatObject);
        manager.addMessageToMessageTable(getApplicationContext(), messageObject);


    }


    private void uploadMessageToS3AndSendTOServer(final MessageObject messageObject) {


        File fileToSave = new File(pathName);

        S3Helper s3Helper = new S3Helper(this);
        s3Helper.upload(S3_SUB_FOLDER, null, fileToSave, new UploadListener() {
            @Override
            public void onUploadStart(int id, File fileLocation) {

            }

            @Override
            public void onProgressChanged(int id, long bytesCurrent, long bytesTotal) {

            }

            @Override
            public void onUploadWaiting(int id, TransferState state, File fileLocation) {

            }

            @Override
            public void onUploadReceivedStop(int id, TransferState state, File fileLocation) {

            }

            @Override
            public void onUploadFinish(int id, String link) {

                Gson gson = new Gson();
                MessageObject messageObject1 = gson.fromJson(gson.toJson(messageObject), MessageObject.class); // set message s3 only in server
                messageObject1.setMessage(link);
                sendMessageToServerAndSaveInLocalStorage(messageObject1);
            }

            @Override
            public void onUploadError(int id, File fileLocation, Exception ex) {

            }
        });


    }


    /**
     * check if network available
     *
     * @return boolean
     */
    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) Objects.requireNonNull(this).getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = Objects.requireNonNull(connectivityManager).getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }


    /**
     * set record button
     */
    private void setRecordView() {

        mRecordButtonRB.setRecordView(mRecordViewRV);


        mRecordViewRV.setOnRecordListener(new OnRecordListener() {
            @Override
            public void onStart() {
                //Start Recording..

                final Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        startRecording();
                    }
                }, 700);

                mMessageET.setHint("");
                mMessageET.setCursorVisible(false);


            }

            @Override
            public void onCancel() {
                //On Swipe To Cancel

                stopRecording();

                final Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        mMessageET.setCursorVisible(true);
                    }
                }, 1500);


                mMessageET.setHint(getResources().getString(R.string.write_message));

            }

            @Override
            public void onFinish(long recordTime) {
                //Stop Recording..

                stopRecording();
                mProgress.setVisibility(View.VISIBLE);

                sendRecordMessageToServer();


                mMessageET.setText("");

                String time = String.valueOf(recordTime);

                mMessageET.setHint(getResources().getString(R.string.write_message));
                mMessageET.setCursorVisible(true);

            }

            @Override
            public void onLessThanSecond() {
                //When the record time is less than One Second

            }
        });


        mRecordViewRV.setOnBasketAnimationEndListener(new OnBasketAnimationEnd() {
            @Override
            public void onAnimationEnd() {

            }
        });


        mRecordViewRV.setSmallMicColor(getResources().getColor(R.color.orange_btn)); // not working from R.color. .

        //disable Sounds
        mRecordViewRV.setSoundEnabled(true);

        //prevent recording under one Second (it's false by default)
        mRecordViewRV.setLessThanSecondAllowed(false);

        //set Custom sounds onRecord
        //you can pass 0 if you don't want to play sound in certain state
        mRecordViewRV.setCustomSounds(R.raw.record_start, R.raw.record_finished, 0);

        //change slide To Cancel Text Color
        mRecordViewRV.setSlideToCancelTextColor(getResources().getColor(R.color.slide_color));

        mRecordViewRV.setSlideToCancelArrowColor(getResources().getColor(R.color.slide_color));

        //change Counter Time (Chronometer) color
        mRecordViewRV.setCounterTimeColor(getResources().getColor(R.color.bottom_nevi_blue));


    }


    private void sendRecordMessageToServer() {


        MessageObject messagesObject = new MessageObject();
        messagesObject.setMessage(pathName);

        String title = "";
        if (mTalmudType.equals(GEMARA_TABLE)) {

            title = mPageItem.getMasechetName() + " " + mPageItem.getPageNumber();
            messagesObject.setIsGemara(true);

        } else if (mTalmudType.equals(MISHNA_TABLE)) {

            title = mPageItem.getMasechetName() + " " + mPageItem.getChapter() + "-" + mPageItem.getPageNumber();
            messagesObject.setIsGemara(false);


        }

        messagesObject.setLessonId(mPageItem.getId());
        messagesObject.setTitle(ASK_THE_RABBI + title);
        messagesObject.setIsMine(true);
        messagesObject.setMessageType(AUDIO);
        messagesObject.setIsMine(true);

        if (mPageItem.getPresenter() != null) {

            messagesObject.setToUser(mPageItem.getPresenter().getId());
        }


        Gson gson = new Gson();
        User userFromJson = gson.fromJson(UserManager.getUser(VideoActivity.this), User.class);
        messagesObject.setFromUser(userFromJson.getId());

        messagesObject.setSentAt(System.currentTimeMillis());

        uploadMessageToS3AndSendTOServer(messagesObject);

        mMessageET.setText("");


    }


    /**
     * start recording
     */
    private void startRecording() {


        File folder = new File(Config.getPathName(this) + AUDIO_RECORD);
        if (!folder.exists()) {
            folder.mkdirs();
        }


        pathName = Config.getPathName(this) + AUDIO_RECORD + getRecordAudioFileName();

        recorder = new MediaRecorder();
        recorder.setAudioSource(MediaRecorder.AudioSource.MIC);
        recorder.setOutputFormat(MediaRecorder.OutputFormat.MPEG_4);
        recorder.setOutputFile(pathName);
        recorder.setAudioEncoder(MediaRecorder.AudioEncoder.AAC);

        try {
            recorder.prepare();
        } catch (IOException e) {
        }

        recorder.start();

    }


    /**
     * set record file name to end with "mp4"
     *
     * @return String
     */
    private String getRecordAudioFileName() {

        return "/" + System.currentTimeMillis() + M4A;
    }


    /**
     * stop recording
     */
    private void stopRecording() {

        if (recorder != null) {

            recorder.stop();
            recorder.release();
            recorder = null;
        }
    }


    /**
     * check if user has record permission
     */
    private void checkItHasAudioRecordPermission() {

        if (ContextCompat.checkSelfPermission(VideoActivity.this, Manifest.permission.RECORD_AUDIO) != PackageManager.PERMISSION_GRANTED) {

            mRecordButtonRB.setListenForRecord(false);

            mRecordButtonRB.setOnRecordClickListener(new OnRecordClickListener() {
                @Override
                public void onClick(View v) {

                    closeMessageMode();
                    ActivityCompat.requestPermissions(VideoActivity.this, new String[]{Manifest.permission.RECORD_AUDIO}, MY_PERMISSIONS_RECORD_AUDIO);

                }
            });


        } else {

            mRecordButtonRB.setListenForRecord(true);

        }


    }


    /**
     * close message mode
     */
    private void closeMessageMode() {

        hideKeyboard(this);
        mMessageBoxLL.setVisibility(View.GONE);
        mViewForSeekBar.setVisibility(View.GONE);

        mLinearForMessageLL.setVisibility(View.GONE);
        mLinearTopLL.setVisibility(View.VISIBLE);


//        mLinearForMessageLL.startAnimation(AnimationUtils.loadAnimation(AudioActivity.this, android.R.anim.slide_out_right)); // for animation
//        mLinearTopLL.startAnimation(AnimationUtils.loadAnimation(AudioActivity.this, android.R.anim.slide_in_left)); // for animation

        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED);

    }


    /**
     * check if there is navigation bar (for samsung) to push the message box above the keyboard
     *
     * @return navigation bar height
     */
    public int navigationBarHeight() {

        if (hasNavBar(getResources())) {

            Resources resources = this.getResources();
            int resourceId = resources.getIdentifier("navigation_bar_height", "dimen", "android");
            if (resourceId > 0) {
                return resources.getDimensionPixelSize(resourceId);
            }
        } else {

            return 0;
        }
        return 0;

    }


    /**
     * check size of the nav bar
     *
     * @param resources Resources
     * @return boolean
     */
    public boolean hasNavBar(Resources resources) {
        int id = resources.getIdentifier("config_showNavigationBar", "bool", "android");
        return id > 0 && resources.getBoolean(id);
    }


    @Override
    public void OnAppreciateClicked(int crownId) {
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED);


        LessonLike lessonLike = new LessonLike();

        if (mTalmudType.equals(GEMARA_TABLE)) {

            lessonLike.setIsGemara(true);

        } else if (mTalmudType.equals(MISHNA_TABLE)) {

            lessonLike.setIsGemara(false);
        }

        lessonLike.setLessonId(mPageItem.getId());
        lessonLike.setCrownId(crownId);


        RequestManager.sendLikeLesson(UserManager.getToken(this), lessonLike).subscribe(new Observer<Result>() {
            @Override
            public void onSubscribe(Disposable d) {

            }

            @Override
            public void onNext(Result result) {

            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onComplete() {

            }
        });

    }


    @Override
    public void onOnBackClicked() {
//        onBackPressed();
        finish();
    }




    @Override
    public void onDonateDonationClicked() {

        Intent intent = new Intent(this, DonationActivity.class);
        startActivity(intent);
        finish();


    }




    @Override
    public void onSkipBtnClicked() {


        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED);

        LessonLike lessonLike = new LessonLike();

        if (mTalmudType.equals(GEMARA_TABLE)) {

            lessonLike.setIsGemara(true);

        } else if (mTalmudType.equals(MISHNA_TABLE)) {

            lessonLike.setIsGemara(false);
        }

        lessonLike.setLessonId(mPageItem.getId());
        lessonLike.setCrownId(0);


        RequestManager.sendLikeLesson(UserManager.getToken(this), lessonLike).subscribe(new Observer<Result>() {
            @Override
            public void onSubscribe(Disposable d) {

            }

            @Override
            public void onNext(Result result) {

            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onComplete() {

            }
        });

    }


    private AudioManager.OnAudioFocusChangeListener initAudioListener() {

        AudioManager.OnAudioFocusChangeListener videoListener = new AudioManager.OnAudioFocusChangeListener() {

            private boolean inListening;

            @Override
            public void onAudioFocusChange(int focusChange) {

                switch (focusChange) {
                    case AudioManager.AUDIOFOCUS_GAIN:
                        // resume playback


                        if (mVideoView != null && inListening) {


                            mVideoView.getPlayer().setPlayWhenReady(true);
                            inListening = false;
                            mVideoView.getPlayer().getPlaybackState();
                            mPauseIV.setImageDrawable(ContextCompat.getDrawable(VideoActivity.this, R.drawable.ic_jabru_pause_selector));

                            int paddingDp = 10;
                            float density = VideoActivity.this.getResources().getDisplayMetrics().density;
                            int paddingPixel = (int) (paddingDp * density);
                            mPauseIV.setPadding(paddingPixel, paddingPixel, paddingPixel, paddingPixel);

                            if (mTalmudType.equals(GEMARA_TABLE)) {

                                nPanel = new NotificationPanel(VideoActivity.this,
                                        mPageItem.getMasechetName() + " " + mPageItem.getPageNumber(), mVideoView.getPlayer().getPlayWhenReady(), true);

                            } else if (mTalmudType.equals(MISHNA_TABLE)) {

                                nPanel = new NotificationPanel(VideoActivity.this,
                                        mPageItem.getMasechetName() + " " + mPageItem.getChapter() + "-" + mPageItem.getPageNumber(), mVideoView.getPlayer().getPlayWhenReady(), false);


                            }


                        }


                        break;


                    case AudioManager.AUDIOFOCUS_LOSS:
                        // Lost focus for an unbounded amount of time: stop playback and release media player

                    case AudioManager.AUDIOFOCUS_LOSS_TRANSIENT:
                        // Lost focus for a short time, but we have to stop
                        // playback. We don't release the media player because playback
                        // is likely to resume

                        if (mVideoView != null && (mVideoView.getPlayer().getPlaybackState() == Player.STATE_READY && mVideoView.getPlayer().getPlayWhenReady())) {

                            mVideoView.getPlayer().setPlayWhenReady(false);
                            inListening = true;
                            mVideoView.getPlayer().getPlaybackState();
                            mPauseIV.setImageDrawable(ContextCompat.getDrawable(VideoActivity.this, R.drawable.ic_play_selector));

                            if (startTime != 0) {

                                if (mTalmudType.equals(GEMARA_TABLE)) {

                                    nPanel = new NotificationPanel(VideoActivity.this, mPageItem.getMasechetName() + " " + mPageItem.getPageNumber(), mVideoView.getPlayer().getPlayWhenReady(), true);

                                } else if (mTalmudType.equals(MISHNA_TABLE)) {

                                    nPanel = new NotificationPanel(VideoActivity.this, mPageItem.getMasechetName() + " " + mPageItem.getChapter() + "-" + mPageItem.getPageNumber(), mVideoView.getPlayer().getPlayWhenReady(), false);


                                }


                            }
                        }

                        break;

                    case AudioManager.AUDIOFOCUS_LOSS_TRANSIENT_CAN_DUCK:
                        // Lost focus for a short time, but it's ok to keep playing
                        // at an attenuated level

                }

            }


        };

        return videoListener;
    }


    @Override
    public void onKeyPrime() {

        closeMessageMode();
        startVideoAgain();
    }


    public void startVideoAgain(){


        mVideoView.getPlayer().setPlayWhenReady(true);
        mVideoView.getPlayer().getPlaybackState();
        mPauseIV.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.ic_jabru_pause_selector));

        int paddingDp = 10;
        float density = this.getResources().getDisplayMetrics().density;
        int paddingPixel = (int) (paddingDp * density);
        mPauseIV.setPadding(paddingPixel, paddingPixel, paddingPixel, paddingPixel);


        if (mTalmudType.equals(GEMARA_TABLE)) {

            nPanel = new NotificationPanel(this, mPageItem.getMasechetName() + " " + mPageItem.getPageNumber(), mVideoView.getPlayer().getPlayWhenReady(), true);

        } else if (mTalmudType.equals(MISHNA_TABLE)) {

            nPanel = new NotificationPanel(this, mPageItem.getMasechetName() + " " + mPageItem.getChapter() + "-" + mPageItem.getPageNumber(), mVideoView.getPlayer().getPlayWhenReady(), false);


        }

    }
}

