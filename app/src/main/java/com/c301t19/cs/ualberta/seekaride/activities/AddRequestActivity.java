package com.c301t19.cs.ualberta.seekaride.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.StrictMode;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.c301t19.cs.ualberta.seekaride.R;
import com.c301t19.cs.ualberta.seekaride.core.Location;
import com.c301t19.cs.ualberta.seekaride.core.Rider;
import com.google.gson.Gson;

import org.osmdroid.api.IMapController;
import org.osmdroid.bonuspack.routing.OSRMRoadManager;
import org.osmdroid.bonuspack.routing.Road;
import org.osmdroid.bonuspack.routing.RoadManager;
import org.osmdroid.tileprovider.tilesource.TileSourceFactory;
import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.MapView;
import org.osmdroid.views.overlay.Marker;
import org.osmdroid.views.overlay.Polyline;

import java.text.DecimalFormat;
import java.util.ArrayList;

/**
 *  Activity for a rider to create requests
 */
public class AddRequestActivity extends Activity {

    public Button createR;
    public Button Back;
    private EditText descriptionText;
    private EditText startLocationText;
    private EditText endLocationText;
    private EditText fare;
    private TextView recommendedFare;
    private Location startLocation;
    private Location endLocation;
    private GeoPoint startPoint;
    private MapView map;

    String descriptText;

    /**
     * Takes the filled in information sets variables to it.
     */
    public void write() {
        descriptionText = (EditText) findViewById(R.id.add_Description_Text);
        startLocationText = (EditText) findViewById(R.id.add_Slocation_Text);
        endLocationText = (EditText) findViewById(R.id.add_Elocation_Text);
        fare = (EditText) findViewById(R.id.add_Fare_Text);
        recommendedFare = (TextView) findViewById(R.id.recommendedFareNumber);

        descriptText = descriptionText.getText().toString();

        // EditText as button from
        // http://stackoverflow.com/questions/19765491/android-edittext-field-like-button
        startLocationText.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(),
                        ChooseLocationActivity.class);
                intent.putExtra("key", "start");
                intent.putExtra("callingActivity", "add");
                startActivityForResult(intent, RESULT_OK);
            }
        });

        endLocationText.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(),
                        ChooseLocationActivity.class);
                intent.putExtra("key", "end");
                intent.putExtra("callingActivity", "add");
                startActivityForResult(intent, RESULT_OK);
            }
        });
    }

    /**
     *  Responsible for moving back to rider activity one a request is made
     */
    public void move(){
        createR = (Button) findViewById(R.id.add_Create_Button);
        Back = (Button) findViewById(R.id.add_Back_Button);

        //creates the request and moves you back to the initial rider screen
        createR.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Making toasts from
                // http://stackoverflow.com/questions/3500197/how-to-display-toast-in-android
                if (startLocation == null) {
                    Toast.makeText(getApplicationContext(), "Please fill in " +
                            "start location",
                            Toast.LENGTH_LONG).show();
                }
                if (endLocation == null) {
                    Toast.makeText(getApplicationContext(), "Please fill in " +
                                    "destination location",
                            Toast.LENGTH_LONG).show();
                }
                if (fare.getText().toString().isEmpty()) {
                    Toast.makeText(getApplicationContext(), "Please fill in " +
                                    "request fare",
                            Toast.LENGTH_LONG).show();
                }
                double farePrice;
                try {
                    farePrice = Double.parseDouble(fare.getText().toString());
                } catch (NumberFormatException e) {
                    Toast.makeText(getApplicationContext(), "invalid fare format",
                            Toast.LENGTH_LONG).show();
                    return;
                }
                //changed to !fare.getTex().toString().isEmpty())) for cleaner code.
                if ((startLocation != null) & (endLocation != null) &
                        (!fare.getText().toString().isEmpty())) {
                    write();
                    Rider.getInstance().makeRequest(descriptText, startLocation,
                            endLocation, farePrice);
                    finish();
                }
            }
        });
        //moves you back to the initial rider screen without creating a request.
        Back.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                /*
                Intent Bswitch = new Intent(AddRequestActivity.this, RiderActivity.class);
                startActivity(Bswitch);
                */
                finish();
            }
        });
    }

    /**
     *  Gets locations from ChooseLocationActivity, prints them, and puts them on the map
     */
    public void getLocations() {
        Intent intent = getIntent();
        Gson gson = new Gson();

        // clean the map in case user changes location
        map.getOverlays().clear();

        if (intent.getStringExtra("start") != null) {
            startLocation = new Location("");
            startLocation = gson.fromJson(intent.getStringExtra("start"), startLocation.getClass());
        }
        if (startLocation == null) {
            startLocationText.setText("");
        }
        if (startLocation != null) {
            startLocationText.setText(startLocation.getAddress());
            Marker sMarker = new Marker(map);
            sMarker.setPosition(startLocation.getGeoLocation());
            sMarker.setTitle(startLocation.getAddress());
            sMarker.setIcon(getResources().getDrawable(R.drawable.person));
            sMarker.setAnchor(Marker.ANCHOR_CENTER, Marker.ANCHOR_BOTTOM);
            map.getOverlays().add(sMarker);
        }

        if (intent.getStringExtra("end") != null) {
            endLocation = new Location("");
            endLocation = gson.fromJson(intent.getStringExtra("end"), endLocation.getClass());
        }
        if (endLocation == null)  {
            endLocationText.setText("");
        }
        if (endLocation != null) {
            endLocationText.setText(endLocation.getAddress());
            Marker eMarker = new Marker(map);
            eMarker.setPosition(endLocation.getGeoLocation());
            eMarker.setTitle(endLocation.getAddress());
            eMarker.setIcon(getResources().getDrawable(R.drawable.marker_default));
            eMarker.setAnchor(Marker.ANCHOR_CENTER, Marker.ANCHOR_BOTTOM);
            map.getOverlays().add(eMarker);
        }

        // If both locations specified, draw the route
        if((startLocation != null) & (endLocation != null)) {
            RoadManager roadManager = new OSRMRoadManager(this);
            ArrayList<GeoPoint> waypoints = new ArrayList<GeoPoint>();
            waypoints.add(startLocation.getGeoLocation());
            waypoints.add(endLocation.getGeoLocation());
            Road road = roadManager.getRoad(waypoints);
            Polyline roadOverlay = RoadManager.buildRoadOverlay(road);
            recommendedFare.setText(startLocation.calculateFare(endLocation));
            map.getOverlays().add(roadOverlay);
        }
        map.invalidate();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_request);
        write();
        move();

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        // initialize display map
        map = (MapView) findViewById(R.id.addRequestMap);
        map.setTileSource(TileSourceFactory.MAPNIK);
        map.setBuiltInZoomControls(true);
        map.setMultiTouchControls(true);
        startPoint = new GeoPoint(53.52676, -113.52715);
        IMapController mapController = map.getController();
        mapController.setZoom(10);
        mapController.setCenter(startPoint);

        getLocations();
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        setIntent(intent);
        getLocations();
    }
}
