package com.example.companies.repository;

import android.util.Log;

import com.example.companies.adapter.Tiket;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.List;

public class TaskRepository {
    private static final String TAG = "TaskRepository";


    public void getTasksByProfessions(List<String> professions, OnTasksFetchedListener listener) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("tickets")
                .whereIn("profession", professions)//TODO: Добавить фильтрацию тикетов по статусу
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        List<Tiket> tiket = new ArrayList<>();
                        for (QueryDocumentSnapshot document : task.getResult()) {
                            Log.d("TaskRepository", "document: " + document);
                            Tiket fetchedTask = document.toObject(Tiket.class);
                            tiket.add(fetchedTask);
                        }
                        listener.onTasksFetched(tiket);
                    } else {
                        listener.onError(task.getException());
                    }
                });
    }

    public interface OnTasksFetchedListener {
        void onTasksFetched(List<Tiket> tasks);
        void onError(Exception e);
    }
}
