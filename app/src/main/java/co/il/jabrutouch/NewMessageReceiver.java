package co.il.jabrutouch;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import java.util.Objects;
import co.il.model.model.ChatObject;
import co.il.model.model.MessageObject;



public class NewMessageReceiver extends BroadcastReceiver {

    public static final String NEW_MESSAGE_RECIVER = "NEW_MESSAGE_RECIVER";
    public static final String NEW_MESSAGE_ITEM = "NEW_MESSAGE_ITEM";
    public static final String NEW_MESSAGE_ITEM_PARS = "NEW_MESSAGE_ITEM_PARS";
    public static final String NEW_CHAT_RECIVER = "NEW_CHAT_RECIVER";
    public static final String NEW_CHAT_ITEM = "NEW_CHAT_ITEM";
    public static final String NEW_CHAT_ITEM_PARS = "NEW_CHAT_ITEM_PARS";

    private NewMessageReceiverListener mListener;

    public NewMessageReceiver(NewMessageReceiverListener listener) {

        mListener = listener;

    }

    @Override
    public void onReceive(Context context, Intent intent) {

        if (intent.getAction() != null && intent.getAction().equals(NEW_MESSAGE_RECIVER)) {

            Bundle args = intent.getBundleExtra(NEW_MESSAGE_ITEM);

            mListener.onNewMessageReceived((MessageObject) Objects.requireNonNull(args).getSerializable(NEW_MESSAGE_ITEM_PARS));
        }

        else if (intent.getAction() != null && intent.getAction().equals(NEW_CHAT_RECIVER)) {

            Bundle args = intent.getBundleExtra(NEW_CHAT_ITEM);

            mListener.onNewChatReceived((ChatObject) Objects.requireNonNull(args).getSerializable(NEW_CHAT_ITEM_PARS));
        }

    }

    public interface NewMessageReceiverListener{


        void onNewMessageReceived(MessageObject messageObject);

        void onNewChatReceived(ChatObject chatObject);
    }

}
