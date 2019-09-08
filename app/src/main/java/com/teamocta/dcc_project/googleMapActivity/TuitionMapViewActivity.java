package com.teamocta.dcc_project.googleMapActivity;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;

import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.teamocta.dcc_project.R;
import com.teamocta.dcc_project.pojo.Support;
import com.teamocta.dcc_project.pojo.UserProfile;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

public class TuitionMapViewActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private UserProfile currentStudent;
    private Button btnLocationName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        currentStudent = (UserProfile) getIntent().getSerializableExtra("studentProfile");
        btnLocationName = findViewById(R.id.btnLocationName);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        mMap.setMyLocationEnabled(true);
        mMap.getUiSettings().setMyLocationButtonEnabled(false);
        mMap.getUiSettings().setCompassEnabled(true);

        if(currentStudent.getLatitude() != null && currentStudent.getLongitude() != null){
            double latitude = Double.parseDouble(currentStudent.getLatitude());
            double longitude = Double.parseDouble(currentStudent.getLongitude());
            LatLng location = new LatLng(latitude, longitude);
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(location, 15));
            mMap.addMarker(new MarkerOptions().position(location).title("Tuition Location"));
            btnLocationName.setText("Tuition Location: " + getAddress(latitude, longitude));
        }else {
            Support.toastMessageLong("Wait", getApplicationContext());
        }
    }

    public void btnCurrentLocation(View view) {
        getCurrentLocation();
    }
    private void getCurrentLocation() {
        FusedLocationProviderClient fusedLocationProviderClient = new FusedLocationProviderClient(this);
        Task location = fusedLocationProviderClient.getLastLocation();
        location.addOnCompleteListener(new OnCompleteListener() {
            @Override
            public void onComplete(@NonNull Task task) {
                if(task.isSuccessful()){
                    Location currentLocation = (Location) task.getResult();
                    LatLng currentLatLng;
                    if (currentLocation != null) {
                        currentLatLng = new LatLng(currentLocation.getLatitude(), currentLocation.getLongitude());
                        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(currentLatLng, 15));
                        mMap.addMarker(new MarkerOptions().position(currentLatLng).title("Your Location"));
                    }else{
                        Support.toastMessageLong("Enable location from Device", getApplicationContext());
                    }
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Support.toastMessageLong(e.getMessage(), getApplicationContext());
            }
        });
    }

    public String getAddress(double lat, double lng){
        String address = "";

        Geocoder geocoder = new Geocoder(getApplicationContext(), Locale.getDefault());
        try {
            List<Address> addresses = geocoder.getFromLocation(lat, lng, 1);
            if(addresses.size()>0){
                address = addresses.get(0).getAddressLine(0);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return address;
    }

}
