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
import android.view.Window;
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
    private static final String URL_DATA = "http://10.0.2.2:8080/beers";

    private BottomNavigationView bottomNavigation;
    private TextView mUser_textview;
    private ArrayList<JSONObject> mbeer_data = new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        bottomNavigation = findViewById(R.id.bottomNavigation);
        mUser_textview = findViewById(R.id.user_textview);

        Menu menu = bottomNavigation.getMenu();
        MenuItem menuItem = menu.getItem(0);
        menuItem.setChecked(true);


        if (LoginActivity.user == null) {
            mUser_textview.setText("Notandi ekki skráður inn");
        } else {
            mUser_textview.setText("Velkominn " + LoginActivity.user);
        }


        // Navigation bar to change activities
        bottomNavigation.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {

                switch (item.getItemId()){
                    case R.id.nav_home:
                        break;

                    case R.id.nav_search:
                        ArrayList<String> mbeer_data_string = new ArrayList<String>();

                        for(int i = 0; i < mbeer_data.size(); i++){
                            mbeer_data_string.add(mbeer_data.get(i).toString());
                        }

                        Intent intent1 = new Intent(MainActivity.this, SearchActivity.class);

                        intent1.putStringArrayListExtra("BEER_DATA", mbeer_data_string);
                        startActivity(intent1);
                        break;

                    case R.id.nav_my_page:
                        Intent intent2 = new Intent(MainActivity.this, LoginActivity.class);
                        startActivity(intent2);
                        break;
                }

                return false;
            }
        });

        // The data is only loaded if the data array is empty
        if(mbeer_data.size() == 0){
            loadBeerData();
        }


    }

    // This function is called to load the data
    private void loadBeerData() {
        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Loading data...");
        progressDialog.show();

        StringRequest stringRequest = new StringRequest(Request.Method.GET,
                URL_DATA,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        progressDialog.dismiss();
                        try {
                            JSONArray jsonArray = new JSONArray(response);
                            System.out.println(jsonArray.get(1));
                            System.out.println(jsonArray.getJSONObject(1).get("beerId"));
                            System.out.println("Náði að sækja");

                            // add the beers to ArrayList of beer
                            for (int i = 0; i < jsonArray.length(); i++) {
                                mbeer_data.add(jsonArray.getJSONObject(i));
                            }

                            System.out.println(mbeer_data.get(0));

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        progressDialog.dismiss();
                        System.out.println("Error response");
                        Toast.makeText(MainActivity.this, error.getMessage(), Toast.LENGTH_SHORT).show();

                    }
                });

        // The Volley package is used to make a request on the database
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }
}
