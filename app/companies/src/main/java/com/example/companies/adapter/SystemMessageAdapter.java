package com.example.companies.adapter;

import static com.google.android.material.internal.ViewUtils.dpToPx;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Handler;
import android.os.Looper;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.appcompat.view.menu.MenuView;
import androidx.constraintlayout.widget.Constraints;
import androidx.core.content.ContextCompat;
import androidx.core.content.res.ResourcesCompat;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.RecyclerView;

import com.example.common.utils.FirestoreHelper;
import com.example.companies.R;
import com.example.companies.SharedViewModel;
import com.example.companies.repository.ChatRepository;
import com.example.companies.ui.chat.ChatMessageModel;
import com.example.companies.ui.chat.SystemChatMessageModel;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;

import java.util.List;

public class SystemMessageAdapter extends RecyclerView.Adapter<SystemMessageAdapter.SystemMessageViewHolder> {

    private List<SystemChatMessageModel> systemMessages;
    private boolean isTyping = false;
    private final Handler handler = new Handler(Looper.getMainLooper());
    private static final long TYPING_DELAY = 500;
    private ChatRepository chatRepository;
    private SharedViewModel sharedViewModel;
    private Context context;

    public SystemMessageAdapter(Context context, SharedViewModel sharedViewModel, List<SystemChatMessageModel> systemMessages) {
        this.context = context;
        this.systemMessages = systemMessages;
        this.sharedViewModel = sharedViewModel;
    }

    @NonNull
    @Override
    public SystemMessageViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.chat_system_messages, parent, false);

        chatRepository = new ChatRepository(context, new FirestoreHelper(), sharedViewModel);
        return new SystemMessageViewHolder(view);
    }

    @SuppressLint("ResourceAsColor")
    @Override
    public void onBindViewHolder(@NonNull SystemMessageViewHolder holder, int position) {
        SystemChatMessageModel messageModel = systemMessages.get(position);

        // Очистка старых кнопок и текста
        holder.buttonsLayout.removeAllViews();
        holder.buttonsLayout.setVisibility(View.GONE);
        holder.leftChatTextview.setVisibility(View.VISIBLE);
        holder.leftChatTextview.setText(messageModel.getMessage());


        if (messageModel.getButtons() != null && !messageModel.getButtons().isEmpty()) {
            holder.buttonsLayout.setVisibility(View.VISIBLE);
            holder.leftChatTextview.setText(messageModel.getMessage());

            holder.leftChatLayout.setBackgroundTintList(null);
            Drawable drawable = ContextCompat.getDrawable(holder.itemView.getContext(), R.drawable.edit_text_rounded_corner_black);

            holder.leftChatLayout.setBackground(drawable);
            // Добавление кнопок
            for (String buttonText : messageModel.getButtons()) {
                Button button = new Button(holder.itemView.getContext());
                button.setText(buttonText);
                // Создание параметров макета с указанием ширины и высоты
                LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                        dpToPx(158,holder), // Ширина в пикселях
                        dpToPx(30,holder)   // Высота в пикселях
                );
                // Установка внешних отступов
                params.setMargins(dpToPx(10,holder), dpToPx(5,holder), dpToPx(10,holder), dpToPx(5,holder));
                button.setLayoutParams(params);
                button.setTextColor(ContextCompat.getColor(holder.itemView.getContext(), com.example.common.R.color.white));
                button.setPadding(dpToPx(10,holder), dpToPx(4,holder), dpToPx(10,holder), dpToPx(5,holder));
                button.setBackground(ContextCompat.getDrawable(holder.itemView.getContext(), R.drawable.rounded_background_selecttime));
                button.setTextSize(12);
                button.setOnClickListener(v -> {
                    String messageContent = buttonText.trim();
                    chatRepository.sendFirstMessage(messageContent,true);
                });
                holder.buttonsLayout.addView(button);
            }
        }

    }

    // Метод для преобразования dp в пиксели
    private int dpToPx(int dp,SystemMessageViewHolder holder) {
        float density = holder.itemView.getContext().getResources().getDisplayMetrics().density;
        return Math.round(dp * density);
    }


    @Override
    public int getItemCount() {
        return systemMessages.size();
    }

    // Класс ViewHolder для системных сообщений
    class SystemMessageViewHolder extends RecyclerView.ViewHolder {
        LinearLayout leftChatLayout;
        TextView leftChatTextview;
        LinearLayout buttonsLayout;

        public SystemMessageViewHolder(@NonNull View itemView) {
            super(itemView);
            leftChatLayout = itemView.findViewById(R.id.left_chat_system_layout);
            leftChatTextview = itemView.findViewById(R.id.left_chat_system_textview);
            buttonsLayout = itemView.findViewById(R.id.buttons_layout);
        }
    }


}
