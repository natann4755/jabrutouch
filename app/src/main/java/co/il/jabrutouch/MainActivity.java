package co.il.jabrutouch;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;

import androidx.annotation.NonNull;

import com.amazonaws.mobileconnectors.s3.transferutility.TransferState;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationView;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;

import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.Bundle;

import androidx.appcompat.widget.Toolbar;

import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import org.java_websocket.client.WebSocketClient;
import org.java_websocket.drafts.Draft_6455;
import org.java_websocket.handshake.ServerHandshake;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Type;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import co.il.jabrutouch.data_base_manager.DataBaseManager;
import co.il.jabrutouch.server.RequestManager;
import co.il.jabrutouch.ui.main.about_screen.AboutActivity2;
import co.il.jabrutouch.ui.main.audio_screen.AudioActivity;
import co.il.jabrutouch.ui.main.audio_screen.AudioDownloadProgressReceiver;
import co.il.jabrutouch.ui.main.donation_screen.DonationActivity;
import co.il.jabrutouch.ui.main.donation_screen.DonationFragment;
import co.il.jabrutouch.ui.main.donation_screen.DonationFragmentNew;
import co.il.jabrutouch.ui.main.gemara_screen.NoPagesDialog;
import co.il.jabrutouch.ui.main.message_screen.ChatActivity;
import co.il.jabrutouch.ui.main.profile_screen.ProfileActivity;
import co.il.jabrutouch.ui.main.video_screen.VideoActivity;
import co.il.jabrutouch.ui.main.video_screen.VideoDownloadProgressReciver;
import co.il.jabrutouch.ui.main.wall_screen.NoInternetDialog;
import co.il.model.model.ChatObject;
import co.il.model.model.DonationData;
import co.il.model.model.MessageObject;
import co.il.model.model.Payment;
import co.il.model.model.SocketDonateMessage;
import co.il.model.model.TodayDafYomiDetailes;
import co.il.model.model.AnalyticsData;
import co.il.model.model.DownloadQueue;
import co.il.model.model.GemaraPages;
import co.il.model.model.MishnaMishnaiot;
import co.il.model.model.MishnayotItem;
import co.il.model.model.PagesItem;
import co.il.model.model.User;
import co.il.model.model.UserDonationStatus;
import co.il.model.model.masechet_list_model.ChaptersItem;
import co.il.model.model.masechet_list_model.MasechetItem;
import co.il.model.model.masechet_list_model.MasechetList;
import co.il.model.model.Result;
import co.il.jabrutouch.ui.sign_in.SignInActivity;
import co.il.jabrutouch.ui.main.profile_screen.LogOutDialog;
import co.il.jabrutouch.ui.main.downloads_screen.fragments.DownloadsFragment;
import co.il.jabrutouch.ui.main.downloads_screen.fragments.GemaraDownloadsFragment;
import co.il.jabrutouch.ui.main.downloads_screen.fragments.MishnaDownloadsFragment;
import co.il.jabrutouch.ui.main.gemara_screen.fragments.GemaraMainFragment;
import co.il.jabrutouch.ui.main.gemara_screen.fragments.GemaraMediaFragment;
import co.il.jabrutouch.ui.main.mishna_screen.MishnaMainFragment;
import co.il.jabrutouch.ui.main.mishna_screen.fragments.MishnaChapterFragment;
import co.il.jabrutouch.ui.main.mishna_screen.fragments.MishnaMediaFragment;
import co.il.jabrutouch.ui.main.wall_screen.WallFragment;
import co.il.jabrutouch.user_manager.UserManager;
import co.il.jabrutouch.utils.ActivityRunning;
import co.il.jabrutouch.utils.FragmentHelper;
import co.il.s3.interfaces.DownloadListener;
import co.il.s3.utils.Config;
import co.il.s3.utils.S3Helper;
import co.il.sqlcore.DBHandler;
import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;

import static co.il.jabrutouch.NewMessageReceiver.NEW_CHAT_RECIVER;
import static co.il.jabrutouch.ui.main.audio_screen.AudioActivity.DOWNLOAD_AUDIO_PROGRESS;
import static co.il.jabrutouch.ui.main.donation_screen.DedicationFragment.DONATION_DATA;
import static co.il.jabrutouch.ui.main.donation_screen.DedicationFragment.PAYMENT;
import static co.il.jabrutouch.ui.main.message_screen.MessageActivity.LINK_TO_TYPE;
import static co.il.jabrutouch.ui.main.message_screen.adapters.MessageAdapter.LINK_CROWNS;
import static co.il.jabrutouch.ui.main.message_screen.adapters.MessageAdapter.LINK_DOWNLOAD;
import static co.il.jabrutouch.ui.main.message_screen.adapters.MessageAdapter.LINK_GEMARA;
import static co.il.jabrutouch.ui.main.message_screen.adapters.MessageAdapter.LINK_MISHNA;
import static co.il.jabrutouch.ui.main.profile_screen.ProfileActivity.GO_TO_DONATION;
import static co.il.jabrutouch.ui.main.profile_screen.ProfileActivity.LOG_OUT;
import static co.il.jabrutouch.ui.main.profile_screen.ProfileActivity.RESULTS_FOR_PROFILE;
import static co.il.jabrutouch.ui.main.video_screen.VideoActivity.CURRENT_PAGE_ID;
import static co.il.jabrutouch.ui.main.video_screen.VideoActivity.CURRENT_PROGRESS;
import static co.il.jabrutouch.ui.main.video_screen.VideoActivity.DOWNLOAD_VIDEO_PROGRESS;
import static co.il.jabrutouch.ui.main.wall_screen.WallFragment.DAF_YOMI_DETAILES;
import static co.il.model.model.AnalyticsData.AUDIO;
import static co.il.model.model.AnalyticsData.DOWNLOAD;
import static co.il.sqlcore.DBKeys.CHAT_TABLE;
import static co.il.sqlcore.DBKeys.GEMARA_TABLE;
import static co.il.sqlcore.DBKeys.MISHNA_TABLE;


