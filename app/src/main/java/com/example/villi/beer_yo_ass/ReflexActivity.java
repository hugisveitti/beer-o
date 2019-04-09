package com.example.villi.beer_yo_ass;

import android.content.Context;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.media.MediaPlayer;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.VibrationEffect;
import android.os.Vibrator;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import java.util.ArrayList;
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

public class ReflexActivity extends AppCompatActivity {

    private RelativeLayout layout;
    private RelativeLayout gameStats;
    private RelativeLayout resultsContainer;
    private TextView timerInfo;
    private TextView errorInfo;
    private TextView errors;
    private TextView greeting;
    private TextView input;
    private TextView results;
    private TextView scoreDisplay;
    private TextView drunkDescription;

    private Button restartButton;

    private final int height = 120;
    private final int width = 120;
    private TableLayout mTlayout;
    private TableRow tr;

    //private LinearLayout layout;
    char[] alphabet = "abcdefghijklmnopqrstuvwxyz".toCharArray();

    private int[][] multi = new int[alphabet.length][2];

    private int screenHeight;
    private int screenWidth;

    private TextView timerTextView;
    private long startTime = 0;

    private static final String HOST_URL = "https://beer-yo-ass-backend.herokuapp.com/";

    private MediaPlayer easy;
    private MediaPlayer intense;
    private Boolean timeRunning = false;
    private Boolean gameRunning = false;
    private int errorCount = 0;
    private String alpha = "abcdefghijklmnopqrstuvwxyz";
    private String gameString = "";
    private int currentPlace = 1;
    private String[] drunkList = {"You are not even drunk, stop cheating!",
    "Oh wow, Mr. Healthy over here, never drinks beer and probably does cross-fit.",
    "One beer, that´s all?! Step it up",
    "I have seen a preschooler drunker than you",
    "You could use a beer, the Lager gods are getting angry",
    "You can see clearly now the beer has gone",
    "You´re not as think as you drunk you are",
    "You are drinking like a big boy now",
    "Two beer or not two beers, you have at least had seven!",
    "Seeing double? You are making your mother proud!",
    "Your blood is literally 13% alcohol",
    "BEER YOUR ASS",
    "You are getting quite drunk sir, should I ring Jeeves to fetch a car?",
    "Please stop drinking, little Timmy needs money for surgery.",
    "Things are out of control, you are drunk like grandpa AGAIN!",
    "STOP in the name of lov... IPA",
    "I would be surprised if you remember your name.",
    "I am an app and I am ashamed of you, get some help",
    "Call your ex and tell her you´ve changed, you know it´s a good idea.",
    "That´s it, Im cutting you off. GO HOME",
    "You are something beyond drunk, go home!"
    };
    //runs without a timer by reposting this handler at the end of the runnable
    Handler timerHandler = new Handler();
    Runnable timerRunnable = new Runnable() {

        @Override
        public void run() {
            long millis = System.currentTimeMillis() - startTime;
            long mill = millis/10;
            int seconds = (int) (millis / 1000);
            int minutes = seconds / 60;
            seconds = seconds % 60;
            mill = mill % 100;

            updateDescription(seconds);
            timerTextView.setText(String.format("%d:%02d:%02d", minutes, seconds,mill));
            timerHandler.postDelayed(this, 50);
        }
    };

    private void updateDescription(int sec) {
        if(sec < 5){
            drunkDescription.setText("We are assessing your situation darling.");
        }
        else{
            long millis = System.currentTimeMillis() - startTime;
            int seconds = (int) (millis / 1000);
            float scalar = (float) (currentPlace-1) / alpha.length();
            float score = (seconds + errorCount*4)/scalar;
            drunkDescription.setText(findDescription(score));
        }

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reflex);

        gameStats = (RelativeLayout) findViewById(R.id.gameStats);
        resultsContainer = (RelativeLayout) findViewById(R.id.resultContainer);
        timerInfo = (TextView) findViewById(R.id.timerInfo);
        errorInfo = (TextView) findViewById(R.id.errorInfo);
        errors = (TextView) findViewById(R.id.errors);
        greeting = (TextView) findViewById(R.id.greeting);
        input = (TextView) findViewById(R.id.input);
        results = (TextView) findViewById(R.id.results);
        scoreDisplay = (TextView) findViewById(R.id.score);
        drunkDescription = (TextView) findViewById(R.id.drunkDescription);
        restartButton = (Button) findViewById(R.id.restartButton);
        gameStats.setVisibility(View.GONE);
        resultsContainer.setVisibility(View.GONE);
        timerTextView = (TextView) findViewById(R.id.timerTextView);
        mTlayout = (TableLayout) findViewById(R.id.mTlayout);

        easy = MediaPlayer.create(ReflexActivity.this,R.raw.easy);
        intense = MediaPlayer.create(ReflexActivity.this,R.raw.intense);
        startMusic();
        createButtons(true);

        restartButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                restartGame();
            }
        });
    }

    private void startMusic() {
        easy.start();
        easy.setLooping(true);
    }

    private void restartGame() {
        toggleMusic(true);
        drunkDescription.setText("");
        gameStats.setVisibility(View.GONE);
        resultsContainer.setVisibility(View.GONE);
        timerTextView.setText("0");
        startTime = 0;
        timeRunning = false;
        gameRunning = false;
        currentPlace = 1;
        errorCount = 0;
        gameString = "";
        input.setText("Start typing");
        timerHandler.removeCallbacks(timerRunnable);
        createButtons(true);
    }

    private void createButtons(boolean first){
        mTlayout.removeAllViews();
        final String[] values;
        if(first){
            values = new String[alphabet.length];
            for (int i = 0; i < alphabet.length; i++){
                values[i] = String.valueOf(alphabet[i]);
            }
        }
        else{
            char[] alphabetTemp = alphabet.clone();
            char[] shortArray = new char[alphabet.length - (currentPlace-1)];
            for(int i = currentPlace-1; i < alphabetTemp.length; i++){
                shortArray[i - (currentPlace-1)] = alphabetTemp[i];
            }
            values = shuffleArray(shortArray);
        }
        for(int i = 0; i < values.length; i++){
            if (i % 5 == 0) {
                tr = new TableRow(this);
                tr.setGravity(Gravity.CENTER_HORIZONTAL);
                mTlayout.addView(tr);
            }
            final int id = i;
            Button btn = new Button(this);
            btn.setText(values[i]);
            btn.setId(i);
            btn.getBackground().setColorFilter(ContextCompat.getColor(this, R.color.colorPrimaryDark), PorterDuff.Mode.MULTIPLY);
            btn.setPadding(1,1,1,1);
            //btn.setBackgroundColor(getResources().getColor(R.color.buttonYellow));
            TableRow.LayoutParams lp = new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.MATCH_PARENT);
            lp.width = 18;



            btn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (!gameRunning) {
                        input.setText("...");
                        results.setText("");
                        startTime = System.currentTimeMillis();
                        timerHandler.postDelayed(timerRunnable, 0);
                        timeRunning = true;
                        gameRunning = true;
                        gameStats.setVisibility(View.VISIBLE);
                        resultsContainer.setVisibility(View.GONE);
                        toggleMusic(false);
                    }
                    if(gameRunning){
                        timerHandler.postDelayed(timerRunnable, 0);
                        if((gameString + values[id]).equals(alpha.substring(0,currentPlace))){
                            errors.setTextColor(getResources().getColor(R.color.black));
                            errors.setTextSize(14);
                            gameString = gameString + values[id];
                            input.setText(gameString.toUpperCase());
                            currentPlace++;
                            if(gameString.equals(alpha)){
                                gameOver();
                            }
                            else{
                                createButtons(false);
                            }
                        }
                        else{
                            Vibrator v = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
                            // Vibrate for 500 milliseconds
                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                                v.vibrate(VibrationEffect.createOneShot(500, VibrationEffect.DEFAULT_AMPLITUDE));
                            } else {
                                //deprecated in API 26
                                v.vibrate(500);
                            }
                            errorCount++;
                            errors.setText(String.valueOf(errorCount));
                            errors.setTextColor(getResources().getColor(R.color.errorRed));
                            errors.setTextSize(40);
                            createButtons(false);
                        }
                    }
                }
            });
            tr.addView(btn, lp);
        }

    }

    private void gameOver() {
        createButtons(false);
        toggleMusic(true);
        timeRunning = false;
        gameRunning = false;
        timerHandler.removeCallbacks(timerRunnable);
        resultsContainer.setVisibility(View.VISIBLE);

        long millis = System.currentTimeMillis() - startTime;
        int seconds = (int) (millis / 1000);
        float score = seconds + errorCount*4;
        results.setText("Your score is");
        scoreDisplay.setText(String.valueOf(score));
        drunkDescription.setText(findDescription(score));
        sendToDatabase(score);

    }

    private void sendToDatabase(float score) {
        int intscore = Math.round(score);
        if(UserActivity.user != null){
            String url = HOST_URL +
                    "newGameScore/" +
                    UserActivity.user + "/" +
                    + intscore;
            StringRequest stringRequest = new StringRequest(Request.Method.POST,
                    url,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            Toast.makeText(ReflexActivity.this, "Score saved", Toast.LENGTH_SHORT).show();
                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            System.out.println("Error response");
                            Toast.makeText(ReflexActivity.this, error.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });

            RequestQueue requestQueue = Volley.newRequestQueue(ReflexActivity.this);
            requestQueue.add(stringRequest);
        }
    }

    private String findDescription(float score) {
        if(score < 30){
            return drunkList[0];
        }
        for(int i = 0; i < drunkList.length-1; i++){
            if(score < 30 + (i*5)){
                return drunkList[i];
            }
        }
        return drunkList[drunkList.length-1];
    }

    @Override
    public void onPause() {
        super.onPause();
        timerHandler.removeCallbacks(timerRunnable);
        timeRunning = false;
        easy.stop();
        intense.stop();
    }

    @Override
    public void onResume(){
        super.onResume();
        if(gameRunning){
            intense.start();
        }
        else{
            easy.start();
        }

    }

    private String[] shuffleArray(char[] ar)
    {
        // If running on Java 6 or older, use `new Random()` on RHS here
        Random rnd = ThreadLocalRandom.current();
        String[] ret = new String[ar.length];
        for (int i = ar.length - 1; i > 0; i--)
        {
            int index = rnd.nextInt(i + 1);
            // Simple swap
            char a = ar[index];
            ar[index] = ar[i];
            ar[i] = a;
        }
        for (int i = 0; i < ar.length; i++)
        {
            ret[i] = String.valueOf(ar[i]);
        }
        return ret;
    }

    private void toggleMusic(Boolean normal){
        if(!normal){
            easy.stop();
            intense = MediaPlayer.create(ReflexActivity.this,R.raw.intense);
            intense.start();
            intense.setLooping(true);
        }
        else{
            intense.stop();
            easy = MediaPlayer.create(ReflexActivity.this,R.raw.easy);
            easy.start();
            easy.setLooping(true);
        }
    }
}
