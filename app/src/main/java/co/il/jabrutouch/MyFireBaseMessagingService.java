package co.il.jabrutouch;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.List;
import java.util.Objects;

import co.il.jabrutouch.data_base_manager.DataBaseManager;
import co.il.jabrutouch.ui.main.message_screen.MessageActivity;
import co.il.jabrutouch.user_manager.UserManager;
import co.il.model.model.ChatObject;
import co.il.model.model.MessageObject;
import co.il.model.model.MishnayotItem;
import co.il.model.model.PagesItem;

import static co.il.jabrutouch.NewMessageReceiver.NEW_CHAT_ITEM;
import static co.il.jabrutouch.NewMessageReceiver.NEW_CHAT_ITEM_PARS;
import static co.il.jabrutouch.NewMessageReceiver.NEW_CHAT_RECIVER;
import static co.il.jabrutouch.NewMessageReceiver.NEW_MESSAGE_ITEM;
import static co.il.jabrutouch.NewMessageReceiver.NEW_MESSAGE_ITEM_PARS;
import static co.il.jabrutouch.NewMessageReceiver.NEW_MESSAGE_RECIVER;
import static co.il.jabrutouch.ui.main.message_screen.ChatActivity.CHAT_OBJECT;
import static co.il.jabrutouch.ui.main.message_screen.MessageActivity.AUDIO;
import static co.il.jabrutouch.ui.main.message_screen.MessageActivity.LINK_MESSAGE;
import static co.il.sqlcore.DBKeys.CHAT_TABLE;
import static co.il.sqlcore.DBKeys.MESSAGE_TABLE;


public class MyFireBaseMessagingService extends FirebaseMessagingService {

    private static final String CHANNEL_ID = "921";
    public static final String UPDATE_MAIN_ACTIVITY = "UPDATE_MAIN_ACTIVITY";
    public static final String AUDIO_MESSAGE = "Audio";


    @Override
    public void onMessageReceived(@NonNull RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);

        Gson gson = new Gson();
        String txt = Objects.requireNonNull(remoteMessage.getData().get("data"));
        MessageObject messageObject = gson.fromJson(txt, MessageObject.class);

        addLinkTypeForLinkMessage(messageObject);

        saveMessageToDataBase(messageObject);


    }


    /**
     * if message type == LINK(5), add link type to message type
     * @param messageObject MessageObject
     */
    private void addLinkTypeForLinkMessage(MessageObject messageObject) {

        if (messageObject.getMessageType() == LINK_MESSAGE){

            messageObject.setMessageType(Integer.valueOf(String.valueOf(messageObject.getMessageType()) + messageObject.getLinkTo()));

        }


    }



    private void saveMessageToDataBase(MessageObject messageObject) {

        DataBaseManager manager = new DataBaseManager();
        manager.addMessageToMessageTable(getApplicationContext(), messageObject);

        checkIfChatExistAndSendNotification(manager, messageObject);


    }


    private void checkIfChatExistAndSendNotification(DataBaseManager manager, MessageObject messageObject) {

        List<ChatObject> chatObjectList = manager.getAllChats(getApplicationContext(), CHAT_TABLE);
        boolean chatExist = false;
        ChatObject chatObject = null;

        for (int i = 0; i < chatObjectList.size(); i++) {

            if (chatObjectList.get(i).getChatId() == messageObject.getChatId()) {
                chatExist = true;
                chatObject = chatObjectList.get(i);
            }

        }

        if (!chatExist) {

            ChatObject newChatObject = new ChatObject();
            newChatObject.setChatId(messageObject.getChatId());
            newChatObject.setCreatedAt(messageObject.getSentAt());
            newChatObject.setTitle(messageObject.getTitle());
            newChatObject.setFromUser(messageObject.getFromUser());
            newChatObject.setToUser(messageObject.getToUser());

            if (messageObject.getMessageType() == AUDIO) {

                newChatObject.setLastMessage(AUDIO_MESSAGE);

            } else {

                newChatObject.setLastMessage(messageObject.getMessage());
            }
            newChatObject.setLastMessageTime(messageObject.getSentAt());
            newChatObject.setUnreadMessages(1);

            manager.addChatToChatTable(getApplicationContext(), newChatObject);
            chatObject = newChatObject;

        } else {

            if (messageObject.getMessageType() == AUDIO) {

                chatObject.setLastMessage(AUDIO_MESSAGE);

            } else {

                chatObject.setLastMessage(messageObject.getMessage());
            }

            chatObject.setLastMessageTime(messageObject.getSentAt());
            chatObject.setUnreadMessages(chatObject.getUnreadMessages() + 1);

            manager.updateChatToChatTable(getApplicationContext(), chatObject, CHAT_TABLE);

        }


        if (UserManager.getCurrentChat(getApplicationContext()) != messageObject.getChatId()) { // send notification only when user not in the chat

            sendNotification(manager, chatObject, messageObject);

            if (chatObject.getChatType() == 2 && chatObject.getUnreadMessages() == 1) { // set recent only when is from ask the rabbi and only if there is no more unread messages than 1

                addUnreadMessageIfPageInRecent(messageObject);
            }
        }


        sendBroadcastReceiver(chatObject, messageObject);


    }


    private void sendNotification(DataBaseManager manager, ChatObject chatObject, MessageObject messageObject) {


        NotificationManager nManager = (NotificationManager) getApplicationContext().getSystemService(Context.NOTIFICATION_SERVICE);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(
                    CHANNEL_ID,
                    "Channel human readable title",
                    NotificationManager.IMPORTANCE_DEFAULT);

//            channel.setSound(null, null); // to close sound notification
            channel.setDescription("YOUR_NOTIFICATION_CHANNEL_DISCRIPTION");
            Objects.requireNonNull(nManager).createNotificationChannel(channel);
        }


        Intent intent = new Intent(getApplicationContext(), MessageActivity.class);


        List<MessageObject> messageList = manager.getAllMessages(this, MESSAGE_TABLE, chatObject.getChatId());
        chatObject.setMessages(messageList);
        intent.putExtra(CHAT_OBJECT, chatObject);
        intent.putExtra(UPDATE_MAIN_ACTIVITY, true);

        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
        PendingIntent pendingIntent = PendingIntent.getActivity(getApplicationContext(), messageObject.getMessageId(), intent, PendingIntent.FLAG_UPDATE_CURRENT);

        Uri defaultSound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);

        String message;
        if (messageObject.getMessageType() == AUDIO) {

            message = AUDIO_MESSAGE;
        } else {
            message = messageObject.getMessage();
        }

        NotificationCompat.Builder nBuilder = new NotificationCompat.Builder(getApplicationContext(), CHANNEL_ID)
                .setSmallIcon(R.drawable.jabru_notify_icon)
