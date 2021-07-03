package com.example.ass3.fragment;

import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.example.ass3.R;
import com.example.ass3.databinding.HomeFragmentBinding;
import com.example.ass3.entity.PainRecord;
import com.example.ass3.viewmodel.PainRecordViewModel;
import com.example.ass3.viewmodel.SharedViewModel;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.kwabenaberko.openweathermaplib.constant.Languages;
import com.kwabenaberko.openweathermaplib.constant.Units;
import com.kwabenaberko.openweathermaplib.implementation.OpenWeatherMapHelper;
import com.kwabenaberko.openweathermaplib.implementation.callback.CurrentWeatherCallback;
import com.kwabenaberko.openweathermaplib.model.currentweather.CurrentWeather;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class HomeFragment extends Fragment {
    private TextView tCity;
    private TextView tTemp;
    private TextView tHumidity;
    private TextView tPressure;
    private TextView tDescription;


    public HomeFragment() {
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the View for this fragment
        View view = inflater.inflate(R.layout.home_fragment, container, false);

        //Instantiate Class With Your ApiKey As The Parameter
        OpenWeatherMapHelper helper = new OpenWeatherMapHelper("APIKEY");

        //Set Units
        helper.setUnits(Units.METRIC);

        //Set Languages
        helper.setLanguage(Languages.ENGLISH);

        tCity = view.findViewById(R.id.tv_city_name);
        tTemp = view.findViewById(R.id.tv_temp);
        tHumidity = view.findViewById(R.id.tv_humidity);
        tPressure = view.findViewById(R.id.tv_pressure);
        tDescription = view.findViewById(R.id.tv_disc);

        PainRecordViewModel model = new ViewModelProvider(requireActivity()).get(PainRecordViewModel.class);


        helper.getCurrentWeatherByGeoCoordinates(-37.91208766782282, 145.1327710748287, new CurrentWeatherCallback() {
            @Override
            public void onSuccess(CurrentWeather currentWeather) {
                String cityName = currentWeather.getName();
                String countryName = currentWeather.getSys().getCountry();
                double temp = currentWeather.getMain().getTemp();
                double humidity = currentWeather.getMain().getHumidity();
                double pressure = currentWeather.getMain().getPressure();
                String desc = currentWeather.getWeather().get(0).getDescription();
                tCity.setText("City, Country: " + cityName + ", " + countryName);
                tTemp.setText("Temperature: " + temp);
                tHumidity.setText("Humidity: " + humidity);
                tPressure.setText("Pressure: " + pressure);
                tDescription.setText("Weather Description: " + desc);

                model.setTemperature(temp);
                model.setHumidity(humidity);
                model.setPressure(pressure);

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
    }
}
