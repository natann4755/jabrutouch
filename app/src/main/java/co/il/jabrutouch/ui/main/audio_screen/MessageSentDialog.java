package co.il.jabrutouch.ui.main.audio_screen;

import android.app.Dialog;
import android.content.Context;
import android.view.Gravity;
import java.util.Objects;
import co.il.jabrutouch.R;


public class MessageSentDialog {


    private Dialog dialog;

    public void showDialog(Context context) {

        if (context != null) {
            dialog = new Dialog(context);
            dialog.setContentView(R.layout.dialog_message_sent);
            Objects.requireNonNull(dialog.getWindow()).setGravity(Gravity.TOP);
            Objects.requireNonNull(dialog.getWindow()).setBackgroundDrawableResource(android.R.color.transparent);
            dialog.show();


        }



    }


    public void dismiss() {
        dialog.dismiss();
    }

}
