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

public class MainActivity extends AppCompatActivity {


    //    private static final String URL_DATA = "http://localhost:8080/beers";
    private static final String URL_DATA = "http://10.0.2.2:8080/beers";

    private BottomNavigationView bottomNavigation;
    private Button test_Button;
    private String test_string;

    private ArrayList<JSONObject> mbeer_data = new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        bottomNavigation = findViewById(R.id.bottomNavigation);
        test_Button = (Button) findViewById(R.id.test_button);

        if (getIntent().getStringExtra("EXTRA_VALUE") == null){
            test_string = "Virkar ekki";
        } else {
            test_string = getIntent().getStringExtra("EXTRA_VALUE");
        }

        test_Button.setOnClickListener(new View.OnClickListener() {
             @Override
             public void onClick(View view) {
                 Toast.makeText(MainActivity.this, test_string, Toast.LENGTH_SHORT).show();
             }
        });

        Menu menu = bottomNavigation.getMenu();
        MenuItem menuItem = menu.getItem(0);
        menuItem.setChecked(true);

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

        if(mbeer_data.size() == 0){
            loadBeerData();
        }


    }

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
                            System.out.println("Lengd: " + jsonArray.length());
                            System.out.println(jsonArray.getJSONObject(1).get("beerId"));
                            System.out.println("Náði að sækja");

                            // add the beers to ArrayList of beer
                            for (int i = 0; i < jsonArray.length(); i++) {
                                mbeer_data.add(jsonArray.getJSONObject(i));
                            }

                            System.out.println(mbeer_data.get(0));
                            System.out.println(mbeer_data.size());


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

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }
}
