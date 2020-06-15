package co.il.jabrutouch.ui.main.audio_screen;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import java.util.Objects;
import static co.il.jabrutouch.ui.main.audio_screen.AudioReceiver.ACTION_AUDIO;
import static co.il.jabrutouch.ui.main.audio_screen.AudioReceiver.AUDIO_STOP;
import static co.il.jabrutouch.ui.main.audio_screen.NotificationPanel.ACTION_PLAY;


public class NotificationReturnSlot extends Activity {

    public static final String AUDIO_BACK = "AUDIO_BACK";

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        String action = getIntent().getAction();

        if (Objects.requireNonNull(action).equals(ACTION_PLAY)) {

            Intent resultsIntent = new Intent(ACTION_AUDIO);
            resultsIntent.putExtra(AUDIO_STOP, "AUDIO_PLAY");
            sendBroadcast(resultsIntent);

        } else {

            Intent resultsIntent = new Intent(ACTION_AUDIO);
            resultsIntent.putExtra(AUDIO_STOP, "AUDIO_STOP");
            sendBroadcast(resultsIntent);

        }

        finish();
    }
}