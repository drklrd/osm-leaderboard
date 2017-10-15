package com.example.drklrd.osmcontributions;

import android.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.drklrd.osmcontributions.models.ContributionsResponse;
import com.example.drklrd.osmcontributions.models.HashtagsResponse;
import com.example.drklrd.osmcontributions.rest.ApiHelper;
import com.example.drklrd.osmcontributions.rest.ContributionsApiService;
import com.example.drklrd.osmcontributions.rest.LeaderboardApiService;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class Profile extends AppCompatActivity {

    private static Retrofit retrofit = null;
    private ProgressBar progressBar;
    private TextView buildings;
    private TextView roads;
    private TextView changesets;
    private TextView user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        buildings = (TextView) findViewById(R.id.buildings);
        roads = (TextView) findViewById(R.id.roads);
        changesets = (TextView) findViewById(R.id.changeset);
        user = (TextView) findViewById(R.id.user);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        progressBar = (ProgressBar) findViewById(R.id.progressBar);
        retrofit = new Retrofit.Builder()
                .baseUrl(ApiHelper.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        ContributionsApiService contributionsApiService = retrofit.create(ContributionsApiService.class);
        Call<ContributionsResponse> callContributions = contributionsApiService.getUserContributions(getIntent().getStringExtra("user"));

        progressBar.setVisibility(View.VISIBLE);

        callContributions.enqueue(new Callback<ContributionsResponse>() {
            @Override
            public void onResponse(Call<ContributionsResponse> call, Response<ContributionsResponse> response) {
                progressBar.setVisibility(View.INVISIBLE);
                buildings.setText(String.valueOf(response.body().getTotalBuildingCount()));
                roads.setText(String.format("%.2f",(response.body().getTotalRoad())) + "km");
                changesets.setText(String.valueOf(response.body().getChangeset()));
                user.setText(String.valueOf(response.body().getName()));
            }

            @Override
            public void onFailure(Call<ContributionsResponse> call, Throwable t) {

            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return true;
    }
}
