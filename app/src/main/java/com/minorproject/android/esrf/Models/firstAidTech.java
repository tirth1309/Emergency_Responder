package com.minorproject.android.esrf.Models;

import com.google.firebase.database.IgnoreExtraProperties;

import java.io.Serializable;

@IgnoreExtraProperties
public class firstAidTech implements Serializable {
    public String name;
    public String text;
    public String url;

    public  firstAidTech(){

    }

    public firstAidTech(String name,String text,String url){
        this.name = name;
        this.text = text;
        this.url = url;
    }
}
