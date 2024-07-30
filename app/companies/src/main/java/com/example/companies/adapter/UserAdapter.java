package com.example.companies.adapter;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.companies.R;
import com.google.android.material.chip.Chip;

import java.util.List;

public class UserAdapter extends  RecyclerView.Adapter<UserAdapter.UserViewHolder>{

    private List<User> userList;
    private Context context;

    public UserAdapter(Context context, List<User> userList) {
        this.context = context;
        this.userList = userList;
    }

    @NonNull
    @Override
    public UserViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.messages_card, parent, false);
        return new UserViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull UserAdapter.UserViewHolder holder, int position) {
        User user = userList.get(position);

        // Устанавливаем фото пользователя
        Glide.with(context)
                .load(user.getPhotoUrl())
                .placeholder(com.example.comon.R.drawable.ic_user_placeholder) // Замените на ваше изображение-заполнитель
                .into(holder.userPhoto);

        // Устанавливаем онлайн статус
        holder.onlineIndicator.setVisibility(user.isOnline() ? View.VISIBLE : View.GONE);

        // Устанавливаем имя пользователя
        holder.userName.setText(user.getName());

        // Устанавливаем последнее сообщение и время
        holder.lastMessage.setText(user.getLastMessage());
        holder.messageTime.setText(user.getMessageTime());

        // Устанавливаем количество непрочитанных сообщений
        holder.unreadCount.setText(String.valueOf(user.getUnreadCount()));
        holder.unreadCount.setVisibility(user.getUnreadCount() > 0 ? View.VISIBLE : View.GONE);

        // Устанавливаем статус работы
        if (user.getStatus() == User.STATUS_ACTIVE) {
            holder.statusChip.setText(context.getString(com.example.comon.R.string.messages_completed));
        } else {
            holder.statusChip.setText(context.getString(com.example.comon.R.string.messages_inprogress));
        }

    }

    @Override
    public int getItemCount() {
        return userList.size();
    }


    public static class UserViewHolder extends RecyclerView.ViewHolder {
        ImageView userPhoto;
        View onlineIndicator;
        TextView userName;
        TextView lastMessage;
        TextView messageTime;
        TextView unreadCount;
        Chip statusChip;

        public UserViewHolder(@NonNull View itemView) {
            super(itemView);
            userPhoto = itemView.findViewById(R.id.user_photo);
            onlineIndicator = itemView.findViewById(R.id.online_indicator);
            userName = itemView.findViewById(R.id.user_name);
            lastMessage = itemView.findViewById(R.id.last_message);
            messageTime = itemView.findViewById(R.id.message_time);
            unreadCount = itemView.findViewById(R.id.unread_count);
            statusChip = itemView.findViewById(R.id.status_chip_message);
        }
    }


}
