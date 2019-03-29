package com.example.villi.beer_yo_ass;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.Button;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;


public class RatingPopup extends AppCompatActivity {

    private Button mSubmitButton;
    private RatingBar mRatingBar;
    private TextView mMessage;

    private String beerId;

    private static final String HOST_URL = "https://beer-yo-ass-backend.herokuapp.com/";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.popup_rating);

        Bundle p = getIntent().getExtras();
        beerId = p.getString("BEER_ID");

        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);

        int width = dm.widthPixels;
        int height = dm.heightPixels;

        getWindow().setLayout((int)(width*0.8), (int)(height*0.6));

        mRatingBar = (RatingBar) findViewById(R.id.beerRatingBar);
        mSubmitButton = (Button) findViewById(R.id.rateSubmitButton);
        mMessage = (TextView) findViewById(R.id.ratingMessage);
        mMessage.setText("");
        mSubmitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(mRatingBar.getRating() >= 0 && mRatingBar.getRating() <= 5){
                    float rating = mRatingBar.getRating();
                    sendRating(rating);
                }
                else{
                    mMessage.setText("You have to select stars");
                }
            }
        });
    }

    private void sendRating(float rating) {
        ///rate/{username}/{beerId}/{stars}
        if(UserActivity.user != null){
            String url = HOST_URL +
                    "rate/" +
                    UserActivity.user + "/" +
                    beerId + "/" +
                    rating;

            final ProgressDialog progressDialog = new ProgressDialog(this);
            progressDialog.setMessage("Rating this beer");
            progressDialog.show();
            StringRequest stringRequest = new StringRequest(Request.Method.POST,
                    url,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            Toast.makeText(RatingPopup.this, "Beer was rated", Toast.LENGTH_SHORT).show();
                            progressDialog.dismiss();
                            Intent returnIntent = new Intent();
                            setResult(Activity.RESULT_OK, returnIntent);
                            finish();
                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            progressDialog.dismiss();
                            System.out.println("Error response");
                            Toast.makeText(RatingPopup.this, error.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });

            RequestQueue requestQueue = Volley.newRequestQueue(this);
            requestQueue.add(stringRequest);
        }
        else{
            mMessage.setText("Please login to rate beers");
        }
    }

}
