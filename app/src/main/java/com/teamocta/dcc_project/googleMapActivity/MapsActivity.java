package com.teamocta.dcc_project.googleMapActivity;

import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.teamocta.dcc_project.R;
import com.teamocta.dcc_project.pojo.Support;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private Button btnLocationName;
    private LatLng currentMarkerPosition;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        btnLocationName = findViewById(R.id.btnLocationName);
        registerForContextMenu(btnLocationName);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        mMap.setMyLocationEnabled(true);
        mMap.getUiSettings().setMyLocationButtonEnabled(false);

        getCurrentLocation();
    }

    private void getCurrentLocation() {
        FusedLocationProviderClient fusedLocationProviderClient = new FusedLocationProviderClient(this);
        Task location = fusedLocationProviderClient.getLastLocation();
        location.addOnCompleteListener(new OnCompleteListener() {
            @Override
            public void onComplete(@NonNull Task task) {
                if(task.isSuccessful()){
                    mMap.clear();
                    Location currentLocation = (Location) task.getResult();
                    LatLng currentLatLng;
                    if (currentLocation != null) {
                        currentLatLng = new LatLng(currentLocation.getLatitude(), currentLocation.getLongitude());
                        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(currentLatLng, 15));
                        mMap.addMarker(new MarkerOptions().position(currentLatLng).draggable(true));
                        btnLocationName.setText(getAddress(currentLatLng.latitude, currentLatLng.longitude));

                        currentMarkerPosition = currentLatLng;
                        markerDragLocation();
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

    public void btnCurrentLocation(View view) {
        getCurrentLocation();
    }

    private void markerDragLocation() {
        mMap.setOnMarkerDragListener(new GoogleMap.OnMarkerDragListener() {
            @Override
            public void onMarkerDragStart(Marker marker) {
            }

            @Override
            public void onMarkerDrag(Marker marker) {
            }

            @Override
            public void onMarkerDragEnd(Marker marker) {
                LatLng position = marker.getPosition();
                btnLocationName.setText(getAddress(position.latitude, position.longitude));
                currentMarkerPosition = position;
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

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);

        getMenuInflater().inflate(R.menu.map_location_click_menu, menu);
    }

    @Override
    public boolean onContextItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case R.id.saveLocation:
                saveLocation();
                return true;
            default:
                return super.onContextItemSelected(item);
        }
    }

    private void saveLocation() {
        if (currentMarkerPosition!=null){

            Map<String, Object> location = new HashMap<>();
            location.put("latitude", String.valueOf(currentMarkerPosition.latitude));
            location.put("longitude", String.valueOf(currentMarkerPosition.longitude));

            String uid = FirebaseAuth.getInstance().getCurrentUser().getUid();
            DatabaseReference studentRef = FirebaseDatabase.getInstance().getReference();
            studentRef.child("Student").child(uid).updateChildren(location);
            Support.toastMessageLong("Location Saved", getApplicationContext());

            /*Support.toastMessageLong(String.valueOf(currentMarkerPosition.latitude), getApplicationContext());
            Support.toastMessageLong(String.valueOf(currentMarkerPosition.longitude), getApplicationContext());*/
        }else{
            Support.toastMessageLong("Location not detected", getApplicationContext());
        }
    }
}
