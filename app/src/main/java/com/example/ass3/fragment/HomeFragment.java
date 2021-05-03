package com.example.ass3.fragment;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import com.example.ass3.R;
import com.example.ass3.databinding.HomeFragmentBinding;
import com.example.ass3.viewmodel.SharedViewModel;
import com.kwabenaberko.openweathermaplib.constant.Languages;
import com.kwabenaberko.openweathermaplib.constant.Units;
import com.kwabenaberko.openweathermaplib.implementation.OpenWeatherMapHelper;
import com.kwabenaberko.openweathermaplib.implementation.callback.CurrentWeatherCallback;
import com.kwabenaberko.openweathermaplib.model.currentweather.CurrentWeather;

public class HomeFragment extends Fragment {
    private HomeFragmentBinding addBinding;
    private TextView tCity;
    private TextView tTemp;
    private TextView tHumidity;
    private TextView tPressure;
    private TextView tDescription;


    public HomeFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the View for this fragment
        addBinding = HomeFragmentBinding.inflate(inflater, container, false);
        View view = addBinding.getRoot();

        //Instantiate Class With Your ApiKey As The Parameter
        OpenWeatherMapHelper helper = new OpenWeatherMapHelper("9fdf5bb43454d997b3047a4fc699b7a3");

        //Set Units
        helper.setUnits(Units.METRIC);

        //Set Languages
        helper.setLanguage(Languages.ENGLISH);

        tCity = view.findViewById(R.id.tv_city_name);
        tTemp = view.findViewById(R.id.tv_temp);
        tHumidity = view.findViewById(R.id.tv_humidity);
        tPressure = view.findViewById(R.id.tv_pressure);
        tDescription = view.findViewById(R.id.tv_disc);

        helper.getCurrentWeatherByGeoCoordinates(-37.91208766782282, 145.1327710748287, new CurrentWeatherCallback() {
            @Override
            public void onSuccess(CurrentWeather currentWeather) {
                tCity.setText("City, Country: " + currentWeather.getName() + ", " + currentWeather.getSys().getCountry());
                tTemp.setText("Temperature: " + currentWeather.getMain().getTemp());
                tHumidity.setText("Humidity: " + currentWeather.getMain().getHumidity());
                tPressure.setText("Pressure: " + currentWeather.getMain().getPressure());
                tDescription.setText("Weather Description: " + currentWeather.getWeather().get(0).getDescription());
            }
            @Override
            public void onFailure (Throwable throwable){
                //
            }
        });
        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        addBinding = null;
    }
}
