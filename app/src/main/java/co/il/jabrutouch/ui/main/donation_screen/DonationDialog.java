package co.il.jabrutouch.ui.main.donation_screen;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Handler;
import android.view.KeyEvent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

import androidx.appcompat.widget.AppCompatTextView;
import com.jcminarro.roundkornerlayout.RoundKornerLinearLayout;

import java.util.List;
import java.util.Objects;
import co.il.jabrutouch.R;
import co.il.model.model.DonatedByItem;
import co.il.model.model.LessonDonationBy;


public class DonationDialog {


    private DonationDialogListener mListener;
    private Context mContext;
    private AppCompatTextView mDonationName;
    private AppCompatTextView mDonationLocation;
    private Dialog dialog;
    private List<DonatedByItem> mDonationByList;
    private AppCompatTextView mDonationMemorialName;
    private int mCrownId;
    private ImageView mDonationLocationIV;

    public void showDialog(Context context, DonationDialogListener listener, LessonDonationBy donatedByList) {

        if (context != null) {
            mListener = listener;
            mContext = context;
            mDonationByList = donatedByList.getDonatedBy();
            mCrownId = donatedByList.getCrownId();
            dialog = new Dialog(context);
            dialog.setCanceledOnTouchOutside(false);
            dialog.setContentView(R.layout.dialog_donation);
            mDonationName = dialog.findViewById(R.id.DD_name);
            mDonationLocation = dialog.findViewById(R.id.DD_Donation_location_TV);
            mDonationLocationIV = dialog.findViewById(R.id.dd_location_IV);
            mDonationMemorialName = dialog.findViewById(R.id.DD_memorial_name_ACTV);
            setDonationName();
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


            dialog.findViewById(R.id.DD_appreciate).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    setImageClickedAnimation((RoundKornerLinearLayout) dialog.findViewById(R.id.DD_appreciate));

                    final Handler handler = new Handler();
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            dialog.dismiss();
                            mListener.OnAppreciateClicked(mCrownId);
                        }
                    }, 100);

                }
            });


        }
    }



    @SuppressLint("SetTextI18n")
    private void setDonationName() {


        for (int i = 0; i < mDonationByList.size(); i++) {

            mDonationName.setText(mDonationByList.get(i).getNameToRepresent());
            mDonationMemorialName.setText(mDonationByList.get(i).getDedicationText() + " "  + mDonationByList.get(i).getDedicationTemplateText());

            if (!mDonationByList.get(i).getCountry().equals("")){

                mDonationLocation.setText(mDonationByList.get(i).getCountry());
                mDonationLocationIV.setVisibility(View.VISIBLE);

            }else {

                mDonationLocation.setText(mDonationByList.get(i).getCountry());
                mDonationLocationIV.setVisibility(View.GONE);
            }
            break;
        }




//        int index = UserManager.getDonationIndex(mContext);
//        if (index < Donations.donationLists.length - 1) {
//            index++;
//        } else {
//            index = 0;
//        }
//        UserManager.setDonationIndex(index, mContext);
//
//        Donation donation = Donations.donationLists[index];
//        mDonationName.setText(String.format("%s %s", donation.getName(), donation.getSurname()));
//        mDonationLocation.setText(donation.getCountry());

    }




    private void setImageClickedAnimation(RoundKornerLinearLayout Icon) {

        Animation animation = AnimationUtils.loadAnimation(mContext, R.anim.alpha);
        Icon.startAnimation(animation);

    }



    public void dismiss() {
        dialog.dismiss();
    }


    public interface DonationDialogListener {

        void OnAppreciateClicked(int crownId);

        void onOnBackClicked();
    }
}
