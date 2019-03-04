package com.example.villi.beer_yo_ass;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
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

import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Array;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

public class SearchActivity extends AppCompatActivity {

//    private static final String URL_DATA = "http://localhost:8080/beers";
    private static final String URL_DATA = "http://10.0.2.2:8080/beers";

    private BottomNavigationView bottomNavigation;

    private static final String TAG = "SearchActivity";

    // variables for the json data
    private ArrayList<String> mbeer_id = new ArrayList<>();
    private ArrayList<String> mbeer_name = new ArrayList<>();
    private ArrayList<String> mbeer_volume = new ArrayList<>();
    private ArrayList<String> mbeer_price = new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        bottomNavigation = findViewById(R.id.bottomNavigation);

        Menu menu = bottomNavigation.getMenu();
        MenuItem menuItem = menu.getItem(1);
        menuItem.setChecked(true);



        bottomNavigation.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {

                switch (item.getItemId()){
                    case R.id.nav_home:
                        Intent intent1 = new Intent(SearchActivity.this, MainActivity.class);
                        intent1.putExtra("EXTRA_VALUE", "bitch ass");
                        startActivity(intent1);
                        break;

                    case R.id.nav_search:
                        break;

                    case R.id.nav_my_page:
                        Intent intent2 = new Intent(SearchActivity.this, LoginActivity.class);
                        startActivity(intent2);
                        break;
                }
                return false;
            }
        });

        loadRecyclerViewData();

    }

    private void loadRecyclerViewData() {
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
                            System.out.println("INNÍ RESPONSE");
                            JSONArray jsonArray = new JSONArray(response);
                            System.out.println(jsonArray.get(1));
                            System.out.println("Lengd: " + jsonArray.length());
                            System.out.println(jsonArray.getJSONObject(1).get("beerId"));
                            System.out.println(jsonArray.getJSONObject(1).get("name"));
                            System.out.println("Náði að sækja");

                            for (int i = 0; i < jsonArray.length(); i++) {

                                mbeer_id.add(jsonArray.getJSONObject(i).get("beerId") + "");
                                mbeer_name.add(jsonArray.getJSONObject(i).get("name") + "");
                                mbeer_volume.add("Magn " + jsonArray.getJSONObject(i).get("volume") + " ml.");
                                mbeer_price.add(jsonArray.getJSONObject(i).get("price") + " kr.");
                            }

                            initRecyclerView();


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
                        Toast.makeText(SearchActivity.this, error.getMessage(), Toast.LENGTH_SHORT).show();

                    }
                });

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }

    private void initRecyclerView() {
        RecyclerView recyclerView = findViewById(R.id.recycler_view);
        RecyclerViewAdapter adapter = new RecyclerViewAdapter(this, mbeer_id, mbeer_name, mbeer_volume, mbeer_price);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
    }

}
