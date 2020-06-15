package co.il.jabrutouch.ui.main.mishna_screen.adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;
import co.il.jabrutouch.R;
import co.il.model.model.masechet_list_model.ChaptersItem;
import co.il.model.model.masechet_list_model.MasechetItem;



public class MishnaChapterAdapter extends RecyclerView.Adapter<MishnaChapterAdapter.ViewHolder> {

    private final String TAG = MishnaChapterAdapter.class.getSimpleName();
    private final MishnaChapterAdapterListener mListener;
    private final MasechetItem mMasechetItem;
    private final int mSederOrder;
    private List<ChaptersItem> mChaptersList;
    private Context mContext;



    public MishnaChapterAdapter(Context context, MasechetItem masechetItem, int sederOrder, MishnaChapterAdapterListener mishnaAdapterListener) {

        mMasechetItem = masechetItem;
        mChaptersList = masechetItem.getChapters();
        mListener = mishnaAdapterListener;
        mContext = context;
        mSederOrder =  sederOrder;

    }




    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.page_chapter_item, parent, false);
        return new ViewHolder(view);
    }





    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, final int position) {

        holder.updateItem(position);
        holder.mView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mListener.onMishnaChapterClicked(mChaptersList.get(position), mMasechetItem, mSederOrder);
            }
        });

    }





    @Override
    public int getItemCount() {

        return mChaptersList.size();
    }





    class ViewHolder extends RecyclerView.ViewHolder {
        private final View mView;
        private final TextView mChapterTV;
        private final TextView mMishnaiotsCountTV;


        private ViewHolder(View view) {
            super(view);
            mView = view;
            mChapterTV = view.findViewById(R.id.PMI_masechet_name_TV);
            mMishnaiotsCountTV = view.findViewById(R.id.PMI_chapter_TV);

        }


        @SuppressLint("SetTextI18n")
        private void updateItem(final int position) {

            ChaptersItem chaptersItem = mChaptersList.get(position);

            mChapterTV.setText(String.valueOf(chaptersItem.getChapter()));
            mMishnaiotsCountTV.setText(chaptersItem.getMishnayot() + " " + mContext.getResources().getString(R.string.mishnaiot));

        }


    }



    public interface MishnaChapterAdapterListener {


        void onMishnaChapterClicked(ChaptersItem chaptersItem, MasechetItem masechetItem, int mSederOrder);
    }


}
