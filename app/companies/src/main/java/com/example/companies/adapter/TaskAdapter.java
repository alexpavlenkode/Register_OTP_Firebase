package com.example.companies.adapter;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.companies.R;
import com.google.android.gms.maps.model.LatLng;
import com.google.firebase.Timestamp;
import com.google.firebase.firestore.GeoPoint;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class TaskAdapter extends RecyclerView.Adapter<TaskAdapter.TaskViewHolder> {
    private static final String TAG = "TaskAdapter";
    private static LatLng userLocation;
    // В начале класса
    private List<Task> taskList;
    private OnItemClickListener onItemClickListener;

    // Интерфейс для кликов
    public interface OnItemClickListener {
        void onItemClick(Task task);
    }

    public TaskAdapter(List<Task> taskList,LatLng userLocation, OnItemClickListener onItemClickListener){
        this.taskList = taskList;
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

        if (taskList == null || taskList.size() <= position) {
            Log.e(TAG, "onBindViewHolder: Task list is null or position out of bounds");
            return;
        }

        Task task = taskList.get(position);
        Log.d(TAG, "onBindViewHolder: Binding task - " + task.getTitle());

        holder.bind(task, onItemClickListener);

    }

    @Override
    public int getItemCount() {
        if (taskList == null) {
            Log.e(TAG, "getItemCount: Task list is null");
            return 0;
        } else {
            Log.d(TAG, "getItemCount: Task list size is " + taskList.size());
            return taskList.size();
        }
    }
    public void updateTasks(List<Task> newTasks) {
        this.taskList.clear();
        this.taskList.addAll(newTasks);
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
        public void bind(final Task task, final OnItemClickListener listener) {
            Log.d(TAG, "bind: Binding task " + task.getTitle());
            if (task == null) {
                Log.e(TAG, "bind: Task is null");
                return;
            }

            // Установка данных в элементы

            title.setText(task.getTitle());
            description.setText(task.getDescription());
            time.setText(getTimeAgo(task.getTimestamp()));

            if (userLocation != null) {
                distance.setText(task.getDistanceTo(userLocation,task.getLocation()));
                Log.e(TAG, "Disstance from me " + userLocation);
            }else {
                distance.setText("00");
            }


            views.setText(String.valueOf(task.getViews()));
            if(task.getUrgency() == 1){
                urgency.setImageResource(com.example.common.R.drawable.hot);
                frameLayout.setBackgroundResource(com.example.common.R.drawable.rounded_background_task_card_hot);
            } else if (task.getUrgency() == 2) {
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
                        listener.onItemClick(task);
                    }
                }
            });
        }

        private static double getDistanc(GeoPoint location){
            if (location == null) {
                return Double.NaN; // Возвращаем "не число", если местоположение не задано
            }
            final int R = 6371;

            double userLat = location.getLatitude();
            double userLon = location.getLongitude();

            double lat1 = Math.toRadians(location.getLatitude());
            double lon1 = Math.toRadians(location.getLongitude());
            double lat2 = Math.toRadians(userLat);
            double lon2 = Math.toRadians(userLon);

            double latDistance = lat2 - lat1;
            double lonDistance = lon2 - lon1;
            double a = Math.sin(latDistance / 2) * Math.sin(latDistance / 2)
                    + Math.cos(lat1) * Math.cos(lat2)
                    * Math.sin(lonDistance / 2) * Math.sin(lonDistance / 2);
            double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));

            return R * c;
        }

        // Метод для конвертации Timestamp в Date
        private static Date convertTimestampToDate(Timestamp timestamp) {
            return timestamp != null ? timestamp.toDate() : null;
        }

        // Метод для получения времени в формате "X дней назад", "X часов назад" и т.д.
        public static String getTimeAgo(Timestamp timestamp) {
            Date date = convertTimestampToDate(timestamp);
            if (date == null) {
                return "Unbekannt"; // "Unknown" or some placeholder
            }
            long time = date.getTime();
            long now = System.currentTimeMillis();
            long diff = now - time;

            long seconds = diff / 1000;
            long minutes = seconds / 60;
            long hours = minutes / 60;
            long days = hours / 24;

            if (days > 0) {
                return "Vor " + days + (days == 1 ? " Tag" : " Tagen"); // "1 day ago" or "X days ago"
            } else if (hours > 0) {
                return "Vor " + hours + (hours == 1 ? " Stunde" : " Stunden"); // "1 hour ago" or "X hours ago"
            } else if (minutes > 0) {
                return "Vor " + minutes + (minutes == 1 ? " Minute" : " Minuten"); // "1 minute ago" or "X minutes ago"
            } else {
                return "Jetzt"; // "Just now"
            }
        }

    }


}
