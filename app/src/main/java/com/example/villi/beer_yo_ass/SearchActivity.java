package com.example.villi.beer_yo_ass;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;

public class SearchActivity extends AppCompatActivity {

    private BottomNavigationView bottomNavigation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        bottomNavigation = findViewById(R.id.bottomNavigation);

        bottomNavigation.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {


                if (item.getItemId() == R.id.nav_home) {
                    Intent myIntent = new Intent(SearchActivity.this, MainActivity.class);
                    SearchActivity.this.startActivity(myIntent);
                    return true;
                }

                if (item.getItemId() == R.id.nav_my_page) {
                    Intent myIntent = new Intent(SearchActivity.this, LoginActivity.class);
                    SearchActivity.this.startActivity(myIntent);
                    return true;
                }

                return false;

            }
        });

    }


}
