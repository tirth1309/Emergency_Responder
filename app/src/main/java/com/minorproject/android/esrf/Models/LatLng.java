package com.minorproject.android.esrf.Models;

import com.google.firebase.database.IgnoreExtraProperties;

@IgnoreExtraProperties
public class LatLng {
    public Double latitude,longitude;

    public LatLng(){

    }

    public LatLng(Double latitude,Double longitude){
        this.latitude = latitude;
        this.longitude = longitude;
    }
}
