package com.example.villi.beer_yo_ass;

import android.app.ProgressDialog;
import android.content.Intent;
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
import android.widget.RelativeLayout;
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

public class BeerActivity extends AppCompatActivity {
    private BottomNavigationView bottomNavigation;

    //Views
    private TextView viewName;
    private TextView viewVolume;
    private TextView viewAlcohol;
    private TextView viewPrice;
    private TextView viewTaste;
    private TextView viewStars;
    private ImageView viewImage;
    private EditText commentText;
    private Button commentButton;

    //URL and Request parameters
    private static final String HOST_URL_DATA = "http://10.0.2.2:8080/beers";
    private static String URL_DATA = "http://10.0.2.2:8080/beers";
    private static String COMMENT_URL_DATA = "http://10.0.2.2:8080/comment/";

    //beerdata variables
    private String name;
    private String stars;
    private String alcohol;
    private String volume;
    private String linkToVinbudin;
    private String taste;
    private String price;
    private String beerId;

    // variables for the json data
    private ArrayList<JSONObject> mbeer_data = new ArrayList<>();
    private ArrayList<String> commenter_name = new ArrayList<>();
    private ArrayList<String> comment = new ArrayList<>();
    private ArrayList<String> commenter_id = new ArrayList<>();
    private ArrayList<String> comment_time = new ArrayList<>();
    private ArrayList<String> comment_title = new ArrayList<>();
    private ArrayList<JSONObject> comment_data = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_beer);

        Bundle p = getIntent().getExtras();
        beerId = p.getString("BEER_ID");

        //We send a get Request on this beerId
        URL_DATA = HOST_URL_DATA + "/" + beerId;

        bottomNavigation = findViewById(R.id.bottomNavigation);

        Menu menu = bottomNavigation.getMenu();
        MenuItem menuItem = menu.getItem(1);
        menuItem.setChecked(true);

        bottomNavigation.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {

                switch (item.getItemId()){
                    case R.id.nav_home:
                        Intent intent1 = new Intent(BeerActivity.this, MainActivity.class);
                        intent1.putExtra("EXTRA_VALUE", "bitch ass");
                        startActivity(intent1);
                        break;

                    case R.id.nav_search:
                        break;

                    case R.id.nav_my_page:
                        Intent intent2 = new Intent(BeerActivity.this, LoginActivity.class);
                        startActivity(intent2);
                        break;
                }
                return false;
            }
        });
        //Load data and process to variables
        loadBeerData();

        //Make it so you are only able to comment when logged in
        commentText = (EditText) findViewById(R.id.comment_input);
        commentButton = (Button) findViewById(R.id.comment_button);
        if(LoginActivity.user == null){
            commentText.setVisibility(View.GONE);
            commentButton.setVisibility(View.GONE);
        }
        else{
            //Handle if someone presses comment button
            //first check if comment is not empty then post
            commentButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Toast.makeText(BeerActivity.this, "TETETE", Toast.LENGTH_SHORT).show();
                    if(!isEmpty(commentText)){
                        String comment = commentText.getText().toString().trim();
                        postComment(comment);
                    }
                }
            });
        }

    }

    private boolean isEmpty(EditText etText) {
        if (etText.getText().toString().trim().length() > 0)
            return false;

        return true;
    }

    //Simple Post request with parameters in url
    private void postComment(String comment) {
        String url = COMMENT_URL_DATA +
                     LoginActivity.user + "/" +
                     beerId + "/" +
                     LoginActivity.user + "/" +
                     comment + "/" +
                     -1;
        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Loading data...");
        progressDialog.show();
        StringRequest stringRequest = new StringRequest(Request.Method.POST,
                url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        progressDialog.dismiss();

                        finish();
                        startActivity(getIntent());
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        progressDialog.dismiss();
                        System.out.println("Error response");
                        Toast.makeText(BeerActivity.this, error.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }

    //Make request and then process data
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
                            JSONObject object = new JSONObject(response);

                            name = object.getString("name");
                            viewName = findViewById(R.id.beerName);
                            viewName.setText(name);
                            System.out.println(name);

                            stars = object.getString("stars");
                            viewStars = findViewById(R.id.beerStars);
                            viewStars.setText("Rating: " + stars);
                            System.out.println(stars);

                            alcohol = object.getString("alcohol");
                            viewAlcohol = findViewById(R.id.beerAlcohol);
                            viewAlcohol.setText("Alcohol: " + alcohol + "%");
                            System.out.println(alcohol);

                            volume = object.getString("volume");
                            viewVolume = findViewById(R.id.beerVolume);
                            viewVolume.setText("Volume: " + volume);

                            linkToVinbudin = object.getString("linkToVinbudin");

                            taste = object.getString("taste");
                            viewTaste = findViewById(R.id.beerTaste);
                            viewTaste.setText(taste);

                            price = object.getString("price");
                            viewPrice = findViewById(R.id.beerPrice);
                            viewPrice.setText("Price: " + price + "kr");

                            //beerId = object.getString("beerId");
                            viewImage = findViewById(R.id.beerImage);
                            Picasso.get().load("https://www.vinbudin.is/Portaldata/1/Resources/vorumyndir/medium/"+beerId+"_r.jpg").into(viewImage);


                            JSONArray comment_list = object.getJSONArray("comments");
                            for (int i = 0; i < comment_list.length(); i++) {
                                comment_data.add(new JSONObject(String.valueOf(comment_list.get(i))));
                            }
                            initRecyclerView(comment_data);

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
                        Toast.makeText(BeerActivity.this, error.getMessage(), Toast.LENGTH_SHORT).show();

                    }
                });

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }

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
    }

}
