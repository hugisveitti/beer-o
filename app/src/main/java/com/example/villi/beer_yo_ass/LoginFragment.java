/**
 * The Login Fragment handles specifically a login function.
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
public class LoginFragment extends Fragment {

    private Button button_login;
    private TextView text_username;
    private TextView text_password;
    private TextView no_account;
    private SignupFragment signupFragment;
    private UserPageFragment userPageFragment;

    public LoginFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {


        View view = inflater.inflate(R.layout.fragment_login, container, false);

        text_username = view.findViewById(R.id.text_username);
        text_password = view.findViewById(R.id.text_password);
        button_login = view.findViewById(R.id.button_login);
        no_account = view.findViewById(R.id.no_account);

        // Switch to the signup fragment
        no_account.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                signupFragment = new SignupFragment();

                android.support.v4.app.FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                fragmentManager.popBackStackImmediate();
                fragmentManager.beginTransaction()
                        .replace(R.id.login_form, signupFragment)
                        .addToBackStack(null)
                        .commit();

            }
        });

        // Try to login if user clicks on login button
        button_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String username = text_username.getText().toString();
                String password = text_password.getText().toString();

                attemptLogin(username,password);
            }
        });


        // Inflate the layout for this fragment

        return view;
    }

    /* attemptLogin sends a GET request on a URL and returns true if
     * user was able to login otherwise it sends false
     */
    private void attemptLogin(final String username, String password) {
        String URL_DATA = "https://beer-yo-ass-backend.herokuapp.com/login/"+username+"/"+password;

        final ProgressDialog progressDialog = new ProgressDialog(getActivity());
        progressDialog.setMessage("Loading data...");
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
                                Toast.makeText(getActivity(), "user = " + UserActivity.user, Toast.LENGTH_SHORT).show();
                                userPageFragment = new UserPageFragment();

                                android.support.v4.app.FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                                fragmentManager.popBackStackImmediate();
                                fragmentManager.beginTransaction()
                                        .replace(R.id.login_form, userPageFragment)
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
