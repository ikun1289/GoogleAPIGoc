package com.example.googleapi.Login;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import com.example.googleapi.R;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

public class PickPlaceDialog extends AppCompatActivity implements OnMapReadyCallback {


    double lat = 10.955449 ,lng = 106.849722;
    CreateQuanAnActivity activity = new CreateQuanAnActivity();
    GoogleMap mapAPI;
    View mMapView;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_pick_place);

        Bundle b = getIntent().getExtras();
        if(b!=null)
        {
            lat = b.getDouble("lat");
            lng = b.getDouble("lng");
        }

        Button btnPickPlace = findViewById(R.id.btnPickPlace);

        SupportMapFragment supportMapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.mapAPI);


        mMapView = supportMapFragment.getView();
        supportMapFragment.getMapAsync(this);
        //

        btnPickPlace.setOnClickListener(v -> {
            Intent returnIntent = new Intent();
            returnIntent.putExtra("result",lat+" "+lng);
            setResult(Activity.RESULT_OK,returnIntent);
            finish();
        });

    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mapAPI = googleMap;
        LatLng posisiabsen = new LatLng(lat, lng); ////your lat lng
        googleMap.addMarker(new MarkerOptions().position(posisiabsen).title("Place"));
        //googleMap.moveCamera(CameraUpdateFactory.newLatLng(posisiabsen));
        googleMap.getUiSettings().setZoomControlsEnabled(true);

        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(lat,
                lng), 20
        ));

        mapAPI.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
            @Override
            public void onMapClick(LatLng latLng) {
                mapAPI.clear();
                LatLng posisiabsen = latLng;
                lat = latLng.latitude;
                lng = latLng.longitude;
                googleMap.addMarker(new MarkerOptions().position(posisiabsen).title("Place"));
                //googleMap.moveCamera(CameraUpdateFactory.newLatLng(posisiabsen));
                ;
            }
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent returnIntent = new Intent();
        returnIntent.putExtra("result",lat+" "+lng);
        setResult(Activity.RESULT_CANCELED,returnIntent);
        finish();

    }
}
