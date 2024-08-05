package com.example.companies.adapter;


import android.util.Log;

import com.google.firebase.Timestamp;
import com.google.firebase.firestore.GeoPoint;
import com.google.android.gms.maps.model.LatLng;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class Tiket {
    //public int imageResId; // Ресурс изображения иконки
    private String ticketId;
    private String userId;
    private String title; // Заголовок
    private String description; // Описание
    private int views; // Количество просмотров
    public Timestamp date; // Дата публикации
    private Timestamp start_time;
    private Timestamp end_time;
    private String adresse; // Адрес
    private int urgency; // Срочность исполнения
    private int status; // Количество просмотров
    private GeoPoint geopoint; // Координаты местоположения
    private  String distance; //Расстояние до места
    private List<String> chats;



    // Пустой конструктор необходим для Firestore
    public Tiket() {

    }


    public String getTicketId() {
        return ticketId;
    }



    public Tiket(int imageResId,
                 String ticketId,
                 String userId,
                 String title,
                 String description,
                 int views,
                 Timestamp date,
                 String adresse,
                 int status,
                 int urgency,
                 GeoPoint geopoint,
                 Timestamp start_time, Timestamp end_time,
                 List<String> chats,
                 String distance) {
        //this.imageResId = imageResId;
        this.ticketId = ticketId;
        this.userId = userId;
        this.title = title;
        this.description = description;
        this.views = views;
        this.date = date;
        this.status = status; // 1, 2 , 3
        this.adresse = adresse;
        this.urgency = urgency; //1 , 2 , 3
        this.geopoint = geopoint;
        this.start_time = start_time; // Период времени для посещения на КФ
        this.end_time = end_time;   // Период времени для посещения на КФ
        this.chats = chats;
        this.distance = distance;
    }

    /*//public void setImageResId(int imageResId) {
        //this.imageResId = imageResId;
    //}

    public int getImageResId() {
        return imageResId;
    }*/

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }
    public List<String> getChats() {
        return chats;
    }

    public Timestamp getEnd_time() {
        return end_time;
    }

    public void setEnd_time(Timestamp end_time) {
        this.end_time = end_time;
    }
    public String getKvDate(){
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd MMM", Locale.getDefault());
        Date startDate = getStart_time().toDate();
        Date endDate = getEnd_time().toDate();

        String startDateString = dateFormat.format(startDate);
        String endDateString = dateFormat.format(endDate);
        return startDateString + " - " + endDateString;
    }
    public String getKvTime(){
        SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm", Locale.getDefault());
        Date startTime = getStart_time().toDate();
        Date endTime = getEnd_time().toDate();
        String startTimeString = timeFormat.format(startTime);
        String endTimeString = timeFormat.format(endTime);
        return startTimeString + " - " + endTimeString;
    }
    public Timestamp getStart_time() {
        return start_time;
    }

    public void setStart_time(Timestamp start_time) {
        this.start_time = start_time;
    }

    public GeoPoint getGeopoint() {
        return geopoint;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public int getViews() {
        return views;
    }

    public String getTimeAgo(){
        return calculateTimeAgo(date);
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

    //Получаем локацию содания тикета
    public GeoPoint getLocation() {
        return geopoint;
    }
    //Устанавливаем локацию создания тикета
    public void setLocation(GeoPoint locationUser) {
        this.geopoint = locationUser;
    }
    public void setDistance(LatLng locationCompany){
        this.distance = getDistanceTo(locationCompany);
    }
    public String getDistance(){
        return distance;
    }



    // Метод для получения расстояния от переданных координат пользователя
    public String getDistanceTo(LatLng CompanyLocation) {
        if (CompanyLocation == null || geopoint == null) {
            return "-1"; // Если координаты не заданы, возвращаем -1 или какое-то значение по умолчанию
        }

        LatLng taskLocation = new LatLng(geopoint.getLatitude(), geopoint.getLongitude());
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
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
        return R * c;
    }

    // Метод для получения времени в формате "X дней назад", "X часов назад" и т.д.
    public static String calculateTimeAgo(Timestamp timestamp) {

        Date date = convertTimestampToDate(timestamp);
        Log.d("TiketDate", "Timestamp " + timestamp);
        Log.d("TiketDate", "Date " + date);
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
    // Метод для конвертации Timestamp в Date
    private static Date convertTimestampToDate(Timestamp timestamp) {
        return timestamp != null ? timestamp.toDate() : null;
    }

}