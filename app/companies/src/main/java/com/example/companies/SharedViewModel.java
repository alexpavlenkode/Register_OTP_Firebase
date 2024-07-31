package com.example.companies;


import static androidx.core.app.PendingIntentCompat.getActivity;

import android.Manifest;
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
import com.example.registerotp.utils.FirebaseUtil;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.List;

public class SharedViewModel extends ViewModel {
    private static final String TAG = "SharedViewModel";
    private FirebaseAuth mAuth;
    private FirebaseUser user;
    private FirebaseFirestore db;
    private final MutableLiveData<FirmenModel> userProfile = new MutableLiveData<>();
    private final FirebaseFirestore firestore = FirebaseFirestore.getInstance();

    private MutableLiveData<LatLng> userLocation = new MutableLiveData<>();
    private final MutableLiveData<Boolean> locationPermissionGranted = new MutableLiveData<>(false);
    private static final int LOCATION_PERMISSION_REQUEST_CODE = 1;
    private Context context;
    private Activity activity;

    public void init(Context context,Activity activity) {
        this.context = context;
        this.activity = activity;
        checkLocationPermission(activity);
        if (locationPermissionGranted.getValue() != null && locationPermissionGranted.getValue()) {
            fetchUserLocation();
        }
    }

    private void checkLocationPermission(Activity activity) {
        if (context != null) {
            // Создаем список для хранения отсутствующих разрешений
            List<String> missingPermissions = new ArrayList<>();

            // Проверяем разрешение на доступ к местоположению
            if (ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                missingPermissions.add(Manifest.permission.ACCESS_FINE_LOCATION);
            }

            if (ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                missingPermissions.add(Manifest.permission.ACCESS_COARSE_LOCATION);
            }

            // Если есть отсутствующие разрешения
            if (!missingPermissions.isEmpty()) {
                // Устанавливаем флаг в false
                ActivityCompat.requestPermissions(activity, missingPermissions.toArray(new String[0]), LOCATION_PERMISSION_REQUEST_CODE);

                // Выводим отсутствующие разрешения
                Log.e("PermissionCheck", "Missing permissions: " + missingPermissions.toString());

                // Можно также уведомить пользователя о необходимости предоставить разрешения
                // Например, отображая сообщение или диалог
            } else {
                // Если все разрешения есть, устанавливаем флаг в true и получаем местоположение
                locationPermissionGranted.setValue(true);
                fetchUserLocation();
            }
        }
    }
    public void fetchUserLocation() {
        if (context != null && locationPermissionGranted.getValue() != null && locationPermissionGranted.getValue()) {
            LocationManager locationManager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
            if (locationManager != null) {
                Location location = null;
                try {
                    location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
                } catch (SecurityException e) {
                    e.printStackTrace();
                }
                if (location != null) {
                    LatLng latLng = new LatLng(location.getLatitude(), location.getLongitude());
                    userLocation.setValue(latLng);
                }
            }
        }
    }

    public LiveData<FirmenModel> getUserProfile() {
        return userProfile;
    }
    public void setUserProfile(FirmenModel profile) {
        userProfile.setValue(profile);
    }
    public LiveData<LatLng> getUserLocation() {
        return userLocation;
    }
    public LiveData<Boolean> isLocationPermissionGranted() {
        return locationPermissionGranted;
    }

    // Метод для обновления местоположения, если разрешения уже есть
    public void updateLocation() {
        if (locationPermissionGranted.getValue() != null && locationPermissionGranted.getValue()) {
            fetchUserLocation();
        }
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
                            Log.d("Start Connect","to Firestore");
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
