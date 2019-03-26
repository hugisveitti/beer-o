package com.example.villi.beer_yo_ass;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Switch;
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


public class BeerlistPopup extends AppCompatActivity {

    private Button mNewBeerlistButton;
    private EditText mNewBeerlistInput;
    private RecyclerView mbeerlistList;
    private TextView mMessage;
    private Switch mSwitch;

    private String beerId;
    private ArrayList<String> mBeerlistIds = new ArrayList<>();
    private ArrayList<String> mBeerlistNames = new ArrayList<>();
    private ArrayList<JSONObject> mBeerlist_data = new ArrayList<>();

    private static final String HOST_URL = "https://beer-yo-ass-backend.herokuapp.com/";
    private static final String CREATE_BEERLIST_URL = HOST_URL + "createDrinklist/";
    private static final String MY_BEERLISTS_URL = HOST_URL + "getMyDrinklists/";
    private static final String ADD_TO_BEERLIST_URL = HOST_URL + "addToDrinklist/";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.popup_beerlist);

        Bundle p = getIntent().getExtras();
        beerId = p.getString("BEER_ID");

        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);

        int width = dm.widthPixels;
        int height = dm.heightPixels;

        getWindow().setLayout((int)(width*0.85), (int)(height*0.7));

        loadBeerData();

        mNewBeerlistButton = (Button) findViewById(R.id.submitNewDrinklistButton);
        mbeerlistList = (RecyclerView) findViewById(R.id.beerlistRecyclerView);
        mNewBeerlistInput = (EditText) findViewById(R.id.newBeerlistInput);
        mMessage = (TextView) findViewById(R.id.messageView);
        mSwitch = (Switch) findViewById(R.id.switchPrivate);
        mNewBeerlistButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(mNewBeerlistInput.getText().length() <= 0){
                    mMessage.setText("Please write a name for your new beerlist");
                }
                else{
                    createNewBeerlist(mNewBeerlistInput.getText().toString(), mSwitch.isChecked());
                }
            }
        });

        mSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if(mSwitch.isChecked()){
                    mSwitch.setText("Public");
                }
                else{
                    mSwitch.setText("Private");
                }
            }
        });

    }

    private void createNewBeerlist(String name, Boolean publ) {

        ///createDrinklist/{username}/{name}/{public}
        String url = CREATE_BEERLIST_URL +
                     UserActivity.user + "/" +
                     name + "/" +
                     publ;

        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Creating beerlist");
        progressDialog.show();
        StringRequest stringRequest = new StringRequest(Request.Method.POST,
                url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Toast.makeText(BeerlistPopup.this, "Beerlist created", Toast.LENGTH_SHORT).show();
                        progressDialog.dismiss();
                        try {
                            JSONObject newBeerlist = new JSONObject(response);
                            int beerlistId = newBeerlist.getInt("drinklistId");
                            addBeerToDrinklist(beerlistId);
                            System.out.println(newBeerlist);
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
                        Toast.makeText(BeerlistPopup.this, error.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }

    private void addBeerToDrinklist(int beerlistId) {

        ///addToDrinklist/{username}/{drinklistId}/{beerId}
        String url = ADD_TO_BEERLIST_URL +
                     UserActivity.user + "/" +
                     beerlistId + "/" +
                     beerId;
        System.out.println(url);
        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Adding beer to beerlist");
        progressDialog.show();
        StringRequest stringRequest = new StringRequest(Request.Method.POST,
                url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Toast.makeText(BeerlistPopup.this, "Beer added to beerlist", Toast.LENGTH_SHORT).show();
                        progressDialog.dismiss();

                        Intent returnIntent = new Intent();
                        setResult(Activity.RESULT_CANCELED, returnIntent);
                        finish();
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        progressDialog.dismiss();
                        System.out.println("Error response");
                        Toast.makeText(BeerlistPopup.this, error.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }

    private void initRecyclerView() {

        try {
            for (int i = 0; i < mBeerlist_data.size(); i++) {
                mBeerlistNames.add(mBeerlist_data.get(i).get("name") + "");
                mBeerlistIds.add(mBeerlist_data.get(i).get("drinklistId") + "");
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
        String type = "addbeerlist";
        RecyclerView recyclerView = findViewById(R.id.beerlistRecyclerView);
        RecyclerViewAdapterBeerlist adapter = new RecyclerViewAdapterBeerlist(this, mBeerlistNames, mBeerlistIds,beerId,type);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
    }

    private void loadBeerData() {
        String url = MY_BEERLISTS_URL + UserActivity.user;

        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Getting your beerlists...");
        progressDialog.show();
        StringRequest stringRequest = new StringRequest(Request.Method.GET,
                url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        progressDialog.dismiss();
                        try {
                            JSONArray jsonArray = new JSONArray(response);


                            for (int i = 0; i < jsonArray.length(); i++) {
                                mBeerlist_data.add(jsonArray.getJSONObject(i));
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
                        Toast.makeText(BeerlistPopup.this, error.getMessage(), Toast.LENGTH_SHORT).show();

                    }
                });

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }

}
