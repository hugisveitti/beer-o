package com.example.villi.beer_yo_ass;


import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;


/**
 * A simple {@link Fragment} subclass.
 */
public class LoginFragment extends Fragment {

    private Button button_login;
    private TextView text_username;
    private TextView text_password;
    private TextView no_account;
    private SignupFragment signupFragment;

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


        no_account.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(getActivity(), "auli", Toast.LENGTH_SHORT).show();

                signupFragment = new SignupFragment();


                android.support.v4.app.FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                fragmentManager.popBackStackImmediate();
                fragmentManager.beginTransaction()
                        .replace(R.id.login_form, signupFragment)
                        .addToBackStack(null)
                        .commit();

            }
        });


        button_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String username = text_username.getText().toString();
                String password = text_password.getText().toString();

                System.out.println("Username: " + username);
                System.out.println("Password: " + password);

                attemptLogin(username,password);
            }
        });


        // Inflate the layout for this fragment

        return view;
    }

    private void attemptLogin(String username, String password) {
        Toast.makeText(getActivity(), "username, password", Toast.LENGTH_SHORT).show();
    }

}
