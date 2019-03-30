package com.example.villi.beer_yo_ass;

import android.app.ProgressDialog;
import android.content.Context;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.RequestFuture;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

public class BeerData {

    private static final String URL_DATA = "https://beer-yo-ass-backend.herokuapp.com/beers";

    private static ArrayList<JSONObject> mbeer_data = new ArrayList<>();

    public static void insertBeer(JSONObject beer){
        mbeer_data.add(beer);
    }

    public static ArrayList<JSONObject> getBeer_data() {
        return mbeer_data;
    }

    // get a beer with certain id
    // NOT READY!!
    public static JSONObject getBeer(String id) {
        System.out.println(mbeer_data.get(0));
        return mbeer_data.get(0);
    }

    // Return a random beer
    public static JSONObject getRandomBeer(){
        int ran = (int)(Math.random()*BeerData.getBeerListSize());
        return mbeer_data.get(ran);
    }


    public static int getBeerListSize(){
        return mbeer_data.size();
    }

    // This function is called to load the data
    public static ArrayList<JSONObject> loadBeerData(Context context) {
        final ProgressDialog progressDialog = new ProgressDialog(context);
        progressDialog.setMessage("Loading data...");
        progressDialog.show();


        StringRequest stringRequest = new StringRequest(Request.Method.GET,
                URL_DATA,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            progressDialog.dismiss();
                            JSONArray jsonArray = new JSONArray(response);

                            // add the beers to ArrayList of beer
                            for (int i = 0; i < jsonArray.length(); i++) {
                                mbeer_data.add(jsonArray.getJSONObject(i));
                            }

                            System.out.println("Versace");
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
                        System.out.println("Error response: " + error.getMessage());

                    }
                });


        // The Volley package is used to make a request on the database
        RequestQueue requestQueue = Volley.newRequestQueue(context);
        requestQueue.add(stringRequest);


        return mbeer_data;
    }


}
