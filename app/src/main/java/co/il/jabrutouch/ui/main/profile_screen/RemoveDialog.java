package co.il.jabrutouch.ui.main.profile_screen;

import android.app.Dialog;
import android.content.Context;
import android.view.View;
import java.util.Objects;
import co.il.jabrutouch.R;





class RemoveDialog {


    private RemoveDialogListener mListener;

    public void showDialog(Context context, RemoveDialogListener listener) {

        if (context != null) {
            mListener = listener;
            final Dialog dialog = new Dialog(context);
            dialog.setContentView(R.layout.dialog_remove);
            Objects.requireNonNull(dialog.getWindow()).setBackgroundDrawableResource(android.R.color.transparent);
            dialog.show();

            dialog.findViewById(R.id.RD_close_btn).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dialog.dismiss();
                }
            });

            dialog.findViewById(R.id.RD_cancel_btn).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dialog.dismiss();
                }
            });


            dialog.findViewById(R.id.RD_remove_btn).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mListener.onRemoveBtnClicked();
                }
            });


        }
    }

    public interface RemoveDialogListener {
        void onRemoveBtnClicked();
    }

}
