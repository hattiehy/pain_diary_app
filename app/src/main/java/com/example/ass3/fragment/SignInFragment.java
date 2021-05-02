package com.example.ass3.fragment;

import android.content.Context;
import android.os.Bundle;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.ass3.FragmentInteraction;
import com.example.ass3.R;
import com.example.ass3.model.User;

public class SignInFragment extends Fragment {

    private EditText eEmail;
    private EditText ePassword;
    private Button bLogin;
    private TextView tRegister;

    private FragmentInteraction interact;

    public SignInFragment() {
    }

    public static SignInFragment newInstance() {
        return new SignInFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.signin_fragment, container, false);
        eEmail = view.findViewById(R.id.et_email);
        ePassword = view.findViewById(R.id.et_password);
        bLogin = view.findViewById(R.id.btn_sign_in);
        tRegister = view.findViewById(R.id.tv_register);

        bLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                User user = new User();
                String username = eEmail.getText().toString().trim();
                String password = ePassword.getText().toString().trim();
                if (isValid(username, password)) {
                    user.setEmail(username);
                    user.setPassword(password);
                    interact.onSignInClicked(user);
                }
            }
        });

        tRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                interact.changeFragment();
            }
        });

        return view;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof FragmentInteraction) {
            interact = (FragmentInteraction) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement FragmentInteraction");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        interact = null;
    }

    private boolean isValid(String username, String password) {
        if (!isUserNameValid(username)) {
            Toast.makeText(getContext(), "Not a valid username" , Toast.LENGTH_SHORT).show();
            return false;
        } else if (!isPasswordValid(password)) {
            Toast.makeText(getContext(), "Password must be >5 characters" , Toast.LENGTH_SHORT).show();
            return false;
        } else {
            return true;
        }
    }

    // A placeholder username validation check
    private boolean isUserNameValid(String username) {
        if (username == null) {
            return false;
        }
        if (!username.contains("@")) {
            return false;
        } else {
            return Patterns.EMAIL_ADDRESS.matcher(username).matches();
        }
    }

    // A placeholder password validation check
    private boolean isPasswordValid(String password) {
        return password != null && password.trim().length() > 5;
    }
}
