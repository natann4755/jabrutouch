package co.il.jabrutouch.ui.main.mishna_screen.adapters;


import android.annotation.SuppressLint;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.dinuscxj.progressbar.CircleProgressBar;
import com.jcminarro.roundkornerlayout.RoundKornerLinearLayout;

import java.util.List;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

import co.il.jabrutouch.R;
import co.il.model.model.MishnayotItem;
import co.il.model.model.masechet_list_model.MasechetItem;
import co.il.sqlcore.DBHandler;

import static co.il.sqlcore.DBKeys.MISHNA_TABLE;


public class MishnaMediaAdapter extends RecyclerView.Adapter<MishnaMediaAdapter.ViewHolder> {

    private final String TAG = MishnaMediaAdapter.class.getSimpleName();
    private final MishnaMediaAdapterListener mListener;
    private List<MishnayotItem> mMishnaiotList;
    private Context mContext;
    private MasechetItem mMashechetItem;
    private int mSederOrder;


    public MishnaMediaAdapter(Context context, MasechetItem mMasechetItem, List<MishnayotItem> mishnaiotList, int sederOrder, MishnaMediaAdapterListener mishnaDownloadAdapterListener) {

        mMishnaiotList = mishnaiotList;
        mListener = mishnaDownloadAdapterListener;
        mMashechetItem = mMasechetItem;
        mSederOrder = sederOrder;
        mContext = context;
    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.page_chapter_download_item, parent, false);
        return new ViewHolder(view);
    }


    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, final int position) {

        holder.updateItem(position);


    }


    @Override
    public int getItemCount() {

        return mMishnaiotList.size();
    }


    /**
     * on download mode
     */
    public void onDownloadMode() {


        for (int i = 0; i < mMishnaiotList.size(); i++) {
            mMishnaiotList.get(i).setDownMode(true);
        }

        notifyDataSetChanged();

    }


    /**
     * on done mode
     */
    public void onDoneMode() {

        for (int i = 0; i < mMishnaiotList.size(); i++) {
            mMishnaiotList.get(i).setDownMode(false);
        }

        notifyDataSetChanged();

    }


    class ViewHolder extends RecyclerView.ViewHolder {
        private final View mView;
        private final RoundKornerLinearLayout mDownModeLL;
        private final RoundKornerLinearLayout mNotDownModeLL;
        private final TextView mMishnaNumber;
        private final TextView mMishnaNumberDownMode;
        private final TextView mMishnaDuration;
        private final TextView mMishnaDurationDownMode;
        private final ImageView mMishnaAudioIcon;
        private final ImageView mMishnaAudioIconDownMode;
        private final ImageView mMishnaVideoIcon;
        private final ImageView mMishnaVideoIconDownMode;
        private final LinearLayout mDownloadLL;
        private final LinearLayout mDurationLL;
        private final CircleProgressBar mdownloadProgress;
        private final CircleProgressBar mdownloadProgressAudioDoneMode;
        private final CircleProgressBar mdownloadProgressVideoDoneMode;
        private final LinearLayout mAudioIconLL;
        private final LinearLayout mDurationDoneModeLL;
        private final LinearLayout mdownAudioIconLL;
        private final LinearLayout mdownloadVideoProgressLL;
        private final LinearLayout mdownVideoIconLL;
        private final CircleProgressBar mdownloadVideoProgress;
        private final ProgressBar mProgressLineDownMode;
        private ProgressBar mProgressLine;


        private ViewHolder(View view) {
            super(view);
            mView = view;
            mDownModeLL = view.findViewById(R.id.PCDI_down_mode_RLL);
            mNotDownModeLL = view.findViewById(R.id.PCDI_not_down_mode_RLL);
            mMishnaNumber = view.findViewById(R.id.MDA_mishna_name_TV);
            mMishnaDuration = view.findViewById(R.id.MDA_mishna_duration_TV);
            mMishnaAudioIcon = view.findViewById(R.id.MDA_mishna_audio_IV);
            mMishnaVideoIcon = view.findViewById(R.id.MDA_mishna_video_IV);
            mMishnaNumberDownMode = view.findViewById(R.id.MDA_mishna_name_down_mode_TV);
            mMishnaDurationDownMode = view.findViewById(R.id.MDA_mishna_duration_down_mode_TV);
            mMishnaAudioIconDownMode = view.findViewById(R.id.MDA_mishna_audio_down_mode_IV);
            mMishnaVideoIconDownMode = view.findViewById(R.id.MDA_mishna_video_down_mode_IV);
            mDurationDoneModeLL = view.findViewById(R.id.PCDI_duration_done_mode_LL);
            mDownloadLL = view.findViewById(R.id.PCDI_download_LL);
            mDurationLL = view.findViewById(R.id.PCDI_duration_LL);
            mdownloadProgress = view.findViewById(R.id.PCDI_progress_bar);
            mdownloadProgressAudioDoneMode = view.findViewById(R.id.PCDI_progress_bar_done_mode);
            mdownloadProgressVideoDoneMode = view.findViewById(R.id.PCDI_progress_bar_for_video_done_mode);
            mdownAudioIconLL = view.findViewById(R.id.MDA_down_audio_LL);
            mdownloadVideoProgressLL = view.findViewById(R.id.PCDI_download_video_LL);
            mdownVideoIconLL = view.findViewById(R.id.MDA_down_video_LL);
            mdownloadVideoProgress = view.findViewById(R.id.PCDI_progress_bar_video);
            mProgressLine = view.findViewById(R.id.AS_progressbar);
            mProgressLineDownMode = view.findViewById(R.id.AS_progressbar_down_mode);
            mAudioIconLL = view.findViewById(R.id.PCDI_audio_icon_LL);

        }


        @SuppressLint("SetTextI18n")
        private void updateItem(final int position) {


            final MishnayotItem mishnayotItem = mMishnaiotList.get(position);

            mMishnaNumber.setText(String.valueOf(mishnayotItem.getMishna()));
            mMishnaNumberDownMode.setText(String.valueOf(mishnayotItem.getMishna()));
            mMishnaDuration.setText(TimeUnit.SECONDS.toMinutes(mishnayotItem.getDuration()) + " " + mContext.getResources().getString(R.string.min));
            mMishnaDurationDownMode.setText(TimeUnit.SECONDS.toMinutes(mishnayotItem.getDuration()) + " " + mContext.getResources().getString(R.string.min));




            if (mishnayotItem.getAudio().length() == 0) {

                mMishnaAudioIcon.setVisibility(View.GONE);
                mMishnaAudioIconDownMode.setVisibility(View.GONE);

            } else {

                mMishnaAudioIcon.setVisibility(View.VISIBLE);
                mMishnaAudioIconDownMode.setVisibility(View.VISIBLE);

            }

            if (mishnayotItem.getVideo() == null || (mishnayotItem.getVideo() != null && mishnayotItem.getVideo().length() == 0)) {

                mMishnaVideoIcon.setVisibility(View.GONE);
                mMishnaVideoIconDownMode.setVisibility(View.GONE);

            } else {

                mMishnaVideoIcon.setVisibility(View.VISIBLE);
                mMishnaVideoIconDownMode.setVisibility(View.VISIBLE);

            }


            if (!mishnayotItem.isDownMode()) {
                mNotDownModeLL.setVisibility(View.VISIBLE);
                mDownModeLL.setVisibility(View.GONE);
            } else {

                mNotDownModeLL.setVisibility(View.GONE);
                mDownModeLL.setVisibility(View.VISIBLE);
            }




            mView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    if (mishnayotItem.getVideo().length() > 0) {

                        setViewClickedAnimation(mView);

                        mishnayotItem.setMasechetOrder(mMashechetItem.getOrder());
                        mishnayotItem.setSederOrder(mSederOrder);
                        mishnayotItem.setMasechetName(mMashechetItem.getName());

                        mishnayotItem.setPosition(position);
                        mListener.onVideoMishnaClicked(mishnayotItem);

                    } else if (mishnayotItem.getAudio().length() > 0) {

                        setViewClickedAnimation(mView);
                        mishnayotItem.setMasechetOrder(mMashechetItem.getOrder());
                        mishnayotItem.setSederOrder(mSederOrder);
                        mishnayotItem.setMasechetName(mMashechetItem.getName());

                        mishnayotItem.setPosition(position);
                        mListener.onAudioMishnaClicked(mishnayotItem);

                    }

                }
            });


            mMishnaAudioIcon.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    setImageClickedAnimation(mMishnaAudioIcon);

                    mishnayotItem.setMasechetOrder(mMashechetItem.getOrder());
                    mishnayotItem.setSederOrder(mSederOrder);
                    mishnayotItem.setMasechetName(mMashechetItem.getName());
                    mishnayotItem.setPosition(position);

                    mListener.onAudioMishnaClicked(mishnayotItem);

                }
            });

            mMishnaVideoIcon.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    setImageClickedAnimation(mMishnaVideoIcon);

                    mishnayotItem.setMasechetOrder(mMashechetItem.getOrder());
                    mishnayotItem.setSederOrder(mSederOrder);
                    mishnayotItem.setMasechetName(mMashechetItem.getName());
                    mishnayotItem.setPosition(position);

                    mListener.onVideoMishnaClicked(mishnayotItem);

                }
            });


            if (mishnayotItem.getAudioProgress() != -1 && mishnayotItem.getAudioProgress() != 100) {

                mMishnaAudioIcon.setVisibility(View.GONE);
                mdownloadProgressAudioDoneMode.setVisibility(View.VISIBLE);

                mDownloadLL.setVisibility(View.VISIBLE);
                mdownAudioIconLL.setVisibility(View.GONE);

                mdownloadProgressAudioDoneMode.setProgress(mishnayotItem.getAudioProgress());
                mdownloadProgress.setProgress(mishnayotItem.getAudioProgress());

            } else if (mishnayotItem.getAudioProgress() == 100) {

                mMishnaAudioIcon.setVisibility(View.VISIBLE);
                mdownloadProgressAudioDoneMode.setVisibility(View.GONE);

                mDownloadLL.setVisibility(View.GONE);
                mdownAudioIconLL.setVisibility(View.VISIBLE);

                mdownloadProgressAudioDoneMode.setProgress(mishnayotItem.getAudioProgress());
                mdownloadProgress.setProgress(mishnayotItem.getAudioProgress());
            } else {

                mMishnaAudioIcon.setVisibility(View.VISIBLE);
                mdownloadProgressAudioDoneMode.setVisibility(View.GONE);

                mDownloadLL.setVisibility(View.GONE);
                mdownAudioIconLL.setVisibility(View.VISIBLE);

                mdownloadProgressAudioDoneMode.setProgress(mishnayotItem.getAudioProgress());
                mdownloadProgress.setProgress(mishnayotItem.getAudioProgress());

            }


            if (mishnayotItem.getVideoProgress() != -1 && mishnayotItem.getVideoProgress() != 100) {

                mMishnaVideoIcon.setVisibility(View.GONE);
                mdownloadProgressVideoDoneMode.setVisibility(View.VISIBLE);

                mdownloadVideoProgressLL.setVisibility(View.VISIBLE);
                mdownVideoIconLL.setVisibility(View.GONE);

                mdownloadProgressVideoDoneMode.setProgress(mishnayotItem.getVideoProgress());
                mdownloadVideoProgress.setProgress(mishnayotItem.getVideoProgress());

            } else if (mishnayotItem.getVideoProgress() == 100) {

                mMishnaVideoIcon.setVisibility(View.VISIBLE);
                mdownloadProgressVideoDoneMode.setVisibility(View.GONE);

                mdownloadVideoProgressLL.setVisibility(View.GONE);
                mdownVideoIconLL.setVisibility(View.VISIBLE);

                mdownloadProgressVideoDoneMode.setProgress(mishnayotItem.getVideoProgress());
                mdownloadVideoProgress.setProgress(mishnayotItem.getVideoProgress());
            } else {


                mMishnaVideoIcon.setVisibility(View.VISIBLE);
                mdownloadProgressVideoDoneMode.setVisibility(View.GONE);

                mdownloadProgressVideoDoneMode.setProgress(mishnayotItem.getVideoProgress());
                mdownloadVideoProgress.setProgress(mishnayotItem.getVideoProgress());

                mdownloadVideoProgressLL.setVisibility(View.GONE);
                mdownVideoIconLL.setVisibility(View.VISIBLE);


            }


            DBHandler dbHandler = new DBHandler(mContext);

            MishnayotItem dbMishnaItem = dbHandler.findMishnaById(String.valueOf(mishnayotItem.getId()), MISHNA_TABLE);

            if (mishnayotItem.getTimeLine() > 0) {

                int currentProgress = (int) (TimeUnit.MILLISECONDS.toSeconds(mishnayotItem.getTimeLine()) * 100 / mishnayotItem.getDuration());
                mProgressLine.setProgress(currentProgress);
                mProgressLineDownMode.setProgress(currentProgress);

            } else if (dbMishnaItem != null && dbMishnaItem.getTimeLine() > 0) {

                int currentProgress = (int) (TimeUnit.MILLISECONDS.toSeconds(dbMishnaItem.getTimeLine()) * 100 / dbMishnaItem.getDuration());
                mProgressLine.setProgress(currentProgress);
                mProgressLineDownMode.setProgress(currentProgress);

            } else {

                mProgressLine.setProgress(0);
                mProgressLineDownMode.setProgress(0);
            }


            if (dbMishnaItem != null && !dbMishnaItem.getAudio().equals("")) {

                mMishnaAudioIcon.setBackground(ContextCompat.getDrawable(Objects.requireNonNull(mContext), R.drawable.audio2_downloaded));
                mMishnaAudioIconDownMode.setBackground(ContextCompat.getDrawable(Objects.requireNonNull(mContext), R.drawable.audio2_downloaded));
                mMishnaAudioIconDownMode.setOnClickListener(null);

            } else {

                mMishnaAudioIcon.setBackground(ContextCompat.getDrawable(Objects.requireNonNull(mContext), R.drawable.ic_audio_selector));
                mMishnaAudioIconDownMode.setBackground(ContextCompat.getDrawable(Objects.requireNonNull(mContext), R.drawable.ic_audio_white));


                mMishnaAudioIconDownMode.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {


                        if (!isNetworkAvailable()) {

                            Toast.makeText(mContext, mContext.getResources().getString(R.string.offline), Toast.LENGTH_LONG).show();


                        } else {
                            setImageClickedAnimation(mMishnaAudioIconDownMode);

                            mishnayotItem.setMasechetOrder(mMashechetItem.getOrder());
                            mishnayotItem.setSederOrder(mSederOrder);
                            mishnayotItem.setMasechetName(mMashechetItem.getName());

                            mMishnaAudioIcon.setVisibility(View.GONE);
                            mdownloadProgressAudioDoneMode.setVisibility(View.VISIBLE);

                            mDownloadLL.setVisibility(View.VISIBLE);
                            mdownAudioIconLL.setVisibility(View.GONE);

                            mishnayotItem.setAudioProgress(0);
                            mdownloadProgressAudioDoneMode.setProgress(mishnayotItem.getAudioProgress());
                            mdownloadProgress.setProgress(mishnayotItem.getAudioProgress());


                            mListener.onMishnaAudioDownloadClicked(mishnayotItem, position);
                        }
                    }
                });
            }

            if (dbMishnaItem != null && !dbMishnaItem.getVideo().equals("")) {

                mMishnaVideoIcon.setBackground(ContextCompat.getDrawable(Objects.requireNonNull(mContext), R.drawable.video2_downloaded));
                mMishnaVideoIconDownMode.setBackground(ContextCompat.getDrawable(Objects.requireNonNull(mContext), R.drawable.video2_downloaded));
                mMishnaVideoIconDownMode.setOnClickListener(null);

            } else {

                mMishnaVideoIcon.setBackground(ContextCompat.getDrawable(Objects.requireNonNull(mContext), R.drawable.ic_video_selector));
                mMishnaVideoIconDownMode.setBackground(ContextCompat.getDrawable(Objects.requireNonNull(mContext), R.drawable.ic_video_white));


                mMishnaVideoIconDownMode.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        if (!isNetworkAvailable()) {

                            Toast.makeText(mContext, mContext.getResources().getString(R.string.offline), Toast.LENGTH_LONG).show();


                        } else {

                            setImageClickedAnimation(mMishnaVideoIconDownMode);

                            mishnayotItem.setMasechetOrder(mMashechetItem.getOrder());
                            mishnayotItem.setSederOrder(mSederOrder);
                            mishnayotItem.setMasechetName(mMashechetItem.getName());

                            mMishnaVideoIcon.setVisibility(View.GONE);
                            mdownloadProgressVideoDoneMode.setVisibility(View.VISIBLE);


                            mdownloadVideoProgressLL.setVisibility(View.VISIBLE);
                            mdownVideoIconLL.setVisibility(View.GONE);

                            mishnayotItem.setVideoProgress(0);
                            mdownloadProgressAudioDoneMode.setProgress(mishnayotItem.getVideoProgress());
                            mdownloadVideoProgress.setProgress(mishnayotItem.getVideoProgress());


                            mListener.onMishnaVideoDownloadClicked(mishnayotItem, position);
                        }
                    }
                });
            }



        }


    }


    /**
     * check if network available
     *
     * @return boolean
     */
    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) Objects.requireNonNull(mContext).getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = Objects.requireNonNull(connectivityManager).getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }


    private void setViewClickedAnimation(View mView) {

        Animation animation = AnimationUtils.loadAnimation(mContext, R.anim.alpha);
        mView.startAnimation(animation);

    }


    private void setImageClickedAnimation(ImageView Icon) {

        Animation animation = AnimationUtils.loadAnimation(mContext, R.anim.alpha);
        Icon.startAnimation(animation);

    }


    public interface MishnaMediaAdapterListener {

        void onMishnaAudioDownloadClicked(MishnayotItem mishnayotItem, int position);

        void startProgress();

        void onAudioMishnaClicked(MishnayotItem mishnayotItem);

        void onVideoMishnaClicked(MishnayotItem mishnayotItem);

        void onMishnaVideoDownloadClicked(MishnayotItem mishnayotItem, int position);


    }


}
