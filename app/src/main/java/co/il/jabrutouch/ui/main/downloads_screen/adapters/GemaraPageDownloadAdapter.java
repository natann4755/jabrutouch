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
import co.il.model.model.PagesItem;
import co.il.sqlcore.DBHandler;
import static co.il.sqlcore.DBKeys.GEMARA_GALLERY_TABLE;
import static co.il.sqlcore.DBKeys.GEMARA_TABLE;
import static co.il.sqlcore.DBKeys.GEMARA_VIDEO_PARTS_TABLE;




public class GemaraPageDownloadAdapter extends RecyclerView.Adapter<GemaraPageDownloadAdapter.ViewHolder> {

    private final String TAG = GemaraPageDownloadAdapter.class.getSimpleName();
    private final GemaraPageDownloadAdapterListener mListener;
    private final List<PagesItem> mPages;
    private final int mParentPosition;
    private Context mContext;


    public GemaraPageDownloadAdapter(Context context, List<PagesItem> dataBaseObjects, int parentPosition, GemaraPageDownloadAdapterListener gemaraDownloadAdapterListener) {

        mListener = gemaraDownloadAdapterListener;
        mContext = context;
        mPages = dataBaseObjects;
        mParentPosition = parentPosition;
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

            final PagesItem pagesItem = mPages.get(position);

            mPageName.setText(String.valueOf(pagesItem.getPageNumber()));
            mPageDuration.setText(TimeUnit.SECONDS.toMinutes(pagesItem.getDuration()) + " " + mContext.getResources().getString(R.string.min));

            if (pagesItem.getVideo().equals("")) {

                mVideoIconIV.setVisibility(View.GONE);

            } else {

                mView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        setViewClickedAnimation(mView);

                        mListener.onVideoClicked(pagesItem);

                    }
                });

                mVideoIconIV.setVisibility(View.VISIBLE);
                mVideoIconIV.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        setImageClickedAnimation(mVideoIconIV);
                        mListener.onVideoClicked(pagesItem);

                    }
                });

            }


            final DBHandler dbHandler = new DBHandler(mContext);
            pagesItem.setVideoPart(dbHandler.getVideoPart(GEMARA_VIDEO_PARTS_TABLE, String.valueOf(pagesItem.getId())));
            pagesItem.setGallery(dbHandler.getGallery(GEMARA_GALLERY_TABLE, String.valueOf(pagesItem.getId())));

            PagesItem dbPageItem = dbHandler.findGemaraById(String.valueOf(pagesItem.getId()), GEMARA_TABLE);


            if (pagesItem.getTimeLine() > 0) {
                int currentProgress = (int) (TimeUnit.MILLISECONDS.toSeconds(pagesItem.getTimeLine()) * 100 / pagesItem.getDuration());
                mProgressLine.setProgress(currentProgress);
                pagesItem.setTimeLine(0);

            } else if (dbPageItem.getTimeLine() > 0) {

                int currentProgress = (int) (TimeUnit.MILLISECONDS.toSeconds(dbPageItem.getTimeLine()) * 100 / dbPageItem.getDuration());
                mProgressLine.setProgress(currentProgress);

            } else {

                mProgressLine.setProgress(0);
            }


            if (pagesItem.isDeleteMode()) {


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

                                        dbHandler.deletePageFromGemaraTable(pagesItem);
                                        mListener.removeItemInLocalStorage(pagesItem);

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


            if (pagesItem.getAudio().equals("")) {

                mAudioIconIV.setVisibility(View.GONE);

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


                if (pagesItem.getVideo().equals("")) {

                    mView.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            setViewClickedAnimation(mView);

                            mListener.onAudioClicked(pagesItem);

                        }
                    });

                }

                mAudioIconIV.setVisibility(View.VISIBLE);
                mAudioIconIV.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        setImageClickedAnimation(mAudioIconIV);

                        mListener.onAudioClicked(pagesItem);
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


    public interface GemaraPageDownloadAdapterListener {


        void onVideoClicked(PagesItem pagesItem);

        void onAudioClicked(PagesItem pagesItem);

        void notifyDataInParentRecycler(int mParentPosition);

        void removeItemInLocalStorage(PagesItem pagesItem);
    }


}