//                .setLargeIcon(BitmapFactory.decodeResource(getApplicationContext().getResources(), R.drawable.ic_jabru_icon_egg))
//                .setStyle(new androidx.media.app.NotificationCompat.MediaStyle().setShowActionsInCompactView(0))
                .setStyle(new NotificationCompat.BigTextStyle().bigText(message))
                .setOngoing(false)
                .setColor(getApplicationContext().getResources().getColor(R.color.bottom_nevi_blue))
//                .setColorized(true)
                .setSound(defaultSound)
                .setAutoCancel(true)
                .setContentIntent(pendingIntent)
                .setContentTitle(messageObject.getTitle())
                .setContentText(message);


        nManager.notify(messageObject.getChatId(), nBuilder.build());

    }


    private void sendBroadcastReceiver(ChatObject chatObject, MessageObject messageObject) {

        Intent resultsIntent = new Intent(NEW_MESSAGE_RECIVER);
        Bundle bundle = new Bundle();
        bundle.putSerializable(NEW_MESSAGE_ITEM_PARS, messageObject);
        resultsIntent.putExtra(NEW_MESSAGE_ITEM, bundle);
        sendBroadcast(resultsIntent);

        Intent resultsIntent2 = new Intent(NEW_CHAT_RECIVER);
        Bundle bundle2 = new Bundle();
        bundle2.putSerializable(NEW_CHAT_ITEM_PARS, chatObject);
        resultsIntent2.putExtra(NEW_CHAT_ITEM, bundle2);
        sendBroadcast(resultsIntent2);


    }


    private void addUnreadMessageIfPageInRecent(MessageObject messageObject) {

        Gson gson = new Gson();


        if (messageObject.isGemara()) {

            Type type = new TypeToken<List<PagesItem>>() {
            }.getType();
            List<PagesItem> recentPagesItemList = gson.fromJson(UserManager.getRecentGemaraPlayed(getApplicationContext()), type);

            if (recentPagesItemList != null) {

                for (int i = 0; i < recentPagesItemList.size(); i++) {

                    if (recentPagesItemList.get(i).getId() == messageObject.getLessonId()) {

                        recentPagesItemList.get(i).setHasNewMessageFromRabbi(recentPagesItemList.get(i).getHasNewMessageFromRabbi() + 1);

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

                    if (recentMishnayotItemList.get(i).getId() == messageObject.getLessonId()) {

                        recentMishnayotItemList.get(i).setHasNewMessageFromRabbi(recentMishnayotItemList.get(i).getHasNewMessageFromRabbi() + 1);

                    }


                }
            }

            String jsonMishnaPage = gson.toJson(recentMishnayotItemList);

            UserManager.setRecentMishnaPlayed(jsonMishnaPage, this);
        }

    }
}
