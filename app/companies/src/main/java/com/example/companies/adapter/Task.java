package com.example.companies.adapter;


import android.util.Log;

import com.google.firebase.Timestamp;
import com.google.firebase.firestore.GeoPoint;
import com.google.android.gms.maps.model.LatLng;

public class Task {
    //public int imageResId; // Ресурс изображения иконки
    public String title; // Заголовок
    public String description; // Описание
    public int views; // Количество просмотров
    public Timestamp date; // Дата публикации
    public String adresse; // Расстояние до места
    public int urgency; // Срочность исполнения
    public int status; // Количество просмотров
    public GeoPoint geopoint; // Координаты местоположения
    public  String distance;


    // Пустой конструктор необходим для Firestore
    public Task() {

    }



    public Task(int imageResId,
                String title,
                String description,
                int views,
                Timestamp date,
                String adresse,
                int status,
                int urgency,
                GeoPoint geopoint) {
        //this.imageResId = imageResId;
        this.title = title;
        this.description = description;
        this.views = views;
        this.date = date;
        this.adresse = adresse;
        this.urgency = urgency;
        this.geopoint = geopoint;
    }

    /*//public void setImageResId(int imageResId) {
        //this.imageResId = imageResId;
    //}

    public int getImageResId() {
        return imageResId;
    }*/

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public int getViews() {
        return views;
    }

    public Timestamp getTimestamp() {
        return date;
    }

    public String getAdresse() {
        return adresse;
    }
    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public int getUrgency() {
        return urgency;
    }

    public void setUrgency(int urgency) {
        this.urgency = urgency;
    }


    public GeoPoint getLocation() {
        return geopoint;
    }

    public void setLocation(GeoPoint location) {
        this.geopoint = location;
    }

    // Метод для получения расстояния от переданных координат пользователя
    public String getDistanceTo(LatLng CompanyLocation, GeoPoint userLocation) {
        if (CompanyLocation == null || userLocation == null) {
            return "-1"; // Если координаты не заданы, возвращаем -1 или какое-то значение по умолчанию
        }

        LatLng taskLocation = new LatLng(userLocation.getLatitude(), userLocation.getLongitude());
        double distanceinKm =  calculateDistance(CompanyLocation.latitude, CompanyLocation.longitude, taskLocation.latitude, taskLocation.longitude);
        String formattedDistance = formatDistance(distanceinKm);
        return formattedDistance;
    }
    private String formatDistance(double distanceInKm) {
        if (distanceInKm < 1) {
            int distanceInMeters = (int) (distanceInKm * 1000);
            return distanceInMeters + " m"; // "метры"
        } else {
            return String.format("%.2f km", distanceInKm); // "километры"
        }
    }
    // Вспомогательный метод для расчета расстояния
    private double calculateDistance(double lat1, double lon1, double lat2, double lon2) {
        final int R = 6371; // Радиус Земли в км
        double dLat = Math.toRadians(lat2 - lat1);
        double dLon = Math.toRadians(lon2 - lon1);
        double a = Math.sin(dLat / 2) * Math.sin(dLat / 2) +
                Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2)) *
                        Math.sin(dLon / 2) * Math.sin(dLon / 2);
        Log.e("Distance", "Disstance from a " + a);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
        Log.e("Distance", "Disstance from c " + c);
        return R * c; // Расстояние в км
    }

    /*// Метод для получения строки расстояния
    public String getFormattedDistance(LatLng CompanyLocation) {
        double distance = getDistanceTo(CompanyLocation);
        if (distance < 0) {
            return "Неизвестно"; // Если расстояние не рассчитано
        } else if (distance < 1) {
            return String.format("%.0f м", distance * 1000); // Если расстояние менее 1 км, выводим в метрах
        } else {
            return String.format("%.2f км", distance); // В противном случае, выводим в км
        }
    }*/


}