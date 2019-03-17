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
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
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
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class SearchActivity extends AppCompatActivity {

    private BottomNavigationView bottomNavigation;
    private static final String TAG = "SearchActivity";
    private static final String URL_DATA = "http://10.0.2.2:8080/beers";

    private EditText search_string;
    private EditText over_price;
    private EditText under_price;

    // variables for the json data
    private ArrayList<String> mbeer_id = new ArrayList<>();
    private ArrayList<String> mbeer_name = new ArrayList<>();
    private ArrayList<String> mbeer_volume = new ArrayList<>();
    private ArrayList<String> mbeer_price = new ArrayList<>();

    private ArrayList<JSONObject> mbeer_data = new ArrayList<>();
    private ArrayList<JSONObject> sorted_data;

    private JSONArray jsonArray;
    private JSONArray sortedJsonArray;

    private String sortby = "";
    private int underPrice = -1;
    private int overPrice = -1;
    private String searchName = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        bottomNavigation = findViewById(R.id.bottomNavigation);

        Menu menu = bottomNavigation.getMenu();
        MenuItem menuItem = menu.getItem(1);
        menuItem.setChecked(true);

        //load data into mbeer_data and process it
        try {
            makeBeerList();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        //make list of beers to display
        initRecyclerView();


        bottomNavigation.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {

                switch (item.getItemId()){
                    case R.id.nav_home:
                        Intent intent1 = new Intent(SearchActivity.this, MainActivity.class);
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


    }

    //Make request on server and put response of all beers in mbeer_data
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
                        Toast.makeText(SearchActivity.this, error.getMessage(), Toast.LENGTH_SHORT).show();

                    }
                });

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }

    //Annoyingly complicated function processing data, some JSON gymnastics to be able to sort
    //In the end data gets loaded and sorted into sorted_data
    private void makeBeerList() throws JSONException {
        System.out.println("Search that shit cuz");

        try {
            if(mbeer_data.size() == 0){
                ArrayList<JSONObject> beer_list = (ArrayList<JSONObject>) getIntent().getSerializableExtra("BEER_DATA");
                if(beer_list == null){
                    loadBeerData();
                }
                else {
                    for (int i = 0; i < beer_list.size(); i++) {
                        mbeer_data.add(new JSONObject(String.valueOf(beer_list.get(i))));
                    }
                }
            }

            jsonArray = new JSONArray();
            for (int i = 0; i < mbeer_data.size(); i++) {
                jsonArray.put(mbeer_data.get(i));
            }
            if(sortby != "") {
                sortedJsonArray = sortList(jsonArray);
            }
            else{
                sortedJsonArray = jsonArray;
            }
            sorted_data = new ArrayList<>();
            for(int i = 0; i < sortedJsonArray.length(); i++){
                sorted_data.add(new JSONObject(String.valueOf(sortedJsonArray.get(i))));
            }

        } catch (JSONException e) {
            System.out.print(e);
        }
    }

    //Load every beer in mbeerdata in RecycleView list of beers
    private void initRecyclerView() {

        try {
            for (int i = 0; i < sorted_data.size(); i++) {
                mbeer_id.add(sorted_data.get(i).get("beerId") + "");
                mbeer_name.add(sorted_data.get(i).get("name") + "");
                mbeer_volume.add("Magn " + sorted_data.get(i).get("volume") + " ml.");
                mbeer_price.add(sorted_data.get(i).get("price") + " kr.");
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }

        RecyclerView recyclerView = findViewById(R.id.recycler_view);
        RecyclerViewAdapter adapter = new RecyclerViewAdapter(this, mbeer_id, mbeer_name, mbeer_volume, mbeer_price);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
    }

    //Filter out beers if they dont meet constraints in search filter
    private  boolean checkConstraints(JSONObject obj) throws JSONException {
        Boolean bool = true;
        if(underPrice > 0 && underPrice > overPrice){
            if(underPrice < Double.parseDouble(obj.get("price") + "")){
                bool = false;
                return bool;
            }
        }
        if(overPrice > 0 && underPrice > overPrice){
            if(overPrice > Double.parseDouble(obj.get("price") + "")){
                bool = false;
                return bool;
            }
        }
        if(searchName != ""){
            if(!(obj.get("name") + "").toLowerCase().contains(searchName.toLowerCase())){
                bool = false;
                return bool;
            }
        }

        return bool;
    }

    //Same as initRecyclerView but filter the beers
    private void searchRecyclerView() {

        try {
            for (int i = 0; i < sorted_data.size(); i++) {
                if(checkConstraints(sorted_data.get(i))){
                    mbeer_id.add(sorted_data.get(i).get("beerId") + "");
                    mbeer_name.add(sorted_data.get(i).get("name") + "");
                    mbeer_volume.add("Magn " + sorted_data.get(i).get("volume") + " ml.");
                    mbeer_price.add(sorted_data.get(i).get("price") + " kr.");
                }
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }

        RecyclerView recyclerView = findViewById(R.id.recycler_view);
        RecyclerViewAdapter adapter = new RecyclerViewAdapter(this, mbeer_id, mbeer_name, mbeer_volume, mbeer_price);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
    }

    //Take in JSONArray and sort it with regards to a KEY, here the variable sortby
    //Sortby is changed with radio buttons
    private JSONArray sortList(JSONArray jsonArr) throws JSONException {

        JSONArray sortedJsonArray = new JSONArray();

        List<JSONObject> jsonValues = new ArrayList<JSONObject>();
        for (int i = 0; i < jsonArr.length(); i++) {
            jsonValues.add(jsonArr.getJSONObject(i));
        }
        Collections.sort( jsonValues, new Comparator<JSONObject>() {
            //You can change "Name" with "ID" if you want to sort by ID
            private String KEY_NAME = sortby;

            @Override
            public int compare(JSONObject a, JSONObject b) {
                String valA = new String();
                String valB = new String();

                try {
                    if(sortby != "name"){
                        valA = (String) String.valueOf(a.get(KEY_NAME));
                        valB = (String) String.valueOf(b.get(KEY_NAME));
                    }
                    else{
                        valA = (String) a.get(KEY_NAME);
                        valB = (String) b.get(KEY_NAME);
                    }
                }
                catch (JSONException e) {
                    //do something
                }

                return valA.compareTo(valB);
                //if you want to change the sort order, simply use the following:
                //return -valA.compareTo(valB);
            }
        });

        for (int i = 0; i < jsonArr.length(); i++) {
            sortedJsonArray.put(jsonValues.get(i));
        }
        return sortedJsonArray;
    }

    //Radio buttons change sortby variable which is referred to when sorting
    public void onRadioButtonClicked(View view) throws JSONException {
        // Is the button now checked?
        boolean checked = ((RadioButton) view).isChecked();

        // Check which radio button was clicked
        switch(view.getId()) {
            case R.id.by_alphabetical:
                if (checked) {
                    sortby = "name";
                    reloadView();
                    break;
                }
            case R.id.by_alcohol:
                if (checked){
                    sortby = "alcohol";
                    reloadView();
                    break;
                }
            case R.id.by_price:
                if (checked) {
                    sortby = "price";
                    reloadView();
                    break;
                }
        }
    }

    private void reloadView() throws JSONException {
        mbeer_id = new ArrayList<>();
        mbeer_name = new ArrayList<>();
        mbeer_volume = new ArrayList<>();
        mbeer_price = new ArrayList<>();
        makeBeerList();
        searchRecyclerView();
    }

    private boolean isEmpty(EditText etText) {
        if (etText.getText().toString().trim().length() > 0)
            return false;

        return true;
    }

    //Take all filter inputs and process to get results
    public void onSearchButtonClicked(View view) throws JSONException {
        search_string = (EditText) findViewById(R.id.search_name);
        over_price = (EditText) findViewById(R.id.price_over_input);
        under_price = (EditText) findViewById(R.id.price_under_input);
        if(isEmpty(search_string)){
            searchName = "";
        }
        else{
            searchName = search_string.getText().toString().trim();
        }
        if(isEmpty(over_price)){
            overPrice = -1;
        }
        else{
            overPrice = Integer.valueOf(over_price.getText().toString().trim());
        }
        if(isEmpty(under_price)){
            underPrice = -1;
        }
        else{
            underPrice = Integer.valueOf(under_price.getText().toString().trim());
        }
        reloadView();
    }
}
