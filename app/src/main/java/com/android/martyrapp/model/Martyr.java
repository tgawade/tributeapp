package com.android.martyrapp.model;

import java.io.Serializable;

/**
 * Created by Lincoln on 15/01/16.
 */
public class Martyr implements Serializable{
    private String title, genre, year, imageName;

    public Martyr() {
    }

    public Martyr(String title, String genre, String year,String imageName) {
        this.title = title;
        this.genre = genre;
        this.year = year;
        this.imageName = imageName;

    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String name) {
        this.title = name;
    }

    public String getYear() {
        return year;
    }

    public void setYear(String year) {
        this.year = year;
    }

    public String getGenre() {
        return genre;
    }

    public void setGenre(String genre) {
        this.genre = genre;
    }

    public String getImageName() {
        return imageName;
    }

    public void setImageName(String imageName) {
        this.imageName = imageName;
    }
}
