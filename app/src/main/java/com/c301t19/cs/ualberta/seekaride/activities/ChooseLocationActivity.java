package com.c301t19.cs.ualberta.seekaride.activities;

import android.app.Activity;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.os.StrictMode;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.c301t19.cs.ualberta.seekaride.R;
import com.c301t19.cs.ualberta.seekaride.core.Location;

import com.google.gson.Gson;

import org.osmdroid.api.IMapController;
import org.osmdroid.bonuspack.location.NominatimPOIProvider;
import org.osmdroid.bonuspack.location.POI;
import org.osmdroid.events.MapEventsReceiver;
import org.osmdroid.tileprovider.tilesource.TileSourceFactory;
import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.MapView;
import org.osmdroid.views.overlay.FolderOverlay;
import org.osmdroid.views.overlay.MapEventsOverlay;
import org.osmdroid.views.overlay.Marker;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Locale;


/**
 *  Activity responsible for all location searching and setting. Any activity
 *  thata requires a location to be inputted uses this activity.
 */
public class ChooseLocationActivity extends Activity implements MapEventsReceiver {

    private EditText location;
    private GeoPoint startPoint;
    private ArrayList<POI> pois;
    private Marker longTapMarker;
    private Drawable poiIcon;
    private Location locationToSend;
    private String startOrEnd;
    private String callingActivity;

    MapEventsOverlay mapEventsOverlay = new MapEventsOverlay(this, this);

    // Does Nothing
    @Override public boolean singleTapConfirmedHelper(GeoPoint p) {
        return true;
    }

    // Long Press on the map will pick the pressed location
    @Override public boolean longPressHelper(GeoPoint p) {
        final MapView map = (MapView) findViewById(R.id.chooseMap);
        if (pois != null) {
            pois.clear();
            map.getOverlays().clear();
            map.getOverlays().add(0, mapEventsOverlay);
        }
        if (longTapMarker != null) {
            longTapMarker.remove(map);
        }

        String locationName;
        Address address;

        // GeoPoint to address code from
        // http://stackoverflow.com/questions/11197070/geopoint-to-address
        Geocoder geoCoder = new Geocoder(getApplicationContext(), Locale.getDefault());
        try {
            address = geoCoder.getFromLocation(p.getLatitude(), p.getLongitude(), 1).get(0);
            locationName = address.getAddressLine(0);
        } catch (IOException e) {
            locationName = "Custom Location";
        }

        locationToSend = new Location(locationName);
        locationToSend.setGeoLocation(p);

        longTapMarker = new Marker(map);
        longTapMarker.setTitle(locationName);
        longTapMarker.setPosition(p);
        longTapMarker.setIcon(poiIcon);
        map.getOverlays().add(longTapMarker);

        map.invalidate();

        return false;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose_locations);

        // Required for non asynchronous map functionality
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        poiIcon = getResources().getDrawable(R.drawable.marker_default);
        pois = new ArrayList<POI>();

        // Initialize map
        final MapView map = (MapView) findViewById(R.id.chooseMap);
        map.setTileSource(TileSourceFactory.MAPNIK);
        map.setBuiltInZoomControls(true);
        map.setMultiTouchControls(true);
        map.getOverlays().add(0, mapEventsOverlay);
        startPoint = new GeoPoint(53.52676, -113.52715);
        IMapController mapController = map.getController();
        mapController.setZoom(10);
        mapController.setCenter(startPoint);

        // Check which activity called this one, and check what it wants back
        Intent intention = getIntent();
        startOrEnd =  intention.getStringExtra("key");
        callingActivity = intention.getStringExtra("callingActivity");


        Button searchButton = (Button) findViewById(R.id.locationSearchButton);
        location = (EditText) findViewById(R.id.chooseLocationText);

        searchButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if (location.getText().toString() != null) {
                    NominatimPOIProvider poiProvider = new NominatimPOIProvider("wat");
                    if (pois != null) {
                        pois.clear();
                        map.getOverlays().clear();
                        map.getOverlays().add(0, mapEventsOverlay);
                    }

                    // check if keyword is entered
                    pois = poiProvider.getPOICloseTo(startPoint,
                            location.getText().toString(), 50, 0.2);

                    // if keyword (mall, park, etc) not entered
                    // check if location entered is address or name of location
                    if (pois.size() == 0) {
                        String query = location.getText().toString().toLowerCase()
                                .replace("street", "").replace(" st", "").replace("avenue", "")
                                .replace(" ave", "").replace(" ", "+");
                        String url = ("http://nominatim.openstreetmap.org/search" + "?q=" + query +
                                "&format=json");
                        pois = poiProvider.getThem(url);
                    }

                    // if neither gets any results, toast no results found
                    if (pois.size() == 0) {
                        Toast toast = Toast.makeText(getApplicationContext(),
                                "No Results Found", Toast.LENGTH_LONG);
                        toast.show();
                    }

                    FolderOverlay poiMarkers = new FolderOverlay(ChooseLocationActivity.this);
                    map.getOverlays().add(poiMarkers);

                    // Draw all the pois returned by the search
                    for (POI poi:pois){
                        Marker poiMarker = new Marker(map);
                        final String title = poi.mDescription;
                        poiMarker.setTitle(poi.mType);
                        poiMarker.setSnippet(poi.mDescription);
                        final GeoPoint loc = poi.mLocation;
                        poiMarker.setPosition(poi.mLocation);
                        poiMarker.setIcon(poiIcon);
                        poiMarkers.add(poiMarker);
                        poiMarker.setOnMarkerClickListener(new Marker.OnMarkerClickListener() {
                            @Override
                            public boolean onMarkerClick(Marker item, MapView map) {
                                item.showInfoWindow();

                                // Set the location to send if clicked
                                locationToSend = new Location(title);
                                locationToSend.setGeoLocation(loc);
                                return true;
                            }
                        });
                    }
                    map.invalidate();
                }
            }
        });

        // Returns and sends location selected to the activity that called this one
        Button selectButton = (Button) findViewById(R.id.locationSelectButton);
        selectButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                if (locationToSend != null) {
                    Gson gson = new Gson();
                    String send;
                    send = gson.toJson(locationToSend);
                    Intent intent;

                    // Communication between activites from
                    // http://www.helloandroid.com/tutorials/communicating-between-running-activities
                    // http://stackoverflow.com/questions/4967799/how-to-know-the-calling-activity-in-android
                    if (callingActivity.equals("add")) {
                        intent = new Intent(getApplicationContext(), AddRequestActivity.class);
                    }
                    else if (callingActivity.equals("edit")) {
                        intent = new Intent(getApplicationContext(), EditRequestActivity.class);
                    }
                    else if (callingActivity.equals("drive")) {
                        intent = new Intent(getApplicationContext(), SearchRequestsActivity.class);
                    }
                    else {
                        intent = new Intent(getApplicationContext(), MainActivity.class);
                        Toast.makeText(getApplicationContext(),
                                "ChooseLocationActivity intents bugged", Toast.LENGTH_LONG).show();
                    }
                    intent.putExtra(startOrEnd, /*gson.toString()*/ send);
                    startActivity(intent);
                    finish();
                }
            }
        });
    }
}
