package co.il.jabrutouch.ui.main.gemara_screen.adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.ArrayList;
import java.util.List;
import co.il.jabrutouch.R;
import co.il.model.model.masechet_list_model.MasechetItem;
import co.il.model.model.masechet_list_model.SederItem;



public class GemaraAdapter extends RecyclerView.Adapter<GemaraAdapter.ViewHolder> implements Filterable {

    private final String TAG = GemaraAdapter.class.getSimpleName();
    private final GemaraAdapterListener mListener;
    private final LinearLayout mSederLinear;
    private final int mSederOrder;
    private List<MasechetItem> mMasechtotList;
    private List<MasechetItem> mFilterList;
    private Context mContext;



    public GemaraAdapter(Context context, SederItem SederItem, GemaraAdapterListener gemaraAdapterListener, LinearLayout mLinearKodshim) {

        mMasechtotList = SederItem.getMasechet();
        mFilterList = SederItem.getMasechet();
        mSederOrder = SederItem.getOrder();
        mListener = gemaraAdapterListener;
        mSederLinear = mLinearKodshim;
        mContext = context;
    }



    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.page_gemara_item, parent, false);
        return new ViewHolder(view);
    }



    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, final int position) {

        holder.updateItem(position);
        holder.mView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mListener.onMasechetClicked(mSederOrder, mFilterList.get(position));
            }
        });

    }



    @Override
    public int getItemCount() {

        return mFilterList.size();
    }





    /**
     * filter gemara list
     * @return results
     */
    @Override
    public Filter getFilter() {
        return new Filter() {
            @SuppressWarnings("unchecked")
            @Override
            protected void publishResults(CharSequence constraint, FilterResults results) {
                mFilterList = (List<MasechetItem>) results.values;
                notifyDataSetChanged();

                if (mFilterList.size() == 0) {

                    mListener.onFilterSizeZero(mSederLinear);
                } else {
                    mListener.onFilterNonSizeZero(mSederLinear);

                }

            }

            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                List<MasechetItem> filteredResults = null;
                if (constraint.length() < 2) {
                    filteredResults = mMasechtotList;
                } else {
                    filteredResults = getFilteredResults(constraint.toString().toLowerCase());
                }

                FilterResults results = new FilterResults();
                results.values = filteredResults;

                return results;
            }
        };
    }




    private List<MasechetItem> getFilteredResults(String constraint) {
        List<MasechetItem> results = new ArrayList<>();

        for (MasechetItem item : mMasechtotList) {
            if (item.getName().toLowerCase().contains(constraint)) {
                results.add(item);
            }
        }


        return results;
    }





    class ViewHolder extends RecyclerView.ViewHolder {
        private final View mView;
        private final TextView mMasechetTV;
        private final TextView mChaptersCountTV;
        private final ImageView mArrowIV;


        private ViewHolder(View view) {
            super(view);
            mView = view;
            mMasechetTV = view.findViewById(R.id.PGI_masechet_name_TV);
            mChaptersCountTV = view.findViewById(R.id.PGI_chapter_TV);
            mArrowIV = view.findViewById(R.id.PGI_arrow_TV);


        }


        @SuppressLint("SetTextI18n")
        private void updateItem(final int position) {

            MasechetItem masechtotItem = mFilterList.get(position);

            mMasechetTV.setText(masechtotItem.getName());
            mChaptersCountTV.setText(masechtotItem.getPages() + " " + mContext.getResources().getString(R.string.pages));


        }


    }





    public interface GemaraAdapterListener {

        void onFilterSizeZero(LinearLayout sederLinear);

        void onFilterNonSizeZero(LinearLayout mSederLinear);

        void onMasechetClicked(int mSederOrder, MasechetItem masechtotItem);
    }


}
