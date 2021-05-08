package com.example.ass3.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.example.ass3.FragmentInteraction;
import com.example.ass3.R;
import com.example.ass3.fragment.SignInFragment;
import com.example.ass3.fragment.SignUpFragment;
import com.example.ass3.model.User;
import com.example.ass3.viewmodel.PainRecordViewModel;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class LoginActivity extends AppCompatActivity implements FragmentInteraction {

    SignInFragment loginFragment;
    SignUpFragment signUpFragment;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        loginFragment = SignInFragment.newInstance();
        signUpFragment = SignUpFragment.newInstance();
        addFragment();

        mAuth = FirebaseAuth.getInstance();
//        updateUI(currentUser);

    }

    @Override
    public void onSignInClicked(User user) {
        signIn(user.getEmail(), user.getPassword());
    }

    private void loginAndNavigate(User user) {
        signIn(user.getEmail(), user.getPassword());
    }

    private void signIn(String email, String password) {
        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            FirebaseUser user = mAuth.getCurrentUser();
                            Toast.makeText(LoginActivity.this, "Authentication success.",
                                    Toast.LENGTH_SHORT).show();
                            toMainActivity();
                        } else {
                            Toast.makeText(LoginActivity.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    private void toMainActivity() {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
        finish();
    }

    @Override
    public void changeFragment() {
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragment_container, signUpFragment)
                .commitNow();
    }

    @Override
    public void onSignupClicked(User user) {
        createAccount(user.getEmail(), user.getPassword());
    }

    private void createAccount(String email, String password) {
        final User user1 = new User();
        user1.setEmail(email);
        user1.setPassword(password);
        mAuth.createUserWithEmailAndPassword(user1.getEmail(), user1.getPassword())
                .addOnCompleteListener(LoginActivity.this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            Log.d("TAG", "Create user with password success");
                            FirebaseUser user = mAuth.getCurrentUser();
                            loginAndNavigate(user1);
                            updateUI(user);
                        } else {
                            Log.w("TAG", "Create user with password failure", task.getException());
                            Toast.makeText(LoginActivity.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                            updateUI(null);
                        }
                    }
                });
    }

    @Override
    public void addFragment() {
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragment_container, loginFragment)
                .commitNow();
    }

    private void updateUI(FirebaseUser user) {

    }


}