package com.example.villi.beer_yo_ass;


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
                Toast.makeText(getActivity(), "auli", Toast.LENGTH_SHORT).show();

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
