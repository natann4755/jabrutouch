package co.il.jabrutouch.ui.main.audio_screen;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import java.util.Objects;
import co.il.model.model.PagesItem;
import static co.il.jabrutouch.MainActivity.TALMUD_TYPE;
import static co.il.jabrutouch.ui.main.audio_screen.AudioActivity.AUDIO_PROGRESS;
import static co.il.jabrutouch.ui.main.audio_screen.AudioActivity.AUDIO_PROGRESS_PAGE_ITEM;
import static co.il.jabrutouch.ui.main.audio_screen.AudioActivity.AUDIO_PROGRESS_PAGE_ITEM_PARS;
import static co.il.jabrutouch.ui.main.audio_screen.AudioActivity.DOWNLOAD_AUDIO_PROGRESS;



public class AudioDownloadProgressReceiver extends BroadcastReceiver {


    private AudioDownloadProgressReceiverListener mListener;


    public AudioDownloadProgressReceiver(AudioDownloadProgressReceiverListener listener){
        mListener =listener;
    }


    @Override
    public void onReceive(Context context, Intent intent) {

        if (intent.getAction() != null && intent.getAction().equals(DOWNLOAD_AUDIO_PROGRESS)) {

            Bundle args = intent.getBundleExtra(AUDIO_PROGRESS_PAGE_ITEM);

            mListener.onProgressChanged(intent.getStringExtra(AUDIO_PROGRESS),
                    (PagesItem) Objects.requireNonNull(args).getSerializable(AUDIO_PROGRESS_PAGE_ITEM_PARS),
                    intent.getStringExtra(TALMUD_TYPE));
        }



    }

    public interface AudioDownloadProgressReceiverListener {


        void onProgressChanged(String audioProgress, PagesItem pagesItem, String talmudType);
    }
}
