package co.il.jabrutouch.ui.main.message_screen;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.media.MediaRecorder;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.amazonaws.mobileconnectors.s3.transferutility.TransferState;
import com.devlomi.record_view.OnBasketAnimationEnd;
import com.devlomi.record_view.OnRecordClickListener;
import com.devlomi.record_view.OnRecordListener;
import com.devlomi.record_view.RecordButton;
import com.devlomi.record_view.RecordView;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Objects;

import co.il.jabrutouch.MainActivity;
import co.il.jabrutouch.NewMessageReceiver;
import co.il.jabrutouch.R;
import co.il.jabrutouch.data_base_manager.DataBaseManager;
import co.il.jabrutouch.server.RequestManager;
import co.il.jabrutouch.ui.main.message_screen.adapters.MessageAdapter;
import co.il.jabrutouch.user_manager.UserManager;
import co.il.model.model.ChatObject;
import co.il.model.model.ErrorResponse;
import co.il.model.model.MessageObject;
import co.il.model.model.MishnayotItem;
import co.il.model.model.PagesItem;
import co.il.model.model.Result;
import co.il.model.model.User;
import co.il.s3.interfaces.DownloadListener;
import co.il.s3.interfaces.UploadListener;
import co.il.s3.utils.Config;
import co.il.s3.utils.S3Helper;
import co.il.sqlcore.DBHandler;
import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;
import retrofit2.HttpException;

import static co.il.jabrutouch.MyFireBaseMessagingService.AUDIO_MESSAGE;
import static co.il.jabrutouch.MyFireBaseMessagingService.UPDATE_MAIN_ACTIVITY;
import static co.il.jabrutouch.NewMessageReceiver.NEW_MESSAGE_RECIVER;
import static co.il.jabrutouch.ui.main.message_screen.ChatActivity.CHAT_OBJECT;
import static co.il.sqlcore.DBKeys.CHAT_TABLE;


public class MessageActivity extends AppCompatActivity implements MessageAdapter.MessageAdapterListener, NewMessageReceiver.NewMessageReceiverListener {

    private static final int MY_PERMISSIONS_RECORD_AUDIO = 1921;
    public static final String AUDIO_RECORD = "audioRecord";
    private static final int MY_PERMISSIONS_WRITE_STORAGE = 2342;
    public static final int TEXT = 1;
    public static final int AUDIO = 2;
    public static final int DATE = 3;
    public static final int UNREAD_MESSAGES = 4;
    public static final int LINK_MESSAGE = 5;
    public static final String S3_SUB_FOLDER = "/users-record";
    private static final String AUDIO_RECORD_FILE = "audioRecord/";
    private static final String TODAY = "TODAY";
    private static final String YESTERDAY = "YESTERDAY";
    public static final String FROM_LINK_MESSAGE = "FROM_LINK_MESSAGE";
    public static final String LINK_TO_TYPE = "LINK_TO_TYPE";
    private RecyclerView mRecyclerView;
    private MessageAdapter mAdapter;
    private EditText mMessageET;
    private List<MessageObject> mMessageList;
    private RecordView recordView;
    private RecordButton recordAndSendButton;
    private MediaRecorder recorder = null;
    private String pathName;
    private ChatObject mMessageMainObject;
    private TextView chatTitle;
    private NewMessageReceiver mNewMEssageReciever;
    private boolean mUpdateMainActivity;
    private boolean recordPermission;
    private boolean storagePermission;
    public static boolean closeChatActivity;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        overridePendingTransition(R.anim.slide_in, R.anim.slide_out); // set animation to screen to start from the side

        setContentView(R.layout.activity_message);


