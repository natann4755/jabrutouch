package co.il.jabrutouch.ui.main.profile_screen;

import android.app.Dialog;
import android.content.Context;
import android.view.View;
import java.util.Objects;
import co.il.jabrutouch.R;


public class LogOutDialog {


    private LogOutDialogListener mListener;

    public void showDialog(Context context, LogOutDialogListener listener) {

        if (context != null) {
            mListener = listener;
            final Dialog dialog = new Dialog(context);
            dialog.setContentView(R.layout.dialog_log_out);
            Objects.requireNonNull(dialog.getWindow()).setBackgroundDrawableResource(android.R.color.transparent);
            dialog.show();

            dialog.findViewById(R.id.LOD_close_btn).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dialog.dismiss();
                }
            });

            dialog.findViewById(R.id.LOD_cancel_btn).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dialog.dismiss();
                }
            });


            dialog.findViewById(R.id.LOD_log_out_btn).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mListener.OnLogOutClicked();
                    dialog.dismiss();
                }
            });


        }
    }

    public interface LogOutDialogListener{

        void OnLogOutClicked();
    }
}
