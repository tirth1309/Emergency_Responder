package com.minorproject.android.esrf.Models;

import com.google.firebase.database.IgnoreExtraProperties;
import com.minorproject.android.esrf.Models.LatLng;

import java.io.Serializable;
import java.util.ArrayList;

@IgnoreExtraProperties
public class Location implements Serializable {
    public String name;
    public ArrayList<LatLng> coords;

    public Location(){

    }

    public Location(String name,ArrayList<LatLng> coords){
        this.name = name;
        this.coords = new ArrayList<>();
        this.coords = coords;

    }
}
