package com.c301t19.cs.ualberta.seekaride.activities;

import android.app.Activity;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.StrictMode;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.c301t19.cs.ualberta.seekaride.R;

import org.osmdroid.api.IMapController;
import org.osmdroid.bonuspack.location.NominatimPOIProvider;
import org.osmdroid.bonuspack.location.POI;
import org.osmdroid.tileprovider.tilesource.TileSourceFactory;
import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.MapView;
import org.osmdroid.views.overlay.FolderOverlay;
import org.osmdroid.views.overlay.Marker;

import java.util.ArrayList;

public class ChooseLocationActivity extends Activity {

    private EditText location;

    private GeoPoint startPoint;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose_locations);

        //
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        final MapView map = (MapView) findViewById(R.id.chooseMap);
        map.setTileSource(TileSourceFactory.MAPNIK);
        map.setBuiltInZoomControls(true);
        map.setMultiTouchControls(true);

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
                    ArrayList<POI> pois = poiProvider.getPOICloseTo(startPoint,
                            location.getText().toString(), 50, 0.1);

                    FolderOverlay poiMarkers = new FolderOverlay(ChooseLocationActivity.this);
                    map.getOverlays().add(poiMarkers);

                    Drawable poiIcon = getResources().getDrawable(R.drawable.marker_default);
                    for (POI poi:pois){
                        Marker poiMarker = new Marker(map);
                        poiMarker.setTitle(poi.mType);
                        poiMarker.setSnippet(poi.mDescription);
                        poiMarker.setPosition(poi.mLocation);
                        poiMarker.setIcon(poiIcon);
                        poiMarkers.add(poiMarker);
                    }
                    map.invalidate();

                }
            }
        });
    }
}
