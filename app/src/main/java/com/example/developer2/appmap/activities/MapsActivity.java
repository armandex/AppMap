package com.example.developer2.appmap.activities;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.widget.Toast;

import com.example.developer2.appmap.R;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }


    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        mMap.setMinZoomPreference(10);
        mMap.setMaxZoomPreference(15);

        // Add a marker in Sydney and move the camera
        LatLng sydney = new LatLng(-12.08385189873338, -77.03732140449227);
        mMap.addMarker(new MarkerOptions().position(sydney).title("Hola desde OL").draggable(true));
        //mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));

        CameraPosition cameraPosition = new CameraPosition.Builder()
                .target(sydney)
                .zoom(10)               //limit 21
                .bearing(0)             //0 - 360°
                .tilt(90)               //limit 90
                .build();
        mMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));


        mMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
            @Override
            public void onMapClick(LatLng latLng) {
                Toast.makeText(MapsActivity.this,"Click on: "+latLng.latitude+" - "+latLng.longitude,Toast.LENGTH_SHORT).show();
            }
        });
        mMap.setOnMapLongClickListener(new GoogleMap.OnMapLongClickListener() {
            @Override
            public void onMapLongClick(LatLng latLng) {

            }
        });
        mMap.setOnMarkerDragListener(new GoogleMap.OnMarkerDragListener() {
            @Override
            public void onMarkerDragStart(Marker marker) {

            }

            @Override
            public void onMarkerDrag(Marker marker) {

            }

            @Override
            public void onMarkerDragEnd(Marker marker) {
                Toast.makeText(MapsActivity.this,"Marker Dragged: "+marker.getPosition().latitude+" - "+marker.getPosition().longitude,Toast.LENGTH_SHORT).show();
            }
        });
    }
}
