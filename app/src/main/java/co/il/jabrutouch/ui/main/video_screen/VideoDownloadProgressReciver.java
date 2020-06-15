package co.il.jabrutouch.ui.main.video_screen;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import java.util.Objects;
import co.il.model.model.PagesItem;
import static co.il.jabrutouch.MainActivity.TALMUD_TYPE;
import static co.il.jabrutouch.ui.main.video_screen.VideoActivity.DOWNLOAD_VIDEO_PROGRESS;
import static co.il.jabrutouch.ui.main.video_screen.VideoActivity.VIDEO_PROGRESS;
import static co.il.jabrutouch.ui.main.video_screen.VideoActivity.VIDEO_PROGRESS_PAGE_ITEN;
import static co.il.jabrutouch.ui.main.video_screen.VideoActivity.VIDEO_PROGRESS_PAGE_ITEN_PARS;



public class VideoDownloadProgressReciver extends BroadcastReceiver {



    private VideoDownloadProgressReciverListener mListener;

    public VideoDownloadProgressReciver(){};

    public VideoDownloadProgressReciver(VideoDownloadProgressReciverListener listener){
        mListener =listener;
    }


    @Override
    public void onReceive(Context context, Intent intent) {

        if (intent.getAction() != null && intent.getAction().equals(DOWNLOAD_VIDEO_PROGRESS)) {

            Bundle args = intent.getBundleExtra(VIDEO_PROGRESS_PAGE_ITEN);

            mListener.onVideoProgressChanged(intent.getStringExtra(VIDEO_PROGRESS),
                    (PagesItem) Objects.requireNonNull(args).getSerializable(VIDEO_PROGRESS_PAGE_ITEN_PARS),
                    intent.getStringExtra(TALMUD_TYPE));
        }



    }

    public interface VideoDownloadProgressReciverListener{

        void onVideoProgressChanged(String audioProgress, PagesItem pagesItem, String talmudType);

    }
}
