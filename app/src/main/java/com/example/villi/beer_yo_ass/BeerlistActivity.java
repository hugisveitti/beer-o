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
    private static final String HOST_URL_DATA = "https://beer-yo-ass-backend.herokuapp.com/beers";
    private static String URL_DATA = "https://beer-yo-ass-backend.herokuapp.com/beers";
    private static String COMMENT_URL_DATA = "https://beer-yo-ass-backend.herokuapp.com/comment/";
    private static String MY_BEERS_URL = HOST_URL + "addToMyBeers/";

    //beerdata variables
    private String name;
    private String stars;
    private String alcohol;
    private String volume;
    private String linkToVinbudin;
    private String taste;
    private String price;
    private String beerId;
    private ArrayList<String> mbeerlist_string_data;
    // variables for the json data
    private ArrayList<JSONObject> mbeer_data = new ArrayList<>();
    private ArrayList<String> commenter_name = new ArrayList<>();
    private ArrayList<String> comment = new ArrayList<>();
    private ArrayList<String> commenter_id = new ArrayList<>();
    private ArrayList<String> comment_time = new ArrayList<>();
    private ArrayList<String> comment_title = new ArrayList<>();
    private ArrayList<JSONObject> comment_data = new ArrayList<>();

    private Boolean isLiked = null;
    private String beerlistID;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_beerlist);

        ArrayList<JSONObject> beerlist_list = (ArrayList<JSONObject>) getIntent().getSerializableExtra("BEERLIST_DATA");
        beerlistID = (String) getIntent().getStringExtra("BEERLIST_ID");
        System.out.println(String.valueOf(beerlist_list.get(1)));
        URL_DATA = HOST_URL_DATA + "/" + beerId;

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

/*
    //Load comments in a recycle view
    private void initRecyclerView(ArrayList<JSONObject> comment_data) {

        try {
            for (int i = 0; i < comment_data.size(); i++) {
                commenter_name.add(comment_data.get(i).get("username") + "");
                comment.add(comment_data.get(i).get("comment") + "");
                commenter_id.add(comment_data.get(i).get("userId") + "");
                comment_time.add(comment_data.get(i).get("date") + "");
                comment_title.add(comment_data.get(i).get("title") + "");
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }

        RecyclerView recyclerView = findViewById(R.id.recycler_view);
        RecyclerViewAdapterComments adapter = new RecyclerViewAdapterComments(this, commenter_id, commenter_name, comment, comment_time, comment_title);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
    }*/

}
