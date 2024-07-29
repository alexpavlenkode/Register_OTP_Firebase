package com.example.companies.adapter;

import java.util.Date;

public class Task {
    public int imageResId; // Ресурс изображения иконки
    public String title; // Заголовок
    public String description; // Описание
    public int views; // Количество просмотров
    public Date date; // Дата публикации
    public String distance; // Расстояние до места

    public Task(int imageResId, String title, String description, int views, Date date, String distance) {
        this.imageResId = imageResId;
        this.title = title;
        this.description = description;
        this.views = views;
        this.date = date;
        this.distance = distance;
    }

    public int getImageResId() {
        return imageResId;
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

    public Date getDate() {
        return date;
    }

    public String getDistance() {
        return distance;
    }
}