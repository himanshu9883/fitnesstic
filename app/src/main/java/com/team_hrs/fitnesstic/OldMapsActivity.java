package com.team_hrs.fitnesstic;

import android.annotation.TargetApi;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.widget.RelativeLayout;
import android.widget.TextView;


import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class OldMapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private String TAG = "MapsActivity";
    public ArrayList locationArray = new ArrayList();



    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_old_maps);
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        Toolbar myToolbar = (Toolbar) findViewById(R.id.my_toolbar);

        Intent intent = getIntent();
        locationArray = intent.getParcelableArrayListExtra("Location");

        final TextView noteText;
        final RelativeLayout noteArea = (RelativeLayout)findViewById(R.id.noteArea);
        noteArea.setBackgroundColor(Color.parseColor("#fff2cc"));
        noteText = (TextView)findViewById(R.id.notesTextView);


        noteText.setText(intent.getStringExtra("Note"));
        myToolbar.setTitle(intent.getStringExtra("Title"));
    }

    public LatLng[] convertLocation(){
        LatLng[] map = new LatLng[locationArray.size()];
        for (int i = 0; i < locationArray.size(); i++){
            try {
                JSONObject jObject = new JSONObject(locationArray.get(i).toString());
                double lat = jObject.getDouble("latitude");
                double lng = jObject.getDouble("longitude");
                LatLng dot = new LatLng(lat, lng);
                map[i] = dot;
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return map;
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        LatLng[] drawMap = convertLocation();

        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(drawMap[0], 17.0f));

        for (int i=0; i < drawMap.length ; i++) {
            double lat = drawMap[i].latitude;
            Log.i(TAG, String.valueOf(lat));
            mMap.addCircle(new CircleOptions()
                    .center(drawMap[i])
                    .radius(12)
                    .fillColor(0x7f0000ff)
                    .strokeWidth(0));
        }
    }
}
