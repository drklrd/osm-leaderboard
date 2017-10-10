package com.example.drklrd.osmcontributions;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.SearchView;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.drklrd.osmcontributions.models.ContributionsResponse;
import com.example.drklrd.osmcontributions.rest.ContributionsApiService;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Iterator;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


public class MainActivity extends AppCompatActivity {

    private static Retrofit retrofit = null;
    public static final String BASE_URL = "https://osm-api-explorer.herokuapp.com/";

    private TextView buildings;
    private TextView roads;
    private TextView changesets;
    private ProgressBar progressBar;
    private ListView hashtags;
    private SearchView searchbar;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        buildings = (TextView) findViewById(R.id.buildings);
        roads = (TextView) findViewById(R.id.roads);
        changesets = (TextView) findViewById(R.id.changeset);
        progressBar = (ProgressBar) findViewById(R.id.progressBar);
        hashtags = (ListView) findViewById(R.id.hashtags);
        searchbar = (SearchView) findViewById(R.id.searchbar);

        searchbar.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                Log.i("Submitted",s);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                return false;
            }
        });

        final ArrayList<String> hashes= new ArrayList<String>();
        final ArrayAdapter adapter = new ArrayAdapter(this,android.R.layout.simple_list_item_1,hashes);


        if(retrofit == null){
            retrofit = new Retrofit.Builder()
                        .baseUrl(BASE_URL)
                        .addConverterFactory(GsonConverterFactory.create())
                        .build();
        }

        ContributionsApiService contributionsApiService = retrofit.create(ContributionsApiService.class);

        Call<ContributionsResponse> call = contributionsApiService.getUserContributions();

        progressBar.setVisibility(View.VISIBLE);
        call.enqueue(new Callback<ContributionsResponse>() {
            @Override
            public void onResponse(Call<ContributionsResponse> call, Response<ContributionsResponse> response) {
                progressBar.setVisibility(View.INVISIBLE);
                buildings.setText(String.valueOf(response.body().getTotalBuildingCount()));
                roads.setText(String.format("%.2f",(response.body().getTotalRoad())) + "km");
                changesets.setText(String.valueOf(response.body().getChangeset()) );
                JSONObject obj = new JSONObject(response.body().getHashtags());

                Iterator<String> iter = obj.keys();
                while(iter.hasNext()){
                    String key = iter.next();
                    try {
                        Object value = obj.get(key);
                        hashes.add(String.valueOf(key) + " : " + String.valueOf(value));
                    }
                    catch(Exception e){
                        Log.i("Error","Error");
                    }
                }

                hashtags.setAdapter(adapter);
            }

            @Override
            public void onFailure(Call<ContributionsResponse> call, Throwable t) {
                Log.i("NICE",String.valueOf(t));
            }
        });
    }


}
