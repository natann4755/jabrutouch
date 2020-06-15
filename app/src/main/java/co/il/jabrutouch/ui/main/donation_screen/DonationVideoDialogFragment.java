package co.il.jabrutouch.ui.main.donation_screen;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

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

import java.util.Objects;

import co.il.jabrutouch.R;


public class DonationVideoDialogFragment extends Fragment {


    public static final String TAG = DonationVideoDialogFragment.class.getSimpleName();
    private static final String VIDEO_DONATION = "https://player.vimeo.com/external/397786632.sd.mp4?s=cfb416d6ffd7e59731c447f803f58c44e3b94149&profile_id=165&oauth2_token_id=1135058799";
    private OnDonationVideoDialogFragmentListener mListener;
    private PlayerView mVideoView;
    private ImageView mPlayBtnIV;
    private TextView mSkipBtnTV;

    public DonationVideoDialogFragment() {
        // Required empty public constructor
    }


    public static DonationVideoDialogFragment newInstance() {
        DonationVideoDialogFragment fragment = new DonationVideoDialogFragment();
        Bundle args = new Bundle();

        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {

        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_donation_video_dialog, container, false);
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        initViews();
        setVideoView();
    }





    private void initViews() {


        mVideoView = Objects.requireNonNull(getView()).findViewById(R.id.DDV_video_view_EPV);
        mPlayBtnIV = getView().findViewById(R.id.DDV_play_btn_IV);
        mSkipBtnTV = getView().findViewById(R.id.DDV_skip_btn_TV);

        
        mSkipBtnTV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mListener.onSkipVideoClicked();
                mVideoView.getPlayer().stop();
            }
        });




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
        final SimpleExoPlayer player = ExoPlayerFactory.newSimpleInstance(getContext(), trackSelector);
        mVideoView.setPlayer(player);



        Uri uri;
        MediaSource mediaSource;
        uri = Uri.parse(VIDEO_DONATION);

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





    @Override
    public void onPause() {
        super.onPause();

        if (mVideoView != null && mVideoView.getPlayer() != null){

            mVideoView.getPlayer().stop();

        }
    }


    @Override
    public void onDestroy() {
        super.onDestroy();

//        if (mVideoView != null && mVideoView.getPlayer() != null){
//
//            mVideoView.getPlayer().stop();
//
//        }

    }

    private MediaSource buildMediaSource(Uri uri) {
        return new ExtractorMediaSource.Factory(
                new DefaultHttpDataSourceFactory("exoplayer-codelab")).
                createMediaSource(uri);
    }





    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnDonationVideoDialogFragmentListener) {
            mListener = (OnDonationVideoDialogFragmentListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }


    public interface OnDonationVideoDialogFragmentListener {

        void onSkipVideoClicked();
    }
}
