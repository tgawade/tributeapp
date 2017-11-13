package com.android.martyrapp.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import java.util.List;

/**
 * Created by rgundal on 11/6/2017.
 */

public class Martyr {


    @SerializedName("martyr")
    @Expose
    private List<MartyrPojo> martyr = null;

    public List<MartyrPojo> getMartyr() {
        return martyr;
    }

    public void setMartyr(List<MartyrPojo> martyr) {
        this.martyr = martyr;
    }
}
