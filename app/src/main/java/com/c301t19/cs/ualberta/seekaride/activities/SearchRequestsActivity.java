package com.c301t19.cs.ualberta.seekaride.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.c301t19.cs.ualberta.seekaride.R;
import com.c301t19.cs.ualberta.seekaride.core.Location;
import com.google.gson.Gson;

import org.osmdroid.api.IMapController;
import org.osmdroid.tileprovider.tilesource.TileSourceFactory;
import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.MapView;
import org.osmdroid.views.overlay.Marker;

import io.searchbox.core.Search;

public class SearchRequestsActivity extends Activity {
    private Button search;
    private EditText keywords;
    private EditText radius;
    private MapView map;

    private Location keyLoc;
    private GeoPoint startPoint;
    String keywordsText;
    String radiusText;

    //gets the keywords used and the radius inputted.
    public void write(){
        keywords = (EditText) findViewById(R.id.search_Keywords_Text);
        radius = (EditText) findViewById(R.id.search_Radius_Text);

        //we don't always need keywords, so what should we do to handle cases when it isn't used?
        //keywordsText = keywords.getText().toString();
        radiusText = radius.getText().toString();

        keywords.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(),
                        ChooseLocationActivity.class);
                intent.putExtra("key", "keyword");
                intent.putExtra("callingActivity", "drive");
                startActivityForResult(intent, RESULT_OK);
            }
        });
    }

    public void move(){
        search = (Button) findViewById(R.id.search_Search_Button);

        //takes the information from the map, and moves you to the requests found screen
        search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                write();
                Intent Nswitch = new Intent(SearchRequestsActivity.this, SearchResultsActivity.class);
                Nswitch.putExtra("keywords", keywordsText);
                Nswitch.putExtra("radius", radiusText);
                startActivity(Nswitch);
            }
        });

    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_requests);
        write();
        move();

        map = (MapView) findViewById(R.id.Map);
        map.setTileSource(TileSourceFactory.MAPNIK);
        map.setBuiltInZoomControls(true);
        map.setMultiTouchControls(true);

        // SET TO CURRENT PHONE LOCATION
        startPoint = new GeoPoint(53.52676, -113.52715);
        IMapController mapController = map.getController();
        mapController.setZoom(9);
        mapController.setCenter(startPoint);

        getLocation();
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        setIntent(intent);
        getLocation();
    }

    public void getLocation() {
        Intent intent = getIntent();
        Gson gson = new Gson();

        map.getOverlays().clear();
        if (intent.getStringExtra("keyword") != null) {
            keyLoc = new Location("");
            keyLoc = gson.fromJson(intent.getStringExtra("keyword"), keyLoc.getClass());
        }
        if (keyLoc == null) {
            keywords.setText("");
        }
        if (keyLoc != null) {
            keywords.setText(keyLoc.getAddress());
            Marker locMarker = new Marker(map);
            locMarker.setPosition(keyLoc.getGeoLocation());
            locMarker.setTitle(keyLoc.getAddress());
            locMarker.setIcon(getResources().getDrawable(R.drawable.person));
            locMarker.setAnchor(Marker.ANCHOR_CENTER, Marker.ANCHOR_BOTTOM);
            map.getOverlays().add(locMarker);
        }
    }
}
