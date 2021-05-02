package com.example.ass3;

import com.example.ass3.model.User;

public interface FragmentInteraction {

    void onSignInClicked(User user);

    void changeFragment();

    void onSignupClicked(User user);

    void addFragment();

}
