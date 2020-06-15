package co.il.jabrutouch.ui.main.profile_screen;

import android.app.Dialog;
import android.content.Context;
import android.view.View;
import androidx.appcompat.widget.AppCompatTextView;
import java.util.Objects;
import co.il.jabrutouch.R;



public class EmailSentDialog {

    private final String emailAddress;
    private EmailSentDialogListener mListener;
    private AppCompatTextView mailText;

    public EmailSentDialog(String emailAddress) {

        this.emailAddress = emailAddress;
    }

    public void showDialog(Context context, EmailSentDialogListener listener) {

        if (context != null) {
            mListener = listener;
            final Dialog dialog = new Dialog(context);
            dialog.setContentView(R.layout.dialog_email_sent);
            Objects.requireNonNull(dialog.getWindow()).setBackgroundDrawableResource(android.R.color.transparent);
            dialog.show();

            mailText = dialog.findViewById(R.id.ESD_email_text_ACTV);
            mailText.setText(emailAddress);

            dialog.findViewById(R.id.LOD_close_btn).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dialog.dismiss();
                }
            });


            dialog.findViewById(R.id.ESD_ok_btn).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    dialog.dismiss();
                }
            });


        }
    }

    public interface EmailSentDialogListener {
    }
}
