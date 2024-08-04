package com.example.companies.adapter;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.companies.R;
import com.google.android.gms.maps.model.LatLng;
import com.google.firebase.Timestamp;

import java.util.Collections;
import java.util.Date;
import java.util.List;

public class TiketAdapter extends RecyclerView.Adapter<TiketAdapter.TaskViewHolder> {
    private static final String TAG = "TiketAdapter";
    private static LatLng userLocation;
    // В начале класса
    private List<Tiket> tiketList;
    private OnItemClickListener onItemClickListener;

    // Интерфейс для кликов
    public interface OnItemClickListener {
        void onItemClick(Tiket task);
    }

    public TiketAdapter(List<Tiket> tiketList, LatLng userLocation, OnItemClickListener onItemClickListener){
        this.tiketList = tiketList;
        this.onItemClickListener = onItemClickListener;
        this.userLocation = userLocation;

    }
    @NonNull
    @Override
    public TaskViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.task_card, parent, false);
        return new TaskViewHolder(itemView);
    }


    @Override
    public void onBindViewHolder(@NonNull TaskViewHolder holder, int position) {
        Log.d(TAG, "onBindViewHolder: Binding view for position " + position);

        if (tiketList == null || tiketList.size() <= position) {
            Log.e(TAG, "onBindViewHolder: Task list is null or position out of bounds");
            return;
        }

        Tiket tiket = tiketList.get(position);
        Log.d(TAG, "onBindViewHolder: Binding task - " + tiket.getTitle());

        holder.bind(tiket, onItemClickListener);

    }

    @Override
    public int getItemCount() {
        if (tiketList == null) {
            return 0;
        } else {
            return tiketList.size();
        }
    }
    public void updateTasks(List<Tiket> newTasks) {
        this.tiketList.clear();
        this.tiketList.addAll(newTasks);
        notifyDataSetChanged();
    }
    // Метод для обновления списка тикетов

    // Метод для обновления адаптера с одним тикетом
    public void updateWithSingleTiket(Tiket tiket) {
        this.tiketList = Collections.singletonList(tiket);
        notifyDataSetChanged();
    }

    static class TaskViewHolder extends RecyclerView.ViewHolder {

        public TextView title;
        public TextView description;
        public TextView time;
        public TextView distance;
        public TextView views;
        public ImageView urgency;
        public FrameLayout frameLayout;

        public TaskViewHolder(View view) {
            super(view);
            title = view.findViewById(R.id.task_title);
            description = view.findViewById(R.id.task_description);
            time = view.findViewById(R.id.task_time);
            distance = view.findViewById(R.id.task_distance);
            views = view.findViewById(R.id.task_views);
            urgency = view.findViewById(R.id.task_icon);
            frameLayout = view.findViewById(R.id.status_task_color);

        }
        public void bind(final Tiket tiket, final OnItemClickListener listener) {
            if (tiket == null) {
                return;
            }

            // Установка данных в элементы

            title.setText(tiket.getTitle());
            description.setText(tiket.getDescription());
            time.setText(tiket.getTimeAgo());
            if (userLocation != null) {
                distance.setText(tiket.getDistanceTo(userLocation));
            }else {
                distance.setText("--");
            }
            views.setText(String.valueOf(tiket.getViews()));
            if(tiket.getUrgency() == 1){
                urgency.setImageResource(com.example.common.R.drawable.hot);
                frameLayout.setBackgroundResource(com.example.common.R.drawable.rounded_background_task_card_hot);
            } else if (tiket.getUrgency() == 2) {
                urgency.setImageResource(com.example.common.R.drawable.calendar);
                frameLayout.setBackgroundResource(com.example.common.R.drawable.rounded_background_task_card_calender);
            }else {
                urgency.setImageResource(com.example.common.R.drawable.more);
                frameLayout.setBackgroundResource(com.example.common.R.drawable.rounded_background_task_card_notrap);
            }

            // Обработка клика
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (listener != null) {
                        listener.onItemClick(tiket);
                    }
                }
            });
        }





    }


}
