package com.example.villi.beer_yo_ass;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    private BottomNavigationView bottomNavigation;

    private Button test_Button;

    private MenuItem nav_search;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        bottomNavigation = findViewById(R.id.bottomNavigation);
        test_Button = (Button) findViewById(R.id.test_button);
        nav_search = (MenuItem) findViewById(R.id.nav_search);

        test_Button.setOnClickListener(new View.OnClickListener() {
             @Override

//             nav_search.
             public void onClick(View view) {
                 Toast.makeText(MainActivity.this, "testing", Toast.LENGTH_SHORT).show();
             }
        });

        bottomNavigation.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {


                if (item.getItemId() == R.id.nav_search) {
                    Intent myIntent = new Intent(MainActivity.this, SearchActivity.class);
                    MainActivity.this.startActivity(myIntent);
                    return true;
                }

                if (item.getItemId() == R.id.nav_my_page) {
                    Intent myIntent = new Intent(MainActivity.this, LoginActivity.class);
                    MainActivity.this.startActivity(myIntent);
                    return true;
                }

                return false;

            }
        });

    }
}
