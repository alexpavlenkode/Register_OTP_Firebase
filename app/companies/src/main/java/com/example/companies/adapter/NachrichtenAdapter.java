package com.example.companies.adapter;

import android.annotation.SuppressLint;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.companies.R;
import com.example.companies.ui.chat.ChatroomModel;
import com.example.companies.ui.chat.NachrichtenModel;
import com.google.android.material.chip.Chip;

import java.util.List;

public class NachrichtenAdapter extends RecyclerView.Adapter<NachrichtenAdapter.TaskViewHolder>{
    private static final String TAG = "NachrichtenAdapter";
    private List<NachrichtenModel> allRoomsList;
    private OnNachrichtClickListener onNachrichtClickListener;
    public interface OnNachrichtClickListener {
        void onItemClick(NachrichtenModel nachrichtenModel);
    }


    public NachrichtenAdapter(List<NachrichtenModel> allRoomsList, OnNachrichtClickListener onNachrichtClickListener){
        this.allRoomsList = allRoomsList;
        this.onNachrichtClickListener = onNachrichtClickListener;
    }
    @NonNull
    @Override
    public TaskViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View nachrichten_view = LayoutInflater.from(parent.getContext()).inflate(R.layout.messages_card, parent, false);
        return new TaskViewHolder(nachrichten_view);
    }

    @Override
    public void onBindViewHolder(@NonNull NachrichtenAdapter.TaskViewHolder holder, int position) {
        Log.d(TAG, "onBindViewHolder: Binding view for position " + position);
        if(allRoomsList == null || allRoomsList.size() <= position){
            Log.e(TAG, "onBindViewHolder: Task list is null or position out of bounds");
            return;
        }

        NachrichtenModel nachrichtenModel = allRoomsList.get(position);
        Log.d(TAG, "onBindViewHolder: Binding task - " + nachrichtenModel.getChatroomId());

        holder.bind(nachrichtenModel, onNachrichtClickListener);

    }

    @Override
    public int getItemCount() {
        if (allRoomsList == null) {
            return 0;
        } else {
            return allRoomsList.size();
        }
    }

    static class TaskViewHolder extends RecyclerView.ViewHolder{

        private ImageView user_photo;
        private View online_indicator;
        private TextView user_name;
        private TextView last_message;
        private TextView message_time;
        private TextView unread_count;
        private Chip status_chip_message;

        public TaskViewHolder(View view) {
            super(view);
            user_photo = view.findViewById(R.id.user_photo);
            online_indicator = view.findViewById(R.id.online_indicator);
            user_name = view.findViewById(R.id.user_name);
            last_message = view.findViewById(R.id.last_message);
            message_time = view.findViewById(R.id.message_time);
            unread_count = view.findViewById(R.id.unread_count);
            status_chip_message = view.findViewById(R.id.status_chip_message);
        }

        @SuppressLint("ResourceAsColor")
        public void bind(final NachrichtenModel nachrichtenModel, final OnNachrichtClickListener listener){
            if(nachrichtenModel == null){
                return;
            }
            user_photo.setImageResource(com.example.common.R.drawable.ic_user_placeholder);
            online_indicator.setBackgroundResource(com.example.common.R.drawable.online_indicator_background);
            user_name.setText(nachrichtenModel.getUserName());
            last_message.setText(nachrichtenModel.getLastMessage());
            message_time.setText(nachrichtenModel.getMessageTimestamp());
            unread_count.setText("2");
            int status = nachrichtenModel.getTicketStatus();
            if(status == 0){
                status_chip_message.setVisibility(View.INVISIBLE);
            } else if (status == 1) {
                status_chip_message.setChipBackgroundColorResource(com.example.common.R.color.messages_inprogress);
                status_chip_message.setText(com.example.common.R.string.messages_inprogress);
                status_chip_message.setTextColor(com.example.common.R.color.black);
            } else if (status == 2) {
                status_chip_message.setChipBackgroundColorResource(com.example.common.R.color.messages_completed);
                status_chip_message.setText(com.example.common.R.string.messages_completed);
                status_chip_message.setTextColor(com.example.common.R.color.black);
            }

            Log.e(TAG, "userAdapter is Null " + online_indicator);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (listener != null) {
                        listener.onItemClick(nachrichtenModel);
                    }
                }
            });

        }

    }
}
