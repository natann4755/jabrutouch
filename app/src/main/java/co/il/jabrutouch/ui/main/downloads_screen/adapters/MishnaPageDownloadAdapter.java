package co.il.jabrutouch.ui.main.downloads_screen.adapters;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;
import java.util.concurrent.TimeUnit;

import co.il.jabrutouch.R;
import co.il.model.model.MishnayotItem;
import co.il.sqlcore.DBHandler;

import static co.il.sqlcore.DBKeys.MISHNA_TABLE;


public class MishnaPageDownloadAdapter extends RecyclerView.Adapter<MishnaPageDownloadAdapter.ViewHolder> {

    private final String TAG = GemaraPageDownloadAdapter.class.getSimpleName();
    private final MishnaPageDownloadAdapterListener mListener;
    private final List<MishnayotItem> mPages;
    private final int mParentPosition;
    private Context mContext;


    public MishnaPageDownloadAdapter(Context context, List<MishnayotItem> dataBaseObjects, int position, MishnaPageDownloadAdapterListener mishnaPageDownloadAdapterListener) {


        mListener = mishnaPageDownloadAdapterListener;
        mContext = context;
        mPages = dataBaseObjects;
        mParentPosition = position;
    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.download_page_item, parent, false);
        return new ViewHolder(view);
    }


    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, final int position) {

        holder.updateItem(position);

    }


    @Override
    public int getItemCount() {

        return mPages.size();
    }


    class ViewHolder extends RecyclerView.ViewHolder {
        private final View mView;
        private final TextView mPageName;
        private final TextView mPageDuration;
        private final ImageView mAudioIconIV;
        private final ImageView mVideoIconIV;
        private ProgressBar mProgressLine;
        private final LinearLayout mDeleteMode;


        private ViewHolder(View view) {
            super(view);
            mView = view;
            mPageName = view.findViewById(R.id.DPI_mishna_name_TV);
            mPageDuration = view.findViewById(R.id.DPI_mishna_duration_TV);
            mAudioIconIV = view.findViewById(R.id.DPI_mishna_audio_IV);
            mVideoIconIV = view.findViewById(R.id.DPI_mishna_video_IV);
            mProgressLine = view.findViewById(R.id.AS_progressbar);
            mDeleteMode = view.findViewById(R.id.DPI_delete_btn_LL);


        }


        @SuppressLint("SetTextI18n")
        private void updateItem(final int position) {

            final MishnayotItem mishnayotItem = mPages.get(position);

            mPageName.setText(mishnayotItem.getChapter() + "-" + mishnayotItem.getMishna());
            mPageDuration.setText(TimeUnit.SECONDS.toMinutes(mishnayotItem.getDuration()) + " " + mContext.getResources().getString(R.string.min));

            if (mishnayotItem.getVideo().equals("")) {

                mVideoIconIV.setVisibility(View.GONE);

            } else {

                mView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        setViewClickedAnimation(mView);

                        mListener.onVideoClicked(mishnayotItem);

                    }
                });


                mVideoIconIV.setVisibility(View.VISIBLE);
                mVideoIconIV.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        setImageClickedAnimation(mVideoIconIV);

                        mListener.onVideoClicked(mishnayotItem);

                    }
                });



            }

            final DBHandler dbHandler = new DBHandler(mContext);
            MishnayotItem dbMishnayotItem = dbHandler.findMishnaById(String.valueOf(mishnayotItem.getId()), MISHNA_TABLE);

            if (mishnayotItem.getTimeLine() > 0) {

                int currentProgress = (int) (TimeUnit.MILLISECONDS.toSeconds(mishnayotItem.getTimeLine()) * 100 / mishnayotItem.getDuration());
                mProgressLine.setProgress(currentProgress);
                mishnayotItem.setTimeLine(0);
            } else if (dbMishnayotItem.getTimeLine() > 0) {

                int currentProgress = (int) (TimeUnit.MILLISECONDS.toSeconds(dbMishnayotItem.getTimeLine()) * 100 / dbMishnayotItem.getDuration());
                mProgressLine.setProgress(currentProgress);

            } else {

                mProgressLine.setProgress(0);
            }



            if (mishnayotItem.isDeleteMode()) {


                mDeleteMode.setVisibility(View.VISIBLE);

                mDeleteMode.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        AlertDialog.Builder myAlertDialog = new AlertDialog.Builder(mContext);
                        myAlertDialog.setTitle(mContext.getResources().getString(R.string.want_to_delete));
                        myAlertDialog.setMessage(mContext.getResources().getString(R.string.are_yoe_sure));

                        myAlertDialog.setPositiveButton(mContext.getResources().getString(R.string.yes),
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface arg0, int arg1) {

                                        setViewClickedAnimation(mDeleteMode);

                                        final DBHandler dbHandler = new DBHandler(mContext);

                                        dbHandler.deletePageFromMishnaTable(mishnayotItem);
                                        mListener.removeItemInLocalStorage(mishnayotItem);

                                        mPages.remove(position);
                                        notifyItemRemoved(position);
                                        notifyItemRangeChanged(position, mPages.size());

                                        if (mPages.size() == 0) {

                                            mListener.notifyDataInParentRecycler(mParentPosition);
                                        }

                                    }
                                });

                        myAlertDialog.setNegativeButton(mContext.getResources().getString(R.string.no),
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface arg0, int arg1) {


                                    }
                                });

                        myAlertDialog.show();


                    }
                });


            } else {

                mDeleteMode.setVisibility(View.GONE);

            }


            if (mishnayotItem.getAudio().equals("")) {

                mAudioIconIV.setVisibility(View.GONE);

            } else {


                if (mishnayotItem.getVideo().equals("")) {

                    mView.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            setViewClickedAnimation(mView);
                            mListener.onAudioClicked(mishnayotItem);

                        }
                    });

                }

                mAudioIconIV.setVisibility(View.VISIBLE);
                mAudioIconIV.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        setImageClickedAnimation(mAudioIconIV);

                        mListener.onAudioClicked(mishnayotItem);
                    }
                });




            }


        }


    }


    private void setViewClickedAnimation(View mView) {

        Animation animation = AnimationUtils.loadAnimation(mContext, R.anim.alpha);
        mView.startAnimation(animation);

    }


    private void setImageClickedAnimation(ImageView Icon) {

        Animation animation = AnimationUtils.loadAnimation(mContext, R.anim.alpha);
        Icon.startAnimation(animation);

    }


    public interface MishnaPageDownloadAdapterListener {


        void onVideoClicked(MishnayotItem mishnayotItem);

        void onAudioClicked(MishnayotItem mishnayotItem);

        void notifyDataInParentRecycler(int mParentPosition);

        void removeItemInLocalStorage(MishnayotItem mishnayotItem);
    }


}
