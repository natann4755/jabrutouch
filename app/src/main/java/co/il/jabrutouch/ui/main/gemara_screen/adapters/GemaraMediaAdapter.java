package co.il.jabrutouch.ui.main.gemara_screen.adapters;


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
import co.il.model.model.PagesItem;
import co.il.model.model.masechet_list_model.MasechetItem;
import co.il.sqlcore.DBHandler;

import static co.il.sqlcore.DBKeys.GEMARA_TABLE;


public class GemaraMediaAdapter extends RecyclerView.Adapter<GemaraMediaAdapter.ViewHolder> {

    private final String TAG = GemaraMediaAdapter.class.getSimpleName();
    private final GemaraMediaAdapterListener mListener;
    private final MasechetItem mMashechetItem;
    private final int mSederOrder;
    private List<PagesItem> mPagesList;
    private Context mContext;


    public GemaraMediaAdapter(Context context, List<PagesItem> pagesList, MasechetItem masechetItem, int sederOrder, GemaraMediaAdapterListener gemaraDownloadAdapterListener) {

        mPagesList = pagesList;
        mListener = gemaraDownloadAdapterListener;
        mMashechetItem = masechetItem;
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

        return mPagesList.size();
    }


    /**
     * on download mode
     */
    public void onDownloadMode() {


        for (int i = 0; i < mPagesList.size(); i++) {
            mPagesList.get(i).setDownMode(true);
        }

        notifyDataSetChanged();

    }


    /**
     * on done mode
     */
    public void onDoneMode() {


        for (int i = 0; i < mPagesList.size(); i++) {
            mPagesList.get(i).setDownMode(false);
        }

        notifyDataSetChanged();


    }


    class ViewHolder extends RecyclerView.ViewHolder {

        private final View mView;
        private final RoundKornerLinearLayout mDownModeLL;
        private final RoundKornerLinearLayout mNotDownModeLL;
        private final TextView mPageNumber;
        private final TextView mPageNumberDownMode;
        private final TextView mPageDuration;
        private final TextView mPageDurationDownMode;
        private final ImageView mPageAudioIcon;
        private final ImageView mPageAudioIconDownMode;
        private final ImageView mPageVideoIcon;
        private final ImageView mPageVideoIconDownMode;
        private final LinearLayout mDownloadLL;
        private final LinearLayout mDurationLL;
        private final CircleProgressBar mdownloadProgress;
        private final CircleProgressBar mdownloadVideoProgress;
        private final CircleProgressBar mdownloadProgressAudioDoneMode;
        private final CircleProgressBar mdownloadProgressVideoDoneMode;
        private final LinearLayout mDurationDoneModeLL;
        private final LinearLayout mdownAudioIconLL;
        private final LinearLayout mdownVideoIconLL;
        private final LinearLayout mdownloadVideoProgressLL;
        private final ProgressBar mProgressLine;
        private final ProgressBar mProgressLineDownMode;


        private ViewHolder(View view) {
            super(view);
            mView = view;
            mDownModeLL = view.findViewById(R.id.PCDI_down_mode_RLL);
            mNotDownModeLL = view.findViewById(R.id.PCDI_not_down_mode_RLL);
//            mProgressDoneModeLL = view.findViewById(R.id.PCDI_progress_done_mode_LL);
            mDurationDoneModeLL = view.findViewById(R.id.PCDI_duration_done_mode_LL);
            mPageNumber = view.findViewById(R.id.MDA_mishna_name_TV);
            mPageDuration = view.findViewById(R.id.MDA_mishna_duration_TV);
            mPageAudioIcon = view.findViewById(R.id.MDA_mishna_audio_IV);
            mPageVideoIcon = view.findViewById(R.id.MDA_mishna_video_IV);
            mPageNumberDownMode = view.findViewById(R.id.MDA_mishna_name_down_mode_TV);
            mPageDurationDownMode = view.findViewById(R.id.MDA_mishna_duration_down_mode_TV);
            mPageAudioIconDownMode = view.findViewById(R.id.MDA_mishna_audio_down_mode_IV);
            mPageVideoIconDownMode = view.findViewById(R.id.MDA_mishna_video_down_mode_IV);
            mDownloadLL = view.findViewById(R.id.PCDI_download_LL);
            mDurationLL = view.findViewById(R.id.PCDI_duration_LL);
            mdownloadProgress = view.findViewById(R.id.PCDI_progress_bar);
            mdownloadVideoProgress = view.findViewById(R.id.PCDI_progress_bar_video);
            mdownloadVideoProgressLL = view.findViewById(R.id.PCDI_download_video_LL);
            mdownloadProgressAudioDoneMode = view.findViewById(R.id.PCDI_progress_bar_done_mode);
            mdownloadProgressVideoDoneMode = view.findViewById(R.id.PCDI_progress_bar_for_video_done_mode);
            mdownAudioIconLL = view.findViewById(R.id.MDA_down_audio_LL);
            mdownVideoIconLL = view.findViewById(R.id.MDA_down_video_LL);
            mProgressLine = view.findViewById(R.id.AS_progressbar);
            mProgressLineDownMode = view.findViewById(R.id.AS_progressbar_down_mode);

        }


