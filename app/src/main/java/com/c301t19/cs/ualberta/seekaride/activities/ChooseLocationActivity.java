package com.c301t19.cs.ualberta.seekaride.activities;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
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

import java.util.ArrayList;

public class ChooseLocationActivity extends Activity implements MapEventsReceiver {

    private EditText location;
    private GeoPoint startPoint;
    private ArrayList<POI> pois;
    private Marker longTapMarker;
    private Drawable poiIcon;

    private Location locToSend;
    private String startOrEnd; // PULL FROM INTENT!!!

    MapEventsOverlay mapEventsOverlay = new MapEventsOverlay(this, this);

    @Override public boolean singleTapConfirmedHelper(GeoPoint p) {
        return true;
    }

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

        locToSend = new Location("Custom Location");
        locToSend.setGeoLocation(p);

        longTapMarker = new Marker(map);
        longTapMarker.setTitle("Custom Location");
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

        //
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        poiIcon = getResources().getDrawable(R.drawable.marker_default);

        final MapView map = (MapView) findViewById(R.id.chooseMap);
        map.setTileSource(TileSourceFactory.MAPNIK);
        map.setBuiltInZoomControls(true);
        map.setMultiTouchControls(true);
        map.getOverlays().add(0, mapEventsOverlay);

        // SET TO CURRENT PHONE LOCATION
        startPoint = new GeoPoint(53.52676, -113.52715);
        IMapController mapController = map.getController();
        mapController.setZoom(9);
        mapController.setCenter(startPoint);

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

                    pois = poiProvider.getPOICloseTo(startPoint,
                            location.getText().toString(), 50, 0.2);

                    if (pois.size() == 0) {
                        Toast toast = Toast.makeText(getApplicationContext(),
                                "No Results Found", Toast.LENGTH_LONG);
                        toast.show();
                    }

                    FolderOverlay poiMarkers = new FolderOverlay(ChooseLocationActivity.this);
                    map.getOverlays().add(poiMarkers);


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
                                //ADD THE RETURN HERE
                                locToSend = new Location(title);
                                locToSend.setGeoLocation(loc);
                                return true;
                            }
                        });
                    }
                    map.invalidate();
                }
            }
        });

        Button selectButton = (Button) findViewById(R.id.locationSelectButton);

        selectButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                if (locToSend != null) {
                    Gson gson = new Gson();
                    gson.toJson(locToSend);
                    Intent intent = new Intent(getApplicationContext(), getCallingActivity().getClass());
                    intent.putExtra(startOrEnd, gson.toString());
                    startActivity(intent);
                }
            }
        });
    }
}
