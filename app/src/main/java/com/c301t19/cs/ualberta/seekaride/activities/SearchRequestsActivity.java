package com.c301t19.cs.ualberta.seekaride.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.c301t19.cs.ualberta.seekaride.R;
import com.c301t19.cs.ualberta.seekaride.core.Location;
import com.google.gson.Gson;

import org.osmdroid.api.IMapController;
import org.osmdroid.tileprovider.tilesource.TileSourceFactory;
import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.MapView;
import org.osmdroid.views.overlay.Marker;

/**
 *  Activity for a driver to search for requests
 */
public class SearchRequestsActivity extends Activity {
    private Button search;
    private EditText keywords;
    private EditText radius;
    private EditText startLocation;
    private MapView map;
    private Location keyLocation;
    private GeoPoint startPoint;
    private String keywordsText;
    private String radiusText;

    /**
     *  gets the keywords used and the radius inputted.
     */
    public void write(){
        keywords = (EditText) findViewById(R.id.search_Keywords_Text);
        radius = (EditText) findViewById(R.id.search_Radius_Text);

        startLocation = (EditText) findViewById(R.id.starting_location);
        //we don't always need keywords, so what should we do to handle cases when it isn't used?
        keywordsText = keywords.getText().toString();
        radiusText = radius.getText().toString();

        startLocation.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(),
                        ChooseLocationActivity.class);
                intent.putExtra("key", "keyword");
                intent.putExtra("callingActivity", "drive");
                startActivityForResult(intent, RESULT_OK);
            }
        });
    }

    /**
     *  Responsible for moving to SearchResultsActivity if the driver presses the search button
     */
    public void move(){
        search = (Button) findViewById(R.id.search_Search_Button);

        //takes the information from the map, and moves you to the requests found screen
        search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                write();
                Intent Nswitch = new Intent(SearchRequestsActivity.this, SearchResultsActivity.class);
                if (keyLocation != null) {
                    if (radiusText.equals("")) {
                        Toast.makeText(getApplicationContext(), "Please enter radius",
                                Toast.LENGTH_LONG).show();
                        return;
                    }
                    // Converting string to double from
                    // http://stackoverflow.com/questions/1102891/how-to-check-if-a-string-is-numeric-in-java
                    try {
                        Double.parseDouble(radiusText);
                    } catch (NumberFormatException e) {
                        Toast.makeText(getApplicationContext(), "Invalid radius format",
                                Toast.LENGTH_LONG).show();
                        return;
                    }
                    Gson gson = new Gson();
                    String send;
                    send = gson.toJson(keyLocation);
                    Nswitch.putExtra("queryLocation", send);
                }
                else {
                    Nswitch.putExtra("keywords", keywordsText);
                }
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

        // Initialize map
        map = (MapView) findViewById(R.id.Map);
        map.setTileSource(TileSourceFactory.MAPNIK);
        map.setBuiltInZoomControls(true);
        map.setMultiTouchControls(true);
        startPoint = new GeoPoint(53.52676, -113.52715);
        IMapController mapController = map.getController();
        mapController.setZoom(10);
        mapController.setCenter(startPoint);

        getLocation();
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        setIntent(intent);
        getLocation();
    }

    /**
     *  Gets location from ChooseLocationActivity, prints it, and puts it on the map
     */
    public void getLocation() {
        Intent intent = getIntent();
        Gson gson = new Gson();

        map.getOverlays().clear();
        if (intent.getStringExtra("keyword") != null) {
            keyLocation = new Location("");
            keyLocation = gson.fromJson(intent.getStringExtra("keyword"), keyLocation.getClass());
        }
        if (keyLocation == null) {
            keywords.setText("");
        }
        if (keyLocation != null) {
            startLocation.setText(keyLocation.getAddress());
            Marker locMarker = new Marker(map);
            locMarker.setPosition(keyLocation.getGeoLocation());
            locMarker.setTitle(keyLocation.getAddress());
            locMarker.setIcon(getResources().getDrawable(R.drawable.person));
            locMarker.setAnchor(Marker.ANCHOR_CENTER, Marker.ANCHOR_BOTTOM);
            map.getOverlays().add(locMarker);
        }
    }

    @Override
    // dropdown menu
    public boolean onCreateOptionsMenu(Menu m) {
        getMenuInflater().inflate(R.menu.main, m);
        return true;
    }

    // dropdown menu
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.mainMenuRider:
                return false;
            case R.id.mainMenuDriver:
                startActivity(new Intent(this, DriverActivity.class));
                return true;
            case R.id.mainMenuProfile:
                startActivity(new Intent(this, EditAccountActivity.class));
                return true;
            default:
                return false;
            //return super.onOptionsItemSelected(item);
        }
    }
}
