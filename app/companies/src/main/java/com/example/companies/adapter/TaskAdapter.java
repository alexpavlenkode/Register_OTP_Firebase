package com.example.companies.adapter;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.companies.R;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class TaskAdapter extends RecyclerView.Adapter<TaskAdapter.TaskViewHolder> {
    private static final String TAG = "TaskAdapter";
    // В начале класса
    private List<Task> taskList;
    private OnItemClickListener onItemClickListener;

    // Интерфейс для кликов
    public interface OnItemClickListener {
        void onItemClick(Task task);
    }

    public TaskAdapter(List<Task> taskList, OnItemClickListener onItemClickListener){
        this.taskList = taskList;
        this.onItemClickListener = onItemClickListener;

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

    static class TaskViewHolder extends RecyclerView.ViewHolder {

        public ImageView icon;
        public TextView title;
        public TextView description;
        public TextView time;
        public TextView distance;
        public TextView views;

        public TaskViewHolder(View view) {
            super(view);
            icon = view.findViewById(R.id.task_icon);
            title = view.findViewById(R.id.task_title);
            description = view.findViewById(R.id.task_description);
            time = view.findViewById(R.id.task_time);
            distance = view.findViewById(R.id.task_distance);
            views = view.findViewById(R.id.task_views);
        }
        public void bind(final Task task, final OnItemClickListener listener) {
            Log.d(TAG, "bind: Binding task " + task.getTitle());
            if (task == null) {
                Log.e(TAG, "bind: Task is null");
                return;
            }

            // Установка данных в элементы
            icon.setImageResource(task.getImageResId());
            title.setText(task.getTitle());
            description.setText(task.getDescription());
            time.setText(getTimeAgo(task.getDate()));
            distance.setText(task.getDistance());
            views.setText(String.valueOf(task.getViews()));

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

        private String getTimeAgo(Date date) {
            if (date == null) {
                Log.e(TAG, "getTimeAgo: Date is null");
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
                return days + " Tag(e) zurück"; // "X days ago"
            } else if (hours > 0) {
                return hours + " Stunde(n) zurück"; // "X hours ago"
            } else if (minutes > 0) {
                return minutes + " Minute(n) zurück"; // "X minutes ago"
            } else {
                return "Gerade eben"; // "Just now"
            }
        }

    }


}