        initViews();
        initListeners();
        updateConfigFileForMAinActivity();
        setRecordView();
        checkItHasAudioRecordPermission();
        scrollIfUnreadMessages();
        updateRecentPagesInWall(mMessageMainObject);
        updateUnreadMessagesToZero(mMessageMainObject);
        setBordcastReceiver();


    }


    @Override
    protected void onResume() {
        super.onResume();

        UserManager.setCurrentChat(this, mMessageMainObject.getChatId());
    }


    @Override
    protected void onPause() {
        super.onPause();

        UserManager.setCurrentChat(this, -1);

    }


    private void checkItHasAudioRecordPermission() {

        if (ContextCompat.checkSelfPermission(MessageActivity.this, Manifest.permission.RECORD_AUDIO) != PackageManager.PERMISSION_GRANTED) {

            recordAndSendButton.setListenForRecord(false);

            recordAndSendButton.setOnRecordClickListener(new OnRecordClickListener() {
                @Override
                public void onClick(View v) {

                    String[] PERMISSIONS = {
                            android.Manifest.permission.RECORD_AUDIO,

                    };


                    ActivityCompat.requestPermissions(MessageActivity.this, PERMISSIONS, MY_PERMISSIONS_RECORD_AUDIO);


                }
            });


        } else {

            recordAndSendButton.setListenForRecord(true);

        }


    }


    @Override
    public void onBackPressed() {

        Intent intent = new Intent(this, ChatActivity.class);
        startActivity(intent);
        finish();
    }


    private void initViews() {

        if (mMessageMainObject == null) {

            mMessageMainObject = (ChatObject) getIntent().getSerializableExtra(CHAT_OBJECT);
        }
        mUpdateMainActivity = getIntent().getBooleanExtra(UPDATE_MAIN_ACTIVITY, false);
        mRecyclerView = findViewById(R.id.MA_message_recycler_view_RV);
        mMessageET = findViewById(R.id.MA_message_ET);
        recordView = findViewById(R.id.record_view);
        recordAndSendButton = findViewById(R.id.record_button);
        chatTitle = findViewById(R.id.MA_title_TV);


        mMessageList = mMessageMainObject.getMessages();

        setTheAdapter();

        if (mUpdateMainActivity) {

            closeChatActivity = true;
        }

    }


    private void setTheAdapter() {

        setDatesForMessagesRecyclerView();

        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mAdapter = new MessageAdapter(this, mMessageList, this);
        mRecyclerView.setAdapter(mAdapter);

        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setStackFromEnd(true);
        mRecyclerView.setLayoutManager(layoutManager);


        chatTitle.setText(mMessageMainObject.getTitle());


    }


    private void setDatesForMessagesRecyclerView() {


        for (int i = 0; i < mMessageList.size(); i++) {

            if (i == 0) {

                if (mMessageList.get(i).getSentAt() != null) {


                    MessageObject messageObject = new MessageObject();
                    messageObject.setMessageType(DATE);
                    messageObject.setIsMine(true);
                    messageObject.setMessage(getFormattedDate(mMessageList.get(i).getSentAt()));

                    mMessageList.add(i, messageObject);
                    i++;
                }
            } else {

                if (mMessageList.get(i).getSentAt() != null && mMessageList.get(i - 1).getSentAt() != null) {

                    if (getFormattedDate(mMessageList.get(i).getSentAt()).equals(getFormattedDate(mMessageList.get(i - 1).getSentAt()))) {


                    } else {

                        MessageObject messageObject = new MessageObject();
                        messageObject.setMessageType(DATE);
                        messageObject.setIsMine(true);
                        messageObject.setMessage(getFormattedDate(mMessageList.get(i).getSentAt()));

                        mMessageList.add(i, messageObject);
                        i++;
                    }

                }
            }


        }


    }


    public String getFormattedDate(Long smsTimeInMilis) {
        Calendar smsTime = Calendar.getInstance();
        smsTime.setTimeInMillis(smsTimeInMilis);

        Calendar now = Calendar.getInstance();

        final String timeFormatString = "HH:mm";
        final String dateTimeFormatString = "MMM dd yyyy";
        final long HOURS = 60 * 60 * 60;
        if (now.get(Calendar.DATE) == smsTime.get(Calendar.DATE)) {
            return TODAY;
        } else if (now.get(Calendar.DATE) - smsTime.get(Calendar.DATE) == 1) {
            return YESTERDAY;
        } else if (now.get(Calendar.YEAR) == smsTime.get(Calendar.YEAR)) {
            return DateFormat.format(dateTimeFormatString, smsTime).toString();
        } else {
            return DateFormat.format("MMM dd yyyy", smsTime).toString();
        }
    }


    private void initListeners() {

        mMessageET.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                if (i2 > 0) {

                    recordAndSendButton.setBackground(ContextCompat.getDrawable(MessageActivity.this, R.drawable.ic_arrow_righte));


                    recordAndSendButton.setListenForRecord(false);

                    //ListenForRecord must be false ,otherwise onClick will not be called
                    recordAndSendButton.setOnRecordClickListener(new OnRecordClickListener() {
                        @Override
                        public void onClick(View v) {


                            MessageObject messagesObject = new MessageObject();
                            messagesObject.setMessage(mMessageET.getText().toString());
                            messagesObject.setTitle("");
                            messagesObject.setIsMine(true);
                            messagesObject.setMessageType(TEXT);
                            messagesObject.setChatId(mMessageMainObject.getChatId());
                            messagesObject.setToUser(mMessageMainObject.getFromUser());

                            Gson gson = new Gson();
                            User userFromJson = gson.fromJson(UserManager.getUser(MessageActivity.this), User.class);

                            messagesObject.setFromUser(userFromJson.getId());
                            messagesObject.setImage(userFromJson.getImage());
                            messagesObject.setSentAt(System.currentTimeMillis());

                            updateLastMessageInDataBase(messagesObject);

                            mMessageList.add(messagesObject);

                            sendMessageToServer(messagesObject);
                            saveMessageToDataBase(messagesObject);

                            mMessageET.setText("");
                            mAdapter.notifyDataSetChanged();
                            mRecyclerView.scrollToPosition(mMessageList.size() - 1);
                        }
                    });


                } else if (charSequence.length() == 0) {

                    recordAndSendButton.setBackground(ContextCompat.getDrawable(MessageActivity.this, R.drawable.ic_mic));
                    if (ContextCompat.checkSelfPermission(MessageActivity.this, Manifest.permission.RECORD_AUDIO) == PackageManager.PERMISSION_GRANTED) {
                        {

                            recordAndSendButton.setListenForRecord(true);
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


    private void updateLastMessageInDataBase(MessageObject messagesObject) {

        if (messagesObject.getMessageType() == AUDIO) {

            mMessageMainObject.setLastMessage(AUDIO_MESSAGE);

        } else {

            mMessageMainObject.setLastMessage(messagesObject.getMessage());
        }
        mMessageMainObject.setLastMessageTime(messagesObject.getSentAt());
        DataBaseManager manager = new DataBaseManager();
        manager.updateChatToChatTable(getApplicationContext(), mMessageMainObject, CHAT_TABLE);

    }


    private void updateUnreadMessagesToZero(ChatObject chatObject) {

        chatObject.setUnreadMessages(0);
        DataBaseManager manager = new DataBaseManager();
        manager.updateUnreadMessagesToChatTable(this, chatObject, CHAT_TABLE);

    }


    private void updateRecentPagesInWall(ChatObject chatObject) {

        if (chatObject.getUnreadMessages() > 0) {

            if (chatObject.getChatType() == 2) {
                Gson gson = new Gson();

                if (chatObject.getIsGemara()) {

                    Type type = new TypeToken<List<PagesItem>>() {
                    }.getType();
                    List<PagesItem> recentPagesItemList = gson.fromJson(UserManager.getRecentGemaraPlayed(getApplicationContext()), type);

                    if (recentPagesItemList != null) {

                        for (int i = 0; i < recentPagesItemList.size(); i++) {

                            if (recentPagesItemList.get(i).getId() == chatObject.getLessonId()) {

                                recentPagesItemList.get(i).setHasNewMessageFromRabbi(recentPagesItemList.get(i).getHasNewMessageFromRabbi() - 1);

                            }
                        }
                    }

                    String jsonGemaraPage = gson.toJson(recentPagesItemList);

                    UserManager.setRecentGemaraPlayed(jsonGemaraPage, this);

                } else {


                    Type type = new TypeToken<List<MishnayotItem>>() {
                    }.getType();

                    List<MishnayotItem> recentMishnayotItemList = gson.fromJson(UserManager.getRecentMishnaPlayed(getApplicationContext()), type);

                    if (recentMishnayotItemList != null) {

                        for (int i = 0; i < recentMishnayotItemList.size(); i++) {

                            if (recentMishnayotItemList.get(i).getId() == chatObject.getLessonId()) {

                                recentMishnayotItemList.get(i).setHasNewMessageFromRabbi(recentMishnayotItemList.get(i).getHasNewMessageFromRabbi() - 1);

                            }


                        }

                    }

                    String jsonMishnaPage = gson.toJson(recentMishnayotItemList);

                    UserManager.setRecentMishnaPlayed(jsonMishnaPage, this);

                }
            }
        }
    }


    private void updateConfigFileForMAinActivity() {

        UserManager.setUpdateMainActivity(this, mUpdateMainActivity);

    }


    private void setBordcastReceiver() {

        mNewMEssageReciever = new NewMessageReceiver(this);
        IntentFilter filter = new IntentFilter(NEW_MESSAGE_RECIVER);
        registerReceiver(mNewMEssageReciever, filter);

    }


    private void scrollIfUnreadMessages() {

        if (mMessageMainObject.getUnreadMessages() > 0) {


            MessageObject messageObject = new MessageObject();
            messageObject.setMessageType(UNREAD_MESSAGES);
            messageObject.setIsMine(true);
            messageObject.setMessage("Unread messages");

            mMessageList.add(mMessageList.size() - mMessageMainObject.getUnreadMessages(), messageObject);


            mRecyclerView.scrollToPosition((mMessageList.size() - mMessageMainObject.getUnreadMessages()) + 3);
        }

    }


    @Override
    protected void onDestroy() {
        super.onDestroy();

        unregisterReceiver(mNewMEssageReciever);
    }

    private void sendMessageToServer(MessageObject messageObject) {

        if (isNetworkAvailable()) {

            RequestManager.sendMessage(UserManager.getToken(this), messageObject).subscribe(new Observer<Result<MessageObject>>() {
                @Override
                public void onSubscribe(Disposable d) {

                }

                @Override
                public void onNext(Result<MessageObject> messagesObject2Result) {

                }

                @Override
                public void onError(Throwable e) {

                    if (e instanceof HttpException) {
                        HttpException exception = (HttpException) e;
                        ErrorResponse response = null;
                        try {
                            response = new Gson().fromJson(Objects.requireNonNull(exception.response().errorBody()).string(), ErrorResponse.class);
                            Toast.makeText(MessageActivity.this, Objects.requireNonNull(response).getErrors().get(0).getMessage(), Toast.LENGTH_LONG).show();
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
            UserManager.setMessageForOffline(jsonMessage, MessageActivity.this);

        }

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


    private void saveMessageToDataBase(MessageObject messageObject) {


        DBHandler dbHandler = new DBHandler(this);
        dbHandler.addMessageToTable(messageObject);


    }


    private void checkItHasWriteStoragePermission() {

        if (ContextCompat.checkSelfPermission(MessageActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {


            ActivityCompat.requestPermissions(MessageActivity.this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, MY_PERMISSIONS_WRITE_STORAGE);


        }

    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        switch (requestCode) {
            case MY_PERMISSIONS_RECORD_AUDIO:

                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {


                    recordAndSendButton.setListenForRecord(true);
                }

                break;

        }
    }


    private void setRecordView() {

        recordAndSendButton.setRecordView(recordView);


        recordView.setOnRecordListener(new OnRecordListener() {
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
                addAudioRecordMessageToAdapter();

                String time = String.valueOf(recordTime);

                mMessageET.setHint(getResources().getString(R.string.write_message));
                mMessageET.setCursorVisible(true);

            }

            @Override
            public void onLessThanSecond() {
                //When the record time is less than One Second

            }
        });


        recordView.setOnBasketAnimationEndListener(new OnBasketAnimationEnd() {
            @Override
            public void onAnimationEnd() {

            }
        });


        recordView.setSmallMicColor(getResources().getColor(R.color.orange_btn)); // not working from R.color. .

        //disable Sounds
        recordView.setSoundEnabled(true);

        //prevent recording under one Second (it's false by default)
        recordView.setLessThanSecondAllowed(false);

        //set Custom sounds onRecord
        //you can pass 0 if you don't want to play sound in certain state
        recordView.setCustomSounds(R.raw.record_start, R.raw.record_finished, 0);

        //change slide To Cancel Text Color
        recordView.setSlideToCancelTextColor(getResources().getColor(R.color.slide_color));

        recordView.setSlideToCancelArrowColor(getResources().getColor(R.color.slide_color));

        //change Counter Time (Chronometer) color
        recordView.setCounterTimeColor(getResources().getColor(R.color.bottom_nevi_blue));

    }


    private void addAudioRecordMessageToAdapter() {


        MessageObject messageObject = new MessageObject();
        messageObject.setMessageType(AUDIO);
        messageObject.setIsMine(true);
        messageObject.setChatId(mMessageMainObject.getChatId());
        messageObject.setMessageId(234);  // TODO
        messageObject.setMessage(pathName);

        Gson gson = new Gson();
        User userFromJson = gson.fromJson(UserManager.getUser(this), User.class);
        messageObject.setFromUser(userFromJson.getId());
        messageObject.setToUser(mMessageMainObject.getFromUser());
        messageObject.setImage("");
        messageObject.setSentAt(System.currentTimeMillis());

        saveMessageToDataBase(messageObject);
        uploadMessageToS3AndSendTOServer(messageObject);

        updateLastMessageInDataBase(messageObject);
        mMessageList.add(messageObject);

        mMessageET.setText("");
        mAdapter.notifyDataSetChanged();
        mRecyclerView.scrollToPosition(mMessageList.size() - 1);


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
                sendMessageToServer(messageObject1);
            }

            @Override
            public void onUploadError(int id, File fileLocation, Exception ex) {

            }
        });


    }


    private void startRecording() {


        File folder = new File(Config.getPathName(this) + AUDIO_RECORD);
        if (!folder.exists()) {
            folder.mkdirs();
        }


        pathName = Config.getPathName(this) + AUDIO_RECORD + getRecordAudioFileName();

        recorder = new MediaRecorder();
        recorder.setAudioSource(MediaRecorder.AudioSource.MIC);
        recorder.setOutputFormat(MediaRecorder.OutputFormat.MPEG_4);
        recorder.setAudioEncoder(MediaRecorder.AudioEncoder.AAC);
        recorder.setOutputFile(pathName);

        try {
            recorder.prepare();
        } catch (IOException e) {
            Log.e("", "prepare() failed");
        }

        recorder.start();

    }


    private String getRecordAudioFileName() {

        return "/" + System.currentTimeMillis() + ".m4a";
    }


    private void stopRecording() {

        if (recorder != null) {

            try {
                recorder.stop();
            } catch (RuntimeException stopException) {
                //handle cleanup here
            }
            recorder.release();
            recorder = null;
        }
    }


    @Override
    public void onNewMessageReceived(MessageObject messageObject) {

        if (mMessageMainObject.getChatId() == messageObject.getChatId()) {

            mMessageList = mMessageMainObject.getMessages();
            mMessageList.add(messageObject);
            setDatesForMessagesRecyclerView();
            mAdapter.notifyDataSetChanged();
            mRecyclerView.scrollToPosition(mMessageList.size() - 1);

            updateUnreadMessagesToZero(mMessageMainObject);
        }
    }


    @Override
    public void onNewChatReceived(ChatObject chatObject) {

    }


    @Override
    public void downloadRecordAudio(final MessageObject messageObject, final MessageAdapter.ViewHolder holder) {


        S3Helper s3Helper = new S3Helper(this);
        s3Helper.downloadFile(this, messageObject.getMessage().substring(1), messageObject.getMessage().substring(1), AUDIO_RECORD_FILE, new DownloadListener() {
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

                messageObject.setMessage(pathName);
                mAdapter.setAudioPlayerAfterDownloaded(messageObject, holder);

            }

            @Override
            public void onDownloadError() {

                mAdapter.onAudioDownloadError(holder);
            }
        });


    }



    @Override
    public void onLinkMessageClicked(int linkToType) {

        Intent intent = new Intent(this, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK); // to clear previous activity
        intent.putExtra(LINK_TO_TYPE, linkToType);
        startActivity(intent);

    }


}
