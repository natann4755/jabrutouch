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
import co.il.model.model.MishnayotItem;
import co.il.sqlcore.DBHandler;
import static co.il.sqlcore.DBKeys.MISHNA_TABLE;




public class RecentMishnaAdapter extends RecyclerView.Adapter<RecentMishnaAdapter.ViewHolder> {

    private final String TAG = RecentGemaraAdapter.class.getSimpleName();
    private final List<MishnayotItem> recentMishnaItemList;
    private final RecentMishnaAdapterListener mListener;
    private final Context mContext;




    public RecentMishnaAdapter(Context context, List<MishnayotItem> recentMishnaItemList, RecentMishnaAdapterListener recentMishnaAdapterListener) {

        this.recentMishnaItemList = recentMishnaItemList;
        mListener = recentMishnaAdapterListener;
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
        return recentMishnaItemList == null ? 0 : recentMishnaItemList.size();
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


            final MishnayotItem mishnayotItem = recentMishnaItemList.get(position);

            mMasechetName.setText(mishnayotItem.getMasechetName() + " " + mishnayotItem.getChapter() + "-" + mishnayotItem.getMishna());

            if (mishnayotItem.getVideo() != null && mishnayotItem.getVideo().equals("")) {

                mVideoIcon.setVisibility(View.GONE);

            } else {

                mView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        setViewClickedAnimation(mView);

                        mListener.onVideoMishnaClicked(mishnayotItem);

                    }
                });


                mVideoIcon.setVisibility(View.VISIBLE);
                mVideoIcon.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        setImageClickedAnimation(mVideoIcon);
                        mListener.onVideoMishnaClicked(mishnayotItem);

                    }
                });

            }

            if (mishnayotItem.getAudio().equals("")) {

                mAudioIcon.setVisibility(View.GONE);

            } else {

                if (mishnayotItem.getVideo() != null && mishnayotItem.getVideo().equals("")) {

                    mView.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            setViewClickedAnimation(mView);

                            mListener.onAudioMishnaClicked(mishnayotItem);

                        }
                    });
                }

                mAudioIcon.setVisibility(View.VISIBLE);
                mAudioIcon.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        setImageClickedAnimation(mAudioIcon);

                        mListener.onAudioMishnaClicked(mishnayotItem);
                    }
                });
            }


            DBHandler dbHandler = new DBHandler(mContext);
            MishnayotItem dbMishnayotItem = dbHandler.findMishnaById(String.valueOf(mishnayotItem.getId()), MISHNA_TABLE);


            if (mishnayotItem.getTimeLine() > 0) {

                int currentProgress = (int) (TimeUnit.MILLISECONDS.toSeconds(mishnayotItem.getTimeLine()) * 100 / mishnayotItem.getDuration());
                mProgressLine.setProgress(currentProgress);

            } else if (dbMishnayotItem != null && dbMishnayotItem.getTimeLine() > 0) {

                int currentProgress = (int) (TimeUnit.MILLISECONDS.toSeconds(dbMishnayotItem.getTimeLine()) * 100 / dbMishnayotItem.getDuration());
                mProgressLine.setProgress(currentProgress);

            } else {

                mProgressLine.setProgress(0);
            }


            if (mishnayotItem.getHasNewMessageFromRabbi() > 0){

                mBudgetTV.setText(String.valueOf(mishnayotItem.getHasNewMessageFromRabbi()));

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





    public interface RecentMishnaAdapterListener {

        void onVideoMishnaClicked(MishnayotItem mishnayotItem);

        void onAudioMishnaClicked(MishnayotItem mishnayotItem);
    }


}
