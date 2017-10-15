package com.example.drklrd.osmcontributions.rest;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;
import retrofit2.Call;
import com.example.drklrd.osmcontributions.models.ContributionsResponse;

/**
 * Created by drklrd on 10/9/17.
 */

public interface ContributionsApiService {

    @GET("api/v1/stats/{id}")
    Call<ContributionsResponse> getUserContributions(@Path("id") String id);
}

