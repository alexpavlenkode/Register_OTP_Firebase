package com.example.companies.repository;

import android.util.Log;

import com.example.companies.adapter.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.List;

public class TaskRepository {
    private static final String TAG = "TaskRepository";


    public void getTasksByProfessions(List<String> professions, OnTasksFetchedListener listener) {
        Log.e(TAG, "professions: " + professions);
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("tickets")
                .whereIn("profession", professions)
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        List<Task> tasks = new ArrayList<>();
                        Log.d(TAG, "Documents found on getTasksByProfessions: " + task.getResult().size());
                        for (QueryDocumentSnapshot document : task.getResult()) {
                            Task fetchedTask = document.toObject(Task.class);
                            tasks.add(fetchedTask);
                        }
                        listener.onTasksFetched(tasks);
                    } else {
                        listener.onError(task.getException());
                    }
                });
    }

    public interface OnTasksFetchedListener {
        void onTasksFetched(List<Task> tasks);
        void onError(Exception e);
    }
}
