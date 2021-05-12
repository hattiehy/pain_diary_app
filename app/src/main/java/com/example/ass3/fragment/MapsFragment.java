package com.example.ass3.fragment;

import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.ass3.R;
import com.mapbox.mapboxsdk.Mapbox;
import com.mapbox.mapboxsdk.annotations.MarkerOptions;
import com.mapbox.mapboxsdk.camera.CameraPosition;
import com.mapbox.mapboxsdk.geometry.LatLng;
import com.mapbox.mapboxsdk.maps.MapView;
import com.mapbox.mapboxsdk.maps.MapboxMap;
import com.mapbox.mapboxsdk.maps.OnMapReadyCallback;
import com.mapbox.mapboxsdk.maps.Style;

import java.io.IOException;
import java.util.List;

public class MapsFragment extends Fragment {

    private MapView mapView;
    private EditText etAddress;
    private Geocoder geocoder;
    private LatLng latLng;
    private Button bSearch;

    public MapsFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        String token = getString(R.string.mapbox_access_token);
        Mapbox.getInstance(getContext(), token);
        View view = inflater.inflate(R.layout.fragment_maps, container, false);
        geocoder = new Geocoder(getContext());
        etAddress = view.findViewById(R.id.et_address);

        bSearch = view.findViewById(R.id.btn_search_address);
        bSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String homeAddress = etAddress.getText().toString().trim();
                if (homeAddress == null) {
                    Toast.makeText(getContext(), "Please enter correct address" , Toast.LENGTH_SHORT).show();
                    return;
                }

                try {
                    List<Address> addresses = geocoder.getFromLocationName(homeAddress, 5);
                    if (addresses.size() == 0) {
                        Toast.makeText(getContext(), "Please enter correct address" , Toast.LENGTH_SHORT).show();
                        return;
                    }
                    Address address = addresses.get(0);
                    latLng = new LatLng(address.getLatitude(), address.getLongitude());
                } catch (IOException e) {
                    e.printStackTrace();
                }

                mapView = view.findViewById(R.id.mapView);
                mapView.onCreate(savedInstanceState);
                mapView.getMapAsync(new OnMapReadyCallback() {
                    @Override
                    public void onMapReady(@NonNull MapboxMap mapboxMap) {
                        mapboxMap.setStyle(Style.MAPBOX_STREETS, new Style.OnStyleLoaded() {
                            @Override
                            public void onStyleLoaded(@NonNull Style style) {
                                CameraPosition position = new CameraPosition.Builder()
                                        .target(latLng)
                                        .zoom(13)
                                        .build();
                                mapboxMap.setCameraPosition(position);
                                mapboxMap.addMarker(new MarkerOptions()
                                .position(latLng)
                                .title("Home"));
                            }
                        });
                    }
                });
            }
        });

        return view;
    }
}