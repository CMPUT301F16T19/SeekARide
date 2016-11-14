package com.c301t19.cs.ualberta.seekaride.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.MotionEvent;
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

import java.util.ArrayList;

public class AddRequestActivity extends Activity {

    public Button createR;
    public Button Back;
    private EditText description;
    private EditText sLocation;
    private EditText eLocation;
    private TextView fare;
    private TextView recommendedFare;
    private Location startLoc;
    private Location endLoc;
    private GeoPoint startPoint;
    private MapView map;

    String descriptText;

    //takes the filled in information sets variables to it.
    public void write() {
        description = (EditText) findViewById(R.id.add_Description_Text);
        sLocation = (EditText) findViewById(R.id.add_Slocation_Text);
        eLocation = (EditText) findViewById(R.id.add_Elocation_Text);
        fare = (TextView) findViewById(R.id.add_Fare_Text);
        recommendedFare = (TextView) findViewById(R.id.recommendedFareNumber);

        descriptText = description.getText().toString();
        String startText = sLocation.getText().toString();
        String endText = eLocation.getText().toString();
        String fareText = fare.getText().toString();
        // I don't know how we're setting our recommended fares, so it's commented out.
        //recommendedFare.setText();

        sLocation.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(),
                        ChooseLocationActivity.class);
                intent.putExtra("key", "start");
                intent.putExtra("callingActivity", "add");
                startActivityForResult(intent, RESULT_OK);
            }
        });

        eLocation.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(),
                        ChooseLocationActivity.class);
                intent.putExtra("key", "end");
                intent.putExtra("callingActivity", "add");
                startActivityForResult(intent, RESULT_OK);
            }
        });
    }

    public void move(){
        createR = (Button) findViewById(R.id.add_Create_Button);
        Back = (Button) findViewById(R.id.add_Back_Button);
        //creates the request and moves you back to the initial rider screen
        createR.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /*
                Intent Cswitch = new Intent(AddRequestActivity.this, RiderActivity.class);
                startActivity(Cswitch);
                */
                write();
                Rider.getInstance().makeRequest(descriptText, new Location(""), new Location(""), 0);
                finish();
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

    public void getLocations() {
        Intent intent = getIntent();
        Gson gson = new Gson();

        // clean the map in case user changes location
        map.getOverlays().clear();

        if(intent.getStringExtra("start") != null) {
            startLoc = new Location("");
            startLoc = gson.fromJson(intent.getStringExtra("start"), startLoc.getClass());
            //Toast.makeText(getApplicationContext(), startLoc.getAddress(),
            //        Toast.LENGTH_LONG).show();

        }
        if (startLoc == null) {
            sLocation.setText("");
        }
        if (startLoc != null) {
            sLocation.setText(startLoc.getAddress());
            Marker sMarker = new Marker(map);
            sMarker.setPosition(startLoc.getGeoLocation());
            sMarker.setTitle(startLoc.getAddress());
            sMarker.setIcon(getResources().getDrawable(R.drawable.person));
            sMarker.setAnchor(Marker.ANCHOR_CENTER, Marker.ANCHOR_BOTTOM);
            map.getOverlays().add(sMarker);
        }

        if (intent.getStringExtra("end") != null) {
            endLoc = new Location("");
            endLoc = gson.fromJson(intent.getStringExtra("end"), endLoc.getClass());
        }
        if (endLoc == null)  {
            eLocation.setText("");
        }
        if (endLoc != null) {
            eLocation.setText(endLoc.getAddress());
            Marker eMarker = new Marker(map);
            eMarker.setPosition(endLoc.getGeoLocation());
            eMarker.setTitle(endLoc.getAddress());
            eMarker.setIcon(getResources().getDrawable(R.drawable.marker_default));
            eMarker.setAnchor(Marker.ANCHOR_CENTER, Marker.ANCHOR_BOTTOM);
            map.getOverlays().add(eMarker);
        }

        if((startLoc != null) & (endLoc != null)) {
            RoadManager roadManager = new OSRMRoadManager(this);
            ArrayList<GeoPoint> waypoints = new ArrayList<GeoPoint>();
            waypoints.add(startLoc.getGeoLocation());
            waypoints.add(endLoc.getGeoLocation());
            Road road = roadManager.getRoad(waypoints);
            Polyline roadOverlay = RoadManager.buildRoadOverlay(road);
            recommendedFare.setText(calculateFare(startLoc, endLoc));
            map.getOverlays().add(roadOverlay);
        }

        map.invalidate();
    }

    public String calculateFare(Location loc1, Location loc2) {
        GeoPoint geo1 = loc1.getGeoLocation();
        GeoPoint geo2 = loc2.getGeoLocation();
        float distanceInMeters = geo1.distanceTo(geo2);
        float distanceInKm = distanceInMeters/1000;
        double costPerKm = 1.48;
        return String.valueOf(distanceInKm * costPerKm);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_request);
        write();
        move();

        // initialize display map
        map = (MapView) findViewById(R.id.addRequestMap);
        map.setTileSource(TileSourceFactory.MAPNIK);
        map.setBuiltInZoomControls(true);
        map.setMultiTouchControls(true);
        startPoint = new GeoPoint(53.52676, -113.52715);
        IMapController mapController = map.getController();
        mapController.setZoom(9);
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
