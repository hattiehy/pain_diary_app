package com.example.ass3.fragment;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.example.ass3.R;

public class ReportsFragment extends Fragment {

    private Button bPainLocation;
    private Button bStepTaken;
    private Button bPainWeather;

    public ReportsFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_reports, container, false);
        bPainLocation = view.findViewById(R.id.btn_pain_location);
        bStepTaken = view.findViewById(R.id.btn_steps_taken);
        bPainWeather = view.findViewById(R.id.btn_pain_weather);

        bPainLocation.setOnClickListener(Navigation.createNavigateOnClickListener(R.id.action_nav_report_fragment_to_painLocationFragment, null));


        return view;
    }
}