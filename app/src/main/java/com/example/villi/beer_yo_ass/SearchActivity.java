/**
 * The searchActivity handles all search related things, it makes
 * a request to the backend if data is not sent to it as an intent
 * and sorts the data and displays it. From here one can go to the
 * pages of individual beers.
 * All sorting and filtering is done on the frontend because the
 * data is rather small and no need to make alot of requests.
 */


package com.example.villi.beer_yo_ass;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
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

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class SearchActivity extends AppCompatActivity {

    private BottomNavigationView bottomNavigation;
    private static final String TAG = "SearchActivity";
    private static final String URL_DATA = "https://beer-yo-ass-backend.herokuapp.com/beers";

    private EditText mSearch_string;
    private EditText mOver_price;
    private EditText mUnder_price;

    // variables for the json data
    private ArrayList<String> mbeer_id = new ArrayList<>();
    private ArrayList<String> mbeer_name = new ArrayList<>();
    private ArrayList<String> mbeer_volume = new ArrayList<>();
    private ArrayList<String> mbeer_price = new ArrayList<>();
    private ArrayList<String> mbeer_alcohol = new ArrayList<>();

    private ArrayList<JSONObject> mbeer_data = new ArrayList<>();
    private ArrayList<JSONObject> mSorted_data;

    private JSONArray jsonArray;
    private JSONArray sortedJsonArray;

    private String msortBy = "name";
    private int mUnderPrice = -1;
    private int mOverPrice = -1;
    private String mSearchName = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        bottomNavigation = findViewById(R.id.bottomNavigation);
        mSearch_string = (EditText) findViewById(R.id.search_name);
        mOver_price = (EditText) findViewById(R.id.price_over_input);
        mUnder_price = (EditText) findViewById(R.id.price_under_input);

        Menu menu = bottomNavigation.getMenu();
        MenuItem menuItem = menu.getItem(1);
        menuItem.setChecked(true);

        //load data into mbeer_data and process it
        try {
            makeBeerList();
        } catch (JSONException e) {
            e.printStackTrace();
        }


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
                        Intent intent2 = new Intent(SearchActivity.this, UserActivity.class);
                        startActivity(intent2);
                        break;
                }
                return false;
            }
        });

        mSearch_string.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                try{
                    filterResults();
                } catch(Exception e){
                    System.out.println(e);
                }

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });


        mOver_price.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                try{
                    filterResults();
                } catch(Exception e){
                    System.out.println(e);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });


        mUnder_price.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                try{
                    filterResults();
                } catch(Exception e){
                    System.out.println(e);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

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

                            // add the beers to ArrayList of beer
                            for (int i = 0; i < jsonArray.length(); i++) {
                                mbeer_data.add(jsonArray.getJSONObject(i));
                            }

                            loadDataToSort();
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
    //In the end data gets loaded and sorted into mSorted_data
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
                        loadDataToSort();
                    }
                }
            } else {
                loadDataToSort();
            }


        } catch (JSONException e) {
            System.out.print(e);
        }
    }
    private void loadDataToSort() throws JSONException {
        jsonArray = new JSONArray();
        for (int i = 0; i < mbeer_data.size(); i++) {
            jsonArray.put(mbeer_data.get(i));
        }
        if(msortBy != "") {
            sortedJsonArray = sortList(jsonArray);
        }
        else{
            sortedJsonArray = jsonArray;
        }
        mSorted_data = new ArrayList<>();
        for(int i = 0; i < sortedJsonArray.length(); i++){
            mSorted_data.add(new JSONObject(String.valueOf(sortedJsonArray.get(i))));
        }
        initRecyclerView();
    }
    //Load every beer in mbeerdata in RecycleView list of beers
    private void initRecyclerView() {

        try {
            for (int i = 0; i < mSorted_data.size(); i++) {
                mbeer_id.add(mSorted_data.get(i).get("beerId") + "");
                mbeer_name.add(mSorted_data.get(i).get("name") + "");
                mbeer_volume.add("Magn " + mSorted_data.get(i).get("volume") + " ml.");
                mbeer_price.add(mSorted_data.get(i).get("price") + " kr.");
                mbeer_alcohol.add(mSorted_data.get(i).get("alcohol") + "%");
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }

        RecyclerView recyclerView = findViewById(R.id.recycler_view);
        RecyclerViewAdapter adapter = new RecyclerViewAdapter(this, mbeer_id, mbeer_name, mbeer_volume, mbeer_price, mbeer_alcohol);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
    }

    //Filter out beers if they dont meet constraints in search filter
    private  boolean checkConstraints(JSONObject obj) throws JSONException {
        Boolean bool = true;
        if(mUnderPrice > 0 && mUnderPrice > mOverPrice){
            if(mUnderPrice < Double.parseDouble(obj.get("price") + "")){
                bool = false;
                return bool;
            }
        }
        if(mOverPrice > 0 && mUnderPrice > mOverPrice){
            if(mOverPrice > Double.parseDouble(obj.get("price") + "")){
                bool = false;
                return bool;
            }
        }
        if(mSearchName != ""){
            if(!(obj.get("name") + "").toLowerCase().contains(mSearchName.toLowerCase())){
                bool = false;
                return bool;
            }
        }

        return bool;
    }

    //Same as initRecyclerView but filter the beers
    private void searchRecyclerView() {

        try {
            for (int i = 0; i < mSorted_data.size(); i++) {
                if(checkConstraints(mSorted_data.get(i))){
                    mbeer_id.add(mSorted_data.get(i).get("beerId") + "");
                    mbeer_name.add(mSorted_data.get(i).get("name") + "");
                    mbeer_volume.add("Magn " + mSorted_data.get(i).get("volume") + " ml.");
                    mbeer_price.add(mSorted_data.get(i).get("price") + " kr.");
                    mbeer_alcohol.add(mSorted_data.get(i).get("alcohol") + "%");
                }
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }

        RecyclerView recyclerView = findViewById(R.id.recycler_view);
        RecyclerViewAdapter adapter = new RecyclerViewAdapter(this, mbeer_id, mbeer_name, mbeer_volume, mbeer_price, mbeer_alcohol);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
    }

    //Take in JSONArray and sort it with regards to a KEY, here the variable msortBy
    //msortBy is changed with radio buttons
    private JSONArray sortList(JSONArray jsonArr) throws JSONException {
        System.out.println("msortBy"+msortBy);
        JSONArray sortedJsonArray = new JSONArray();

        List<JSONObject> jsonValues = new ArrayList<JSONObject>();
        for (int i = 0; i < jsonArr.length(); i++) {
            jsonValues.add(jsonArr.getJSONObject(i));
        }
        Collections.sort( jsonValues, new Comparator<JSONObject>() {
            //You can change "Name" with "ID" if you want to sort by ID
            private String KEY_NAME = msortBy;

            @Override
            public int compare(JSONObject a, JSONObject b) {
                String valA = new String();
                String valB = new String();

                try {

                   if(KEY_NAME.equals("price")){
                        valA = (String) String.valueOf(a.get(KEY_NAME));
                        valB = (String) String.valueOf(b.get(KEY_NAME));
                        //viljum skil í minnkandi röð hjá áfengisprósentu
                        return Integer.compare(Integer.parseInt(valA),Integer.parseInt(valB));
                    }
                    else if( KEY_NAME.equals("alcohol")){
                        valA = (String) String.valueOf(a.get(KEY_NAME));
                        valB = (String) String.valueOf(b.get(KEY_NAME));

                        return -Double.compare(Double.parseDouble(valA),Double.parseDouble(valB));
                    } else {
                        valA = (String) String.valueOf(a.get(KEY_NAME));
                        valB = (String) String.valueOf(b.get(KEY_NAME));
//
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

    //Radio buttons change msortBy variable which is referred to when sorting
    public void onRadioButtonClicked(View view) throws JSONException {
        // Is the button now checked?
        boolean checked = ((RadioButton) view).isChecked();

        // Check which radio button was clicked
        switch(view.getId()) {
            case R.id.by_alphabetical:
                if (checked) {
                    msortBy = "name";
                    break;
                }
            case R.id.by_alcohol:
                if (checked){
                    msortBy = "alcohol";
                    break;
                }
            case R.id.by_price:
                if (checked) {
                    msortBy = "price";
                    break;
                }
        }
        reloadView();
    }

    private void reloadView() throws JSONException {
        mbeer_id = new ArrayList<>();
        mbeer_name = new ArrayList<>();
        mbeer_volume = new ArrayList<>();
        mbeer_price = new ArrayList<>();
        mbeer_alcohol = new ArrayList<>();

        searchRecyclerView();
        makeBeerList();
    }

    private boolean isEmpty(EditText etText) {
        if (etText.getText().toString().trim().length() > 0)
            return false;

        return true;
    }

    //Take all filter inputs and process to get results
    public void onSearchButtonClicked(View view) throws JSONException {

        if(isEmpty(mSearch_string)){
            mSearchName = "";
        }
        else{
            mSearchName = mSearch_string.getText().toString().trim();
        }
        if(isEmpty(mOver_price)){
            mOverPrice = -1;
        }
        else{
            mOverPrice = Integer.valueOf(mOver_price.getText().toString().trim());
        }
        if(isEmpty(mUnder_price)){
            mUnderPrice = -1;
        }
        else{
            mUnderPrice = Integer.valueOf(mUnder_price.getText().toString().trim());
        }
        reloadView();
    }

    private void filterResults() throws JSONException{
        if(isEmpty(mSearch_string)){
            mSearchName = "";
        }
        else{
            mSearchName = mSearch_string.getText().toString().trim();
        }
        if(isEmpty(mOver_price)){
            mOverPrice = -1;
        }
        else{
            mOverPrice = Integer.valueOf(mOver_price.getText().toString().trim());
        }
        if(isEmpty(mUnder_price)){
            mUnderPrice = -1;
        }
        else{
            mUnderPrice = Integer.valueOf(mUnder_price.getText().toString().trim());
        }
        reloadView();
    }
}
