package com.minorproject.android.esrf.Models;

import com.google.firebase.database.IgnoreExtraProperties;

import java.io.Serializable;
@IgnoreExtraProperties
public class EmergencyContact implements Serializable {
    public String name1;
    public String name2;
    public String number1;
    public String number2;
}
