package co.il.jabrutouch.ui.main.profile_screen;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;
import java.util.Objects;
import co.il.jabrutouch.R;
import co.il.model.model.IdAndNameDetailed;




public class InterestsAdapter extends RecyclerView.Adapter<InterestsAdapter.ViewHolder> {


    private final List<IdAndNameDetailed> mData;
    private final InterestsListener mListener;
    private LayoutInflater mInflater;
    private boolean mOnClickable;
    private Context mContext;


    public InterestsAdapter(Context context, List<IdAndNameDetailed> mData, InterestsListener interestsListener, boolean onClickable) {
        this.mInflater = LayoutInflater.from(context);
        this.mData = mData;
        mContext = context;
        mListener = interestsListener;
        mOnClickable = onClickable;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int i) {

        if (mOnClickable) {

            View view = mInflater.inflate(R.layout.interest_recycler_view, parent, false);
            return new ViewHolder(view);

        } else {

            View view = mInflater.inflate(R.layout.interest_non_clickable_recycler_view, parent, false);
            return new ViewHolder(view);
        }


    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, final int position) {

        holder.title.setText(mData.get(position).getName());

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            private boolean clicked;

            @SuppressLint("ResourceAsColor")
            @Override
            public void onClick(View v) {

                if (mOnClickable) {

                    if (!mData.get(position).getIsSelected()) {

                        mData.get(position).setIsSelected(true);
                        holder.roundedBackground.setBackground(ContextCompat.getDrawable(Objects.requireNonNull(mContext), R.drawable.rounded_shadow_blue));
                        holder.title.setTextColor(Color.WHITE);
                        mListener.onInterestAdd(mData.get(position));

                    } else {

                        mData.get(position).setIsSelected(false);
                        holder.roundedBackground.setBackground(ContextCompat.getDrawable(Objects.requireNonNull(mContext), R.drawable.rounded_shadow));
                        holder.title.setTextColor(ContextCompat.getColor(Objects.requireNonNull(mContext), R.color.profile_color));
                        mListener.onInterestRemoved(mData.get(position));


                    }
                }

            }
        });

        holder.updateItem(position);

    }


    @Override
    public int getItemCount() {
        return mData.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder {

        LinearLayout roundedBackground;
        TextView title;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            title = itemView.findViewById(R.id.RV_title);
            roundedBackground = itemView.findViewById(R.id.IRV_rounded_LL);

        }


        public void updateItem(int position) {

            if (mOnClickable) {

                if (mData.get(position).getIsSelected()) {

                    roundedBackground.setBackground(ContextCompat.getDrawable(Objects.requireNonNull(mContext), R.drawable.rounded_shadow_blue));
                    title.setTextColor(Color.WHITE);
                } else {

                    roundedBackground.setBackground(ContextCompat.getDrawable(Objects.requireNonNull(mContext), R.drawable.rounded_shadow));
                    title.setTextColor(ContextCompat.getColor(Objects.requireNonNull(mContext), R.color.profile_color));
                }
            }

        }
    }


    public interface InterestsListener {

        void onInterestAdd(IdAndNameDetailed interest);

        void onInterestRemoved(IdAndNameDetailed interest);
    }

}
