package com.example.agrofarm.Models;

import com.google.firebase.database.ServerValue;

public class Crop {

    private String croptype,cropname,temparature,humidity,soilmosture,lightlevel,cropKey;
    //private int humi,soil,light;


    public Crop() {
    }

    public Crop(String croptype, String cropname, String temparature, String humidity, String soilmosture, String lightlevel, String cropKey) {
        this.croptype = croptype;
        this.cropname = cropname;
        this.temparature = temparature;
        this.humidity = humidity;
        this.soilmosture = soilmosture;
        this.lightlevel = lightlevel;
        this.cropKey = cropKey;
    }

    public String getCroptype() {
        return croptype;
    }

    public void setCroptype(String croptype) {
        this.croptype = croptype;
    }

    public String getCropname() {
        return cropname;
    }

    public void setCropname(String cropname) {
        this.cropname = cropname;
    }

    public String getTemparature() {
        return temparature;
    }

    public void setTemparature(String temparature) {
        this.temparature = temparature;
    }

    public String getHumidity() {
        return humidity;
    }

    public void setHumidity(String humidity) {
        this.humidity = humidity;
    }

    public String getSoilmosture() {
        return soilmosture;
    }

    public void setSoilmosture(String soilmosture) {
        this.soilmosture = soilmosture;
    }

    public String getLightlevel() {
        return lightlevel;
    }

    public void setLightlevel(String lightlevel) {
        this.lightlevel = lightlevel;
    }

    public String getCropKey() {
        return cropKey;
    }

    public void setCropKey(String cropKey) {
        this.cropKey = cropKey;
    }
}
