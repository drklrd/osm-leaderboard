package com.example.drklrd.osmcontributions;

import android.content.DialogInterface;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
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




public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

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

    private DrawerLayout drawerLayout;
    private ActionBarDrawerToggle toggle;
    private NavigationView navigationView;


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

    public void showAlert(String title, String message){
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);
        alertDialog.setTitle(title);
        alertDialog.setMessage(message);
        alertDialog.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.cancel();
            }
        });
        alertDialog.show();

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.navigation_drawer);

        drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        toggle = new ActionBarDrawerToggle(this,drawerLayout,R.string.open,R.string.close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        navigationView = (NavigationView) findViewById(R.id.navigation_view);
        navigationView.setNavigationItemSelectedListener(this);



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

//        ContributionsApiService contributionsApiService = retrofit.create(ContributionsApiService.class);
        LeaderboardApiService leaderboardApiService = retrofit.create(LeaderboardApiService.class);

//        Call<ContributionsResponse> call = contributionsApiService.getUserContributions();
        final Call<HashtagsResponse> callHashtags = leaderboardApiService.getHashtags();

        hashtags.setAdapter(adapter);

        progressBar.setVisibility(View.VISIBLE);

        callHashtags.enqueue(new Callback<HashtagsResponse>() {
            @Override
            public void onResponse(Call<HashtagsResponse> call, Response<HashtagsResponse> response) {
                progressBar.setVisibility(View.INVISIBLE);
                String[] hashtagsfromserver = response.body().getHashtags();
                searchView.setSuggestions(hashtagsfromserver);
                showAlert("Search leaderboard","Enter hashtag in searchbar to get the leaderboard");
            }

            @Override
            public void onFailure(Call<HashtagsResponse> call, Throwable t) {
                progressBar.setVisibility(View.INVISIBLE);
                showAlert("Error","There was an error fetching search tags");
            }
        });



//        call.enqueue(new Callback<ContributionsResponse>() {
//            @Override
//            public void onResponse(Call<ContributionsResponse> call, Response<ContributionsResponse> response) {
//
//                buildings.setText(String.valueOf(response.body().getTotalBuildingCount()));
//                roads.setText(String.format("%.2f",(response.body().getTotalRoad())) + "km");
//                changesets.setText(String.valueOf(response.body().getChangeset()) );
//                JSONObject obj = new JSONObject(response.body().getHashtags());
//
//                Iterator<String> iter = obj.keys();
//                while(iter.hasNext()){
//                    String key = iter.next();
//                    try {
//                        Object value = obj.get(key);
//                        hashes.add(String.valueOf(key) + " : " + String.valueOf(value));
//                    }
//                    catch(Exception e){
//                        Log.i("Error","Error");
//                    }
//                }
//                hashtags.setAdapter(adapter);
//
//
//            }
//
//            @Override
//            public void onFailure(Call<ContributionsResponse> call, Throwable t) {
//                progressBar.setVisibility(View.INVISIBLE);
//                Log.i("NICE",String.valueOf(t));
//            }
//        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(toggle.onOptionsItemSelected(item)){
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {

        int id = item.getItemId();

        switch (id){
            case R.id.about:
                Toast.makeText(getApplicationContext(),"About !",Toast.LENGTH_SHORT).show();
                break;
            default:

        }

        drawerLayout.closeDrawer(GravityCompat.START);

        return true;
    }
}
