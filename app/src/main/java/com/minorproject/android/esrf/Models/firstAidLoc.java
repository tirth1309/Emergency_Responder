package com.minorproject.android.esrf.Models;

import com.google.firebase.database.IgnoreExtraProperties;

import java.io.Serializable;

@IgnoreExtraProperties
public class firstAidLoc implements Serializable,Comparable {
    public String name;
    public Double latitude;
    public Double longitude;
    public String type;
    public float distance;

    public firstAidLoc(String name,Double latitude,Double longitude,String type){
        this.name = name;
        this.latitude = latitude;
        this.longitude = longitude;
        this.type =  type;
    }
    public firstAidLoc(){

    }

    @Override
    public int compareTo(Object o) {
        float compareDist = ((firstAidLoc)o).distance;
        return (int)(this.distance - compareDist);
    }

}
