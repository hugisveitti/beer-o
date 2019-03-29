/**
 * The BeerActivity displays each individual beer. It allows us to
 * comment and rate(soon) the beer by making requests to the server.
 * The beerpage will eventually make it possible to add beers to
 * a beerlis.
 */

package com.example.villi.beer_yo_ass;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class BeerlistActivity extends AppCompatActivity {
    private BottomNavigationView bottomNavigation;

    //Views
    private TextView mViewName;
    private TextView mViewVolume;
    private TextView mViewAlcohol;
    private TextView mViewPrice;
    private TextView mViewTate;
    private TextView mViewStars;
    private ImageView mViewImage;
    private EditText mCommentText;
    private Button mCommentButton;
    private ImageButton mHeartButton;
    private ImageButton mBeerlistButton;
    private ImageButton mRatingButton;

    //URL and Request parameters
    private static final String HOST_URL = "https://beer-yo-ass-backend.herokuapp.com/";
    private static final String MY_BEERLISTS_URL = HOST_URL + "getMyDrinklists/";


    private ArrayList<String> mbeer_id_unchecked = new ArrayList<>();
    private ArrayList<String> mbeer_name_unchecked  = new ArrayList<>();
    private ArrayList<String> mbeer_volume_unchecked  = new ArrayList<>();
    private ArrayList<String> mbeer_price_unchecked  = new ArrayList<>();
    private ArrayList<String> mbeer_alcohol_unchecked  = new ArrayList<>();

    private ArrayList<String> mbeer_id_checked = new ArrayList<>();
    private ArrayList<String> mbeer_name_checked  = new ArrayList<>();
    private ArrayList<String> mbeer_volume_checked  = new ArrayList<>();
    private ArrayList<String> mbeer_price_checked  = new ArrayList<>();
    private ArrayList<String> mbeer_alcohol_checked  = new ArrayList<>();

    private Boolean isLiked = null;
    private String beerlistId;
    private String beerlistName;
    private ArrayList<JSONObject> mUncheckedData = new ArrayList<>();
    private ArrayList<JSONObject> mCheckedData = new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_beerlist);

        ArrayList<JSONObject> beerlist_list = (ArrayList<JSONObject>) getIntent().getSerializableExtra("BEERLIST_DATA");
        beerlistId = (String) getIntent().getStringExtra("BEERLIST_ID");
        loadBeerlistData();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (requestCode == 101) {
            if (resultCode == Activity.RESULT_OK) {
                finish();
                startActivity(getIntent());
            }
        }
    }

    private void loadBeerlistData() {
        String url = MY_BEERLISTS_URL + UserActivity.user;

        final ProgressDialog progressDialog = new ProgressDialog(BeerlistActivity.this);
        progressDialog.setMessage("Getting your beerlist...");
        progressDialog.show();
        StringRequest stringRequest = new StringRequest(Request.Method.GET,
                url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        progressDialog.dismiss();
                        try {
                            JSONArray jsonArray = new JSONArray(response);

                            JSONArray mUnchecked = new JSONArray();
                            JSONArray mChecked = new JSONArray();

                            for (int i = 0; i < jsonArray.length(); i++) {
                                if(String.valueOf(jsonArray.getJSONObject(i).get("drinklistId")).equals(beerlistId)){
                                    beerlistName = jsonArray.getJSONObject(i).getString("name");
                                    JSONObject getObject = jsonArray.getJSONObject(i);
                                    mUnchecked = getObject.getJSONArray("uncheckedBeers");
                                    mChecked = getObject.getJSONArray("checkedBeers");
                                }
                            }

                            for(int i = 0; i < mUnchecked.length(); i++)
                            {
                                mUncheckedData.add(mUnchecked.getJSONObject(i));
                            }

                            for(int i = 0; i < mChecked.length(); i++)
                            {
                                mCheckedData.add(mChecked.getJSONObject(i));
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
                        Toast.makeText(BeerlistActivity.this, error.getMessage(), Toast.LENGTH_SHORT).show();

                    }
                });

        RequestQueue requestQueue = Volley.newRequestQueue(BeerlistActivity.this);
        requestQueue.add(stringRequest);
    }

    //Load comments in a recycle view
    private void initRecyclerView() throws JSONException {

        try {
            ArrayList<Boolean> checked = new ArrayList<>();
            for (int i = 0; i < mUncheckedData.size(); i++) {
                mbeer_id_unchecked.add(mUncheckedData.get(i).get("beerId") + "");
                mbeer_name_unchecked.add(mUncheckedData.get(i).get("name") + "");
                mbeer_volume_unchecked.add("Magn " + mUncheckedData.get(i).get("volume") + " ml.");
                mbeer_price_unchecked.add(mUncheckedData.get(i).get("price") + " kr.");
                mbeer_alcohol_unchecked.add(mUncheckedData.get(i).get("alcohol") + "%");
                checked.add(false);
            }

            for (int i = 0; i < mCheckedData.size(); i++) {
                mbeer_id_checked.add(mCheckedData.get(i).get("beerId") + "");
                mbeer_name_checked.add(mCheckedData.get(i).get("name") + "");
                mbeer_volume_checked.add("Magn " + mCheckedData.get(i).get("volume") + " ml.");
                mbeer_price_checked.add(mCheckedData.get(i).get("price") + " kr.");
                mbeer_alcohol_checked.add(mCheckedData.get(i).get("alcohol") + "%");
                checked.add(true);
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
        RecyclerView recyclerView = findViewById(R.id.beerlistBeers);
        RecyclerViewAdapterBeerlistPage adapter = new RecyclerViewAdapterBeerlistPage(this, mUncheckedData, mCheckedData, beerlistId);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
    }

}
