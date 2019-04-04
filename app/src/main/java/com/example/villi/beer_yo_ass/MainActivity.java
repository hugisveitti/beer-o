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
    private ArrayList<JSONObject> mbeer_data = new ArrayList<>();
    private Button reflexButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        bottomNavigation = findViewById(R.id.bottomNavigation);
        mUser_textview = findViewById(R.id.user_textview);
        reflexButton = findViewById(R.id.reflexButton);

        Menu menu = bottomNavigation.getMenu();
        MenuItem menuItem = menu.getItem(0);
        menuItem.setChecked(true);


        if (UserActivity.user == null) {
            mUser_textview.setText("Notandi ekki skráður inn");
        } else {
            mUser_textview.setText("Velkominn " + UserActivity.user);
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
        System.out.println("Wassupfool");
        System.out.println(BeerData.getBeerListSize());

        if(mbeer_data.size() == 0){
            mbeer_data = BeerData.loadBeerData(this);
        }

    }

}
