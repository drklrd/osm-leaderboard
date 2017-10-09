package com.example.drklrd.osmcontributions.models;
import android.util.Log;

import com.google.gson.annotations.SerializedName;

/**
 * Created by drklrd on 10/9/17.
 */

public class ContributionsResponse {

    @SerializedName("total_building_count_add")
    private int totalBuildingCount;


    public int getTotalBuildingCount(){
        return totalBuildingCount;
    }

    public void setTotalBuildingCount(int totalBuildingCount){
        this.totalBuildingCount = totalBuildingCount;
    }

}
