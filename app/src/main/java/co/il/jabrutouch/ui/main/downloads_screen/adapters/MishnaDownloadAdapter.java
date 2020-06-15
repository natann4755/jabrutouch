package co.il.jabrutouch.ui.main.downloads_screen.adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import java.io.File;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import co.il.jabrutouch.R;
import co.il.model.model.MishnaDownloadObject;
import co.il.model.model.MishnayotItem;
import co.il.s3.utils.Config;
import static co.il.model.model.AnalyticsData.AUDIO;
import static co.il.model.model.AnalyticsData.VIDEO;



public class MishnaDownloadAdapter extends RecyclerView.Adapter<MishnaDownloadAdapter.ViewHolder> {

    private final String TAG = MishnaDownloadAdapter.class.getSimpleName();
    private final MishnaDownloadAdapterListener mListener;
    private Context mContext;
    private List<MishnaDownloadObject> mMasechtotList;
    private ViewHolder mHolder;

    public MishnaDownloadAdapter(Context context, List<MishnaDownloadObject> dataBaseObjectList, MishnaDownloadAdapterListener gemaraDownloadAdapterListener) {


        mListener = gemaraDownloadAdapterListener;
        mContext = context;
        mMasechtotList = dataBaseObjectList;

    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.download_item, parent, false);
        return new MishnaDownloadAdapter.ViewHolder(view);
    }


    @Override
    public void onBindViewHolder(@NonNull final MishnaDownloadAdapter.ViewHolder holder, final int position) {

        mHolder = holder;
        holder.updateItem(position);
        holder.mView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

    }


    @Override
    public int getItemCount() {

        return mMasechtotList.size();
    }

    public void notifyData(long currentPosition, int currentPageId) {

        if (mHolder != null){

            mHolder.notifyData(currentPosition, currentPageId);
        }

    }



    class ViewHolder extends RecyclerView.ViewHolder implements MishnaPageDownloadAdapter.MishnaPageDownloadAdapterListener {
        private final View mView;
        private final TextView mMashechetNameTV;
        private final ImageView mMasechetArrowIV;
        private final RecyclerView mMasechetRecyclerRV;
        private MishnaPageDownloadAdapter mMishnaPageDownloadAdapter;


        private ViewHolder(View view) {
            super(view);
            mView = view;
            mMashechetNameTV = view.findViewById(R.id.DI_masechet_name_TV);
            mMasechetArrowIV = view.findViewById(R.id.DI_arrow_IV);
            mMasechetRecyclerRV = view.findViewById(R.id.ID_download_pages_recycler_RV);


            mMasechetArrowIV.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    if (mMasechetRecyclerRV.getVisibility() == View.VISIBLE) {

                        mMasechetRecyclerRV.setVisibility(View.GONE);
                        mMasechetArrowIV.setRotation(180);
                    } else {

                        mMasechetRecyclerRV.setVisibility(View.VISIBLE);
                        mMasechetArrowIV.setRotation(0);


                    }


                }
            });

        }


        @SuppressLint("SetTextI18n")
        private void updateItem(final int position) {

            MishnaDownloadObject downObject = mMasechtotList.get(position);

            mMashechetNameTV.setText(downObject.getMasechetName());

            Collections.sort(downObject.getMishnayotItemsDB(), new Comparator<MishnayotItem>() {
                public int compare(MishnayotItem obj1, MishnayotItem obj2) {

                    return Integer.compare(obj1.getMishna(), obj2.getMishna()); // To compare integer values

                }
            });

            mMasechetRecyclerRV.setLayoutManager(new LinearLayoutManager(mContext));
            mMishnaPageDownloadAdapter = new MishnaPageDownloadAdapter(mContext, downObject.getMishnayotItemsDB(),position,  this);
            mMasechetRecyclerRV.setAdapter(mMishnaPageDownloadAdapter);

        }


        @Override
        public void onVideoClicked(MishnayotItem mishnayotItem) {
            if (existInLocalStorage(mishnayotItem.getVideo(), VIDEO)){

                mListener.onVideoClicked(mishnayotItem);
            }else {

                Toast.makeText(mContext, mContext.getResources().getString(R.string.problme), Toast.LENGTH_SHORT).show();
            }
        }

        @Override
        public void onAudioClicked(MishnayotItem mishnayotItem) {
            if (existInLocalStorage(mishnayotItem.getAudio(), AUDIO)){

                mListener.onAudioClicked(mishnayotItem);
            }else {

                Toast.makeText(mContext, mContext.getResources().getString(R.string.problme), Toast.LENGTH_SHORT).show();
            }

        }




        @Override
        public void notifyDataInParentRecycler(int mParentPosition) {

            mMasechtotList.remove(mParentPosition);
            notifyItemRemoved(mParentPosition);
            notifyItemRangeChanged(mParentPosition, mMasechtotList.size());

            if (mMasechtotList.size() == 0){

                mListener.notifyDataInParentRecycler();
            }


        }

        @Override
        public void removeItemInLocalStorage(MishnayotItem mishnayotItem) {
            mListener.removeItemInLocalStorage(mishnayotItem);
        }

        public void notifyData(long currentPosition, int currentPageId) {

            for (int i = 0; i < mMasechtotList.size(); i++) {
                for (int j = 0; j < mMasechtotList.get(i).getMishnayotItemsDB().size(); j++) {
                    if (mMasechtotList.get(i).getMishnayotItemsDB().get(j).getId() == currentPageId){
                        mMasechtotList.get(i).getMishnayotItemsDB().get(j).setTimeLine(currentPosition);
                    }
                }
            }

            mMishnaPageDownloadAdapter.notifyDataSetChanged();

        }


    }


    private boolean existInLocalStorage(String link, String folderName) {

        File file = new File(Config.getPathName(mContext) + folderName + "/" + getFIleNameFromFileKEy(link));

        return file.exists();

    }


    private String getFIleNameFromFileKEy(String fileKey) {

        return fileKey.substring(fileKey.lastIndexOf("/") + 1);

    }



    public interface MishnaDownloadAdapterListener {


        void onVideoClicked(MishnayotItem mishnayotItem);

        void onAudioClicked(MishnayotItem mishnayotItem);

        void notifyDataInParentRecycler();

        void removeItemInLocalStorage(MishnayotItem mishnayotItem);
    }

}
