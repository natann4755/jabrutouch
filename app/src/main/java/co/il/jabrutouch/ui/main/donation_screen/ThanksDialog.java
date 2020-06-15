package co.il.jabrutouch.ui.main.donation_screen;

import android.app.Dialog;
import android.content.Context;
import android.view.View;

import java.util.Objects;

import co.il.jabrutouch.R;

public class ThanksDialog {


    public void showDialog(Context context) {

        if (context != null) {
            final Dialog dialog = new Dialog(context);
            dialog.setContentView(R.layout.dialog_thanks);
            Objects.requireNonNull(dialog.getWindow()).setBackgroundDrawableResource(android.R.color.transparent);
            dialog.show();

            dialog.findViewById(R.id.TD_close_btn).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dialog.dismiss();
                }
            });




        }
    }
}
