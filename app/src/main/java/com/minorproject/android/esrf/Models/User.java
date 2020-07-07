package com.minorproject.android.esrf.Models;



import com.google.firebase.database.IgnoreExtraProperties;
import com.minorproject.android.esrf.Helping_Classes.statics;

import java.io.Serializable;



@IgnoreExtraProperties
public class User implements Serializable {
    public String name;
    public Double longitude;
    public Double latitude;
    public Double altitude;
    public String number;
    public String email;
    public EmergencyContact er = new EmergencyContact();
    public String bloodgroup;
    public String token;



    public User(){

    }


    public User(String name, String email, Double latitude, Double longitude, Double altitude, String number,String n1,String n2,String no1,String no2,String bloodgroup) {
        this.name = name;
        this.email = email;
        this.latitude = latitude;
        this.longitude = longitude;
        this.altitude = altitude;
        this.number = number;
        this.er.name1 = n1;
        this.er.name2 = n2;
        this.er.number1 = no1;
        this.er.number2 = no2;
        this.bloodgroup = bloodgroup;
        if (statics.token != null)
        {
            this.token = statics.token;
        }
    }

}
