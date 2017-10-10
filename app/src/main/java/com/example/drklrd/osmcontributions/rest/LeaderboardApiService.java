package com.example.drklrd.osmcontributions.rest;
import retrofit2.http.GET;
import retrofit2.Call;
import com.example.drklrd.osmcontributions.models.LeaderboardResponse;

/**
 * Created by drklrd on 10/9/17.
 */

public interface LeaderboardApiService {

    @GET("api/v1/leaderboard/ttkll")
    Call<LeaderboardResponse> getLeaderboard();
}
