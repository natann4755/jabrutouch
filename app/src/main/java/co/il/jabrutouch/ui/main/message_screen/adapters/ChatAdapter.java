package co.il.jabrutouch.ui.main.message_screen.adapters;

import android.content.Context;
import android.graphics.Typeface;
import android.text.format.DateFormat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import co.il.jabrutouch.R;
import co.il.model.model.ChatObject;


public class ChatAdapter extends RecyclerView.Adapter<ChatAdapter.ViewHolder> implements Filterable {


    private static final String YESTERDAY = "Yesterday";
    private final Context mContext;
    private final List<ChatObject> mMessageMainList;
    private List<ChatObject> mFilterMessageMainList;
    private final MessageMainAdapterListener mListener;

    public ChatAdapter(Context context, List<ChatObject> messageMainList, MessageMainAdapterListener messageMainAdapterListener) {

        mContext = context;
        mMessageMainList = messageMainList;
        mFilterMessageMainList = messageMainList;
        mListener = messageMainAdapterListener;

    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.meesage_main_item, parent, false);
        return new ViewHolder(view);

    }


    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, final int position) {

        holder.updateItem(position);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                setImageClickedAnimation(holder.itemView);
                mListener.onMessageClicked(mFilterMessageMainList.get(position));

            }
        });

    }


    private void setImageClickedAnimation(View icon) {

        Animation animation = AnimationUtils.loadAnimation(mContext, R.anim.alpha);
        icon.startAnimation(animation);

    }


    @Override
    public int getItemCount() {
        return mFilterMessageMainList.size();
    }


    /**
     * filter gemara list
     *
     * @return results
     */
    @Override
    public Filter getFilter() {
        return new Filter() {
            @SuppressWarnings("unchecked")
            @Override
            protected void publishResults(CharSequence constraint, FilterResults results) {
                mFilterMessageMainList = (List<ChatObject>) results.values;
                notifyDataSetChanged();

            }

            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                List<ChatObject> filteredResults = null;

                filteredResults = getFilteredResults(constraint.toString().toLowerCase());

                FilterResults results = new FilterResults();
                results.values = filteredResults;

                return results;
            }
        };
    }





    private List<ChatObject> getFilteredResults(String constraint) {
        List<ChatObject> results = new ArrayList<>();

        for (ChatObject item : mMessageMainList) {
            if (item.getTitle().toLowerCase().contains(constraint)) {
                results.add(item);
            }
        }


        return results;
    }





    public class ViewHolder extends RecyclerView.ViewHolder {


        private final TextView mChatLastMEssageTime;
        private final TextView mChatLastMessage;
        private final TextView mChatTitle;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            mChatTitle = itemView.findViewById(R.id.MMI_message_main_title_TV);
            mChatLastMessage = itemView.findViewById(R.id.MMI_last_message_TV);
            mChatLastMEssageTime = itemView.findViewById(R.id.MMI_last_message_time_TV);

        }

        public void updateItem(int position) {


            ChatObject chatObject = mFilterMessageMainList.get(position);
            mChatLastMessage.setText(chatObject.getLastMessage());

            String laseMessageDAte = getFormattedDate(chatObject.getLastMessageTime());
            mChatLastMEssageTime.setText(laseMessageDAte);
            mChatTitle.setText(chatObject.getTitle());

            if (chatObject.getUnreadMessages() > 0) {

                mChatLastMessage.setTypeface(null, Typeface.BOLD);
                mChatLastMEssageTime.setTypeface(null, Typeface.BOLD);
                mChatTitle.setTypeface(null, Typeface.BOLD);

            } else {

                mChatLastMessage.setTypeface(null, Typeface.NORMAL);
                mChatLastMEssageTime.setTypeface(null, Typeface.NORMAL);
                mChatTitle.setTypeface(null, Typeface.NORMAL);

            }


        }
    }





    public String getFormattedDate(Long smsTimeInMilis) {
        Calendar smsTime = Calendar.getInstance();
        smsTime.setTimeInMillis(smsTimeInMilis);

        Calendar now = Calendar.getInstance();

        final String timeFormatString = "HH:mm";
        final String dateTimeFormatString = "MMM d";
        final long HOURS = 60 * 60 * 60;
        if (now.get(Calendar.DATE) == smsTime.get(Calendar.DATE)) {
            return String.valueOf(DateFormat.format(timeFormatString, smsTime));
        } else if (now.get(Calendar.DATE) - smsTime.get(Calendar.DATE) == 1) {
            return YESTERDAY;
        } else if (now.get(Calendar.YEAR) == smsTime.get(Calendar.YEAR)) {
            return DateFormat.format(dateTimeFormatString, smsTime).toString();
        } else {
            return DateFormat.format("MMMM dd", smsTime).toString();
        }
    }


    public interface MessageMainAdapterListener {

        void onMessageClicked(ChatObject chatObject);
    }
}
