package co.il.jabrutouch.ui.main.downloads_screen.adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import org.jetbrains.annotations.NotNull;
import java.io.File;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import co.il.jabrutouch.R;
import co.il.model.model.GemaraDownloadObject;
import co.il.model.model.PagesItem;
import co.il.s3.utils.Config;

import static co.il.model.model.AnalyticsData.AUDIO;
import static co.il.model.model.AnalyticsData.VIDEO;



public class GemaraDownloadAdapter extends RecyclerView.Adapter<GemaraDownloadAdapter.ViewHolder> {

    private final String TAG = GemaraDownloadAdapter.class.getSimpleName();
    private final GemaraDownloadAdapterListener mListener;
    private Context mContext;
    private List<GemaraDownloadObject> mMasechtotList;
    private ViewHolder mHolder;


    public GemaraDownloadAdapter(Context context, List<GemaraDownloadObject> dataBaseObjectList, GemaraDownloadAdapterListener gemaraDownloadAdapterListener) {

        mListener = gemaraDownloadAdapterListener;
        mContext = context;
        mMasechtotList = dataBaseObjectList;

    }



    @NotNull
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.download_item, parent, false);

        return new ViewHolder(view);
    }




    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {

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



    class ViewHolder extends RecyclerView.ViewHolder implements GemaraPageDownloadAdapter.GemaraPageDownloadAdapterListener {

        private final View mView;
        private final TextView mMashechetNameTV;
        private final ImageView mMasechetArrowIV;
        private final RecyclerView mMasechetRecyclerRV;
        private GemaraPageDownloadAdapter mGemaraPageDownloadAdapter;


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

        private void notifyData(long currentPosition, int currentPageId) {


            for (int i = 0; i < mMasechtotList.size(); i++) {
                for (int j = 0; j < mMasechtotList.get(i).getPagesItemsDB().size(); j++) {
                    if (mMasechtotList.get(i).getPagesItemsDB().get(j).getId() == currentPageId){
                        mMasechtotList.get(i).getPagesItemsDB().get(j).setTimeLine(currentPosition);
                    }
                }
            }

            mGemaraPageDownloadAdapter.notifyDataSetChanged();


        }




        @SuppressLint("SetTextI18n")
        private void updateItem(final int position) {

            GemaraDownloadObject downObject = mMasechtotList.get(position);

            mMashechetNameTV.setText(downObject.getMasechetName());

            Collections.sort(downObject.getPagesItemsDB(), new Comparator<PagesItem>() {
                public int compare(PagesItem obj1, PagesItem obj2) {

                    return Integer.compare(obj1.getPageNumber(), obj2.getPageNumber()); // To compare integer values

                }
            });


            mMasechetRecyclerRV.setLayoutManager(new LinearLayoutManager(mContext));
            mGemaraPageDownloadAdapter = new GemaraPageDownloadAdapter(mContext, downObject.getPagesItemsDB(), position, this);
            mMasechetRecyclerRV.setAdapter(mGemaraPageDownloadAdapter);


        }




        @Override
        public void onVideoClicked(PagesItem pagesItem) {
            if (existInLocalStorage(pagesItem.getVideo(), VIDEO)){

                mListener.onVideoClicked(pagesItem);
            }else {

                Toast.makeText(mContext, mContext.getResources().getString(R.string.problme), Toast.LENGTH_SHORT).show();
            }
        }



        @Override
        public void onAudioClicked(PagesItem pagesItem) {
            if (existInLocalStorage(pagesItem.getAudio(), AUDIO)){

                mListener.onAudioClicked(pagesItem);
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
        public void removeItemInLocalStorage(PagesItem pagesItem) {
            mListener.removeItemInLocalStorage(pagesItem);
        }


    }




    private boolean existInLocalStorage(String link, String folderName) {

        File file = new File(Config.getPathName(mContext) + folderName + "/" + getFIleNameFromFileKEy(link));

        return file.exists();

    }




    private String getFIleNameFromFileKEy(String fileKey) {

        return fileKey.substring(fileKey.lastIndexOf("/") + 1);

    }


    public interface GemaraDownloadAdapterListener {

        void onVideoClicked(PagesItem pagesItem);

        void onAudioClicked(PagesItem pagesItem);

        void notifyDataInParentRecycler();

        void removeItemInLocalStorage(PagesItem pagesItem);
    }


}
