package co.il.jabrutouch.ui.main.audio_screen;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.graphics.Rect;
import android.media.AudioManager;
import android.media.MediaPlayer;
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
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
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
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

import co.il.jabrutouch.R;
import co.il.jabrutouch.data_base_manager.DataBaseManager;
import co.il.jabrutouch.server.RequestManager;
import co.il.jabrutouch.ui.main.donation_screen.DonationActivity;
import co.il.jabrutouch.ui.main.donation_screen.DonationDialogNoDonate;
import co.il.model.model.ChatObject;
import co.il.model.model.ErrorResponse;
import co.il.model.model.LessonDonationBy;
import co.il.model.model.LessonLike;
import co.il.model.model.Result;
import co.il.jabrutouch.ui.main.donation_screen.DonationDialog;
import co.il.model.model.MessageObject;
import co.il.jabrutouch.ui.main.video_screen.ClosingService;
import co.il.jabrutouch.ui.main.video_screen.DottedSeekBar;
import co.il.jabrutouch.ui.main.video_screen.GallerySlideActivity;
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
import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;
import retrofit2.HttpException;

import static co.il.jabrutouch.MainActivity.PAGE_ITEM;
import static co.il.jabrutouch.MainActivity.TALMUD_TYPE;
import static co.il.jabrutouch.MyFireBaseMessagingService.AUDIO_MESSAGE;
import static co.il.jabrutouch.ui.main.audio_screen.AudioReceiver.ACTION_AUDIO;
import static co.il.jabrutouch.ui.main.message_screen.MessageActivity.AUDIO;
import static co.il.jabrutouch.ui.main.message_screen.MessageActivity.AUDIO_RECORD;
import static co.il.jabrutouch.ui.main.message_screen.MessageActivity.S3_SUB_FOLDER;
import static co.il.jabrutouch.ui.main.message_screen.MessageActivity.TEXT;
import static co.il.jabrutouch.ui.main.video_screen.VideoActivity.CURRENT_PAGE_ID;
import static co.il.jabrutouch.ui.main.video_screen.VideoActivity.CURRENT_PROGRESS;
import static co.il.jabrutouch.ui.main.video_screen.VideoActivity.IMAGES_LIST;
import static co.il.model.model.AnalyticsData.GEMARA;
import static co.il.model.model.AnalyticsData.MISHNA;
import static co.il.model.model.AnalyticsData.WATCH;
import static co.il.sqlcore.DBKeys.GEMARA_GALLERY_TABLE;
import static co.il.sqlcore.DBKeys.GEMARA_TABLE;
import static co.il.sqlcore.DBKeys.GEMARA_VIDEO_PARTS_TABLE;
import static co.il.sqlcore.DBKeys.MISHNA_GALLERY_TABLE;
import static co.il.sqlcore.DBKeys.MISHNA_TABLE;
import static co.il.sqlcore.DBKeys.MISHNA_VIDEO_PARTS_TABLE;


