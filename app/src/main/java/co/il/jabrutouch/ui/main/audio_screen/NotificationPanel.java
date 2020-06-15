package co.il.jabrutouch.ui.main.audio_screen;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.os.Build;
import androidx.core.app.NotificationCompat;
import co.il.jabrutouch.R;
import co.il.jabrutouch.ui.main.video_screen.VideoActivity;


public class NotificationPanel {

    static final String ACTION_PLAY = "ACTION_PLAY";
    private static final String ACTION_PAUSE = "ACTION_PAUSE";
    private static final String CHANNEL_ID = "111";
    private Context context;
    private NotificationManager nManager;
    private String pageName;




    /**
     * add notification panel when user start video or audio
     * @param context Context
     * @param pageName String
     * @param isPlaying String
     */
    public NotificationPanel(Context context, String pageName, boolean isPlaying, boolean isGemara) {

        this.context = context;
        this.pageName = pageName;


        nManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O)
        {
            NotificationChannel channel = new NotificationChannel(
                    CHANNEL_ID,
                    "Channel human readable title",
                    NotificationManager.IMPORTANCE_DEFAULT);

            channel.setSound(null, null);
            channel.setDescription("YOUR_NOTIFICATION_CHANNEL_DISCRIPTION");
            nManager.createNotificationChannel(channel);
        }



        int notificationAction;
        PendingIntent play_pauseAction = null;


        if (isPlaying) {
            notificationAction = R.drawable.ic_video_pause;
            //create the pause action
            play_pauseAction = playbackAction(1);
        } else {
            notificationAction = R.drawable.ic_play;
            //create the play action
            play_pauseAction = playbackAction(0);
        }

        Intent intent;

        if (context instanceof VideoActivity){
            intent = new Intent(context, VideoActivity.class);
        }
        else {

            intent = new Intent(context, AudioActivity.class);
        }


        String title = "";
        if (isGemara){
            title = context.getResources().getString(R.string.gemara);
        }else {
            title = context.getResources().getString(R.string.mishna);
        }

        intent.setAction(Long.toString(System.currentTimeMillis()));
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
        PendingIntent pendingIntent = PendingIntent.getActivity(context, 0,
                intent, PendingIntent.FLAG_CANCEL_CURRENT);



        NotificationCompat.Builder nBuilder = new NotificationCompat.Builder(context, CHANNEL_ID)
                .setSmallIcon(R.drawable.jabru_notify_icon)
//                .setLargeIcon(BitmapFactory.decodeResource(context.getResources(), R.drawable.ic_jabru_icon))
                .addAction(notificationAction, "pause", play_pauseAction)
                .setStyle(new androidx.media.app.NotificationCompat.MediaStyle().setShowActionsInCompactView(0))
                .setOngoing(isPlaying)
                .setColor(context.getResources().getColor(R.color.bottom_nevi_blue))
//                .setColorized(true)
                .setContentIntent(pendingIntent)
                .setContentTitle(title)
                .setContentText(pageName);


        nManager.notify(2, nBuilder.build());
    }




    public void notificationCancel() {
        nManager.cancel(2);
    }



    private PendingIntent playbackAction(int actionNumber) {
        Intent playbackAction = new Intent(context, NotificationReturnSlot.class);

        switch (actionNumber) {
            case 0:
                // Play
                playbackAction.setAction(ACTION_PLAY);
                return PendingIntent.getActivity(context, actionNumber, playbackAction, 0);
            case 1:
                // Pause
                playbackAction.setAction(ACTION_PAUSE);
                return PendingIntent.getActivity(context, actionNumber, playbackAction, 0);

            default:
                break;
        }
        return null;
    }
}
