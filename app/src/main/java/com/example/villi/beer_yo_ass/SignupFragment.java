/**
 * The Signup Fragment handles specifically a signup function.
 * It is an extension on UserActivity and makes requests to the
 * server and handles the resposes.
 */

package com.example.villi.beer_yo_ass;


import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
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


/**
 * A simple {@link Fragment} subclass.
 */
public class SignupFragment extends Fragment {

    private Button button_signup;
    private TextView text_username;
    private TextView text_password;
    private TextView got_account;
    private LoginFragment loginFragment;
    private UserPageFragment userPageFragment;


    public SignupFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_signup, container, false);

        text_username = view.findViewById(R.id.text_username);
        text_username.requestFocus();
        text_password = view.findViewById(R.id.text_password);
        button_signup = view.findViewById(R.id.button_signup);
        got_account = view.findViewById(R.id.got_account);

        UserActivity.setMenuItemText(2, getResources().getString(R.string.nav_login));

        // Switch to the login fragment
        got_account.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                loginFragment = new LoginFragment();


                android.support.v4.app.FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                fragmentManager.popBackStackImmediate();
                fragmentManager.beginTransaction()
                        .replace(R.id.user_form, loginFragment)
                        .addToBackStack(null)
                        .commit();

            }
        });

        // Try to signup if user clicks on Signup button
        button_signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String username = text_username.getText().toString();
                String password = text_password.getText().toString();

                attemptSignup(username,password);
            }
        });

        // Inflate the layout for this fragment
        return view;
    }

    /* attemptSignup sends a GET request on a URL and if it returns false
     * the username is taken otherwise it returns true and user is logged in
     */
    private void attemptSignup(final String username, final String password) {
        String URL_DATA = "https://beer-yo-ass-backend.herokuapp.com/signup/"+username+"/"+password;

        final ProgressDialog progressDialog = new ProgressDialog(getActivity());
        progressDialog.setMessage("Creating user");
        progressDialog.show();

        System.out.println(URL_DATA);
        StringRequest stringRequest = new StringRequest(Request.Method.GET,
                URL_DATA,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        progressDialog.dismiss();

                        System.out.println(response);
                        System.out.println("Náði að sækja");

                        if (response.equals("false")) {
                            Toast.makeText(getActivity(), "Notandanafn ekki laust", Toast.LENGTH_SHORT).show();
                        } else {
                            attemptLogin(username, password);
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

        // Use Volley to send request on database
        RequestQueue requestQueue = Volley.newRequestQueue(getActivity());
        requestQueue.add(stringRequest);

    }

    private void attemptLogin(final String username, String password) {
        String URL_DATA = "https://beer-yo-ass-backend.herokuapp.com/login/"+username+"/"+password;

        final ProgressDialog progressDialog = new ProgressDialog(getActivity());
        progressDialog.setMessage("Logging in...");
        progressDialog.show();
        StringRequest stringRequest = new StringRequest(Request.Method.GET,
                URL_DATA,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        progressDialog.dismiss();

                        System.out.println(response);
                        System.out.println("Náði að sækja");

                        if (response.equals("true")) {
                            UserActivity.user = username;
                            Toast.makeText(getActivity(), "Velkomin/n " + UserActivity.user, Toast.LENGTH_SHORT).show();
                            userPageFragment = new UserPageFragment();

                            android.support.v4.app.FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                            fragmentManager.popBackStackImmediate();
                            fragmentManager.beginTransaction()
                                    .replace(R.id.user_form, userPageFragment)
                                    .commit();
                        } else {
                            Toast.makeText(getActivity(), "vitlaust notenda nafn eða lykilorð", Toast.LENGTH_SHORT).show();
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

        // Use Volley to send request on database
        RequestQueue requestQueue = Volley.newRequestQueue(getActivity());
        requestQueue.add(stringRequest);
    }
}
