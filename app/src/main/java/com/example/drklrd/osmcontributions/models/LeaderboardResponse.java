package com.example.drklrd.osmcontributions.models;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by drklrd on 10/10/17.
 */

public class LeaderboardResponse {

    @SerializedName("leaderboard")
    private List<Leader> leaderboard;

    public List<Leader> getLeaders(){
        return leaderboard;
    }

    public void setLeaders(List<Leader> leaderboard){
        this.leaderboard = leaderboard;
    }
}