public class AudioActivity extends AppCompatActivity implements View.OnClickListener,
        AudioReceiver.audioReceiverListener, DonationDialog.DonationDialogListener,
        CustomEditText2.CustomEditText2Listener, DonationDialogNoDonate.DonationDialogNoDonateListener {


    private static final String PAGES_FILE = "pages/";
    private static final String POSITION = "POSITION";
    private static final int AUDIO_PERMISSIONS_REQUEST_WRITE_EXTERNAL_STORAGE = 1122;
    private static final String S3_BASE_URL = "https://jabrutouch-cms-media.s3-us-west-2.amazonaws.com/";
    private static final String AUDIO_FILE = "audio/";
    private static final String IMAGES_FILE = "image/";
    public static final String DOWNLOAD_AUDIO_PROGRESS = "DOWNLOAD_AUDIO_PROGRESS";
    public static final String AUDIO_PROGRESS = "AUDIO_PROGRESS";
    public static final String AUDIO_PROGRESS_PAGE_ITEM = "AUDIO_PROGRESS_PAGE_ITEM";
    public static final String AUDIO_PROGRESS_PAGE_ITEM_PARS = "AUDIO_PROGRESS_PAGE_ITEM_PARS";
    private static final int MY_PERMISSIONS_RECORD_AUDIO = 6734;
    private static final String M4A = ".m4a";
    private static final String ASK_THE_RABBI = "Ask the rabbi: ";
    private PDFView mPfdView;
    private PagesItem mPageItem;
    private TextView mNameTV;
    private ProgressBar mProgress;
    private TextView mStartTime;
    private TextView mPastTime;
    private Runnable runnable;
    private Handler handler = new Handler();
    private DottedSeekBar seekBar;
    private MediaPlayer mediaPlayer;
    private ImageView mSpeedIV;
    private ImageView mBackIV;
    private ImageView mPauseIV;
    private ImageView mForwardIV;
    private ImageView mBackArrow;
    private DBHandler dbHandler;
    private PagesItem dbPageItem;
    private String mTalmudType;
    private ImageView mDownloadBtnIV;
    private ImageView mMessageBtnIV;
    private ImageView mImageBtnIV;
    private CircleProgressBar mAudioProgressDownloadCPB;
    private boolean downloadPageFinished;
    private NotificationPanel mPanel;
    private boolean active;
    private AudioReceiver audioReciver;
    private long startTime;
    private long pauseTime;
    private long sumTimeUserUsed;
    private boolean onBackPressed;
    private int onlineForAnalytics = 1;
    private int audioPosition = 0;
    private boolean firstTime = true;
    AnalyticsData analyticsData = new AnalyticsData();
    private AudioManager.OnAudioFocusChangeListener mAudioListsner;
    private LinearLayout mBottomButtonsLL;
    private LinearLayout mMessageBoxLL;
    private CustomEditText2 mMessageET;
    private RecordView mRecordViewRV;
    private RecordButton mRecordButtonRB;
    private View mViewForSeekBar;
    private LinearLayout mAudioActivityLL;
    private String pathName;
    private MediaRecorder recorder;
    private LinearLayout mLinearTopLL;
    private LinearLayout mLinearForMessageLL;
    private TextView mCancelMessageModeTV;
    private ViewTreeObserver.OnGlobalLayoutListener globalLayoutListener;
    private Intent serviceIntent;
//    private ImageView mImageBtnForMessageModeIV;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        overridePendingTransition(R.anim.slide_in, R.anim.slide_out); // set animation to screen to start from the side
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN); // set full screen (no status bar)
        setContentView(R.layout.activity_audio);
        serviceIntent = new Intent(this, ClosingService.class);
        startService(serviceIntent);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);


        registerReceiver();
        initViews();
        initListeners();
        showDonationDialog();
        initImageIcon();
        downloadPdf();
        startFromLastState();
        checkItHasAudioRecordPermission();

        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {

                if (mediaPlayer == null) {

                    setAudioPlayer();
                }
            }
        });
        thread.start();

        setAudioTopBar();

        AudioManager audioManager = (AudioManager) getSystemService(AUDIO_SERVICE);

        mAudioListsner = initAudioListener();

        int result = Objects.requireNonNull(audioManager).requestAudioFocus(mAudioListsner, AudioManager.STREAM_MUSIC,
                AudioManager.AUDIOFOCUS_GAIN);

        if (result != AudioManager.AUDIOFOCUS_REQUEST_GRANTED) {
            // could not get audio focus.
        }

    }


    /**
     * init views
     */
    private void initViews() {

        mTalmudType = getIntent().getStringExtra(TALMUD_TYPE);
        mPageItem = (PagesItem) getIntent().getSerializableExtra(PAGE_ITEM);
        mPfdView = findViewById(R.id.AA_pdfView_PDFV);
        mNameTV = findViewById(R.id.AA_name_TV);
        mProgress = findViewById(R.id.AA_progress_bar);
        mStartTime = findViewById(R.id.AA_time_start_TV);
        mPastTime = findViewById(R.id.AA_time_past_TV);
        seekBar = findViewById(R.id.AA_seekbar_SB);
        mSpeedIV = findViewById(R.id.AA_speed_IV);
        mBackIV = findViewById(R.id.AA_back30_IV);
        mPauseIV = findViewById(R.id.AA_pause_IV);
        mForwardIV = findViewById(R.id.AA_forward10_IV);
        mBackArrow = findViewById(R.id.AA_arrow_back_IV);
        mDownloadBtnIV = findViewById(R.id.AA_download_btn_IV);
        mMessageBtnIV = findViewById(R.id.AA_message_btn_IV);
        mImageBtnIV = findViewById(R.id.AA_image_btn_IV);
        mAudioProgressDownloadCPB = findViewById(R.id.AA_progress_bar_PB);
        mMessageBoxLL = findViewById(R.id.AA_message_box_LL);
        mBottomButtonsLL = findViewById(R.id.AA_bottom_buttons_LL);
        mMessageET = findViewById(R.id.AA_message_ET);
        mRecordViewRV = findViewById(R.id.AA_record_view);
        mRecordButtonRB = findViewById(R.id.AA_record_button);
        mViewForSeekBar = findViewById(R.id.AA_view_for_keyboard);
        mAudioActivityLL = findViewById(R.id.AA_LL);
        mLinearTopLL = findViewById(R.id.AA_linear_top);
        mLinearForMessageLL = findViewById(R.id.AA_linear_for_message);
        mCancelMessageModeTV = findViewById(R.id.AA_cancel_TV);
//        mImageBtnForMessageModeIV = findViewById(R.id.AA_image_btn_in_message_mode_IV);


        checkIfPageExistInDB();

    }


    /**
     * init listeners
     */
    private void initListeners() {


        mSpeedIV.setOnClickListener(this);
        mBackIV.setOnClickListener(this);
        mPauseIV.setOnClickListener(this);
        mForwardIV.setOnClickListener(this);
        mBackArrow.setOnClickListener(this);
        mDownloadBtnIV.setOnClickListener(this);
        mMessageBtnIV.setOnClickListener(this);
        mImageBtnIV.setOnClickListener(this);

        if (mCancelMessageModeTV != null) {

            mCancelMessageModeTV.setOnClickListener(this);
        }

//        if (mImageBtnForMessageModeIV != null) {
//
//            mImageBtnForMessageModeIV.setOnClickListener(this);
//        }

    }


    /**
     * start the audio from the last position
     */
    private void startFromLastState() {

        if (dbPageItem != null) {

            audioPosition = (int) dbPageItem.getTimeLine();
        }


    }


    /**
     * init image icon
     */
    private void initImageIcon() {


        if (mPageItem != null) {

            if (mPageItem.getGallery() == null) {

                mImageBtnIV.setBackground(ContextCompat.getDrawable(this, R.drawable.ic_image_gray));

//                if (mImageBtnForMessageModeIV != null) {
//
//                    mImageBtnForMessageModeIV.setVisibility(View.GONE);
//                }

            } else if (mPageItem.getGallery() != null && mPageItem.getGallery().size() == 0) {

                mImageBtnIV.setBackground(ContextCompat.getDrawable(this, R.drawable.ic_image_gray));

//                if (mImageBtnForMessageModeIV != null) {
//
//                    mImageBtnForMessageModeIV.setVisibility(View.GONE);
//                }
            }
        }


        if (dbPageItem != null) {

            if (dbPageItem.getGallery() == null) {

                mImageBtnIV.setBackground(ContextCompat.getDrawable(this, R.drawable.ic_image_gray));

//                if (mImageBtnForMessageModeIV != null) {
//
//                    mImageBtnForMessageModeIV.setVisibility(View.GONE);
//                }

            } else if (dbPageItem.getGallery() != null && dbPageItem.getGallery().size() == 0) {

                mImageBtnIV.setBackground(ContextCompat.getDrawable(this, R.drawable.ic_image_gray));

//                if (mImageBtnForMessageModeIV != null) {
//
//                    mImageBtnForMessageModeIV.setVisibility(View.GONE);
//                }
            }
        }


    }


    /**
     * show donation dialog only if user didn't download the page
     */
    private void showDonationDialog() {

        if (dbPageItem == null || dbPageItem.getAudio().equals("")) {

            RequestManager.getLessonDonations(UserManager.getToken(this)).subscribe(new Observer<Result<LessonDonationBy>>() {
                @Override
                public void onSubscribe(Disposable d) {

                }

                @Override
                public void onNext(Result<LessonDonationBy> lessonDonationByResult) {

                    setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

                    if (lessonDonationByResult.getData().getDonatedBy().size() == 0){

                        DonationDialogNoDonate donationDialogNoDonate = new DonationDialogNoDonate();
                        donationDialogNoDonate.showDialog(AudioActivity.this, AudioActivity.this);
                    }
                    else {

                        DonationDialog donationDialog = new DonationDialog();
                        donationDialog.showDialog(AudioActivity.this, AudioActivity.this, lessonDonationByResult.getData());

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
//            DonationDialog donationDialog = new DonationDialog();
//            donationDialog.showDialog(this, this);
        }

    }


    /**
     * register receiver
     */
    private void registerReceiver() {

        audioReciver = new AudioReceiver(this);
        IntentFilter filter = new IntentFilter(ACTION_AUDIO);
        registerReceiver(audioReciver, filter);

    }


    @Override
    protected void onDestroy() {

        handler.removeCallbacks(runnable);
        super.onDestroy();

        unregisterReceiver(audioReciver);

        if (mediaPlayer != null) {

            saveUserState(mediaPlayer.getCurrentPosition());
        }


        mAudioListsner = null;

        if (mediaPlayer != null) {


            mediaPlayer.stop();

            if (mPanel != null) {

                mPanel.notificationCancel();
            }

            mediaPlayer.release();
            mediaPlayer = null;

        }


        if (mPanel != null) {
            mPanel.notificationCancel();

        }

        stopService(serviceIntent);

    }


    /**
     * save user state to show in progress
     *
     * @param currentPosition long
     */
    private void saveUserState(long currentPosition) {

        dbHandler.updateTimeLineInTable(String.valueOf(mPageItem.getId()), currentPosition, mTalmudType);
    }


    /**
     * check if page exist in data base
     */
    private void checkIfPageExistInDB() {

        dbHandler = new DBHandler(this);

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

                dbPageItem = generateMishnaItemToPageItem(dbMishnaItem);
            }

            if (dbPageItem != null) {

                dbPageItem.setVideoPart(dbHandler.getVideoPartForMishna(MISHNA_VIDEO_PARTS_TABLE, String.valueOf(mPageItem.getId())));
            }

            if (dbPageItem != null) {

                dbPageItem.setGallery(dbHandler.getMishnaGallery(MISHNA_GALLERY_TABLE, String.valueOf(mPageItem.getId())));
            }


        }


    }


    /**
     * generate  mishna item to page item
     *
     * @param mishnayotItem MishnayotItem
     * @return PagesItem
     */
    private PagesItem generateMishnaItemToPageItem(MishnayotItem mishnayotItem) {

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
    protected void onStart() {
        super.onStart();
        active = true;
    }


    @Override
    protected void onStop() {
        super.onStop();
        active = false;
    }


    /**
     * set image click animation
     *
     * @param Icon ImageView
     */
    private void setImageClickedAnimation(ImageView Icon) {

        Animation animation = AnimationUtils.loadAnimation(this, R.anim.alpha);
        Icon.startAnimation(animation);

    }


    /**
     * send analytics data to server
     *
     * @param sumTimeUserUsed long
     */
    private void sendAnalyticsData(long sumTimeUserUsed) {


        pauseTime = System.currentTimeMillis();
        if (startTime != 0) {

            sumTimeUserUsed += (pauseTime - startTime);
            startTime = 0;
        }

        AnalyticsData analyticsData = new AnalyticsData();
        analyticsData.setEvent(WATCH);

        if (mTalmudType.equals(GEMARA_TABLE)) {

            analyticsData.setCategory(GEMARA);

        } else if (mTalmudType.equals(MISHNA_TABLE)) {

            analyticsData.setCategory(MISHNA);
        }

        analyticsData.setMediaType(AnalyticsData.AUDIO);
        analyticsData.setPageId(String.valueOf(mPageItem.getId()));
        analyticsData.setDuration(sumTimeUserUsed);
        analyticsData.setOnline(onlineForAnalytics);


        RequestManager.sendAnalytics(UserManager.getToken(this), analyticsData).subscribe(new Observer<Result>() {
            @Override
            public void onSubscribe(Disposable d) {

            }

            @Override
            public void onNext(Result result) {


                UserManager.setAnalyticsData(null, AudioActivity.this);
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
     * on screen rotate
     *
     * @param newConfig Configuration
     */
    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public void onConfigurationChanged(@NotNull Configuration newConfig) {
        super.onConfigurationChanged(newConfig);

        int newOrientation = newConfig.orientation;

        if (newOrientation == Configuration.ORIENTATION_LANDSCAPE) {
            setContentView(R.layout.activity_audio_land);
            initViews();
            initImageIcon();
            initListeners();
            downloadPdf();
            updateSpeedButton();

            if (mediaPlayer != null) {
                seekBar.setMax(mediaPlayer.getDuration());
                seekBar.setProgress(mediaPlayer.getCurrentPosition());


                if (!mediaPlayer.isPlaying()) {

                    mPauseIV.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.ic_play_selector));

                } else {

                    mPauseIV.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.ic_jabru_pause_selector));

                }

                mediaPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                    @Override
                    public void onPrepared(MediaPlayer mp) {
                        seekBar.setMax(mp.getDuration());
                        changeSeekBar(seekBar, mp);
                    }

                    ;
                });


                seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
                    @Override
                    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                        if (fromUser) {
                            mediaPlayer.seekTo(progress);
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


        } else {

            setContentView(R.layout.activity_audio);
            initViews();
            initImageIcon();
            initListeners();
            downloadPdf();
            setAudioTopBar();
            updateSpeedButton();

            if (mediaPlayer != null) {


                seekBar.setMax(mediaPlayer.getDuration());
                seekBar.setProgress(mediaPlayer.getCurrentPosition());

                int duration = mediaPlayer.getDuration();

                @SuppressLint("DefaultLocale") String time = String.format("%02d:%02d ",
                        TimeUnit.MILLISECONDS.toMinutes(duration),
                        TimeUnit.MILLISECONDS.toSeconds(duration) -
                                TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(duration))
                );


                mStartTime.setText(time);

            }

            if (dbPageItem != null && !dbPageItem.getAudio().equals("")) {

                mDownloadBtnIV.setVisibility(View.GONE);

            }


            if (!Objects.requireNonNull(mediaPlayer).isPlaying()) {

                mPauseIV.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.ic_play_selector));

            } else {

                mPauseIV.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.ic_jabru_pause_selector));

                int paddingDp = 15;
                float density = this.getResources().getDisplayMetrics().density;
                int paddingPixel = (int) (paddingDp * density);
                mPauseIV.setPadding(paddingPixel, paddingPixel, paddingPixel, paddingPixel);


            }


            mediaPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                @Override
                public void onPrepared(MediaPlayer mp) {
                    seekBar.setMax(mp.getDuration());
                    changeSeekBar(seekBar, mp);
                }


            });


            seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
                @Override
                public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                    if (fromUser) {
                        mediaPlayer.seekTo(progress);
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
    }


    /**
     * update speed button
     */
    @RequiresApi(api = Build.VERSION_CODES.M)
    private void updateSpeedButton() {

        if (mediaPlayer != null) {
            if (mediaPlayer.getPlaybackParams().getSpeed() == 1.0) {

                mSpeedIV.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.ic_jabru_speed_selector));

            } else if (mediaPlayer.getPlaybackParams().getSpeed() == 1.5) {

                mSpeedIV.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.ic_audio_speed_x2_selector));

            } else {

                mSpeedIV.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.ic_audio_speed_x3_selector));

            }

        }

    }


    /**
     * set the audio player
     */
    private void setAudioPlayer() {

        Uri uri;

        if (dbPageItem != null && !dbPageItem.getAudio().equals("")) {

            uri = Uri.parse(dbPageItem.getAudio());
            onlineForAnalytics = 0;

            runOnUiThread(new Runnable() {
                @Override
                public void run() {

                    mDownloadBtnIV.setVisibility(View.GONE);
                }
            });


        } else {

            uri = Uri.parse(S3_BASE_URL + mPageItem.getAudio());
        }


        mediaPlayer = MediaPlayer.create(this, uri);

        if (audioPosition != 0) {
            mediaPlayer.seekTo(audioPosition);
        }

        seekBar.setMax(mediaPlayer.getDuration());


        int duration = mediaPlayer.getDuration();

        @SuppressLint("DefaultLocale") final String time = String.format("%02d:%02d ",
                TimeUnit.MILLISECONDS.toMinutes(duration),
                TimeUnit.MILLISECONDS.toSeconds(duration) -
                        TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(duration))
        );


        runOnUiThread(new Runnable() {
            @Override
            public void run() {

                mStartTime.setText(time);
                seekBar.setProgress(0);
                startTime = System.currentTimeMillis();
                changeSeekBar(seekBar, mediaPlayer);

            }
        });


        mediaPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mp) {
                seekBar.setMax(mp.getDuration());
                changeSeekBar(seekBar, mp);
            }


        });


        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if (fromUser) {
                    mediaPlayer.seekTo(progress);
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


    /**
     * change the seek bar
     *
     * @param seekBar     SeekBar
     * @param mediaPlayer MediaPlayer
     */
    private void changeSeekBar(final SeekBar seekBar, final MediaPlayer mediaPlayer) {

        seekBar.setProgress(mediaPlayer.getCurrentPosition());

        if (mediaPlayer.isPlaying()) {
            runnable = new Runnable() {
                @Override
                public void run() {

                    changeSeekBar(seekBar, mediaPlayer);

                    if (mediaPlayer.isPlaying()) {


                        int duration = mediaPlayer.getCurrentPosition();
                        @SuppressLint("DefaultLocale") String time = String.format("%02d:%02d ",
                                TimeUnit.MILLISECONDS.toMinutes(duration),
                                TimeUnit.MILLISECONDS.toSeconds(duration) -
                                        TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(duration))
                        );

                        mPastTime.setText(time);

                        int backPosition = mediaPlayer.getDuration() - duration;
                        @SuppressLint("DefaultLocale") String backTime = String.format("-%02d:%02d ",
                                TimeUnit.MILLISECONDS.toMinutes(backPosition),
                                TimeUnit.MILLISECONDS.toSeconds(backPosition) -
                                        TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(backPosition))
                        );

                        mStartTime.setText(backTime);
                    }
                }
            };

            handler.postDelayed(runnable, 1000);
        }


    }


    @Override
    protected void onResume() {
        super.onResume();

    }


    @Override
    protected void onPause() {
        super.onPause();


    }


    @Override
    public void onBackPressed() {

        long currentPosition = 0;

        if (mediaPlayer != null) {

            currentPosition = mediaPlayer.getCurrentPosition();
            onBackPressed = true;
            mediaPlayer.stop();

            if (startTime != 0 && mediaPlayer.isPlaying()) {

                pauseTime = System.currentTimeMillis();
                sumTimeUserUsed += (pauseTime - startTime);
                startTime = 0;
            }

            sendAnalyticsData(sumTimeUserUsed);


            if (mPanel != null) {

                mPanel.notificationCancel();
            }
        }


        if (mPanel != null) {

            mPanel.notificationCancel();
        }

        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED);

        Intent returnIntent = new Intent();
        returnIntent.putExtra(CURRENT_PROGRESS, currentPosition);
        returnIntent.putExtra(CURRENT_PAGE_ID, mPageItem.getId());
        setResult(Activity.RESULT_OK, returnIntent);
        finish();
    }


    /**
     * download the pdf page
     */
    private void downloadPdf() {

        if (dbPageItem != null && !dbPageItem.getPage().equals("")) {

            mProgress.setVisibility(View.GONE);
            mPfdView.fromFile(new File(dbPageItem.getPage())).load();


        } else if (!mPageItem.getPage().equals("")) {

            if (checkPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE, AUDIO_PERMISSIONS_REQUEST_WRITE_EXTERNAL_STORAGE)) {

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
                        mProgress.setVisibility(View.GONE);
                        mPfdView.fromFile(new File(pathName)).load();

                        mPageItem.setPage(pathName);
                        addDownloadPageToDB();
                        downloadPageFinished = true;

                    }

                    @Override
                    public void onDownloadError() {

                    }
                });

            }
        }


    }


    /**
     * add downloaded page to data base
     */
    private void addDownloadPageToDB() {

        DataBaseManager dataBaseManager = new DataBaseManager();
        dataBaseManager.addPageToDB(this, mPageItem, mTalmudType);

    }


    /**
     * Check if permission "Manifest.permission.READ_EXTERNAL_STORAGE" or
     * "Manifest.permission.WRITE_EXTERNAL_STORAGE" was granted.
     * If the permission wasn't granted, the app request it and the result is handled at:
     */
    private static boolean checkPermission(Activity activity, String permission, int permissionCode) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (activity.checkSelfPermission(permission)
                    != PackageManager.PERMISSION_GRANTED) {

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


            case AUDIO_PERMISSIONS_REQUEST_WRITE_EXTERNAL_STORAGE:

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
                        mProgress.setVisibility(View.GONE);
                        mPfdView.fromFile(new File(pathName)).load();

                        mPageItem.setPage(pathName);
                        addDownloadPageToDB();
                        downloadPageFinished = true;

                    }

                    @Override
                    public void onDownloadError() {

                    }
                });

                break;


            case MY_PERMISSIONS_RECORD_AUDIO:

                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    mRecordButtonRB.setListenForRecord(true);
                }

                break;

        }

    }


    /**
     * set audio top bar text
     */
    @SuppressLint("SetTextI18n")
    private void setAudioTopBar() {

        if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {

            if (mTalmudType.equals(GEMARA_TABLE)) {

                mNameTV.setText(mPageItem.getMasechetName() + " " + mPageItem.getPageNumber());

            } else if (mTalmudType.equals(MISHNA_TABLE)) {

                mNameTV.setText(mPageItem.getMasechetName() + " " + mPageItem.getChapter() + "-" + mPageItem.getPageNumber());


            }
        }

    }


    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public void onClick(View view) {

        switch (view.getId()) {


            case R.id.AA_speed_IV:

                setSpeedMode();

                break;


            case R.id.AA_back30_IV:

                setBackButton();

                break;


            case R.id.AA_pause_IV:

                setPauseAndPlayButton();

                break;


            case R.id.AA_forward10_IV:

                setForwardButton();

                break;


            case R.id.AA_arrow_back_IV:

                setImageClickedAnimation(mBackArrow);
                setArrowBackButton();

                break;


            case R.id.AA_download_btn_IV:

//                setImageClickedAnimation(mDownloadBtnIV);
                downloadAudioAndRefreshPage();

                break;


            case R.id.AA_image_btn_IV:
//            case R.id.AA_image_btn_in_message_mode_IV:

                setImageClickedAnimation(mImageBtnIV);
                setImageButton();

                break;


            case R.id.AA_message_btn_IV:

                setImageClickedAnimation(mMessageBtnIV);
//                Toast.makeText(this, getResources().getString(R.string.in_development), Toast.LENGTH_LONG).show();

                openMessageBar();

                break;


            case R.id.AA_cancel_TV:

                closeMessageMode();
                startAudioAgain();

                break;


        }


    }


    private void setPauseAndPlayButton() {


        if (mediaPlayer != null) {
            if (mediaPlayer.isPlaying()) {

                mediaPlayer.pause();

                pauseTime = System.currentTimeMillis();
                if (startTime != 0) {

                    sumTimeUserUsed += (pauseTime - startTime);
                    analyticsData.setDuration(sumTimeUserUsed);
                    analyticsData.setPlaying(false);

                    Gson gson = new Gson();
                    String analyticsDataString = gson.toJson(analyticsData);
                    UserManager.setAnalyticsData(analyticsDataString, this);

                    startTime = 0;
                }

                mPauseIV.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.ic_play_selector));

                if (mTalmudType.equals(GEMARA_TABLE)) {

                    mPanel = new NotificationPanel(this, mPageItem.getMasechetName() + " " + mPageItem.getPageNumber(), mediaPlayer.isPlaying(), true);

                } else if (mTalmudType.equals(MISHNA_TABLE)) {


                    mPanel = new NotificationPanel(this, mPageItem.getMasechetName() + " " + mPageItem.getChapter() + "-" + mPageItem.getPageNumber(), mediaPlayer.isPlaying(), false);

                }


            } else {
                mediaPlayer.start();
                firstTime = false;
                startTime = System.currentTimeMillis();
                mPauseIV.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.ic_jabru_pause_selector));


                if (mTalmudType.equals(GEMARA_TABLE)) {

                    mPanel = new NotificationPanel(this, mPageItem.getMasechetName() + " " + mPageItem.getPageNumber(), mediaPlayer.isPlaying(), true);

                } else if (mTalmudType.equals(MISHNA_TABLE)) {


                    mPanel = new NotificationPanel(this, mPageItem.getMasechetName() + " " + mPageItem.getChapter() + "-" + mPageItem.getPageNumber(), mediaPlayer.isPlaying(), false);

                }


                if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {
                    int paddingDp = 15;
                    float density = this.getResources().getDisplayMetrics().density;
                    int paddingPixel = (int) (paddingDp * density);
                    mPauseIV.setPadding(paddingPixel, paddingPixel, paddingPixel, paddingPixel);
                } else {

                    int paddingDp = 10;
                    float density = this.getResources().getDisplayMetrics().density;
                    int paddingPixel = (int) (paddingDp * density);
                    mPauseIV.setPadding(paddingPixel, paddingPixel, paddingPixel, paddingPixel);

                }

                changeSeekBar(seekBar, mediaPlayer);


                mediaPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                    @Override
                    public void onPrepared(MediaPlayer mp) {
                        seekBar.setMax(mp.getDuration());
                        changeSeekBar(seekBar, mp);
                    }

                    ;
                });


                seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
                    @Override
                    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                        if (fromUser) {
                            mediaPlayer.seekTo(progress);
                        }
                    }

                    @Override
                    public void onStartTrackingTouch(SeekBar seekBar) {

                    }

                    @Override
                    public void onStopTrackingTouch(SeekBar seekBar) {

                    }
                });


                analyticsData.setEvent(WATCH);

                if (mTalmudType.equals(GEMARA_TABLE)) {

                    analyticsData.setCategory(GEMARA);

                } else if (mTalmudType.equals(MISHNA_TABLE)) {

                    analyticsData.setCategory(MISHNA);
                }

                analyticsData.setMediaType(AnalyticsData.AUDIO);

                analyticsData.setPageId(String.valueOf(mPageItem.getId()));
                analyticsData.setTimeStart(System.currentTimeMillis());

                analyticsData.setOnline(1);
                analyticsData.setPlaying(true);
                Gson gson = new Gson();
                String analyticsDataString = gson.toJson(analyticsData);
                UserManager.setAnalyticsData(analyticsDataString, this);
            }

        }


    }


    private void setForwardButton() {

        if (mediaPlayer.getCurrentPosition() + 10000 <= mediaPlayer.getDuration()) {

            mediaPlayer.seekTo(mediaPlayer.getCurrentPosition() + 10000);
            seekBar.setProgress(seekBar.getProgress() + 10000);

        } else {
            mediaPlayer.seekTo(mediaPlayer.getDuration());
        }

    }


    private void setArrowBackButton() {

        if (mediaPlayer != null) {

            onBackPressed = true;
            mediaPlayer.stop();

            if (mPanel != null) {

                mPanel.notificationCancel();
            }
            active = false;

        }


        if (startTime != 0 && Objects.requireNonNull(mediaPlayer).isPlaying()) {

            pauseTime = System.currentTimeMillis();

            sumTimeUserUsed += (pauseTime - startTime);
            startTime = 0;
        }

        sendAnalyticsData(sumTimeUserUsed);

        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED);

        finish();


    }


    private void setImageButton() {


        if ((mPageItem != null && mPageItem.getGallery() != null && mPageItem.getGallery().size() > 0) ||
                (dbPageItem != null && dbPageItem.getGallery() != null && dbPageItem.getGallery().size() > 0)
        ) {

//            if (mImageBtnForMessageModeIV != null) {
//
//                setImageClickedAnimation(mImageBtnForMessageModeIV);
//            }

//            closeMessageMode();

            Intent intent = new Intent(this, GallerySlideActivity.class);
            intent.putExtra(IMAGES_LIST, (Serializable) mPageItem.getGallery());
            startActivity(intent);

        }


    }


    private void setBackButton() {


        if (mediaPlayer.getCurrentPosition() - 10000 >= 0) {

            mediaPlayer.seekTo(mediaPlayer.getCurrentPosition() - 10000);
            seekBar.setProgress(seekBar.getProgress() - 10000);

        } else {
            mediaPlayer.seekTo(0);
        }

    }


    @RequiresApi(api = Build.VERSION_CODES.M)
    private void setSpeedMode() {

        if (mediaPlayer != null) {
            if (mediaPlayer.isPlaying()) {

                if (mediaPlayer.getPlaybackParams().getSpeed() == 1.0) {

                    float speed = 1.5f;
                    mediaPlayer.setPlaybackParams(mediaPlayer.getPlaybackParams().setSpeed(speed));
                    mSpeedIV.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.ic_audio_speed_x2_selector));
                } else if (mediaPlayer.getPlaybackParams().getSpeed() == 1.5) {

                    float speed = 2f;
                    mediaPlayer.setPlaybackParams(mediaPlayer.getPlaybackParams().setSpeed(speed));
                    mSpeedIV.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.ic_audio_speed_x3_selector));

                } else {

                    float speed = 1f;
                    mediaPlayer.setPlaybackParams(mediaPlayer.getPlaybackParams().setSpeed(speed));
                    mSpeedIV.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.ic_jabru_speed_selector));

                }
            }
        }

    }


    /**
     * close message mode
     */
    private void closeMessageMode() {

        hideKeyboard(this);
        mBottomButtonsLL.setVisibility(View.VISIBLE);
        mMessageBoxLL.setVisibility(View.GONE);
        mViewForSeekBar.setVisibility(View.GONE);

        mLinearForMessageLL.setVisibility(View.GONE);
        mLinearTopLL.setVisibility(View.VISIBLE);

        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED);

    }


    /**
     * hide keyboard
     *
     * @param activity Activity
     */
    public static void hideKeyboard(Activity activity) {
        InputMethodManager imm = (InputMethodManager) activity.getSystemService(Activity.INPUT_METHOD_SERVICE);
        View view = activity.getCurrentFocus();
        if (view == null) {
            view = new View(activity);
        }
        Objects.requireNonNull(imm).hideSoftInputFromWindow(view.getWindowToken(), 0);
    }





    /**
     * open message bar
     */
    private void openMessageBar() {

        if (mediaPlayer != null) {

            audioPosition = mediaPlayer.getCurrentPosition();
            mediaPlayer.pause();

            if (mTalmudType.equals(GEMARA_TABLE)) {

                mPanel = new NotificationPanel(AudioActivity.this, mPageItem.getMasechetName() + " " + mPageItem.getPageNumber(), mediaPlayer.isPlaying(), true);

            } else if (mTalmudType.equals(MISHNA_TABLE)) {


                mPanel = new NotificationPanel(AudioActivity.this,
                        mPageItem.getMasechetName() + " " + mPageItem.getChapter() + "-" + mPageItem.getPageNumber(), mediaPlayer.isPlaying(), false);

            }
        }

        InputMethodManager inputMethodManager = (InputMethodManager) Objects.requireNonNull(this).getSystemService(this.INPUT_METHOD_SERVICE);
        mMessageET.requestFocus();
        Objects.requireNonNull(inputMethodManager).showSoftInput(mMessageET, 0);

        final Window mRootWindow = getWindow();

        mAudioActivityLL.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            public void onGlobalLayout() {

                setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);


                int screenHeight = (mAudioActivityLL.getRootView().getHeight());
                Rect r = new Rect();
                View view = mRootWindow.getDecorView();
                view.getWindowVisibleDisplayFrame(r);

                int keyBoardHeight = (int) ((screenHeight - (r.bottom + navigationBarHeight())) / getResources().getDisplayMetrics().density);

                if (keyBoardHeight > 50) {

                    int Height = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, keyBoardHeight, getResources().getDisplayMetrics());

                    LinearLayout.LayoutParams params2 = new LinearLayout.LayoutParams(
                            LinearLayout.LayoutParams.MATCH_PARENT, Height
                    );


                    mBottomButtonsLL.setVisibility(View.GONE);
                    mMessageBoxLL.setVisibility(View.VISIBLE);
                    mViewForSeekBar.setVisibility(View.VISIBLE);

                    mLinearForMessageLL.setVisibility(View.VISIBLE);
                    mLinearTopLL.setVisibility(View.GONE);

                    mMessageET.addListener(AudioActivity.this);

                    initMessageTextChangedListener();
                    setRecordView();

                    mViewForSeekBar.setLayoutParams(params2);
                    mAudioActivityLL.getViewTreeObserver().removeOnGlobalLayoutListener(this);

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

                    mRecordButtonRB.setBackground(ContextCompat.getDrawable(AudioActivity.this, R.drawable.ic_arrow_righte));


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
                            User userFromJson = gson.fromJson(UserManager.getUser(AudioActivity.this), User.class);
                            messagesObject.setFromUser(userFromJson.getId());

                            messagesObject.setSentAt(System.currentTimeMillis());

                            sendMessageToServerAndSaveInLocalStorage(messagesObject);

                            mMessageET.setText("");

                            mMessageET.setText("");

                        }
                    });


                } else if (charSequence.length() == 0) {

                    mRecordButtonRB.setBackground(ContextCompat.getDrawable(AudioActivity.this, R.drawable.ic_mic));
                    if (ContextCompat.checkSelfPermission(AudioActivity.this, Manifest.permission.RECORD_AUDIO) == PackageManager.PERMISSION_GRANTED) {
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
                    startAudioAgain();

                }

                @Override
                public void onError(Throwable e) {

                    mProgress.setVisibility(View.GONE);

                    if (e instanceof HttpException) {
                        HttpException exception = (HttpException) e;
                        ErrorResponse response = null;
                        try {
                            response = new Gson().fromJson(Objects.requireNonNull(exception.response().errorBody()).string(), ErrorResponse.class);
                            Toast.makeText(AudioActivity.this, Objects.requireNonNull(response).getErrors().get(0).getMessage(), Toast.LENGTH_LONG).show();
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
            UserManager.setMessageForOffline(jsonMessage, AudioActivity.this);

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
//        newChatObject.setUnreadMessages(1);

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

            messagesObject.setIsGemara(false);
            title = mPageItem.getMasechetName() + " " + mPageItem.getChapter() + "-" + mPageItem.getPageNumber();


        }

        messagesObject.setLessonId(mPageItem.getId());

        messagesObject.setTitle("Ask the rabbi: " + title);
        messagesObject.setIsMine(true);
        messagesObject.setMessageType(AUDIO);
        messagesObject.setIsMine(true);

        if (mPageItem.getPresenter() != null) {

            messagesObject.setToUser(mPageItem.getPresenter().getId());
        }


        Gson gson = new Gson();
        User userFromJson = gson.fromJson(UserManager.getUser(AudioActivity.this), User.class);
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

        if (ContextCompat.checkSelfPermission(AudioActivity.this, Manifest.permission.RECORD_AUDIO) != PackageManager.PERMISSION_GRANTED) {

            mRecordButtonRB.setListenForRecord(false);

            mRecordButtonRB.setOnRecordClickListener(new OnRecordClickListener() {
                @Override
                public void onClick(View v) {

                    closeMessageMode();
                    ActivityCompat.requestPermissions(AudioActivity.this, new String[]{Manifest.permission.RECORD_AUDIO}, MY_PERMISSIONS_RECORD_AUDIO);

                }
            });


        } else {

            mRecordButtonRB.setListenForRecord(true);

        }


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


    /**
     * download audio
     */
    private void downloadAudioAndRefreshPage() {

        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        mDownloadBtnIV.setVisibility(View.GONE);
        mAudioProgressDownloadCPB.setVisibility(View.VISIBLE);

        mPauseIV.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.ic_play_selector));

        if (mediaPlayer != null) {

            audioPosition = mediaPlayer.getCurrentPosition();
            mediaPlayer.pause();
        }


        S3Helper s3Helper = new S3Helper(this);

        if (!mPageItem.getPage().equals("") && !downloadPageFinished) {

            s3Helper.downloadFilePagesWithoutListener(mPageItem.getPage(), PAGES_FILE, this);
        }


        s3Helper.downloadFile(this, mPageItem.getAudio(), mPageItem.getPage(), AUDIO_FILE, new DownloadListener() {
            @Override
            public void onDownloadStart(int id, File fileLocation) {

            }

            @Override
            public void onProgressChanged(int percentDone) {

                if (mAudioProgressDownloadCPB != null) {

                    mAudioProgressDownloadCPB.setProgress(percentDone);

                    Intent resultsIntent = new Intent(DOWNLOAD_AUDIO_PROGRESS);
                    resultsIntent.putExtra(AUDIO_PROGRESS, String.valueOf(percentDone));
                    Bundle bundle = new Bundle();
                    bundle.putSerializable(AUDIO_PROGRESS_PAGE_ITEM_PARS, mPageItem);
                    resultsIntent.putExtra(AUDIO_PROGRESS_PAGE_ITEM, bundle);
                    resultsIntent.putExtra(TALMUD_TYPE, mTalmudType);

                    sendBroadcast(resultsIntent);
                }


            }

            @Override
            public void onDownloadWaiting(int id, TransferState state, File fileLocation) {

            }

            @Override
            public void onDownloadReceivedStop(int id, TransferState state, File fileLocation) {

            }

            @Override
            public void onDownloadFinish(int id, String link, String pathName, String pagePathName) {

                if (mAudioProgressDownloadCPB != null) {

                    mAudioProgressDownloadCPB.setVisibility(View.GONE);
                }

                DataBaseManager dataBaseManager = new DataBaseManager();

                if (mTalmudType.equals(GEMARA_TABLE)) {

                    dataBaseManager.onGeamraAudioDownloadFinished(AudioActivity.this, mPageItem, pathName, pagePathName);

                } else if (mTalmudType.equals(MISHNA_TABLE)) {

                    dataBaseManager.onMishnaAudioDownloadFinished(AudioActivity.this, generateMishnaObject(mPageItem), pathName, pagePathName);

                }


                if (active) {

                    checkIfPageExistInDB();

                    Thread thread = new Thread(new Runnable() {
                        @Override
                        public void run() {


                            setAudioPlayer();
                            downloadPdf();
                            if (mTalmudType.equals(GEMARA_TABLE)) {

                                mPanel = new NotificationPanel(AudioActivity.this, mPageItem.getMasechetName() + " " + mPageItem.getPageNumber(), mediaPlayer.isPlaying(), true);

                            } else if (mTalmudType.equals(MISHNA_TABLE)) {


                                mPanel = new NotificationPanel(AudioActivity.this,
                                        mPageItem.getMasechetName() + " " + mPageItem.getChapter() + "-" + mPageItem.getPageNumber(), mediaPlayer.isPlaying(), false);

                            }

                        }
                    });

                    thread.start();

                }

                setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED);


            }

            @Override
            public void onDownloadError() {

                if (mAudioProgressDownloadCPB != null) {

                    mAudioProgressDownloadCPB.setVisibility(View.GONE);
                }

                setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED);


            }
        });


        for (int i = 0; i < mPageItem.getGallery().size(); i++) {

            s3Helper.downloadFilePagesWithoutListener(mPageItem.getGallery().get(i).getImage(), IMAGES_FILE, this);
        }


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


    /**
     * on user press the pause button in the notification bar
     *
     * @param stringExtra String
     */
    @Override
    public void onStopClicked(String stringExtra) {

        if (stringExtra.equals("AUDIO_STOP")) {

            if (mediaPlayer.isPlaying()) {

                mediaPlayer.pause();

                pauseTime = System.currentTimeMillis();
                if (startTime != 0) {

                    sumTimeUserUsed += (pauseTime - startTime);
                    startTime = 0;
                }
                mPauseIV.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.ic_play_selector));

                if (mTalmudType.equals(GEMARA_TABLE)) {

                    mPanel = new NotificationPanel(this, mPageItem.getMasechetName() + " " + mPageItem.getPageNumber(), mediaPlayer.isPlaying(), true);

                } else if (mTalmudType.equals(MISHNA_TABLE)) {


                    mPanel = new NotificationPanel(this, mPageItem.getMasechetName() + " " + mPageItem.getChapter() + "-" + mPageItem.getPageNumber(), mediaPlayer.isPlaying(), false);

                }


            }

        } else if (stringExtra.equals("AUDIO_PLAY")) {

            if (!mediaPlayer.isPlaying()) {

                mediaPlayer.start();
                startTime = System.currentTimeMillis();
                mPauseIV.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.ic_jabru_pause_selector));

                if (mTalmudType.equals(GEMARA_TABLE)) {

                    mPanel = new NotificationPanel(this, mPageItem.getMasechetName() + " " + mPageItem.getPageNumber(), mediaPlayer.isPlaying(), true);

                } else if (mTalmudType.equals(MISHNA_TABLE)) {


                    mPanel = new NotificationPanel(this, mPageItem.getMasechetName() + " " + mPageItem.getChapter() + "-" + mPageItem.getPageNumber(), mediaPlayer.isPlaying(), false);

                }

                if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {
                    int paddingDp = 15;
                    float density = this.getResources().getDisplayMetrics().density;
                    int paddingPixel = (int) (paddingDp * density);
                    mPauseIV.setPadding(paddingPixel, paddingPixel, paddingPixel, paddingPixel);
                } else {

                    int paddingDp = 10;
                    float density = this.getResources().getDisplayMetrics().density;
                    int paddingPixel = (int) (paddingDp * density);
                    mPauseIV.setPadding(paddingPixel, paddingPixel, paddingPixel, paddingPixel);

                }

                changeSeekBar(seekBar, mediaPlayer);


                mediaPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                    @Override
                    public void onPrepared(MediaPlayer mp) {
                        seekBar.setMax(mp.getDuration());
                        changeSeekBar(seekBar, mp);
                    }

                    ;
                });


                seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
                    @Override
                    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                        if (fromUser) {
                            mediaPlayer.seekTo(progress);
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
        }

    }


    /**
     * when user clicked ok in donation dialog, set the screen orientation to unspecified
     * @param crownId
     */
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
        onBackPressed();
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





    /**
     * detect when user use the phone audio for other things
     *
     * @return
     */
    private AudioManager.OnAudioFocusChangeListener initAudioListener() {

        AudioManager.OnAudioFocusChangeListener audioListener = new AudioManager.OnAudioFocusChangeListener() {
            private boolean inListening;

            @Override
            public void onAudioFocusChange(int focusChange) {

                switch (focusChange) {
                    case AudioManager.AUDIOFOCUS_GAIN:


                        if (mediaPlayer != null && !mediaPlayer.isPlaying() && inListening) {

                            mediaPlayer.start();
                            inListening = false;
                            startTime = System.currentTimeMillis();
                            mPauseIV.setImageDrawable(ContextCompat.getDrawable(AudioActivity.this, R.drawable.ic_jabru_pause_selector));


                            if (mTalmudType.equals(GEMARA_TABLE)) {

                                mPanel = new NotificationPanel(AudioActivity.this, mPageItem.getMasechetName() + " " + mPageItem.getPageNumber(), mediaPlayer.isPlaying(), true);

                            } else if (mTalmudType.equals(MISHNA_TABLE)) {


                                mPanel = new NotificationPanel(AudioActivity.this, mPageItem.getMasechetName() + " " + mPageItem.getChapter() + "-" + mPageItem.getPageNumber(), mediaPlayer.isPlaying(), false);

                            }


                            if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {
                                int paddingDp = 15;
                                float density = AudioActivity.this.getResources().getDisplayMetrics().density;
                                int paddingPixel = (int) (paddingDp * density);
                                mPauseIV.setPadding(paddingPixel, paddingPixel, paddingPixel, paddingPixel);
                            } else {

                                int paddingDp = 10;
                                float density = AudioActivity.this.getResources().getDisplayMetrics().density;
                                int paddingPixel = (int) (paddingDp * density);
                                mPauseIV.setPadding(paddingPixel, paddingPixel, paddingPixel, paddingPixel);

                            }

                            changeSeekBar(seekBar, mediaPlayer);


                            mediaPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                                @Override
                                public void onPrepared(MediaPlayer mp) {
                                    seekBar.setMax(mp.getDuration());
                                    changeSeekBar(seekBar, mp);
                                }

                                ;
                            });


                            seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
                                @Override
                                public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                                    if (fromUser) {
                                        mediaPlayer.seekTo(progress);
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

                        break;

                    case AudioManager.AUDIOFOCUS_LOSS:


                    case AudioManager.AUDIOFOCUS_LOSS_TRANSIENT:
                        // Lost focus for a short time, but we have to stop
                        // playback. We don't release the media player because playback
                        // is likely to resume
                        if (mediaPlayer != null && mediaPlayer.isPlaying()) {
                            mediaPlayer.pause();
                            inListening = true;

                            pauseTime = System.currentTimeMillis();
                            if (startTime != 0) {

                                sumTimeUserUsed += (pauseTime - startTime);
                                startTime = 0;
                            }
                            mPauseIV.setImageDrawable(ContextCompat.getDrawable(AudioActivity.this, R.drawable.ic_play_selector));


                            if (mTalmudType.equals(GEMARA_TABLE)) {

                                mPanel = new NotificationPanel(AudioActivity.this, mPageItem.getMasechetName() + " " + mPageItem.getPageNumber(), mediaPlayer.isPlaying(), true);

                            } else if (mTalmudType.equals(MISHNA_TABLE)) {


                                mPanel = new NotificationPanel(AudioActivity.this,
                                        mPageItem.getMasechetName() + " " + mPageItem.getChapter() + "-" + mPageItem.getPageNumber(), mediaPlayer.isPlaying(), false);

                            }

                        }


                        break;

                    case AudioManager.AUDIOFOCUS_LOSS_TRANSIENT_CAN_DUCK:
                        // Lost focus for a short time, but it's ok to keep playing
                }
            }

        };
        return audioListener;
    }


    @Override
    public void onKeyPrime() {

        closeMessageMode();
        startAudioAgain();

    }




    public void startAudioAgain(){


        if (mediaPlayer != null && !mediaPlayer.isPlaying()) {

            mediaPlayer.start();
            startTime = System.currentTimeMillis();
            mPauseIV.setImageDrawable(ContextCompat.getDrawable(AudioActivity.this, R.drawable.ic_jabru_pause_selector));


            if (mTalmudType.equals(GEMARA_TABLE)) {

                mPanel = new NotificationPanel(AudioActivity.this, mPageItem.getMasechetName() + " " + mPageItem.getPageNumber(), mediaPlayer.isPlaying(), true);

            } else if (mTalmudType.equals(MISHNA_TABLE)) {


                mPanel = new NotificationPanel(AudioActivity.this, mPageItem.getMasechetName() + " " + mPageItem.getChapter() + "-" + mPageItem.getPageNumber(), mediaPlayer.isPlaying(), false);

            }


            if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {
                int paddingDp = 15;
                float density = AudioActivity.this.getResources().getDisplayMetrics().density;
                int paddingPixel = (int) (paddingDp * density);
                mPauseIV.setPadding(paddingPixel, paddingPixel, paddingPixel, paddingPixel);
            } else {

                int paddingDp = 10;
                float density = AudioActivity.this.getResources().getDisplayMetrics().density;
                int paddingPixel = (int) (paddingDp * density);
                mPauseIV.setPadding(paddingPixel, paddingPixel, paddingPixel, paddingPixel);

            }

            changeSeekBar(seekBar, mediaPlayer);


            mediaPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                @Override
                public void onPrepared(MediaPlayer mp) {
                    seekBar.setMax(mp.getDuration());
                    changeSeekBar(seekBar, mp);
                }

                ;
            });


            seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
                @Override
                public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                    if (fromUser) {
                        mediaPlayer.seekTo(progress);
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


    }


    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {

    }


}
