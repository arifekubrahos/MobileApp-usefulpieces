package com.example.arife.gpsaddfirebase;

/**
 * Created by Arife on 14.08.2017.
 */

public class GPSModel {
    private double latitude;
    private double langitude;
    private String name;

    public GPSModel(){

    }
    public GPSModel(double latitude, double langitude, String name) {
        this.latitude = latitude;
        this.langitude = langitude;
        this.name = name;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLangitude() {
        return langitude;
    }

    public void setLangitude(double langitude) {
        this.langitude = langitude;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