        @SuppressLint("SetTextI18n")
        private void updateItem(final int position) {

            final PagesItem pagesItem = mPagesList.get(position);
            pagesItem.setMasechetName(mMashechetItem.getName());

            DBHandler dbHandler = new DBHandler(mContext);


            if (pagesItem.getAudio().length() == 0) {

                mPageAudioIcon.setVisibility(View.GONE);
                mPageAudioIconDownMode.setVisibility(View.GONE);

            } else {

                mPageAudioIcon.setVisibility(View.VISIBLE);
                mPageAudioIconDownMode.setVisibility(View.VISIBLE);


            }


            if (pagesItem.getVideo().length() == 0) {

                mPageVideoIcon.setVisibility(View.GONE);
                mPageVideoIconDownMode.setVisibility(View.GONE);

            } else {

                mPageVideoIcon.setVisibility(View.VISIBLE);
                mPageVideoIconDownMode.setVisibility(View.VISIBLE);

            }


            mView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    if (pagesItem.getVideo().length() > 0) {

                        setViewClickedAnimation(mView);


                        pagesItem.setMasechetOrder(mMashechetItem.getOrder());
                        pagesItem.setSederOrder(mSederOrder);
                        pagesItem.setMasechetName(mMashechetItem.getName());
                        pagesItem.setPosition(position);
                        mListener.onVideoClicked(pagesItem);

                    } else {


                        if (pagesItem.getAudio().length() > 0) {

                            setViewClickedAnimation(mView);

                            pagesItem.setMasechetOrder(mMashechetItem.getOrder());
                            pagesItem.setSederOrder(mSederOrder);
                            pagesItem.setMasechetName(mMashechetItem.getName());
                            pagesItem.setPosition(position);
                            mListener.onAudioClicked(pagesItem);
                        }


                    }

                }
            });


            mPageAudioIcon.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    setImageClickedAnimation(mPageAudioIcon);

                    pagesItem.setMasechetOrder(mMashechetItem.getOrder());
                    pagesItem.setSederOrder(mSederOrder);
                    pagesItem.setMasechetName(mMashechetItem.getName());
                    pagesItem.setPosition(position);
                    mListener.onAudioClicked(pagesItem);

                }
            });


            mPageVideoIcon.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    setImageClickedAnimation(mPageVideoIcon);

                    pagesItem.setMasechetOrder(mMashechetItem.getOrder());
                    pagesItem.setSederOrder(mSederOrder);
                    pagesItem.setMasechetName(mMashechetItem.getName());
                    pagesItem.setPosition(position);
                    mListener.onVideoClicked(pagesItem);

                }
            });


            if (pagesItem.getAudioProgress() != -1 && pagesItem.getAudioProgress() != 100) {

                mPageAudioIcon.setVisibility(View.GONE);
                mdownloadProgressAudioDoneMode.setVisibility(View.VISIBLE);

                mDownloadLL.setVisibility(View.VISIBLE);
                mdownAudioIconLL.setVisibility(View.GONE);

                mdownloadProgressAudioDoneMode.setProgress(pagesItem.getAudioProgress());
                mdownloadProgress.setProgress(pagesItem.getAudioProgress());

            } else if (pagesItem.getAudioProgress() == 100) {

                mPageAudioIcon.setVisibility(View.VISIBLE);
                mdownloadProgressAudioDoneMode.setVisibility(View.GONE);

                mDownloadLL.setVisibility(View.GONE);
                mdownAudioIconLL.setVisibility(View.VISIBLE);

                mdownloadProgressAudioDoneMode.setProgress(pagesItem.getAudioProgress());
                mdownloadProgress.setProgress(pagesItem.getAudioProgress());
            } else {

                mPageAudioIcon.setVisibility(View.VISIBLE);
                mdownloadProgressAudioDoneMode.setVisibility(View.GONE);


                mDownloadLL.setVisibility(View.GONE);
                mdownAudioIconLL.setVisibility(View.VISIBLE);

                mdownloadProgressAudioDoneMode.setProgress(pagesItem.getAudioProgress());
                mdownloadProgress.setProgress(pagesItem.getAudioProgress());

            }


            if (pagesItem.getVideoProgress() != -1 && pagesItem.getVideoProgress() != 100) {

                mPageVideoIcon.setVisibility(View.GONE);
                mdownloadProgressVideoDoneMode.setVisibility(View.VISIBLE);

                mdownloadVideoProgressLL.setVisibility(View.VISIBLE);
                mdownVideoIconLL.setVisibility(View.GONE);

                mdownloadProgressVideoDoneMode.setProgress(pagesItem.getVideoProgress());
                mdownloadVideoProgress.setProgress(pagesItem.getVideoProgress());

            } else if (pagesItem.getVideoProgress() == 100) {

                mPageVideoIcon.setVisibility(View.VISIBLE);
                mdownloadProgressVideoDoneMode.setVisibility(View.GONE);

                mdownloadVideoProgressLL.setVisibility(View.GONE);
                mdownVideoIconLL.setVisibility(View.VISIBLE);

                mdownloadProgressVideoDoneMode.setProgress(pagesItem.getVideoProgress());
                mdownloadVideoProgress.setProgress(pagesItem.getVideoProgress());
            } else {


                mPageVideoIcon.setVisibility(View.VISIBLE);
                mdownloadProgressVideoDoneMode.setVisibility(View.GONE);

                mdownloadProgressVideoDoneMode.setProgress(pagesItem.getVideoProgress());
                mdownloadVideoProgress.setProgress(pagesItem.getVideoProgress());

                mdownloadVideoProgressLL.setVisibility(View.GONE);
                mdownVideoIconLL.setVisibility(View.VISIBLE);


            }


            PagesItem dbPageItem = dbHandler.findGemaraById(String.valueOf(pagesItem.getId()), GEMARA_TABLE);

            if (pagesItem.getTimeLine() > 0) {

                int currentProgress = (int) (TimeUnit.MILLISECONDS.toSeconds(pagesItem.getTimeLine()) * 100 / pagesItem.getDuration());
                mProgressLine.setProgress(currentProgress);
                mProgressLineDownMode.setProgress(currentProgress);

            } else if (dbPageItem != null && dbPageItem.getTimeLine() > 0) {

                int currentProgress = (int) (TimeUnit.MILLISECONDS.toSeconds(dbPageItem.getTimeLine()) * 100 / dbPageItem.getDuration());
                mProgressLine.setProgress(currentProgress);
                mProgressLineDownMode.setProgress(currentProgress);

            } else {

                mProgressLine.setProgress(0);
                mProgressLineDownMode.setProgress(0);
            }


            if (dbPageItem != null && !dbPageItem.getAudio().equals("")) {

                mPageAudioIcon.setBackground(ContextCompat.getDrawable(Objects.requireNonNull(mContext), R.drawable.audio2_downloaded));
                mPageAudioIconDownMode.setBackground(ContextCompat.getDrawable(Objects.requireNonNull(mContext), R.drawable.audio2_downloaded));
                mPageAudioIconDownMode.setOnClickListener(null);


            } else {

                mPageAudioIcon.setBackground(ContextCompat.getDrawable(Objects.requireNonNull(mContext), R.drawable.ic_audio_selector));
                mPageAudioIconDownMode.setBackground(ContextCompat.getDrawable(Objects.requireNonNull(mContext), R.drawable.ic_audio_white));


                mPageAudioIconDownMode.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        if (!isNetworkAvailable()) {

                            Toast.makeText(mContext, mContext.getResources().getString(R.string.offline), Toast.LENGTH_LONG).show();


                        } else {

                            setImageClickedAnimation(mPageAudioIconDownMode);

                            pagesItem.setMasechetOrder(mMashechetItem.getOrder());
                            pagesItem.setSederOrder(mSederOrder);
                            pagesItem.setMasechetName(mMashechetItem.getName());


                            mPageAudioIcon.setVisibility(View.GONE);
                            mdownloadProgressAudioDoneMode.setVisibility(View.VISIBLE);

                            mDownloadLL.setVisibility(View.VISIBLE);
                            mdownAudioIconLL.setVisibility(View.GONE);

                            pagesItem.setAudioProgress(0);
                            mdownloadProgressAudioDoneMode.setProgress(pagesItem.getAudioProgress());
                            mdownloadProgress.setProgress(pagesItem.getAudioProgress());

                            mListener.onAudioDownloadClicked(pagesItem, position);
                        }


                    }
                });
            }

            if (dbPageItem != null && !dbPageItem.getVideo().equals("")) {

                mPageVideoIcon.setBackground(ContextCompat.getDrawable(Objects.requireNonNull(mContext), R.drawable.video2_downloaded));
                mPageVideoIconDownMode.setBackground(ContextCompat.getDrawable(Objects.requireNonNull(mContext), R.drawable.video2_downloaded));
                mPageVideoIconDownMode.setOnClickListener(null);

            } else {

                mPageVideoIcon.setBackground(ContextCompat.getDrawable(Objects.requireNonNull(mContext), R.drawable.ic_video_selector));
                mPageVideoIconDownMode.setBackground(ContextCompat.getDrawable(Objects.requireNonNull(mContext), R.drawable.ic_video_white));

                mPageVideoIconDownMode.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        if (!isNetworkAvailable()) {

                            Toast.makeText(mContext, mContext.getResources().getString(R.string.offline), Toast.LENGTH_LONG).show();


                        } else {


                            setImageClickedAnimation(mPageVideoIconDownMode);

                            pagesItem.setMasechetOrder(mMashechetItem.getOrder());
                            pagesItem.setSederOrder(mSederOrder);
                            pagesItem.setMasechetName(mMashechetItem.getName());


                            mPageVideoIcon.setVisibility(View.GONE);
                            mdownloadProgressVideoDoneMode.setVisibility(View.VISIBLE);

                            mdownloadVideoProgressLL.setVisibility(View.VISIBLE);
                            mdownVideoIconLL.setVisibility(View.GONE);

                            pagesItem.setVideoProgress(0);
                            mdownloadProgressAudioDoneMode.setProgress(pagesItem.getVideoProgress());
                            mdownloadVideoProgress.setProgress(pagesItem.getVideoProgress());

                            mListener.onVideoDownloadClicked(pagesItem, position);
                        }
                    }
                });

            }


            mPageNumber.setText(String.valueOf(pagesItem.getPageNumber()));
            mPageNumberDownMode.setText(String.valueOf(pagesItem.getPageNumber()));
            mPageDuration.setText(TimeUnit.SECONDS.toMinutes(pagesItem.getDuration()) + " " + mContext.getResources().getString(R.string.min));
            mPageDurationDownMode.setText(TimeUnit.SECONDS.toMinutes(pagesItem.getDuration()) + " " + mContext.getResources().getString(R.string.min));


            if (!pagesItem.isDownMode()) {
                mNotDownModeLL.setVisibility(View.VISIBLE);
                mDownModeLL.setVisibility(View.GONE);
            } else {

                mNotDownModeLL.setVisibility(View.GONE);
                mDownModeLL.setVisibility(View.VISIBLE);

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


    public interface GemaraMediaAdapterListener {

        void onAudioDownloadClicked(PagesItem pagesItem, int position);

        void onAudioClicked(PagesItem pagesItem);

        void onVideoClicked(PagesItem pagesItem);

        void onVideoDownloadClicked(PagesItem pagesItem, int position);

    }


}
