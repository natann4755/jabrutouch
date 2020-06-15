package co.il.jabrutouch.ui.main.video_screen;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.IBinder;
import android.util.Log;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.core.app.NotificationCompat;

import com.google.gson.Gson;

import java.util.concurrent.TimeUnit;

import co.il.jabrutouch.MainActivity;
import co.il.jabrutouch.R;
import co.il.jabrutouch.server.RequestManager;
import co.il.jabrutouch.ui.main.audio_screen.AudioActivity;
import co.il.model.model.Result;
import co.il.jabrutouch.user_manager.UserManager;
import co.il.model.model.AnalyticsData;
import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;




public class ClosingService extends Service {


    private static final String TAG = ClosingService.class.getSimpleName();
    private NotificationManager manager;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        return START_STICKY;
    }


    @Override
    public void onCreate() {
        super.onCreate();


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O)
            startMyOwnForeground();
        else
            startForeground(1, new Notification());

    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private void startMyOwnForeground() {

        String NOTIFICATION_CHANNEL_ID = "com.example.simpleapp";
        String channelName = "My Background Service";
        NotificationChannel chan = new NotificationChannel(NOTIFICATION_CHANNEL_ID, channelName, NotificationManager.IMPORTANCE_NONE);
        chan.setLightColor(Color.BLUE);
        chan.setLockscreenVisibility(Notification.VISIBILITY_PRIVATE);
        manager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        assert manager != null;
        manager.createNotificationChannel(chan);

        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this, NOTIFICATION_CHANNEL_ID);
        Notification notification = notificationBuilder.setOngoing(true)
                .setSmallIcon(R.drawable.jabru_notify_icon)
                .setContentTitle("App is running in background")
                .setPriority(NotificationManager.IMPORTANCE_MIN)
                .setCategory(Notification.CATEGORY_SERVICE)
                .build();
        startForeground(2, notification);

    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.d(TAG, "onDestroyService: ");
        manager.cancel(2);

//        Intent broadcastIntent = new Intent(this, SensorRestarterBroadcastReceiver.class);
//        sendBroadcast(broadcastIntent);
    }

    @Override
    public void onTaskRemoved(Intent rootIntent) {

        fireClosingNotification();

    }

    private void fireClosingNotification() {


        Gson gson = new Gson();
        AnalyticsData analyticsData = gson.fromJson(UserManager.getAnalyticsData(this), AnalyticsData.class);


        if (analyticsData != null) {

            if (analyticsData.isPlaying()) {

                analyticsData.setDuration(analyticsData.getDuration() + (System.currentTimeMillis() - analyticsData.getTimeStart()));

            } else {
                analyticsData.setDuration(analyticsData.getDuration());

            }


            String analyticsDataString = gson.toJson(analyticsData);
            Log.e(TAG, "fireClosingNotification: " + TimeUnit.MILLISECONDS.toSeconds(analyticsData.getDuration()));
            UserManager.setAnalyticsData(analyticsDataString, this);


            RequestManager.sendAnalytics(UserManager.getToken(this), analyticsData).subscribe(new Observer<Result>() {
                @Override
                public void onSubscribe(Disposable d) {

                }

                @Override
                public void onNext(Result result) {
                    UserManager.setAnalyticsData(null, ClosingService.this);
                    stopSelf();
                    manager.cancel(2);
                }

                @Override
                public void onError(Throwable e) {
                    stopSelf();


                }

                @Override
                public void onComplete() {
                }
            });


        }


    }
}