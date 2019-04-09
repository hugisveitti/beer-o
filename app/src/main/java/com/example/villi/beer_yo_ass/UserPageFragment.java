/**
 * The Login Fragment handles specifically a login function.
 * It is an extension on UserActivity and makes requests to the
 * server and handles the resposes.
 */

package com.example.villi.beer_yo_ass;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
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


/**
 * A simple {@link Fragment} subclass.
 */
public class UserPageFragment extends Fragment {

    private TextView mWelcomeMessage;
    private TextView mMybeersMessage;
    private TextView mMyBeerlistsMessage;
    private RecyclerView mMyBeersList;
    private RecyclerView mMyBeerlistsList;
    private TextView highscore;
    private Button mLogOutButton;

    private ArrayList<JSONObject> mBeerlist_data = new ArrayList<>();
    private ArrayList<String> mBeerlistIds = new ArrayList<>();
    private ArrayList<String> mBeerlistNames = new ArrayList<>();

    private ArrayList<JSONObject> mMybeers_data = new ArrayList<>();
    private ArrayList<String> mbeer_id = new ArrayList<>();
    private ArrayList<String> mbeer_name = new ArrayList<>();
    private ArrayList<String> mbeer_volume = new ArrayList<>();
    private ArrayList<String> mbeer_price = new ArrayList<>();
    private ArrayList<String> mbeer_alcohol = new ArrayList<>();

    private String score;

    private static final String HOST_URL = "https://beer-yo-ass-backend.herokuapp.com/";
    private static final String MY_BEERLISTS_URL = HOST_URL + "getMyDrinklists/";
    private static final String MY_BEERS_URL = HOST_URL + "myBeers/";

    private View view;
    public UserPageFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {


        view = inflater.inflate(R.layout.fragment_userpage, container, false);

        mWelcomeMessage = (TextView) view.findViewById(R.id.userpageGreeting);
        mLogOutButton = (Button) view.findViewById(R.id.logoutButton);
        mMybeersMessage = (TextView) view.findViewById(R.id.mybeersMessage);
        mMyBeerlistsMessage = (TextView) view.findViewById(R.id.myBeerlistsMessage);
        mMyBeersList = (RecyclerView) view.findViewById(R.id.mybeersList);
        mMyBeerlistsList = (RecyclerView) view.findViewById(R.id.mybeerlistsList);
        highscore = (TextView) view.findViewById(R.id.highscore);
        mWelcomeMessage.setText("Nice to see you " + UserActivity.user);

        UserActivity.setMenuItemText(2, getResources().getString(R.string.nav_my_page));

        mLogOutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                UserActivity.user = null;
                Intent intent1 = new Intent(getActivity(), MainActivity.class);
                intent1.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent1);
            }
        });
        loadBeerlistData();
        loadHighScore();
        return view;
    }

    private void loadHighScore() {
        String url = HOST_URL +
                "getUserGameScore/" +
                UserActivity.user;

        StringRequest stringRequest = new StringRequest(Request.Method.GET,
                url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                            score = response;
                            if(Integer.parseInt(score) > 0){
                                highscore.setText("Your best score is: " + score);
                            }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        System.out.println("Error response");
                        Toast.makeText(getActivity(), error.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });

        RequestQueue requestQueue = Volley.newRequestQueue(getActivity());
        requestQueue.add(stringRequest);
    }

    private void loadBeerlistData() {
        String url = MY_BEERLISTS_URL + UserActivity.user;

        final ProgressDialog progressDialog = new ProgressDialog(getActivity());
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

                            loadMyBeersData();
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
                        Toast.makeText(getActivity(), error.getMessage(), Toast.LENGTH_SHORT).show();

                    }
                });

        RequestQueue requestQueue = Volley.newRequestQueue(getActivity());
        requestQueue.add(stringRequest);
    }

    private void loadMyBeersData() {

        String url = MY_BEERS_URL + UserActivity.user;

        final ProgressDialog progressDialog = new ProgressDialog(getActivity());
        progressDialog.setMessage("Loading your beers...");
        progressDialog.show();
        StringRequest stringRequest = new StringRequest(Request.Method.GET,
                url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        progressDialog.dismiss();
                        try {
                            JSONArray jsonArray = new JSONArray(response);

                            // add the beers to ArrayList of beer
                            for (int i = 0; i < jsonArray.length(); i++) {
                                mMybeers_data.add(jsonArray.getJSONObject(i));
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
                        Toast.makeText(getActivity(), error.getMessage(), Toast.LENGTH_SHORT).show();

                    }
                });

        RequestQueue requestQueue = Volley.newRequestQueue(getActivity());
        requestQueue.add(stringRequest);
    }

    private void initRecyclerView() {

        try {
            for (int i = 0; i < mBeerlist_data.size(); i++) {
                mBeerlistNames.add(mBeerlist_data.get(i).get("name") + "");
                mBeerlistIds.add(mBeerlist_data.get(i).get("drinklistId") + "");
            }

            for (int i = 0; i < mMybeers_data.size(); i++) {
                mbeer_id.add(mMybeers_data.get(i).get("beerId") + "");
                mbeer_name.add(mMybeers_data.get(i).get("name") + "");
                mbeer_volume.add("Magn " + mMybeers_data.get(i).get("volume") + " ml.");
                mbeer_price.add(mMybeers_data.get(i).get("price") + " kr.");
                mbeer_alcohol.add(mMybeers_data.get(i).get("alcohol") + "%");
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        String beerId = "";
        String type = "userpage";
        RecyclerView recyclerViewMyBeerlists = view.findViewById(R.id.mybeerlistsList);
        RecyclerViewAdapterBeerlist adapterBeerlist = new RecyclerViewAdapterBeerlist(getActivity(), mBeerlistNames, mBeerlistIds, beerId, type, mBeerlist_data);
        recyclerViewMyBeerlists.setAdapter(adapterBeerlist);
        recyclerViewMyBeerlists.setLayoutManager(new LinearLayoutManager(getActivity()));

        RecyclerView recyclerViewMyBeers = view.findViewById(R.id.mybeersList);
        RecyclerViewAdapter adapterBeers = new RecyclerViewAdapter(getActivity(), mbeer_id, mbeer_name, mbeer_volume, mbeer_price, mbeer_alcohol);
        recyclerViewMyBeers.setAdapter(adapterBeers);
        recyclerViewMyBeers.setLayoutManager(new LinearLayoutManager(getActivity()));
    }
}
