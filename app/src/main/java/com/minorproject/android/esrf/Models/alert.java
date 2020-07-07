package com.minorproject.android.esrf.Models;

import com.google.firebase.database.IgnoreExtraProperties;

import java.io.Serializable;
import java.util.Date;
import java.util.Map;

@IgnoreExtraProperties
public class alert implements Serializable {
    public String vicName;
    public Double latitude;
    public Double longitude;
    public String locName;
    public Map<String,String> timeStamp;
    public String dateTime;

    public alert(){

    }

    public alert(String vicName,String locName,Double latitude,Double longitude,Map<String,String> timeStamp,String dateTime){
        this.vicName = vicName;
        this.locName = locName;
        this.latitude = latitude;
        this.longitude = longitude;
        this.timeStamp = timeStamp;
        this.dateTime = dateTime;
    }
}
