/**
 * The mainActivity is the greedingpage of the app. At the moment
 * it is rather empty but soon will have more features.
 * Now its main purpose is to load the data and send it along to the
 * searchpage.
 */

package com.example.villi.beer_yo_ass;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

// This is the activity that opens when you open the app
public class MainActivity extends AppCompatActivity {

    // To be able to fetch the data you need to run the beer-yo-ass backend
    // TODO: Set the backend up on Heroku
    //    private static final String URL_DATA = "http://localhost:8080/beers";
    private static final String URL_DATA = "https://beer-yo-ass-backend.herokuapp.com/beers";

    private BottomNavigationView bottomNavigation;
    private TextView mUser_textview;
    private TextView mScoreText;
    private TextView mNr1;
    private TextView mNr2;
    private TextView mNr3;

    private int[] mBestScore = {-1, -1 , -1};
    private String[] mBestScoreName = {"", "", ""};

    private static final String URL = "https://beer-yo-ass-backend.herokuapp.com/getAllGameScores";

    private ArrayList<JSONObject> mbeer_data = new ArrayList<>();
    private Button reflexButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        bottomNavigation = findViewById(R.id.bottomNavigation);
        mUser_textview = findViewById(R.id.user_textview);
        reflexButton = findViewById(R.id.reflexButton);
        mNr1 = (TextView) findViewById(R.id.nr1);
        mNr2 = (TextView) findViewById(R.id.nr2);
        mNr3 = (TextView) findViewById(R.id.nr3);
        Menu menu = bottomNavigation.getMenu();
        MenuItem menuItem = menu.getItem(0);
        menuItem.setChecked(true);

        MenuItem menuItem2 = menu.getItem(2);
        if (UserActivity.user == null) {
            mUser_textview.setText("Notandi ekki skráður inn");
            menuItem2.setTitle(getResources().getString(R.string.nav_login));
        } else {
            mUser_textview.setText("Velkominn " + UserActivity.user);
            menuItem2.setTitle(getResources().getString(R.string.nav_my_page));
        }

        reflexButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, ReflexActivity.class);
                startActivity(intent);


            }
        });
        // Navigation bar to change activities
        bottomNavigation.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {

                switch (item.getItemId()){
                    case R.id.nav_home:
                        break;

                    case R.id.nav_search:
                        Intent intent1 = new Intent(MainActivity.this, SearchActivity.class);
                        startActivity(intent1);
                        break;

                    case R.id.nav_my_page:
                        Intent intent2 = new Intent(MainActivity.this, UserActivity.class);
                        startActivity(intent2);
                        break;
                }

                return false;
            }
        });

        // The data is only loaded if the data array is empty
        mbeer_data = BeerData.getBeer_data();

        if(mbeer_data.size() == 0){
            mbeer_data = BeerData.loadBeerData(this);
        }

        loadHighscores();
    }

    private void loadHighscores() {
        StringRequest stringRequest = new StringRequest(Request.Method.GET,
                URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONArray jsonArray = new JSONArray(response);

                            // add the beers to ArrayList of beer
                            for (int i = 0; i < jsonArray.length(); i++) {
                                int thisScore = Integer.parseInt(String.valueOf(jsonArray.getJSONObject(i).get("score")));
                                if((thisScore < mBestScore[0] || mBestScore[0] == -1) && thisScore > 0){
                                    mBestScore[0] = thisScore;
                                    mBestScoreName[0] = String.valueOf(jsonArray.getJSONObject(i).get("username"));
                                }
                                else if((thisScore < mBestScore[1] || mBestScore[1] == -1) && thisScore > 0){
                                    mBestScore[1] = thisScore;
                                    mBestScoreName[1] = String.valueOf(jsonArray.getJSONObject(i).get("username"));
                                }
                                else if((thisScore < mBestScore[2] || mBestScore[2] == -1) && thisScore > 0){
                                    mBestScore[2] = thisScore;
                                    mBestScoreName[2] = String.valueOf(jsonArray.getJSONObject(i).get("username"));
                                }
                            }
                            updateScore();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                        System.out.println("Error response");
                        Toast.makeText(MainActivity.this, error.getMessage(), Toast.LENGTH_SHORT).show();

                    }
                });

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }

    private void updateScore() {
        if (mBestScore[0] > 0){
            mNr1.setText("1. " + mBestScoreName[0] + " - " + mBestScore[0]);
        }
        else{
            mNr1.setText("1. No top score");
        }
        if (mBestScore[1] > 0){
            mNr2.setText("2. " + mBestScoreName[1] + " - " + mBestScore[1]);
        }
        else{
            mNr2.setText("2. No top score");
        }
        if (mBestScore[2] > 0){
            mNr3.setText("3. " + mBestScoreName[2] + " - " + mBestScore[2]);
        }
        else{
            mNr3.setText("3. No top score");
        }
    }

}
