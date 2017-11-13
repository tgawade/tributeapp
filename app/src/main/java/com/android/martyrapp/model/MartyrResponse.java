package com.android.martyrapp.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class MartyrResponse {


    @SerializedName("martyr")
    @Expose
    Martyr martyrs=new Martyr();

    public Martyr getMartyrs() {
        return martyrs;
    }

    public void setMartyrs(Martyr martyrs) {
        this.martyrs = martyrs;
    }
}
