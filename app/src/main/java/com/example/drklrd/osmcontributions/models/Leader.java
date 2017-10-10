package com.example.drklrd.osmcontributions.models;

import com.google.gson.annotations.SerializedName;

/**
 * Created by drklrd on 10/10/17.
 */

public class Leader {

    @SerializedName("name")
    private String name;

    @SerializedName("changesets")
    private int changesets;

    @SerializedName("roads")
    private float roads;

    @SerializedName("buildings")
    private int buildings;

    @SerializedName("edits")
    private int edits;


    public String getName(){
        return name;
    }

    public void setName(String name){
        this.name = name;
    }

    public  int getChangesets(){
        return changesets;
    }

    public void setChangesets(int changesets){
        this.changesets = changesets;
    }

    public int getBuildings(){
        return buildings;
    }

    public void setBuildings(int buildings){
        this.buildings = buildings;
    }

    public int getEdits(){
        return  edits;
    }

    public  void setEdits(int edits){
        this.edits = edits;
    }

    public float getRoads(){
        return roads;
    }

    public void  setRoads(float roads){
        this.roads = roads;
    }
}