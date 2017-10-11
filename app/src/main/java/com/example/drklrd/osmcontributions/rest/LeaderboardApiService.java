package com.example.drklrd.osmcontributions.rest;
import retrofit2.http.GET;
import retrofit2.Call;
import retrofit2.http.Path;

import com.example.drklrd.osmcontributions.models.HashtagsResponse;
import com.example.drklrd.osmcontributions.models.LeaderboardResponse;

/**
 * Created by drklrd on 10/9/17.
 */

public interface LeaderboardApiService {

    @GET("api/v1/leaderboard/{hashtag}")
    Call<LeaderboardResponse> getLeaderboard(@Path("hashtag") String hashtag);

    @GET("api/v1/hashtags")
    Call<HashtagsResponse> getHashtags();
}
