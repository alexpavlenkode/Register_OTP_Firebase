package com.example.companies.repository;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.LocationManager;
import android.util.Log;

import androidx.core.app.ActivityCompat;
import androidx.databinding.adapters.NumberPickerBindingAdapter;
import androidx.lifecycle.LiveData;

import com.example.companies.SharedViewModel;
import com.google.android.gms.maps.model.LatLng;

import java.util.ArrayList;
import java.util.List;

public class Location {
    private Context context;
    private Activity activity;
    private boolean locationPermissionGranted;
    private LatLng userLocation;
    private static final int LOCATION_PERMISSION_REQUEST_CODE = 1;

    public void init(Context context,Activity activity) {
        this.context = context;
        this.activity = activity;
        checkLocationPermission(activity);
    }

    public Boolean isLocationPermissionGranted() {
        return locationPermissionGranted;
    }

    public void setMyLocation(){

    }
    public LatLng getMyLocation(){
        return userLocation;
    }

    public void fetchUserLocation() {
        if (context != null && locationPermissionGranted) {
            LocationManager locationManager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
            if (locationManager != null) {
                android.location.Location location = null;
                try {
                    location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
                } catch (SecurityException e) {
                    e.printStackTrace();
                }
                if (location != null) {
                    LatLng latLng = new LatLng(location.getLatitude(), location.getLongitude());
                    userLocation = latLng;
                }
            }
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
                locationPermissionGranted = true;
                fetchUserLocation();
            }
        }
    }
}