public class MainActivity extends AppCompatActivity implements
        WallFragment.OnWallFragmentListener,
        NavigationView.OnNavigationItemSelectedListener,
        MishnaMainFragment.OnMishnaMainFragmentListener,
        MishnaChapterFragment.OnMishnaChapterFragmentListener,
        MishnaMediaFragment.OnMishnaDownloadFragmentListener,
        GemaraMainFragment.OnGemaraMainFragmentListener,
        GemaraMediaFragment.OnGemaraDownloadFragmentListener,
        DownloadsFragment.OnDownloadsFragmentListener,
        GemaraDownloadsFragment.OnFragmentInteractionListener,
        MishnaDownloadsFragment.OnFragmentInteractionListener,
        DonationFragment.OnFragmentInteractionListener,
        LogOutDialog.LogOutDialogListener,
        BottomNavigationView.OnNavigationItemSelectedListener,
        AudioDownloadProgressReceiver.AudioDownloadProgressReceiverListener,
        VideoDownloadProgressReciver.VideoDownloadProgressReciverListener,
        DonationFragmentNew.OnDonationFragmentNewListener,
        NewMessageReceiver.NewMessageReceiverListener {


    private static final int START_PROFILE = 1000;
    private static final String AUDIO_FILE = "audio/";
    private static final String PAGES_FILE = "pages/";
    private static final String IMAGES_FILE = "image/";
    private static final int MY_PERMISSIONS_REQUEST_WRITE_EXTERNAL_STORAGE = 1001;
    public static final String PAGE_ITEM = "PAGE_ITEM";
    private static final int MY_MISHNA_PERMISSIONS_REQUEST_WRITE_EXTERNAL_STORAGE = 1002;
    private static final String VIDEO_FILE = "video/";
    private static final String VIDEO = "video";
    public static final String MISHNA_ITEM = "MISHNA_ITEM";
    public static final String TALMUD_TYPE = "TALMUD_TYPE";
    public static final String MISHNA = "MISHNA";
    public static final String GEMARA = "GEMARA";
    private static final int MY_PERMISSIONS_REQUEST_WRITE_EXTERNAL_STORAGE_VIDEO = 1333;
    private static final int BACK_FROM_VIDEO_ACTIVITY = 2655;
    private static final int MY_PERMISSIONS_REQUEST_WRITE_EXTERNAL_STORAGE_VIDEO_MISHNA = 1337;
    private static final int BACK_FROM_CHAT = 1298;
    private static final int BACK_FROM_SLIDE_DONATION_ACTIVITY = 9774;
    private static final int RESULT_FROM_DONATION = 2345;
    public static final String START_SUB_DETAILS = "START_SUB_DETAILS";
    private static final String WEB_SOCKET_URL = "wss://jabrutouch.ravtech.co.il/ws/lesson_watch_count";
    private static final int BACK_FROM_DONATION_NO_DONATED = 2132;
    public static final String USER_DONATION_STATUS = "USER_DONATION_STATUS";
    private Toolbar myToolbar;
    private DrawerLayout mDrawerLayout;
    private NavigationView mNavigationView;
    private FragmentHelper mFragmentHelper;
    private BottomNavigationView mBottomNavigationView;
    private WallFragment mWallFragment;
    private MishnaMainFragment mMishnaMainFragment;
    private MishnaChapterFragment mMishnaChapterFragment;
    private MishnaMediaFragment mMishnaMediaFragment;
    private GemaraMainFragment mGemaraMainFragment;
    private GemaraMediaFragment mGemaraMediaFragment;
    private DownloadsFragment mDownloadsFragment;
    private MasechetList mMasechtotList;
    private PagesItem mPageItemForOnRequestPermissionsResult;
    private DownloadListener mdownloadListenerForOnRequestPermissionsResult;
    private MishnayotItem mMishnayotItemForOnRequestPermissionsResult;
    private int mdownloadPositin;
    private PagesItem mDafYomiDetailes;
    private DonationFragment mDonationFragment;
    private ImageView mBubbleIcon;
    private int downloadCounter;
    private List<DownloadQueue> downloadQueueList = new ArrayList<>();
    private PagesItem pageItemFromPremission;
    private int positionFromPremission;
    private AudioDownloadProgressReceiver audioDownloadProgressReciver;
    private VideoDownloadProgressReciver videoDownloadProgressReciver;
    private MishnayotItem mishnaItemFromPremission;
    private TextView mBubbleIconBudget;
    private NewMessageReceiver mNewChatReciever;
    private DonationFragmentNew mNewDonationFragment;
    private int mLinkToType;
    private WebSocketClient mWebSocketClient;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initViews();
        initListeners();
        getMasechtotListFromLocalStorage();
        setMainToolBar();
        registerReceiver();
        initBubbleBudget();

        if (mDafYomiDetailes != null) {

            startWallFragment();

        } else {

            if (mMasechtotList != null) {

                getGemaraDafYomiDetailes();
            }

        }


        sendAnalyticsDataFromSharedPreferences();
        getMasechtotListFromServer();

    }


    private void sendAnalyticsDataFromSharedPreferences() {

        if (UserManager.getAnalyticsData(this) != null) {

            Gson gson = new Gson();
            AnalyticsData analyticsData = gson.fromJson(UserManager.getAnalyticsData(this), AnalyticsData.class);


            RequestManager.sendAnalytics(UserManager.getToken(this), analyticsData).subscribe(new Observer<Result>() {
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


            UserManager.setAnalyticsData(null, this);


        }


    }


    private void getMasechtotListFromServer() {
        RequestManager.getMasechtotList().subscribe(new Observer<Result<MasechetList>>() {
            @Override
            public void onSubscribe(Disposable d) {
            }

            @Override
            public void onNext(Result<MasechetList> masechetListResult) {
                saveMasechtotList(masechetListResult.getData());
                mMasechtotList = masechetListResult.getData();
            }

            @Override
            public void onError(Throwable e) {
            }

            @Override
            public void onComplete() {
            }
        });


    }


    private void saveMasechtotList(MasechetList masechetList) {
        Gson gson = new Gson();
        String jsonMasechtotList = gson.toJson(masechetList);
        UserManager.setMasechtotList(jsonMasechtotList, this);

    }


    private void checkIfCameFromLinkMessage() {

        if (mLinkToType > 0) {

            switch (mLinkToType) {

                case LINK_CROWNS:

                    if (isNetworkAvailable()) {

                        startDonationFragment();
                        updateNavigationBarState(R.id.action_donations);
                        mLinkToType = 0;
                    } else {

                        NoInternetDialog noInternetDialog = new NoInternetDialog();
                        noInternetDialog.showDialog(this);
                    }

                    break;

                case LINK_DOWNLOAD:

                    startDownloadFragment();
                    updateNavigationBarState(R.id.action_downloads);
                    mLinkToType = 0;

                    break;

                case LINK_GEMARA:

                    startGemaraFragment();
                    updateNavigationBarState(R.id.action_gemara);
                    mLinkToType = 0;


                    break;

                case LINK_MISHNA:


                    startMishnaFragment();
                    updateNavigationBarState(R.id.action_mishna);
                    mLinkToType = 0;

                    break;


            }


        }


    }


    @Override
    protected void onDestroy() {
        super.onDestroy();

        unregisterReceiver(audioDownloadProgressReciver);
        unregisterReceiver(videoDownloadProgressReciver);
        unregisterReceiver(mNewChatReciever);

    }


    @Override
    public void onBackPressed() {


        if (this.mDrawerLayout.isDrawerOpen(GravityCompat.START)) {
            this.mDrawerLayout.closeDrawer(GravityCompat.START);

        } else if (mFragmentHelper.isCurrent(MishnaMainFragment.TAG) ||
                mFragmentHelper.isCurrent(GemaraMainFragment.TAG) ||
                mFragmentHelper.isCurrent(DonationFragment.TAG) ||
                mFragmentHelper.isCurrent(DonationFragmentNew.TAG) ||
                mFragmentHelper.isCurrent(DownloadsFragment.TAG)) {

            updateNavigationBarState(R.id.action_main);
            startWallFragment();

            if (mFragmentHelper.isCurrent(DonationFragmentNew.TAG)) {

                mWebSocketClient.close();
            }

        } else if (mFragmentHelper.isCurrent(WallFragment.TAG)) {

            finish();
        } else {

            super.onBackPressed();
        }


    }


    /**
     * init views
     */
    private void initViews() {

        myToolbar = findViewById(R.id.MA_toolbar_TB);
        mDrawerLayout = findViewById(R.id.MA_drawer_layout_DL);
        mNavigationView = findViewById(R.id.MA_navigation_view_NV);
        mBottomNavigationView = findViewById(R.id.MA_bottom_navigation_BNV);
        mFragmentHelper = new FragmentHelper(this, new ActivityRunning());
        mDafYomiDetailes = (PagesItem) getIntent().getSerializableExtra(DAF_YOMI_DETAILES);
        mBubbleIcon = findViewById(R.id.MA_bubble_ic_IV);
        mBubbleIconBudget = findViewById(R.id.MA_budget_TV);
        mLinkToType = getIntent().getIntExtra(LINK_TO_TYPE, 0);
    }


    /**
     * init listeners
     */
    private void initListeners() {
        mNavigationView.setNavigationItemSelectedListener(this);

        setInactiveNavigationItemAppearInactive(); // TODO remember to change it when message center work

        mBottomNavigationView.setOnNavigationItemSelectedListener(this);


        mBubbleIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                setImageClickedAnimation(mBubbleIcon);

                Intent intent = new Intent(MainActivity.this, ChatActivity.class);
                startActivityForResult(intent, BACK_FROM_CHAT);

            }
        });
    }


    /**
     * to change navigation item color
     */
    private void setInactiveNavigationItemAppearInactive() {

//        SpannableString spanString2 = new SpannableString(mNavigationView.getMenu().getItem(6).getTitle().toString());
//        spanString2.setSpan(new ForegroundColorSpan(ContextCompat.getColor(this, R.color.grayIndicatorInactive)), 0, spanString2.length(), 0);
//        mNavigationView.getMenu().getItem(6).setTitle(spanString2);

    }


    private void setImageClickedAnimation(ImageView icon) {

        Animation animation = AnimationUtils.loadAnimation(this, R.anim.alpha);
        icon.startAnimation(animation);

    }


    private void registerReceiver() {

        audioDownloadProgressReciver = new AudioDownloadProgressReceiver(this);
        IntentFilter filter = new IntentFilter(DOWNLOAD_AUDIO_PROGRESS);
        registerReceiver(audioDownloadProgressReciver, filter);


        videoDownloadProgressReciver = new VideoDownloadProgressReciver(this);
        IntentFilter filter2 = new IntentFilter(DOWNLOAD_VIDEO_PROGRESS);
        registerReceiver(videoDownloadProgressReciver, filter2);

        mNewChatReciever = new NewMessageReceiver(this);
        IntentFilter filter3 = new IntentFilter(NEW_CHAT_RECIVER);
        registerReceiver(mNewChatReciever, filter3);


    }


    private void getMasechtotListFromLocalStorage() {

        Gson gson = new Gson();
        mMasechtotList = gson.fromJson(UserManager.getMasechtotList(this), MasechetList.class);


    }


    private void initBubbleBudget() {

        DataBaseManager dataBaseManager = new DataBaseManager();
        List<ChatObject> chatObjectList = dataBaseManager.getAllChats(this, CHAT_TABLE);

        int unreadMessages = 0;

        for (int i = 0; i < chatObjectList.size(); i++) {

            if (chatObjectList.get(i).getUnreadMessages() > 0) {

                unreadMessages++;
            }
        }

        if (unreadMessages > 0) {

            mBubbleIconBudget.setVisibility(View.VISIBLE);
            mBubbleIconBudget.setText(String.valueOf(unreadMessages));
        } else {

            mBubbleIconBudget.setVisibility(View.GONE);
        }
    }


    /**
     * set the tool bar in the main page
     */
    private void setMainToolBar() {

        setSupportActionBar(myToolbar);
        connectTheToolBarToTheDrawerLayout();
        myToolbar.setNavigationIcon(R.drawable.ic_menu_icon);

    }


    private void getGemaraDafYomiDetailes() {

        Gson gson = new Gson();
        final TodayDafYomiDetailes dafYomi = gson.fromJson(UserManager.getTodayDafYomi(this), TodayDafYomiDetailes.class);

        int masechetId = 0;
        int sederOrder = 0;
        int masechetOrder = 0;

        for (int i = 0; i < mMasechtotList.getSeder().size(); i++) {

            for (int j = 0; j < mMasechtotList.getSeder().get(i).getMasechet().size(); j++) {

                if (dafYomi.getMasechetName().toLowerCase().equals(mMasechtotList.getSeder().get(i).getMasechet().get(j).getName().toLowerCase())) {

                    masechetId = mMasechtotList.getSeder().get(i).getMasechet().get(j).getId();
                    sederOrder = mMasechtotList.getSeder().get(i).getOrder();
                    masechetOrder = mMasechtotList.getSeder().get(i).getMasechet().get(j).getOrder();
                    break;
                }
            }

        }


        final int finalSederOrder = sederOrder;
        final int finalMasechetOrder = masechetOrder;
        RequestManager.getGemaraDafYomi(UserManager.getToken(this), String.valueOf(masechetId),
                dafYomi.getMasechetPage()).subscribe(new Observer<Result<PagesItem>>() {
            @Override
            public void onSubscribe(Disposable d) {

            }

            @Override
            public void onNext(Result<PagesItem> gemaraPagesResult) {

                mDafYomiDetailes = gemaraPagesResult.getData();
                mDafYomiDetailes.setMasechetName(dafYomi.getMasechetName());
                mDafYomiDetailes.setSederOrder(finalSederOrder);
                mDafYomiDetailes.setMasechetOrder(finalMasechetOrder);
                startWallFragment();
            }

            @Override
            public void onError(Throwable e) {

                if (!isNetworkAvailable()) {
                    mDafYomiDetailes = new PagesItem();
                    mDafYomiDetailes.setVideo("");
                    mDafYomiDetailes.setAudio("");
                }

                startWallFragment();

            }

            @Override
            public void onComplete() {

            }
        });


    }


    /**
     * connect the toolBar to the menu, (and the hamburger to open the menu)
     */
    private void connectTheToolBarToTheDrawerLayout() {
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, mDrawerLayout, myToolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        mDrawerLayout.addDrawerListener(toggle);
        toggle.syncState();
    }


    /**
     * control the navigation buttons
     *
     * @param menuItem
     * @return
     */
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {

        switch (menuItem.getItemId()) {

            case R.id.action_main:

                startWallFragment();
                break;

            case R.id.action_mishna:

                startMishnaFragment();
                break;


            case R.id.action_gemara:

                startGemaraFragment();
                break;


            case R.id.action_downloads:

                startDownloadFragment();
                break;

            case R.id.action_donations:

                if (isNetworkAvailable()) {

                    startDonationFragment();
                } else {

                    NoInternetDialog noInternetDialog = new NoInternetDialog();
                    noInternetDialog.showDialog(this);
                    return false; // to avoid bottomNavigationView to select donation item (for white color)
                }

                break;

            case R.id.TNI_log_out:

                LogOutDialog logOutDialog = new LogOutDialog();
                logOutDialog.showDialog(this, this);
                break;

            case R.id.TNI_profile:

                if (isNetworkAvailable()) {

                    moveToProfileActivity();
                }else {

                    NoInternetDialog noInternetDialog = new NoInternetDialog();
                    noInternetDialog.showDialog(this);

                }
                break;


            case R.id.TNI_mishna:

                startMishnaFragment();
                closeSideNavigationView();
                updateNavigationBarState(R.id.action_mishna);
                break;

            case R.id.TNI_gemara:
                startGemaraFragment();
                closeSideNavigationView();
                updateNavigationBarState(R.id.action_gemara);
                break;

            case R.id.TNI_about:

                startAboutActivity();
                break;

            case R.id.TNI_message_center:

                Intent intent = new Intent(MainActivity.this, ChatActivity.class);
                startActivityForResult(intent, BACK_FROM_CHAT);

                break;

            case R.id.TNI_donation_center:

                if (isNetworkAvailable()) {

                    startDonationFragment();
                    closeSideNavigationView();
                    updateNavigationBarState(R.id.action_donations);

                } else {

                    NoInternetDialog noInternetDialog = new NoInternetDialog();
                    noInternetDialog.showDialog(this);
                }

                break;

            case R.id.TNI_donate:

                if (isNetworkAvailable()){

                    Intent donationActivityIntent = new Intent(this, DonationActivity.class);
                    startActivityForResult(donationActivityIntent, BACK_FROM_DONATION_NO_DONATED);

                    closeSideNavigationView();
                }else {

                    NoInternetDialog noInternetDialog = new NoInternetDialog();
                    noInternetDialog.showDialog(this);
                }


                break;


        }

        return true;
    }


    private void startAboutActivity() {

        Intent intent = new Intent(this, AboutActivity2.class);
        startActivity(intent);


    }


    private void startDonationFragment() {


        Gson gson = new Gson();
        User userFromJson = gson.fromJson(UserManager.getUser(this), User.class);


        if (userFromJson.getLessonDonatedObject().isDonated() || UserManager.getInPendingMode(this)) {

            if (mNewDonationFragment == null) {
                mNewDonationFragment = DonationFragmentNew.newInstance("");
            }

            mFragmentHelper.replaceFragment(R.id.MA_content_frame_FL, mNewDonationFragment, DonationFragmentNew.TAG, null);

        } else {

            Intent intent = new Intent(this, DonationActivity.class);
            startActivityForResult(intent, BACK_FROM_DONATION_NO_DONATED);

        }


    }


    private void startSocketForDonation() {

        Thread thread = new Thread(new Runnable() {

            @Override
            public void run() {

                try {

                    mWebSocketClient = new WebSocketClient(new URI(WEB_SOCKET_URL), new Draft_6455()) {
                        @Override
                        public void onOpen(ServerHandshake handshakedata) {

                        }

                        @Override
                        public void onMessage(String message) {

                            Gson gson = new Gson();
                            final SocketDonateMessage socketDonateMessage = gson.fromJson(message, SocketDonateMessage.class);

                            if (mNewDonationFragment != null) {

                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {

                                        if (mWebSocketClient.isOpen()){

                                            mNewDonationFragment.updateHoursGlobalFromSocket(socketDonateMessage);
                                        }
                                    }
                                });

                            }

                        }

                        @Override
                        public void onClose(int code, String reason, boolean remote) {

                        }

                        @Override
                        public void onError(Exception ex) {

                        }
                    };

                    mWebSocketClient.connect();


                } catch (URISyntaxException e) {
                    e.printStackTrace();
                }

            }
        });

        thread.start();


    }


    /**
     * start download fragment
     */
    private void startDownloadFragment() {

        if (mDownloadsFragment == null) {
            mDownloadsFragment = DownloadsFragment.newInstance();
        }

        mFragmentHelper.replaceFragment(R.id.MA_content_frame_FL, mDownloadsFragment, DownloadsFragment.TAG, null);


    }


    /**
     * start mishna fragment
     */
    private void startMishnaFragment() {

        if (mMishnaMainFragment == null) {

            Gson gson = new Gson();
            MasechetList masechetList = gson.fromJson(gson.toJson(mMasechtotList), MasechetList.class);

            mMishnaMainFragment = MishnaMainFragment.newInstance(masechetList);

        }

        mFragmentHelper.replaceFragment(R.id.MA_content_frame_FL, mMishnaMainFragment, MishnaMainFragment.TAG, null);


    }


    /**
     * start gemara fragment
     */
    private void startGemaraFragment() {

        if (mGemaraMainFragment == null) {

            Gson gson = new Gson();
            MasechetList masechetList = gson.fromJson(gson.toJson(mMasechtotList), MasechetList.class);

            mGemaraMainFragment = GemaraMainFragment.newInstance(masechetList);

        }

        mFragmentHelper.replaceFragment(R.id.MA_content_frame_FL, mGemaraMainFragment, GemaraMainFragment.TAG, null);


    }


    /**
     * start wall fragment
     */
    private void startWallFragment() {

        if (mWallFragment == null) {
            mWallFragment = WallFragment.newInstance(mDafYomiDetailes);
        }


        mFragmentHelper.replaceFragment(R.id.MA_content_frame_FL, mWallFragment, WallFragment.TAG, null);

        checkIfCameFromLinkMessage();

    }


    /**
     * move to profile activity
     */
    private void moveToProfileActivity() {

        Intent profileActivityIntent = new Intent(this, ProfileActivity.class);
        startActivityForResult(profileActivityIntent, START_PROFILE);

    }


    /**
     * set selected and unselected icons if needed
     */
    private void updateNavigationBarState(int itemId) {

        Menu menu = mBottomNavigationView.getMenu();

        for (int i = 0; i < menu.size(); i++) {
            MenuItem item = menu.getItem(i);
            if (item.getItemId() == itemId) {
                item.setChecked(true);
                break;
            }
        }
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch (requestCode) {

            case START_PROFILE:

                if (resultCode == RESULT_OK) {

                    if (data != null) {

                        String resultsFromProfile = data.getStringExtra(RESULTS_FOR_PROFILE);

                        if (Objects.requireNonNull(resultsFromProfile).equals(LOG_OUT)) {

                            Intent signInIntent = new Intent(this, SignInActivity.class);
                            startActivity(signInIntent);
                            finish();

                        } else if (resultsFromProfile.equals(GO_TO_DONATION)) {

                            if (isNetworkAvailable()) {

                                startDonationFragment();
                                closeSideNavigationView();
                                updateNavigationBarState(R.id.action_donations);
                            } else {

                                NoInternetDialog noInternetDialog = new NoInternetDialog();
                                noInternetDialog.showDialog(this);
                            }

                        }

                    }
                }

                break;

            case BACK_FROM_VIDEO_ACTIVITY:

                if (data != null) {

                    long currentPosition = data.getLongExtra(CURRENT_PROGRESS, 0);
                    int currentPageId = data.getIntExtra(CURRENT_PAGE_ID, 0);


                    if (mGemaraMediaFragment != null && mFragmentHelper.isCurrent(GemaraMediaFragment.TAG)) {

                        mGemaraMediaFragment.notifyData(currentPosition, currentPageId);

                    } else if (mWallFragment != null && mFragmentHelper.isCurrent(WallFragment.TAG)) {

                        mWallFragment.notifyData(currentPosition, currentPageId);

                    } else if (mMishnaMediaFragment != null && mFragmentHelper.isCurrent(MishnaMediaFragment.TAG)) {

                        mMishnaMediaFragment.notifyData(currentPosition, currentPageId);

                    } else if (mDownloadsFragment != null && mFragmentHelper.isCurrent(DownloadsFragment.TAG)) {

                        mDownloadsFragment.notifyData(currentPosition, currentPageId);


                    }

                }
                break;

            case BACK_FROM_CHAT:

                initBubbleBudget();

                if (mWallFragment != null && mFragmentHelper.isCurrent(WallFragment.TAG)) {

                    mWallFragment.refreshRecentPages();

                }

                break;


//            case BACK_FROM_SLIDE_DONATION_ACTIVITY:
//
//                updateNavigationBarState(R.id.action_main);
//
//                break;


            case RESULT_FROM_DONATION:

                if (resultCode == RESULT_OK) {

                    if (data != null) {

                        Payment payment = (Payment) Objects.requireNonNull(data.getExtras()).getSerializable(PAYMENT);
                        DonationData donationData = (DonationData) Objects.requireNonNull(data.getExtras()).getSerializable(DONATION_DATA);

                        if (mFragmentHelper.isCurrent(DonationFragmentNew.TAG)) {

                            updatePaymentFragment(payment, donationData);

                        } else {

                            if (mNewDonationFragment == null) {
                                mNewDonationFragment = DonationFragmentNew.newInstance(payment.getStatus());
                            }

                            mFragmentHelper.replaceFragment(R.id.MA_content_frame_FL, mNewDonationFragment, DonationFragmentNew.TAG, null);

                        }
                    }

                } else if (resultCode == RESULT_CANCELED) {

                    updateNavigationBarState(R.id.action_donations);

                    if (mNewDonationFragment != null) {

                        startSocketForDonation();
                    }

                }

                break;


            case BACK_FROM_DONATION_NO_DONATED:

                if (resultCode == RESULT_OK) {

                    if (data != null) {

                        Payment payment = (Payment) Objects.requireNonNull(data.getExtras()).getSerializable(PAYMENT);
                        DonationData donationData = (DonationData) Objects.requireNonNull(data.getExtras()).getSerializable(DONATION_DATA);

//                        if (mNewDonationFragment == null) {
                        mNewDonationFragment = DonationFragmentNew.newInstance(payment.getStatus());
//                        }

                        mFragmentHelper.replaceFragment(R.id.MA_content_frame_FL, mNewDonationFragment, DonationFragmentNew.TAG, null);

                        updateNavigationBarState(R.id.action_donations);


                    } else {

                        updateNavigationBarState(R.id.action_main);
                    }

                } else if (resultCode == RESULT_CANCELED) {

                    if (mFragmentHelper.isCurrent(WallFragment.TAG)){

                        updateNavigationBarState(R.id.action_main);

                    }


                }

                break;

        }

    }

    private void updatePaymentFragment(Payment payment, DonationData donationData) {

        if (mFragmentHelper.isCurrent(DonationFragmentNew.TAG)) {

            mNewDonationFragment.updatePayment();
        }


    }


    /**
     * close navigation view if needed
     */
    private void closeSideNavigationView() {

        if (mDrawerLayout.isDrawerOpen(GravityCompat.START)) {
            mDrawerLayout.closeDrawer(GravityCompat.START, false);
        }

    }


    /**
     * on log out button clicked, delete the token and the user and move to sign in screen
     */
    @Override
    public void OnLogOutClicked() {

        UserManager.setToken(null, this);
        UserManager.setUser(null, this);

        Intent signInIntent = new Intent(this, SignInActivity.class);
        startActivity(signInIntent);
        finish();

    }


    /**
     * on masechet clicked move to mishna chapter fragment
     *
     * @param masechtotItem MasechetItem
     * @param sederOrder    int
     */
    @Override
    public void onMasechetClicked(MasechetItem masechtotItem, int sederOrder) {

        if (masechtotItem.getChapters().size() == 0) {

            NoPagesDialog noPagesDialog = new NoPagesDialog();
            noPagesDialog.showDialog(this);

        } else {

            mMishnaChapterFragment = MishnaChapterFragment.newInstance(masechtotItem, sederOrder);

            mFragmentHelper.replaceFragment(R.id.MA_content_frame_FL, mMishnaChapterFragment, MishnaChapterFragment.TAG, MishnaChapterFragment.TAG);

        }

    }


    /**
     * on mishna chapter clicked move to mishna download fragment
     *
     * @param chaptersItem ChaptersItem
     * @param masechetItem MasechetItem
     * @param sederOrder   int
     */
    @Override
    public void onMishnaChapterClicked(final ChaptersItem chaptersItem, final MasechetItem masechetItem, final int sederOrder) {

        if (isNetworkAvailable()) {

            RequestManager.getMishna(UserManager.getToken(this), String.valueOf(masechetItem.getId()), String.valueOf(chaptersItem.getChapter()))
                    .subscribe(new Observer<Result<MishnaMishnaiot>>() {
                        @Override
                        public void onSubscribe(Disposable d) {

                        }

                        @Override
                        public void onNext(Result<MishnaMishnaiot> mishnaMishnaiotResult) {

                            if (mishnaMishnaiotResult.getData().getMishnayot().size() == 0) {

                                NoPagesDialog noPagesDialog = new NoPagesDialog();
                                noPagesDialog.showDialog(MainActivity.this);


                            } else {


                                mMishnaMediaFragment = MishnaMediaFragment.newInstance(mishnaMishnaiotResult.getData(), masechetItem, sederOrder, chaptersItem.getChapter());

                                mFragmentHelper.replaceFragment(R.id.MA_content_frame_FL, mMishnaMediaFragment, MishnaMediaFragment.TAG, MishnaMediaFragment.TAG);


                            }
                        }

                        @Override
                        public void onError(Throwable e) {

                        }

                        @Override
                        public void onComplete() {

                        }
                    });

        } else {

            getAllTheMishnaFIlesFromLocalStorage(masechetItem, sederOrder, chaptersItem);


        }


    }


    /**
     * on gemara masechet clicked move to gemara download fragment
     *
     * @param masechtotItem MasechetItem
     * @param sederOrder    int
     */
    @Override
    public void onMasechetGemaraClicked(final MasechetItem masechtotItem, final int sederOrder) {

        if (isNetworkAvailable()) {

            RequestManager.getGemara(UserManager.getToken(this), String.valueOf(masechtotItem.getId())).subscribe(new Observer<Result<GemaraPages>>() {
                @Override
                public void onSubscribe(Disposable d) {

                }

                @Override
                public void onNext(Result<GemaraPages> gemaraListResult) {

                    if (gemaraListResult.getData().getPages().size() == 0) {

                        NoPagesDialog noPagesDialog = new NoPagesDialog();
                        noPagesDialog.showDialog(MainActivity.this);
                        mGemaraMainFragment.clearProgressBar();

                    } else {


                        mGemaraMediaFragment = GemaraMediaFragment.newInstance(gemaraListResult.getData(), masechtotItem, sederOrder);

                        mFragmentHelper.replaceFragment(R.id.MA_content_frame_FL, mGemaraMediaFragment, GemaraMediaFragment.TAG, GemaraMediaFragment.TAG);


                    }

                }

                @Override
                public void onError(Throwable e) {

                }

                @Override
                public void onComplete() {

                }
            });

        } else {

            getAllTheFIlesFromLocalStorage(masechtotItem, sederOrder);


        }


    }


    @Override
    public void hideToolBar() {
        myToolbar.setVisibility(View.GONE);

    }


    @Override
    public void openDonationActivity() {

        mWebSocketClient.close();

        Intent intent = new Intent(this, DonationActivity.class);
        startActivityForResult(intent, RESULT_FROM_DONATION);


    }


    @Override
    public void openDetailsDonationActivity(UserDonationStatus userDonationStatus) {

        Intent intent = new Intent(this, DonationActivity.class);
        intent.putExtra(START_SUB_DETAILS, true);
        intent.putExtra(USER_DONATION_STATUS, userDonationStatus);
        startActivity(intent);


    }


    @Override
    public void startSocketForDonationHours() {

        startSocketForDonation();


    }

    @Override
    public void closeSocket() {

        if (mWebSocketClient != null) {

            mWebSocketClient.close();
        }

    }


    @Override
    public void onAudioMishnaClicked(MishnayotItem mishnayotItem) {

        saveRecentMishnaPlayd(mishnayotItem);
        Intent intent = new Intent(this, AudioActivity.class);
        intent.putExtra(TALMUD_TYPE, MISHNA_TABLE);
        PagesItem pagesItem = genaretMishnaItemToPageItem(mishnayotItem);
        intent.putExtra(PAGE_ITEM, pagesItem);
        startActivityForResult(intent, BACK_FROM_VIDEO_ACTIVITY);
    }


    @Override
    public void itemsInMishnaDowloads(int mishnaListItems) {

        if (mDownloadsFragment != null) {

            mDownloadsFragment.itemsInMishnaDowloads(mishnaListItems);
        }
    }


    @Override
    public void onVideoMishnaClicked(MishnayotItem mishnayotItem) {

        saveRecentMishnaPlayd(mishnayotItem);
        Intent intent = new Intent(this, VideoActivity.class);
        intent.putExtra(TALMUD_TYPE, MISHNA_TABLE);
        PagesItem pagesItem = genaretMishnaItemToPageItem(mishnayotItem);
        intent.putExtra(PAGE_ITEM, pagesItem);
        startActivityForResult(intent, BACK_FROM_VIDEO_ACTIVITY);

    }


    @Override
    public void onMishnaVideoDownloadClicked(final MishnayotItem mishnayotItem, final int position) {


        if (checkPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE, MY_PERMISSIONS_REQUEST_WRITE_EXTERNAL_STORAGE_VIDEO_MISHNA)) {
            final PagesItem pagesItem = genaretMishnaItemToPageItem(mishnayotItem);

            String pagePathName = "";

            if (!pagesItem.getPage().equals("")) {

                S3Helper s3Helper = new S3Helper(this);
                s3Helper.downloadFilePagesWithoutListener(pagesItem.getPage(), PAGES_FILE, this);
                pagePathName = Config.getPathName(this) + PAGES_FILE + getPageFIleNameFromFileKEy(pagesItem.getPage());
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

                        URL url = new URL(pagesItem.getVideo());
                        HttpURLConnection con = (HttpURLConnection) url.openConnection();
                        int lenghtOfFile = con.getContentLength();

                        con.setRequestMethod("GET");
                        con.connect();

                        final String pathName = Config.getPathName(MainActivity.this) + VIDEO_FILE;

                        final String fileName = getFIleNameFromFileKEy(pagesItem.getPage());

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
                                        mMishnaMediaFragment.onVideoProgressChange(finalStatus, position, mishnayotItem);
                                    }
                                });

                            }
                            status = total * 100L / lenghtOfFile;

                        }

                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                // change UI elements here
                                mMishnaMediaFragment.stopVideoProgress(position, mishnayotItem);
                                sendAnalyticsData(AnalyticsData.MISHNA, AnalyticsData.VIDEO, pagesItem.getId());
                                DataBaseManager dataBaseManager = new DataBaseManager();
                                dataBaseManager.onVideoMishnaDownloadFinished(MainActivity.this, generateVideoMishnaObject(pagesItem), pathName + fileName, finalPagePathName);
                            }
                        });

                        fos.close();
                        is.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {

                                mMishnaMediaFragment.stopVideoProgress(position, mishnayotItem);
                                Toast.makeText(MainActivity.this, getResources().getString(R.string.error), Toast.LENGTH_LONG).show();
                            }
                        });

                    }


                }
            });
            thread.start();

        } else {

            mishnaItemFromPremission = mishnayotItem;
            positionFromPremission = position;

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
        pagesItem.setPosition(mishnayotItem.getPosition());

        return pagesItem;


    }


    private void saveRecentMishnaPlayd(MishnayotItem mishnayotItem) {

        mishnayotItem.setTimeLine(0);

        Gson gson = new Gson();

        Type type = new TypeToken<List<MishnayotItem>>() {
        }.getType();
        List<MishnayotItem> recentMishnayotItemList = gson.fromJson(UserManager.getRecentMishnaPlayed(this), type);

        if (recentMishnayotItemList == null) {
            recentMishnayotItemList = new ArrayList<>();
        } else {

            for (int i = 0; i < recentMishnayotItemList.size(); i++) {

                if (recentMishnayotItemList.get(i).getId() == mishnayotItem.getId()) {
                    recentMishnayotItemList.remove(recentMishnayotItemList.get(i));
                }

            }

        }

        recentMishnayotItemList.add(mishnayotItem);

        if (recentMishnayotItemList.size() > 4) {
            recentMishnayotItemList = recentMishnayotItemList.subList(recentMishnayotItemList.size() - 4, recentMishnayotItemList.size());
        }


        String jsonMishnaPage = gson.toJson(recentMishnayotItemList);

        UserManager.setRecentMishnaPlayed(jsonMishnaPage, this);
    }


    @Override
    public void onVideoClicked(PagesItem pagesItem) {

        if (pagesItem != null) {

            saveRecentGemaraPlayd(pagesItem);
        }

        Intent intent = new Intent(this, VideoActivity.class);
        intent.putExtra(TALMUD_TYPE, GEMARA_TABLE);
        intent.putExtra(PAGE_ITEM, pagesItem);
        startActivityForResult(intent, BACK_FROM_VIDEO_ACTIVITY);


    }


    @Override
    public void onVideoDownloadClicked(final PagesItem pagesItem, final int position) {


        if (downloadCounter <= 2) {

            downloadVideo(pagesItem, position);
            downloadCounter += 1;

        } else {

            DownloadQueue downloadQueue = new DownloadQueue();
            downloadQueue.setPagesItem(pagesItem);
            downloadQueue.setPosition(position);
            downloadQueue.setType(VIDEO);

            downloadQueueList.add(downloadQueue);

        }


    }


    private void downloadVideo(final PagesItem pagesItem, final int position) {

        if (checkPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE, MY_PERMISSIONS_REQUEST_WRITE_EXTERNAL_STORAGE_VIDEO)) {


            String pagePathName = "";

            if (!pagesItem.getPage().equals("")) {

                S3Helper s3Helper = new S3Helper(this);
                s3Helper.downloadFilePagesWithoutListener(pagesItem.getPage(), PAGES_FILE, this);
                pagePathName = Config.getPathName(this) + PAGES_FILE + getPageFIleNameFromFileKEy(pagesItem.getPage());
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

                        URL url = new URL(pagesItem.getVideo());
                        HttpURLConnection con = (HttpURLConnection) url.openConnection();
                        int lenghtOfFile = con.getContentLength();

                        con.setRequestMethod("GET");
                        con.connect();

                        final String pathName = Config.getPathName(MainActivity.this) + VIDEO_FILE;

                        final String fileName = getFIleNameFromFileKEy(pagesItem.getPage());

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
                                        if (finalStatus != 0) {
                                            mGemaraMediaFragment.onVideoProgressChange(finalStatus, position, pagesItem);
                                        }
                                    }
                                });

                            }
                            status = total * 100L / lenghtOfFile;

                        }

                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                // change UI elements here
                                mGemaraMediaFragment.stopVideoProgress(position, pagesItem);
                                sendAnalyticsData(AnalyticsData.GEMARA, AnalyticsData.VIDEO, pagesItem.getId());
                                DataBaseManager dataBaseManager = new DataBaseManager();
                                dataBaseManager.onVideoGemaraDownloadFinished(MainActivity.this, pagesItem, pathName + fileName, finalPagePathName);

                                downloadCounter -= 1;

                                if (downloadQueueList.size() > 0) {

                                    if (downloadQueueList.get(0).getType().equals(AUDIO)) {
                                        downloadAudio(downloadQueueList.get(0).getPagesItem(), downloadQueueList.get(0).getPosition());
                                    } else {

                                        downloadVideo(downloadQueueList.get(0).getPagesItem(), downloadQueueList.get(0).getPosition());

                                    }

                                    downloadQueueList.remove(0);

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

        } else {

            pageItemFromPremission = pagesItem;
            positionFromPremission = position;


        }


    }


    private String getPageFIleNameFromFileKEy(String page) {

        return page.substring(page.lastIndexOf("/") + 1);
    }


    private String getFIleNameFromFileKEy(String fileKey) {

        String fileName = fileKey.substring(fileKey.lastIndexOf("/") + 1);
        return fileName.substring(0, fileName.indexOf(".pdf")) + ".mp4";

    }


    @Override
    protected void onResume() {
        super.onResume();

        if (UserManager.getUpdateMainActivity(this)) {

            initBubbleBudget();

            if (mWallFragment != null && mFragmentHelper.isCurrent(WallFragment.TAG)) {

                mWallFragment.refreshRecentPages();

            }

            UserManager.setUpdateMainActivity(this, false);
        }

    }


    /**
     * get all files from local storage
     *
     * @param masechtotItem MasechetItem
     * @param sederOrder    int
     */
    private void getAllTheFIlesFromLocalStorage(MasechetItem masechtotItem, int sederOrder) {

        DBHandler dbHandler = new DBHandler(this);
        List<PagesItem> pagesItemList = dbHandler.getMasechetDownloads(GEMARA_TABLE, masechtotItem.getName());

        if (pagesItemList.size() == 0) {

            NoInternetDialog noInternetDialog = new NoInternetDialog();
            noInternetDialog.showDialog(MainActivity.this);
            mGemaraMainFragment.clearProgressBar();

        } else {


            GemaraPages gemaraPages = new GemaraPages();
            gemaraPages.setPages(pagesItemList);

            mGemaraMediaFragment = GemaraMediaFragment.newInstance(gemaraPages, masechtotItem, sederOrder);

            mFragmentHelper.replaceFragment(R.id.MA_content_frame_FL, mGemaraMediaFragment, GemaraMediaFragment.TAG, GemaraMediaFragment.TAG);

        }


    }


    /**
     * get all files from local storage
     *
     * @param masechtotItem MasechetItem
     * @param sederOrder    int
     * @param chaptersItem
     */
    private void getAllTheMishnaFIlesFromLocalStorage(MasechetItem masechtotItem, int sederOrder, ChaptersItem chaptersItem) {

        DBHandler dbHandler = new DBHandler(this);
        List<MishnayotItem> pagesItemList = dbHandler.getMasechetMishnaDownloads(MISHNA_TABLE, masechtotItem.getName());

        if (pagesItemList.size() == 0) {

            NoPagesDialog noPagesDialog = new NoPagesDialog();
            noPagesDialog.showDialog(MainActivity.this);

        } else {


            MishnaMishnaiot mishnaMishnaiot = new MishnaMishnaiot();
            mishnaMishnaiot.setMishnayot(pagesItemList);


            mMishnaMediaFragment = MishnaMediaFragment.newInstance(mishnaMishnaiot, masechtotItem, sederOrder, chaptersItem.getChapter());

            mFragmentHelper.replaceFragment(R.id.MA_content_frame_FL, mMishnaMediaFragment, MishnaMediaFragment.TAG, MishnaMediaFragment.TAG);

        }


    }


    /**
     * check if network available
     *
     * @return boolean
     */
    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = Objects.requireNonNull(connectivityManager).getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
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

            case MY_PERMISSIONS_REQUEST_WRITE_EXTERNAL_STORAGE:

                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    S3Helper s3Helper = new S3Helper(this);
                    s3Helper.downloadFile(this, mPageItemForOnRequestPermissionsResult.getAudio(), mPageItemForOnRequestPermissionsResult.getPage(), AUDIO_FILE, mdownloadListenerForOnRequestPermissionsResult);

                    if (!mPageItemForOnRequestPermissionsResult.getPage().equals("")) {

                        s3Helper.downloadFilePagesWithoutListener(mPageItemForOnRequestPermissionsResult.getPage(), PAGES_FILE, this);
                    }

                    for (int i = 0; i < mPageItemForOnRequestPermissionsResult.getGallery().size(); i++) {

                        s3Helper.downloadFilePagesWithoutListener(mPageItemForOnRequestPermissionsResult.getGallery().get(i).getImage(), IMAGES_FILE, this);
                    }

                }

                break;


            case MY_MISHNA_PERMISSIONS_REQUEST_WRITE_EXTERNAL_STORAGE:

                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    S3Helper s3Helper = new S3Helper(this);
                    s3Helper.downloadFile(this, mMishnayotItemForOnRequestPermissionsResult.getAudio(), mMishnayotItemForOnRequestPermissionsResult.getPage(), AUDIO_FILE, mdownloadListenerForOnRequestPermissionsResult);


                    for (int i = 0; i < mMishnayotItemForOnRequestPermissionsResult.getGallery().size(); i++) {

                        s3Helper.downloadFilePagesWithoutListener(mMishnayotItemForOnRequestPermissionsResult.getGallery().get(i).getImage(), IMAGES_FILE, this);
                    }

                }

                break;


            case MY_PERMISSIONS_REQUEST_WRITE_EXTERNAL_STORAGE_VIDEO:

                downloadVideo(pageItemFromPremission, positionFromPremission);
                break;


            case MY_PERMISSIONS_REQUEST_WRITE_EXTERNAL_STORAGE_VIDEO_MISHNA:

                onMishnaVideoDownloadClicked(mishnaItemFromPremission, positionFromPremission);

                break;
        }

    }


    @Override
    public void onViewAllGemraClicked() {
        startGemaraFragment();
        updateNavigationBarState(R.id.action_gemara);
    }


    @Override
    public void onViewAllMishnaClicked() {

        startMishnaFragment();
        updateNavigationBarState(R.id.action_mishna);

    }


    @Override
    public void onAudioDownloadClicked(final PagesItem pagesItem, final int position) {

        if (downloadCounter <= 2) {

            downloadAudio(pagesItem, position);
            downloadCounter += 1;

        } else {

            DownloadQueue downloadQueue = new DownloadQueue();
            downloadQueue.setPagesItem(pagesItem);
            downloadQueue.setPosition(position);
            downloadQueue.setType(AUDIO);

            downloadQueueList.add(downloadQueue);

        }


    }


    private void downloadAudio(final PagesItem pagesItem, final int position) {

        if (checkPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE, MY_PERMISSIONS_REQUEST_WRITE_EXTERNAL_STORAGE)) {

            S3Helper s3Helper = new S3Helper(this);
            if (!pagesItem.getPage().equals("")) {

                s3Helper.downloadFilePagesWithoutListener(pagesItem.getPage(), PAGES_FILE, this);
            }


            s3Helper.downloadFile(this, pagesItem.getAudio(), pagesItem.getPage(), AUDIO_FILE, new DownloadListener() {
                @Override
                public void onDownloadStart(int id, File fileLocation) {

                }

                @Override
                public void onProgressChanged(int percentDone) {

                    if (percentDone != 0) {

                        mGemaraMediaFragment.onAudioProgressChange(percentDone, position, pagesItem);
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

                    mGemaraMediaFragment.stopAudioProgress(position, pagesItem);
                    sendAnalyticsData(AnalyticsData.GEMARA, AUDIO, pagesItem.getId());

                    DataBaseManager dataBaseManager = new DataBaseManager();
                    dataBaseManager.onGeamraAudioDownloadFinished(MainActivity.this, pagesItem, pathName, pagePathName);

                    downloadCounter -= 1;

                    if (downloadQueueList.size() > 0) {

                        if (downloadQueueList.get(0).getType().equals(AUDIO)) {
                            downloadAudio(downloadQueueList.get(0).getPagesItem(), downloadQueueList.get(0).getPosition());
                        } else {

                            downloadVideo(downloadQueueList.get(0).getPagesItem(), downloadQueueList.get(0).getPosition());

                        }

                        downloadQueueList.remove(0);

                    }


                }

                @Override
                public void onDownloadError() {
                    mGemaraMediaFragment.cancelAudioProgress(position, pagesItem);

                }
            });


            for (int i = 0; i < pagesItem.getGallery().size(); i++) {

                s3Helper.downloadFilePagesWithoutListener(pagesItem.getGallery().get(i).getImage(), IMAGES_FILE, this);
            }

        } else {

            mPageItemForOnRequestPermissionsResult = pagesItem;
            mdownloadListenerForOnRequestPermissionsResult = new DownloadListener() {
                @Override
                public void onDownloadStart(int id, File fileLocation) {

                }

                @Override
                public void onProgressChanged(int percentDone) {
                    mGemaraMediaFragment.onAudioProgressChange(percentDone, position, pagesItem);


                }

                @Override
                public void onDownloadWaiting(int id, TransferState state, File fileLocation) {

                }

                @Override
                public void onDownloadReceivedStop(int id, TransferState state, File fileLocation) {

                }

                @Override
                public void onDownloadFinish(int id, String link, String pathName, String pagePathName) {

                    mGemaraMediaFragment.stopAudioProgress(position, pagesItem);
                    sendAnalyticsData(AnalyticsData.GEMARA, AUDIO, pagesItem.getId());

                    DataBaseManager dataBaseManager = new DataBaseManager();
                    dataBaseManager.onGeamraAudioDownloadFinished(MainActivity.this, pagesItem, pathName, pagePathName);

                    downloadCounter -= 1;

                    if (downloadQueueList.size() > 0) {

                        if (downloadQueueList.get(0).getType().equals(AUDIO)) {
                            downloadAudio(downloadQueueList.get(0).getPagesItem(), downloadQueueList.get(0).getPosition());
                        } else {

                            downloadVideo(downloadQueueList.get(0).getPagesItem(), downloadQueueList.get(0).getPosition());

                        }

                        downloadQueueList.remove(0);

                    }


                }

                @Override
                public void onDownloadError() {

                    mGemaraMediaFragment.cancelAudioProgress(position, pagesItem);


                }
            };

        }


    }


    private void sendAnalyticsData(String category, String media_type, int pageId) {

        AnalyticsData analyticsData = new AnalyticsData();
        analyticsData.setEvent(DOWNLOAD);
        analyticsData.setCategory(category);
        analyticsData.setMediaType(media_type);
        analyticsData.setPageId(String.valueOf(pageId));
        analyticsData.setDuration(0);
        analyticsData.setOnline(0);


        RequestManager.sendAnalytics(UserManager.getToken(this), analyticsData).subscribe(new Observer<Result>() {
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
    public void onAudioClicked(PagesItem pagesItem) {

        if (pagesItem != null) {

            saveRecentGemaraPlayd(pagesItem);
        }


        Intent intent = new Intent(this, AudioActivity.class);
        intent.putExtra(TALMUD_TYPE, GEMARA_TABLE);
        intent.putExtra(PAGE_ITEM, pagesItem);
        startActivityForResult(intent, BACK_FROM_VIDEO_ACTIVITY);


    }


    @Override
    public void itemsInGemaraDowloads(int ListGemaraItems) {

        if (mDownloadsFragment != null) {

            mDownloadsFragment.itemsInGemaraDownloads(ListGemaraItems);
        }

    }


    private void saveRecentGemaraPlayd(PagesItem pagesItem) {

        pagesItem.setTimeLine(0);

        Gson gson = new Gson();

        Type type = new TypeToken<List<PagesItem>>() {
        }.getType();
        List<PagesItem> recentPagesItemList = gson.fromJson(UserManager.getRecentGemaraPlayed(this), type);

        if (recentPagesItemList == null) {
            recentPagesItemList = new ArrayList<>();
        } else {

            for (int i = 0; i < recentPagesItemList.size(); i++) {

                if (recentPagesItemList.get(i).getId() == pagesItem.getId()) {
                    recentPagesItemList.remove(recentPagesItemList.get(i));
                }

            }

        }


        recentPagesItemList.add(pagesItem);

        if (recentPagesItemList.size() > 4) {
            recentPagesItemList = recentPagesItemList.subList(recentPagesItemList.size() - 4, recentPagesItemList.size());
        }

        String jsonGemaraPage = gson.toJson(recentPagesItemList);

        UserManager.setRecentGemaraPlayed(jsonGemaraPage, this);

    }


    @Override
    public void onMishnaAudioDownloadClicked(final MishnayotItem mishnayotItem, final int position) {

        if (checkPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE, MY_MISHNA_PERMISSIONS_REQUEST_WRITE_EXTERNAL_STORAGE)) {


            S3Helper s3Helper = new S3Helper(this);

            if (!mishnayotItem.getPage().equals("")) {
                s3Helper.downloadFilePagesWithoutListener(mishnayotItem.getPage(), PAGES_FILE, this);
            }

            s3Helper.downloadFile(this, mishnayotItem.getAudio(), mishnayotItem.getPage(), AUDIO_FILE, new DownloadListener() {
                @Override
                public void onDownloadStart(int id, File fileLocation) {

                }

                @Override
                public void onProgressChanged(int percentDone) {

                    if (percentDone != 0) {

                        mMishnaMediaFragment.onAudioProgressChange(percentDone, position, mishnayotItem);
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


                    mMishnaMediaFragment.stopAudioProgress(position, mishnayotItem);
                    sendAnalyticsData(AnalyticsData.MISHNA, AUDIO, mishnayotItem.getId());


                    DataBaseManager dataBaseManager = new DataBaseManager();
                    dataBaseManager.onMishnaAudioDownloadFinished(MainActivity.this, mishnayotItem, pathName, pagePathName);
                }

                @Override
                public void onDownloadError() {

                }
            });


            for (int i = 0; i < mishnayotItem.getGallery().size(); i++) {

                s3Helper.downloadFilePagesWithoutListener(mishnayotItem.getGallery().get(i).getImage(), IMAGES_FILE, this);
            }

        } else {

            mMishnayotItemForOnRequestPermissionsResult = mishnayotItem;
            mdownloadListenerForOnRequestPermissionsResult = new DownloadListener() {
                @Override
                public void onDownloadStart(int id, File fileLocation) {

                }

                @Override
                public void onProgressChanged(int percentDone) {
                    mMishnaMediaFragment.onAudioProgressChange(percentDone, position, mishnayotItem);

                }

                @Override
                public void onDownloadWaiting(int id, TransferState state, File fileLocation) {

                }

                @Override
                public void onDownloadReceivedStop(int id, TransferState state, File fileLocation) {

                }

                @Override
                public void onDownloadFinish(int id, String link, String pathName, String pagePathName) {

                    mMishnaMediaFragment.stopAudioProgress(position, mishnayotItem);
                    sendAnalyticsData(AnalyticsData.MISHNA, AUDIO, mishnayotItem.getId());


                    DataBaseManager dataBaseManager = new DataBaseManager();
                    dataBaseManager.onMishnaAudioDownloadFinished(MainActivity.this, mishnayotItem, pathName, pagePathName);
                }

                @Override
                public void onDownloadError() {

                }
            };

        }


    }


    @Override
    public void showToolBar() {

        myToolbar.setVisibility(View.VISIBLE);

    }


    @Override
    public void onProgressChanged(String audioProgress, PagesItem pagesItem, String talmudType) {

        if (talmudType.equals(GEMARA_TABLE) && mGemaraMediaFragment != null) {
            mGemaraMediaFragment.onAudioProgressChange(Integer.parseInt(audioProgress), pagesItem.getPosition(), pagesItem);
        } else if (talmudType.equals(MISHNA_TABLE) && mMishnaMediaFragment != null) {

            mMishnaMediaFragment.onAudioProgressChange(Integer.parseInt(audioProgress), pagesItem.getPosition(), generateMishnaObject(pagesItem));

        }

    }


    @Override
    public void onVideoProgressChanged(String audioProgress, PagesItem pagesItem, String talmudType) {

        if (talmudType.equals(GEMARA_TABLE) && mGemaraMediaFragment != null) {

            mGemaraMediaFragment.onVideoProgressChange(Long.parseLong(audioProgress), pagesItem.getPosition(), pagesItem);

        } else if (talmudType.equals(MISHNA_TABLE) && mMishnaMediaFragment != null) {

            mMishnaMediaFragment.onVideoProgressChange(Long.parseLong(audioProgress), pagesItem.getPosition(), generateMishnaObject(pagesItem));

        }

    }


    /**
     * generate gemara video object
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


        return mishnayotItem;


    }


    /**
     * generate gemara to mishna
     *
     * @param pagesItem PagesItem
     * @return pagesItem
     */
    private MishnayotItem generateVideoMishnaObject(PagesItem pagesItem) {

        MishnayotItem mishnayotItem = new MishnayotItem();
        mishnayotItem.setSederOrder(pagesItem.getSederOrder());
        mishnayotItem.setMasechetOrder(pagesItem.getMasechetOrder());
        mishnayotItem.setMasechetName(pagesItem.getMasechetName());
        mishnayotItem.setId(pagesItem.getId());
        mishnayotItem.setPosition(pagesItem.getPosition());
        mishnayotItem.setVideo(pagesItem.getVideo());
        mishnayotItem.setAudio(pagesItem.getAudio());
        mishnayotItem.setMishna(pagesItem.getPageNumber());
        mishnayotItem.setDuration(pagesItem.getDuration());
        mishnayotItem.setChapter(pagesItem.getChapter());

        return mishnayotItem;


    }


    @Override
    public void onNewMessageReceived(MessageObject messageObject) {

    }


    @Override
    public void onNewChatReceived(ChatObject chatObject) {

        DataBaseManager dataBaseManager = new DataBaseManager();
        List<ChatObject> chatObjectList = dataBaseManager.getAllChats(this, CHAT_TABLE);

        int unreadMessages = 0;

        for (int i = 0; i < chatObjectList.size(); i++) {

            if (chatObjectList.get(i).getUnreadMessages() > 0) {

                unreadMessages++;
            }
        }

        mBubbleIconBudget.setVisibility(View.VISIBLE);
        mBubbleIconBudget.setText(String.valueOf(unreadMessages));


        if (mWallFragment != null && mFragmentHelper.isCurrent(WallFragment.TAG)) {

            mWallFragment.refreshRecentPages();

        }
    }


}
