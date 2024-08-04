package com.example.companies;


import static androidx.core.app.PendingIntentCompat.getActivity;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.common.model.FirmenModel;
import com.example.common.utils.FirestoreHelper;
import com.example.companies.ui.chat.PendingChatCreation;
import com.example.registerotp.utils.FirebaseUtil;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.example.companies.adapter.Tiket;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.List;

@SuppressLint("StaticFieldLeak")
public class SharedViewModel extends ViewModel {
    private static final String TAG = "SharedViewModel";
    private FirebaseAuth mAuth;
    private FirebaseUser user;
    private FirebaseFirestore db;

    private final FirestoreHelper firestoreHelper = new FirestoreHelper();
    private final MutableLiveData<FirmenModel> userProfile = new MutableLiveData<>();
    private final MutableLiveData<Tiket> selectedTiket = new MutableLiveData<>();
    private final MutableLiveData<LatLng> userLocation = new MutableLiveData<>();
    private final MutableLiveData<List<String>> companyChats = new MutableLiveData<>();
    private MutableLiveData<PendingChatCreation> pendingChatCreation = new MutableLiveData<>();


    public LiveData<PendingChatCreation> getPendingChatCreation() {
        return pendingChatCreation;
    }
    public void setPendingChatCreation(PendingChatCreation pendingChatCreation) {
        this.pendingChatCreation.setValue(pendingChatCreation);
    }
    public void clearPendingChatCreation() {
        this.pendingChatCreation.setValue(null);
    }
    public void selectTiket(Tiket tiket) {
        selectedTiket.setValue(tiket);
    }
    public void selectProfile(FirmenModel firmenModel) {
        userProfile.setValue(firmenModel);
    }
    public LiveData<Tiket> getSelectedTiket() {
        return selectedTiket;
    }
    public LiveData<FirmenModel> getUserProfile() {
        return userProfile;
    }
    public void setUserProfile(FirmenModel profile) {
        userProfile.setValue(profile);
    }
    public LiveData<LatLng> getCompanyLocation() {
        return userLocation;
    }
    public LiveData<List<String>> getCompanyChats() {
        return companyChats;
    }

    public void setCompanyLocation(LatLng location) {
        Log.e("setCompanyLocation", "setCompanyLocation Seted ");
        userLocation.setValue(location);
    }

    public void updateTicketChats(String ticketId, List<String> updatedChats) {
        firestoreHelper.updateTicketChats(ticketId, updatedChats, new FirestoreHelper.OnUpdateCompleteListener() {
            @Override
            public void onUpdateComplete() {
                compareAndUpdateChats(ticketId);
            }

            @Override
            public void onError(Exception e) {
                Log.e(TAG, "Error updating ticket chats", e);
            }
        });
    }
    private void compareAndUpdateChats(String ticketId) {
        firestoreHelper.getChatsFromTicket(ticketId, new FirestoreHelper.OnChatsFetchedListener() {
            @Override
            public void onChatsFetched(List<String> ticketChats) {
                FirmenModel currentProfile = userProfile.getValue();
                if (currentProfile != null) {
                    List<String> currentChats = currentProfile.getChats();
                    if (currentChats == null) {
                        currentChats = new ArrayList<>();
                    }

                    // Сравните текущие чаты с чатиками из тикета
                    if (!currentChats.equals(ticketChats)) {
                        // Если есть изменения, обновите чаты в userProfile
                        currentProfile.setChats(ticketChats);
                        setUserProfile(currentProfile);
                    }
                }
            }

            @Override
            public void onError(Exception e) {
                Log.e(TAG, "Error fetching chats from ticket", e);
            }
        });
    }
    // Получение чатов компании
    public void getChatsFromCompany(String companyId) {
        firestoreHelper.getChatsFromCompany(companyId, new FirestoreHelper.OnChatsFetchedListener() {
            @Override
            public void onChatsFetched(List<String> companyChats) {
                FirmenModel currentProfile = userProfile.getValue();
                if (currentProfile != null) {
                    List<String> currentChats = currentProfile.getChats();
                    if (currentChats == null) {
                        currentChats = new ArrayList<>();
                    }

                    // Если чаты компании изменились, обновите профиль
                    if (!currentChats.equals(companyChats)) {
                        currentProfile.setChats(companyChats);
                        setUserProfile(currentProfile);
                    }
                }
            }

            @Override
            public void onError(Exception e) {
                Log.e(TAG, "Error fetching chats from company", e);
            }
        });
    }

    public void fetchUserProfile() {
        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();

        if(FirebaseUtil.isLoggedIn()){
            FirebaseUser currentUser = mAuth.getCurrentUser();
            String uid = currentUser.getUid();
            DocumentReference userRef = db.collection("companies").document(uid);
            userRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                    if (task.isSuccessful()) {
                        DocumentSnapshot document = task.getResult();
                        if (document.exists()) {
                            //Теперь нужно создать FirmenModel
                            FirmenModel profile = document.toObject(FirmenModel.class);
                            if(profile != null){
                                setUserProfile(profile);
                            }else{
                                setUserProfile(null);
                            }
                        }
                    }
                }

            });

        }
    }
}
