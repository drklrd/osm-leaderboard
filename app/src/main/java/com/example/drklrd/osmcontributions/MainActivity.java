package com.example.drklrd.osmcontributions;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.widget.SearchView;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.drklrd.osmcontributions.models.ContributionsResponse;
import com.example.drklrd.osmcontributions.models.HashtagsResponse;
import com.example.drklrd.osmcontributions.models.Leader;
import com.example.drklrd.osmcontributions.models.LeaderboardResponse;
import com.example.drklrd.osmcontributions.rest.ContributionsApiService;
import com.example.drklrd.osmcontributions.rest.LeaderboardApiService;
import com.miguelcatalan.materialsearchview.MaterialSearchView;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;




public class MainActivity extends AppCompatActivity {

    MaterialSearchView searchView;

    private static Retrofit retrofit = null;
    public static final String BASE_URL = "https://osm-api-explorer.herokuapp.com/";

    private TextView buildings;
    private TextView roads;
    private TextView changesets;
    private ProgressBar progressBar;
    private ListView hashtags;

    ArrayList<String> hashes= new ArrayList<String>();
    ArrayAdapter adapter;


    public void searchViewCode(){
        searchView = (MaterialSearchView) findViewById(R.id.search_view);
        searchView.setEllipsize(true);
        searchView.setHint("Search hashtag for leaderboard");
        searchView.setOnQueryTextListener(new MaterialSearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                Retrofit retrofitleader = new Retrofit.Builder()
                        .baseUrl(BASE_URL)
                        .addConverterFactory(GsonConverterFactory.create())
                        .build();

                LeaderboardApiService leaderboardApiService = retrofit.create(LeaderboardApiService.class);
                Call<LeaderboardResponse> callleader = leaderboardApiService.getLeaderboard(query);
                progressBar.setVisibility(View.VISIBLE);
                callleader.enqueue(new Callback<LeaderboardResponse>() {
                    @Override
                    public void onResponse(Call<LeaderboardResponse> call, Response<LeaderboardResponse> response) {
                        progressBar.setVisibility(View.INVISIBLE);
                        List<Leader> leaders = response.body().getLeaders();
                        hashes.clear();
                        int editscount = 0;
                        int buildingscount = 0;
                        float roadscount = (float) 0.0;
                        for(int i =0 ; i< leaders.size(); i++){
                            hashes.add( (i+1) + ". " + String.valueOf(leaders.get(i).getName()) + " : " + String.valueOf(leaders.get(i).getEdits()) + " edits");
                            editscount = editscount + leaders.get(i).getEdits();
                            buildingscount = buildingscount + leaders.get(i).getBuildings();
                            roadscount = roadscount + leaders.get(i).getRoads();
                        }
                        TextView textView1 = (TextView) findViewById(R.id.textView1);
                        textView1.setText("Edits");
                        changesets.setText(String.valueOf(editscount));
                        roads.setText(String.format("%.2f",(roadscount)) + "km");
                        buildings.setText(String.valueOf(buildingscount));
                        adapter.notifyDataSetChanged();
                    }

                    @Override
                    public void onFailure(Call<LeaderboardResponse> call, Throwable t) {
                        progressBar.setVisibility(View.INVISIBLE);
                    }
                });
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_search,menu);
        MenuItem item = menu.findItem(R.id.action_search);
        searchView.setMenuItem(item);
        return true;
    }

    @Override
    public void onBackPressed() {
        if(searchView.isSearchOpen()){
            searchView.closeSearch();
        }else{
            super.onBackPressed();
        }

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        adapter = new ArrayAdapter(this,android.R.layout.simple_list_item_1,hashes);

        buildings = (TextView) findViewById(R.id.buildings);
        roads = (TextView) findViewById(R.id.roads);
        changesets = (TextView) findViewById(R.id.changeset);
        progressBar = (ProgressBar) findViewById(R.id.progressBar);
        hashtags = (ListView) findViewById(R.id.hashtags);
        searchViewCode();

        if(retrofit == null){
            retrofit = new Retrofit.Builder()
                        .baseUrl(BASE_URL)
                        .addConverterFactory(GsonConverterFactory.create())
                        .build();
        }

        ContributionsApiService contributionsApiService = retrofit.create(ContributionsApiService.class);
        LeaderboardApiService leaderboardApiService = retrofit.create(LeaderboardApiService.class);

        Call<ContributionsResponse> call = contributionsApiService.getUserContributions();
        final Call<HashtagsResponse> callHashtags = leaderboardApiService.getHashtags();


        progressBar.setVisibility(View.VISIBLE);
        call.enqueue(new Callback<ContributionsResponse>() {
            @Override
            public void onResponse(Call<ContributionsResponse> call, Response<ContributionsResponse> response) {

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

                callHashtags.enqueue(new Callback<HashtagsResponse>() {
                    @Override
                    public void onResponse(Call<HashtagsResponse> call, Response<HashtagsResponse> response) {
                        progressBar.setVisibility(View.INVISIBLE);
                        String[] hashtagsfromserver = response.body().getHashtags();
                        searchView.setSuggestions(hashtagsfromserver);
                    }

                    @Override
                    public void onFailure(Call<HashtagsResponse> call, Throwable t) {
                        progressBar.setVisibility(View.INVISIBLE);
                    }
                });
            }

            @Override
            public void onFailure(Call<ContributionsResponse> call, Throwable t) {
                progressBar.setVisibility(View.INVISIBLE);
                Log.i("NICE",String.valueOf(t));
            }
        });
    }
}
