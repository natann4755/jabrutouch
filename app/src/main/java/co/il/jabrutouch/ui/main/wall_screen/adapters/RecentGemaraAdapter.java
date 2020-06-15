package co.il.jabrutouch.ui.main.wall_screen.adapters;

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
import android.widget.ProgressBar;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.TimeUnit;
import co.il.jabrutouch.R;
import co.il.model.model.PagesItem;
import co.il.sqlcore.DBHandler;
import static co.il.sqlcore.DBKeys.GEMARA_GALLERY_TABLE;
import static co.il.sqlcore.DBKeys.GEMARA_TABLE;
import static co.il.sqlcore.DBKeys.GEMARA_VIDEO_PARTS_TABLE;


public class RecentGemaraAdapter extends RecyclerView.Adapter<RecentGemaraAdapter.ViewHolder> {


    private final String TAG = RecentGemaraAdapter.class.getSimpleName();
    private final List<PagesItem> mRecentGemataList;
    private final RecentGemaraAdapterListener mListener;
    private final Context mContext;


    public RecentGemaraAdapter(Context context, List<PagesItem> recentPagesItemList, RecentGemaraAdapterListener recentGemaraAdapterListener) {

        mRecentGemataList = recentPagesItemList;
        mListener = recentGemaraAdapterListener;
        mContext = context;
    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.page_item_for_recent, parent, false);
        return new ViewHolder(view);
    }


    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, final int position) {

        holder.updateItem(position);

    }


    @Override
    public int getItemCount() {
        return mRecentGemataList == null ? 0 : mRecentGemataList.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder {
        private final View mView;
        private final TextView mMasechetName;
        private final ImageView mAudioIcon;
        private final ImageView mVideoIcon;
        private final ProgressBar mProgressLine;
        private final TextView mBudgetTV;


        private ViewHolder(View view) {
            super(view);
            mView = view;
            mMasechetName = view.findViewById(R.id.PI_name_TV);
            mAudioIcon = view.findViewById(R.id.PI_audio_ic_IV);
            mVideoIcon = view.findViewById(R.id.PI_video_ic_IV);
            mProgressLine = view.findViewById(R.id.PI_progressbar);
            mBudgetTV = view.findViewById(R.id.PIFR_budget_TV);


        }


        @SuppressLint("SetTextI18n")
        private void updateItem(final int position) {

            final PagesItem pagesItem = mRecentGemataList.get(position);

            mMasechetName.setText(pagesItem.getMasechetName() + " " + pagesItem.getPageNumber());


            if (pagesItem.getVideo().equals("")) {

                mVideoIcon.setVisibility(View.GONE);

            } else {

                mView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        setViewClickedAnimation(mView);

                        mListener.onVideoClicked(pagesItem);

                    }
                });

                mVideoIcon.setVisibility(View.VISIBLE);
                mVideoIcon.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        setImageClickedAnimation(mVideoIcon);

                        mListener.onVideoClicked(pagesItem);

                    }
                });

            }

            if (pagesItem.getAudio().equals("")) {

                mAudioIcon.setVisibility(View.GONE);

            } else {

                if (pagesItem.getVideo().equals("")) {

                    mView.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {

                            setViewClickedAnimation(mView);
                            mListener.onAudioClicked(pagesItem);

                        }
                    });
                }

                mAudioIcon.setVisibility(View.VISIBLE);
                mAudioIcon.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        setImageClickedAnimation(mAudioIcon);

                        mListener.onAudioClicked(pagesItem);
                    }
                });
            }


            DBHandler dbHandler = new DBHandler(mContext);
            PagesItem dbPageItem = dbHandler.findGemaraById(String.valueOf(pagesItem.getId()), GEMARA_TABLE);


            if (dbPageItem != null) {
                dbPageItem.setVideoPart(dbHandler.getVideoPart(GEMARA_VIDEO_PARTS_TABLE, String.valueOf(pagesItem.getId())));
                dbPageItem.setGallery(dbHandler.getGallery(GEMARA_GALLERY_TABLE, String.valueOf(pagesItem.getId())));

            }


            if (pagesItem.getTimeLine() > 0) {

                int currentProgress = (int) (TimeUnit.MILLISECONDS.toSeconds(pagesItem.getTimeLine()) * 100 / pagesItem.getDuration());
                mProgressLine.setProgress(currentProgress);
                mRecentGemataList.get(position).setTimeLine(0);

            } else if (dbPageItem != null && dbPageItem.getTimeLine() > 0) {

                int currentProgress = (int) (TimeUnit.MILLISECONDS.toSeconds(dbPageItem.getTimeLine()) * 100 / dbPageItem.getDuration());
                mProgressLine.setProgress(currentProgress);

            } else {

                mProgressLine.setProgress(0);
            }

            if (pagesItem.getHasNewMessageFromRabbi() > 0){

                mBudgetTV.setText(String.valueOf(pagesItem.getHasNewMessageFromRabbi()));

            }else {

                mBudgetTV.setVisibility(View.GONE);
            }


        }


    }





    private void setViewClickedAnimation(View mView) {

        Animation animation = AnimationUtils.loadAnimation(mContext, R.anim.alpha);
        mView.startAnimation(animation);

    }






    /**
     * check if network available
     *
     * @return boolean
     */
    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) mContext.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = Objects.requireNonNull(connectivityManager).getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }






    private void setImageClickedAnimation(ImageView Icon) {

        Animation animation = AnimationUtils.loadAnimation(mContext, R.anim.alpha);
        Icon.startAnimation(animation);

    }




    public interface RecentGemaraAdapterListener {

        void onVideoClicked(PagesItem pagesItem);

        void onAudioClicked(PagesItem pagesItem);
    }


}
