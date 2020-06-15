package co.il.jabrutouch.ui.main.profile_screen;

import android.app.Dialog;
import android.content.Context;
import android.view.View;
import java.util.Objects;
import co.il.jabrutouch.R;



public class NewRegistrationDialog {


    private NewRegistrationDialogListener mListener;

    public void showDialog(Context context, NewRegistrationDialogListener listener) {

        if (context != null) {
            mListener = listener;
            final Dialog dialog = new Dialog(context);
            dialog.setContentView(R.layout.dialog_new_registration);
            Objects.requireNonNull(dialog.getWindow()).setBackgroundDrawableResource(android.R.color.transparent);
            dialog.show();

            dialog.findViewById(R.id.LOD_close_btn).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dialog.dismiss();
                }
            });


            dialog.findViewById(R.id.ESD_ok_btn).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mListener.onOkClicked();
                    dialog.dismiss();
                }
            });


        }
    }

    public interface NewRegistrationDialogListener {

        void onOkClicked();
    }
}
