package com.example.drklrd.osmcontributions.models;
import android.util.Log;

import com.google.gson.annotations.SerializedName;

/**
 * Created by drklrd on 10/9/17.
 */

public class ContributionsResponse {

    @SerializedName("total_building_count_add")
    private int totalBuildingCount;

    @SerializedName("total_road_km_add")
    private float totalRoad;

    @SerializedName("changeset_count")
    private int changeset;


    public int getTotalBuildingCount(){
        return totalBuildingCount;
    }

    public void setTotalBuildingCount(int totalBuildingCount){
        this.totalBuildingCount = totalBuildingCount;
    }

    public float getTotalRoad(){
        return totalRoad;
    }

    public void setTotalRoad(float totalRoad){
        this.totalRoad = totalRoad;
    }

    public int getChangeset(){
        return changeset;
    }

    public void setChangeset(int changeset){
        this.changeset = changeset;
    }

}
