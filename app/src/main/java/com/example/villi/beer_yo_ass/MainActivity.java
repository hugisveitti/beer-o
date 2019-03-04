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
    private String test_string;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        bottomNavigation = findViewById(R.id.bottomNavigation);
        test_Button = (Button) findViewById(R.id.test_button);

        if (getIntent().getStringExtra("EXTRA_VALUE") == null){
            test_string = "Virkar ekki";
        } else {
            test_string = getIntent().getStringExtra("EXTRA_VALUE");
        }

        test_Button.setOnClickListener(new View.OnClickListener() {
             @Override

             public void onClick(View view) {
                 Toast.makeText(MainActivity.this, test_string, Toast.LENGTH_SHORT).show();
             }
        });

        Menu menu = bottomNavigation.getMenu();
        MenuItem menuItem = menu.getItem(0);
        menuItem.setChecked(true);

        bottomNavigation.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {


                switch (item.getItemId()){
                    case R.id.nav_home:
                        break;

                    case R.id.nav_search:
                        Intent intent1 = new Intent(MainActivity.this, SearchActivity.class);
                        startActivity(intent1);
                        break;

                    case R.id.nav_my_page:
                        Intent intent2 = new Intent(MainActivity.this, LoginActivity.class);
                        startActivity(intent2);
                        break;
                }

                return false;

            }
        });

    }
}
