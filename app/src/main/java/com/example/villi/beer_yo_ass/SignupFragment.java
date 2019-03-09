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

import org.json.JSONArray;
import org.json.JSONException;


/**
 * A simple {@link Fragment} subclass.
 */
public class SignupFragment extends Fragment {

    private Button button_signup;
    private TextView text_username;
    private TextView text_password;
    private TextView got_account;
    private LoginFragment loginFragment;


    public SignupFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_signup, container, false);

        text_username = view.findViewById(R.id.text_username);
        text_password = view.findViewById(R.id.text_password);
        button_signup = view.findViewById(R.id.button_signup);
        got_account = view.findViewById(R.id.got_account);





        got_account.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                loginFragment = new LoginFragment();


                android.support.v4.app.FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                fragmentManager.popBackStackImmediate();
                fragmentManager.beginTransaction()
                        .replace(R.id.login_form, loginFragment)
                        .addToBackStack(null)
                        .commit();

            }
        });


        button_signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String username = text_username.getText().toString();
                String password = text_password.getText().toString();

                System.out.println("Username: " + username);
                System.out.println("Password: " + password);

                attemptSignup(username,password);
            }
        });

        // Inflate the layout for this fragment
        return view;
    }

    private void attemptSignup(final String username, String password) {
        String URL_DATA = "https://beer-yo-ass-server.herokuapp.com/signup/"+username+"/"+password;

        final ProgressDialog progressDialog = new ProgressDialog(getActivity());
        progressDialog.setMessage("Loading data...");
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
                            Toast.makeText(getActivity(), "Velkominn " + username, Toast.LENGTH_SHORT).show();

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

        RequestQueue requestQueue = Volley.newRequestQueue(getActivity());
        requestQueue.add(stringRequest);

    }


}
