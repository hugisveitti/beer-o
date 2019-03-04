package com.example.villi.beer_yo_ass;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class LoginActivity extends AppCompatActivity {

    private BottomNavigationView bottomNavigation;
    private LoginFragment loginFragment;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        bottomNavigation = findViewById(R.id.bottomNavigation);
        loginFragment = new LoginFragment();


        Menu menu = bottomNavigation.getMenu();
        MenuItem menuItem = menu.getItem(2);
        menuItem.setChecked(true);

        setFragment(loginFragment);


        bottomNavigation.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {

                switch (item.getItemId()){
                    case R.id.nav_home:
                        Intent intent1 = new Intent(LoginActivity.this, MainActivity.class);
                        startActivity(intent1);
                        break;

                    case R.id.nav_search:
                        Intent intent2 = new Intent(LoginActivity.this, SearchActivity.class);
                        startActivity(intent2);
                        break;

                    case R.id.nav_my_page:
                        break;
                }

                return false;
            }
        });

    }

    private void setFragment(LoginFragment fragment) {
        android.support.v4.app.FragmentManager manager = getSupportFragmentManager();
        manager.beginTransaction()
                .replace(R.id.login_form, fragment)
                .commit();

    }

}
