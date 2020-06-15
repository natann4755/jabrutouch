package co.il.jabrutouch.ui.main.wall_screen;

import android.app.Dialog;
import android.content.Context;
import android.view.View;
import java.util.Objects;
import co.il.jabrutouch.R;



public class NoInternetDialog {


    public void showDialog(Context context) {

        if (context != null) {
            final Dialog dialog = new Dialog(context);
            dialog.setContentView(R.layout.dialog_no_internet);
            Objects.requireNonNull(dialog.getWindow()).setBackgroundDrawableResource(android.R.color.transparent);
            dialog.show();

            dialog.findViewById(R.id.LOD_close_btn).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dialog.dismiss();
                }
            });

            dialog.findViewById(R.id.NPD_close_btn).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dialog.dismiss();
                }
            });



        }
    }
}
