package co.il.jabrutouch.ui.main.donation_screen;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.LightingColorFilter;
import android.graphics.Outline;
import android.net.Uri;
import android.os.Handler;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewOutlineProvider;
import android.view.WindowManager;
import android.widget.ImageView;

import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.source.ExtractorMediaSource;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.trackselection.AdaptiveTrackSelection;
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;
import com.google.android.exoplayer2.trackselection.TrackSelection;
import com.google.android.exoplayer2.ui.PlayerView;
import com.google.android.exoplayer2.upstream.BandwidthMeter;
import com.google.android.exoplayer2.upstream.DefaultHttpDataSourceFactory;
import com.jcminarro.roundkornerlayout.RoundKornerLinearLayout;

import java.util.Objects;

import co.il.jabrutouch.R;
import co.il.jabrutouch.ui.main.video_screen.VideoActivity;

public class DonationVideoDialog {


    private Context mContext;
    private Dialog dialog;
    private DonationVideoDialogListener mListener;
    private PlayerView mVideoView;
    private ImageView mPlayBtnIV;


    public void showDialog(Context context, DonationVideoDialogListener listener) {

        if (context != null) {
            mContext = context;
            mListener = listener;
            dialog = new Dialog(context);
            dialog.setCanceledOnTouchOutside(false);
            dialog.setContentView(R.layout.dialog_donation_video);
            dialog.setCancelable(false); // to Prevent back button from closing a dialog box

            Objects.requireNonNull(dialog.getWindow()).setBackgroundDrawableResource(android.R.color.transparent);
            dialog.show();
            
            
            mVideoView = dialog.findViewById(R.id.DDV_video_view_EPV);
            mPlayBtnIV = dialog.findViewById(R.id.DDV_play_btn_IV);


            // to fix darker video in dialog
            WindowManager.LayoutParams lp = dialog.getWindow().getAttributes();
            lp.dimAmount = 0.7f;
            dialog.getWindow().setAttributes(lp);
            dialog.getWindow().addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);

            setVideoView();


            dialog.findViewById(R.id.DDV_skip_btn_TV).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    dialog.dismiss();
                    mVideoView.getPlayer().stop();

                }
            });



            dialog.setOnKeyListener(new DialogInterface.OnKeyListener() {
                @Override
                public boolean onKey(DialogInterface dialogInterface, int keyCode, KeyEvent keyEvent) {

                    if (keyCode == KeyEvent.KEYCODE_BACK) {
                        mListener.onOnBackClicked();
                        mVideoView.getPlayer().stop();
                    }
                    return true;
                }
            });




        }
    }

    private void setVideoView() {

        TrackSelection.Factory videoTrackSelectionFactory =
                new AdaptiveTrackSelection.Factory(new BandwidthMeter() {
                    @Override
                    public long getBitrateEstimate() {
                        return 0;
                    }
                });

        DefaultTrackSelector trackSelector = new DefaultTrackSelector(videoTrackSelectionFactory);
        final SimpleExoPlayer player = ExoPlayerFactory.newSimpleInstance(mContext, trackSelector);
        mVideoView.setPlayer(player);



        Uri uri;
        MediaSource mediaSource;

        uri = Uri.parse("https://player.vimeo.com/external/387438884.sd.mp4?s=7265f40fe437d8a5ad3955b3aa69712afcc1562e&profile_id=165&oauth2_token_id=1135058799");
        mediaSource = buildMediaSource(uri);

        player.prepare(mediaSource);


        mPlayBtnIV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                player.setPlayWhenReady(true);
                mPlayBtnIV.setVisibility(View.GONE);
            }
        });


    }




    private MediaSource buildMediaSource(Uri uri) {
        return new ExtractorMediaSource.Factory(
                new DefaultHttpDataSourceFactory("exoplayer-codelab")).
                createMediaSource(uri);
    }

    public interface DonationVideoDialogListener{


        void onOnBackClicked();
    }


}
