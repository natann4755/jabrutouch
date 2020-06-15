package co.il.jabrutouch.ui.main.donation_screen;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Handler;
import android.view.KeyEvent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;

import com.jcminarro.roundkornerlayout.RoundKornerLinearLayout;

import java.util.Objects;

import co.il.jabrutouch.R;
import co.il.model.model.LessonDonationBy;

public class DonationDialogNoDonate {

    private DonationDialogNoDonateListener mListener;
    private Context mContext;
    private Dialog dialog;

    public void showDialog(Context context, DonationDialogNoDonateListener listener) {

        if (context != null) {
            mListener = listener;
            mContext = context;
            dialog = new Dialog(context);
            dialog.setCanceledOnTouchOutside(false);
            dialog.setContentView(R.layout.dialog_donation_no_donate);
//            dialog.setCancelable(false); // to Prevent back button from closing a dialog box
            Objects.requireNonNull(dialog.getWindow()).setBackgroundDrawableResource(android.R.color.transparent);
            dialog.show();


            dialog.setOnKeyListener(new DialogInterface.OnKeyListener() {
                @Override
                public boolean onKey(DialogInterface dialogInterface, int keyCode, KeyEvent keyEvent) {

                    if (keyCode == KeyEvent.KEYCODE_BACK) {
                        mListener.onOnBackClicked();
                    }
                    return true;
                }
            });


            dialog.findViewById(R.id.DDND_donate_BTN).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    setImageClickedAnimation(dialog.findViewById(R.id.DDND_donate_BTN));

                    final Handler handler = new Handler();
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            dialog.dismiss();
                            mListener.onDonateDonationClicked();
                        }
                    }, 100);

                }
            });


            dialog.findViewById(R.id.DDND_skip_BTN).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    setImageClickedAnimation(dialog.findViewById(R.id.DDND_skip_BTN));

                    final Handler handler = new Handler();
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            dialog.dismiss();
                            mListener.onSkipBtnClicked();
                        }
                    }, 100);

                }
            });


        }
    }



    private void setImageClickedAnimation(View Icon) {

        Animation animation = AnimationUtils.loadAnimation(mContext, R.anim.alpha);
        Icon.startAnimation(animation);

    }



    public interface DonationDialogNoDonateListener{

        void onOnBackClicked();

        void onDonateDonationClicked();

        void onSkipBtnClicked();
    }



}
