package co.il.jabrutouch.ui.main.audio_screen;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;



public class AudioReceiver extends BroadcastReceiver {


    public static final String ACTION_AUDIO = "ACTION_AUDIO";
    public static final String AUDIO_STOP = "AUDIO_STOP";
    private audioReceiverListener mListener;


    public AudioReceiver(audioReceiverListener listener){
        mListener =listener;
    }
    

    @Override
    public void onReceive(Context context, Intent intent) {

        if (intent.getAction() != null && intent.getAction().equals(ACTION_AUDIO)) {

            mListener.onStopClicked(intent.getStringExtra(AUDIO_STOP));
        }



    }

    public interface audioReceiverListener {


        void onStopClicked(String stringExtra);
    }
}
