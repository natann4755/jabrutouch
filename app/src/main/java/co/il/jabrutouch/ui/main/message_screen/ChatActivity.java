package co.il.jabrutouch.ui.main.message_screen;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.app.Activity;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.SearchView;
import android.widget.TextView;
import java.util.ArrayList;
import java.util.List;
import co.il.jabrutouch.NewMessageReceiver;
import co.il.jabrutouch.R;
import co.il.jabrutouch.data_base_manager.DataBaseManager;
import co.il.jabrutouch.server.RequestManager;
import co.il.jabrutouch.ui.main.message_screen.adapters.ChatAdapter;
import co.il.jabrutouch.user_manager.UserManager;
import co.il.model.model.ChatObject;
import co.il.model.model.Chats;
import co.il.model.model.MessageObject;
import co.il.model.model.Result;
import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;
import static co.il.jabrutouch.MyFireBaseMessagingService.UPDATE_MAIN_ACTIVITY;
import static co.il.jabrutouch.NewMessageReceiver.NEW_CHAT_RECIVER;
import static co.il.jabrutouch.ui.main.message_screen.MessageActivity.LINK_MESSAGE;
import static co.il.jabrutouch.ui.main.message_screen.MessageActivity.closeChatActivity;
import static co.il.sqlcore.DBKeys.CHAT_TABLE;
import static co.il.sqlcore.DBKeys.MESSAGE_TABLE;




public class ChatActivity extends AppCompatActivity implements ChatAdapter.MessageMainAdapterListener, NewMessageReceiver.NewMessageReceiverListener {

