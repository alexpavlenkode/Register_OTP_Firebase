package com.example.companies.repository;

import static com.google.android.material.internal.ContextUtils.getActivity;

import static java.security.AccessController.getContext;

import android.annotation.SuppressLint;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelStoreOwner;

import android.app.Activity;
import android.content.Context;
import android.util.Log;

import com.example.companies.SharedViewModel;
import com.example.companies.adapter.Tiket;
import com.google.android.gms.maps.model.LatLng;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.List;

public class TaskRepository {

    private LatLng myLocation;
    private SharedViewModel sharedViewModel;
    private Context context;
    private static final String TAG = "TaskRepository";

    // Конструктор
    public TaskRepository(Activity activity) {
        sharedViewModel = new ViewModelProvider((ViewModelStoreOwner) activity).get(SharedViewModel.class);
    }

    public void initializeLocation(Context context, Activity activity) {
        // Создаем экземпляр класса Location
        Location companyLocation = new Location();
        LatLng placeholderLocation = new LatLng(0.0, 0.0);
        myLocation = placeholderLocation;
        companyLocation.init(context,activity);
        // Получаем местоположение и сохраняем его в SharedViewModel
        LatLng currentLocation = companyLocation.getMyLocation();
        if (currentLocation != null) {
            myLocation = currentLocation;
        } else {
            myLocation = placeholderLocation; // Используем placeholder, если местоположение не определено
        }
        sharedViewModel.setCompanyLocation(myLocation);
    }

    @SuppressLint("RestrictedApi")
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
                            LatLng myLocation = sharedViewModel.getCompanyLocation().getValue();
                            if (myLocation != null) {
                                fetchedTask.setDistance(myLocation);
                            } else {
                                // Обработка случая, когда myLocation не определено
                                fetchedTask.setDistance(new LatLng(0.0, 0.0)); // Placeholder
                            }
                            fetchedTask.setDistance(myLocation);
                            tiket.add(fetchedTask);
                        }
                        listener.onTasksFetched(tiket);
                    } else {
                        listener.onError(task.getException());
                    }
                });
    }

    @SuppressLint("RestrictedApi")
    public void getTasksByTicketId(String tiketId, OnTiketsFetchedListener listener) {

        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("tickets").document(tiketId)//TODO: Добавить фильтрацию тикетов по статусу
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        DocumentSnapshot document = task.getResult();
                        if (document != null && document.exists()) {
                            // Создание экземпляра Tiket из данных документа
                            Tiket tiket = document.toObject(Tiket.class);
                            if (tiket != null) {
                                listener.onTasksFetched(tiket);
                            }
                        }
                    }
                });
    }

    public interface OnTasksFetchedListener {
        void onTasksFetched(List<Tiket> tasks);
        void onError(Exception e);
    }
    public interface OnTiketsFetchedListener {
        void onTasksFetched(Tiket tasks);
        void onError(Exception e);
    }
}
