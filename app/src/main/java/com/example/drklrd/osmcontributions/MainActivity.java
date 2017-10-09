package com.example.drklrd.osmcontributions;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import com.example.drklrd.osmcontributions.models.ContributionsResponse;
import com.example.drklrd.osmcontributions.rest.ContributionsApiService;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


public class MainActivity extends AppCompatActivity {

    private static Retrofit retrofit = null;
    public static final String BASE_URL = "https://osm-api-explorer.herokuapp.com/";
    private TextView buildings;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        buildings = (TextView) findViewById(R.id.buildings);

        if(retrofit == null){
            retrofit = new Retrofit.Builder()
                        .baseUrl(BASE_URL)
                        .addConverterFactory(GsonConverterFactory.create())
                        .build();
        }

        ContributionsApiService contributionsApiService = retrofit.create(ContributionsApiService.class);

        Call<ContributionsResponse> call = contributionsApiService.getUserContributions();
        call.enqueue(new Callback<ContributionsResponse>() {
            @Override
            public void onResponse(Call<ContributionsResponse> call, Response<ContributionsResponse> response) {
                buildings.setText(String.valueOf(response.body().getTotalBuildingCount()));
            }

            @Override
            public void onFailure(Call<ContributionsResponse> call, Throwable t) {
                Log.i("NICE",String.valueOf(t));
            }
        });


    }
}