    private static final String TEAM = "TEAM";
    public static final String CHAT_OBJECT = "CHAT_OBJECT";
    private RecyclerView mMessageRecyclerView;
    private ChatAdapter mMessageMainAdapter;
    private List<ChatObject> mChatList = new ArrayList<>();
    private NewMessageReceiver mNewChatReciever;
    private RelativeLayout mHasMessagesRL;
    private LinearLayout mNoMessagesLL;
    private ImageView mSearchIconIV;
    private ProgressBar mProgressBar;
    private ImageView mArrowBachIV;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        initViews();
        initListeners();
        setSearchView();
        setBordcastReceiver();
        getAllChatsFromLocalStorage();

    }




    private void initViews() {

        mMessageRecyclerView = findViewById(R.id.MA_message_recycler_RV);
        mHasMessagesRL = findViewById(R.id.MMA_there_is_messages_RL);
        mNoMessagesLL = findViewById(R.id.MMA_no_messages_LL);
        mProgressBar = findViewById(R.id.CA_video_progress_bar);
        mArrowBachIV = findViewById(R.id.CA_arrow_back_IV);

    }




    private void initListeners() {

        mArrowBachIV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                setViewClickedAnimation(view);
                finish();

            }
        });

    }




    private void setViewClickedAnimation(View view) {

        Animation animation = AnimationUtils.loadAnimation(this, R.anim.alpha);
        view.startAnimation(animation);

    }




    /**
     * set search view
     */
    private void setSearchView() {

        SearchView searchView = findViewById(R.id.CA_search_SV);

        final TextView title = findViewById(R.id.CA_title_TV);

        searchView.setOnSearchClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                title.setVisibility(View.GONE);

            }
        });


        searchView.setOnCloseListener(new SearchView.OnCloseListener() {
            @Override
            public boolean onClose() {
                title.setVisibility(View.VISIBLE);

                return false;
            }
        });


        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {


                if (mChatList != null) {

                    mMessageMainAdapter.getFilter().filter(s);
                }


                return true;
            }
        });


    }




    private void getAllChatsFromLocalStorage() {

        DataBaseManager dataBaseManager = new DataBaseManager();
        List<ChatObject> chatList = dataBaseManager.getAllChats(this, CHAT_TABLE);


        if (chatList.size() == 0) {

            getAllChatsFromServer();

        } else {

            mChatList = chatList;
            setTheAdapter();
            setBackground();
        }
    }





    private void setBordcastReceiver() {

        mNewChatReciever = new NewMessageReceiver(this);
        IntentFilter filter = new IntentFilter(NEW_CHAT_RECIVER);
        registerReceiver(mNewChatReciever, filter);

    }




    @Override
    protected void onStart() {
        super.onStart();
    }




    @Override
    protected void onStop() {
        super.onStop();


    }



    @Override
    protected void onPause() {
        super.onPause();

        if (closeChatActivity){
            finish();
        }
    }




    private void getAllChatsFromServer() {

        mProgressBar.setVisibility(View.VISIBLE);

        RequestManager.getAllChats(UserManager.getToken(this)).subscribe(new Observer<Result<Chats>>() {
            @Override
            public void onSubscribe(Disposable d) {

            }

            @Override
            public void onNext(Result<Chats> chatsResult) {

                mChatList = chatsResult.getData().getChats();
                saveAllChatsToLocalStorage(mChatList);


            }

            @Override
            public void onError(Throwable e) {
                setBackground();
                mProgressBar.setVisibility(View.GONE);

            }

            @Override
            public void onComplete() {

            }
        });


    }




    private void saveAllChatsToLocalStorage(List<ChatObject> chatList) {


        for (int i = 0; i < chatList.size(); i++) {

                DataBaseManager manager = new DataBaseManager();
                manager.addChatToChatTable(this, chatList.get(i));

            for (int j = 0; j < chatList.get(i).getMessages().size(); j++) {

                if (chatList.get(i).getMessages().get(j).getMessageType() == LINK_MESSAGE){

                    chatList.get(i).getMessages().get(j).setMessageType(Integer.valueOf
                            (String.valueOf(chatList.get(i).getMessages().get(j).getMessageType())
                                    + chatList.get(i).getMessages().get(j).getLinkTo()));

                }
                manager.addMessageToMessageTable(getApplicationContext(), chatList.get(i).getMessages().get(j));

            }
        }

        setTheAdapter();
        setBackground();
        mProgressBar.setVisibility(View.GONE);

    }



    private void setBackground() {

        if (mChatList == null || mChatList.size() == 0) {

            mHasMessagesRL.setVisibility(View.GONE);
            mNoMessagesLL.setVisibility(View.VISIBLE);

        } else {

            mHasMessagesRL.setVisibility(View.VISIBLE);
            mNoMessagesLL.setVisibility(View.GONE);
        }
    }




    @Override
    protected void onDestroy() {
        super.onDestroy();

        unregisterReceiver(mNewChatReciever);
    }




    private void setTheAdapter() {


        mMessageRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mMessageMainAdapter = new ChatAdapter(this, mChatList, this);
        mMessageRecyclerView.setAdapter(mMessageMainAdapter);

    }




    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {

    }




    @Override
    public void onMessageClicked(ChatObject chatObject) {


        if (chatObject.getMessages() == null || chatObject.getMessages().size() == 0) {

            DataBaseManager manager = new DataBaseManager();

            List<MessageObject> messageList = manager.getAllMessages(this, MESSAGE_TABLE, chatObject.getChatId());

            chatObject.setMessages(messageList);
        }


        cancelNotification(chatObject);

        Intent intent = new Intent(this, MessageActivity.class);
        intent.putExtra(CHAT_OBJECT, chatObject);
        if (UserManager.getUpdateMainActivity(this)){ // if start this activity from chat activity and not from main (then does not have on back in main activity)
            intent.putExtra(UPDATE_MAIN_ACTIVITY, true);

        }else {

            intent.putExtra(UPDATE_MAIN_ACTIVITY, false);
        }
        startActivity(intent);
        finish();

    }





    private void cancelNotification(ChatObject chatObject) {

        NotificationManager notificationManager =
                (NotificationManager) this.getSystemService(Context.NOTIFICATION_SERVICE);

        if (notificationManager != null) {

            notificationManager.cancel(chatObject.getChatId());
        }

    }




    @Override
    public void onBackPressed() {

        Intent returnIntent = new Intent();
        setResult(Activity.RESULT_CANCELED, returnIntent);
        finish();
    }





    @Override
    public void onNewMessageReceived(MessageObject messageObject) {

    }




    @Override
    public void onNewChatReceived(ChatObject chatObject) {


        getAllChatsFromLocalStorage();
        mMessageMainAdapter.notifyDataSetChanged();
        mMessageRecyclerView.scrollToPosition(0);

    }
}
