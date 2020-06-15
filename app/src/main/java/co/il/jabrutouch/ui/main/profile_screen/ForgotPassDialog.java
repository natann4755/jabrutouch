package co.il.jabrutouch.ui.main.profile_screen;

import android.app.Dialog;
import android.content.Context;
import android.view.View;
import android.widget.EditText;
import java.util.Objects;
import co.il.jabrutouch.R;




public class ForgotPassDialog {


    private ForgotPassDialogListener mListener;

    public void showDialog(Context context, ForgotPassDialogListener listener) {

        if (context != null) {
            mListener = listener;
            final Dialog dialog = new Dialog(context);
            dialog.setContentView(R.layout.dialog_forgot);
            final EditText editText = dialog.findViewById(R.id.FD_edit_text);
            Objects.requireNonNull(dialog.getWindow()).setBackgroundDrawableResource(android.R.color.transparent);
            dialog.show();

            dialog.findViewById(R.id.LOD_close_btn).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dialog.dismiss();
                }
            });


            dialog.findViewById(R.id.FD_send_now_btn).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dialog.findViewById(R.id.FD_progress_bar).setVisibility(View.VISIBLE);
                    mListener.onSendNowClicked(editText.getText().toString(), dialog);

                }
            });


        }
    }

    public interface ForgotPassDialogListener {
        void onSendNowClicked(String emailAdress, Dialog dialog);
    }
}
