/**
 * The UserActivity handles login related problems.
 * It makes fragments for both signup and login.
 * The public variable user is here used to indicate who
 * is logged in.
 */

package com.example.villi.beer_yo_ass;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

public class UserActivity extends AppCompatActivity {

    private static BottomNavigationView bottomNavigation;
    private LoginFragment loginFragment;
    public static String user = null;
    public UserPageFragment userPageFragment;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        bottomNavigation = findViewById(R.id.bottomNavigation);

        Menu menu = bottomNavigation.getMenu();
        MenuItem menuItem = menu.getItem(2);
        menuItem.setChecked(true);

        if(user == null){
            loginFragment = new LoginFragment();
            // set the loginfragment when loginActivity is loaded and no user signed in
            setLoginFragment(loginFragment);
        }
        else{
            userPageFragment = new UserPageFragment();
            setUserpageFragment(userPageFragment);
        }

        // TODO: if user is logged in, load userpage fragment

        bottomNavigation.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {

                switch (item.getItemId()){
                    case R.id.nav_home:
                        Intent intent1 = new Intent(UserActivity.this, MainActivity.class);
                        intent1.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(intent1);
                        break;

                    case R.id.nav_search:
                        Intent intent2 = new Intent(UserActivity.this, SearchActivity.class);
                        startActivity(intent2);
                        break;

                    case R.id.nav_my_page:
                        break;
                }

                return false;
            }
        });

    }

    // Function to load a new fragment
    private void setLoginFragment(LoginFragment fragment) {
        android.support.v4.app.FragmentManager manager = getSupportFragmentManager();
        manager.beginTransaction()
                .replace(R.id.user_form, fragment)
                .commit();

    }

    // Function to load a new fragment
    private void setUserpageFragment(UserPageFragment fragment) {
        android.support.v4.app.FragmentManager manager = getSupportFragmentManager();
        manager.beginTransaction()
                .replace(R.id.user_form, fragment)
                .commit();
    }

    public static void setMenuItemText(int item, String s) {
        Menu menu = bottomNavigation.getMenu();
        MenuItem menuItem = menu.getItem(item);
        menuItem.setTitle(s);
    }
}
