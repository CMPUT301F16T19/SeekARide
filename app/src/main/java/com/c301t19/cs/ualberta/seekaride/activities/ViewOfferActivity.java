package com.c301t19.cs.ualberta.seekaride.activities;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Paint;
import android.os.Bundle;
import android.os.StrictMode;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.c301t19.cs.ualberta.seekaride.R;
import com.c301t19.cs.ualberta.seekaride.core.Driver;
import com.c301t19.cs.ualberta.seekaride.core.Location;
import com.c301t19.cs.ualberta.seekaride.core.Request;

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

/**
 * Activity for Drivers to view and accept requests.
 */
public class ViewOfferActivity extends Activity {

    private Request request;
    private Button acceptOfferButton;
    private Button backButton;
    private TextView description;
    private TextView startLocationText;
    private TextView destinationText;
    private TextView fare;
    private TextView riderInfo;
    private Boolean hasInternet;

    /**
     * fills in the blank text views with the relavent information from the request.
     */
    public void write() {
        description = (TextView) findViewById(R.id.view_Description_Text);
        startLocationText = (TextView) findViewById(R.id.view_Slocation_Text);
        destinationText = (TextView) findViewById(R.id.view_Elocation_Text);
        fare = (TextView) findViewById(R.id.view_Fare_Text);
        riderInfo = (TextView) findViewById(R.id.view_Info_Text);

        description.setText("Description: " + request.getDescription());
        startLocationText.setText("From: " + request.getStart().getAddress());
        destinationText.setText("To: " + request.getDestination().getAddress());
        fare.setText("Fare: $" + ((Double) request.getPrice()).toString());
        riderInfo.setText("Rider: " + request.getRiderProfile().getUsername());
        riderInfo.setPaintFlags(riderInfo.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);

        // Initialize map
        MapView map = (MapView) findViewById(R.id.view_Offer_Map);
        map.setTileSource(TileSourceFactory.MAPNIK);
        map.setBuiltInZoomControls(true);
        map.setMultiTouchControls(true);
        IMapController mapController = map.getController();
        mapController.setZoom(10);

        // Draw start location marker
        Location start = request.getStart();
        Marker startMarker = new Marker(map);
        startMarker.setPosition(start.getGeoLocation());
        startMarker.setTitle(start.getAddress());
        startMarker.setIcon(getResources().getDrawable(R.drawable.person));
        startMarker.setAnchor(Marker.ANCHOR_CENTER, Marker.ANCHOR_BOTTOM);
        map.getOverlays().add(startMarker);
        mapController.setCenter(start.getGeoLocation());

        // Draw destination marker
        Location end = request.getDestination();
        Marker endMarker = new Marker(map);
        endMarker.setPosition(end.getGeoLocation());
        endMarker.setTitle(end.getAddress());
        endMarker.setIcon(getResources().getDrawable(R.drawable.marker_default));
        endMarker.setAnchor(Marker.ANCHOR_CENTER, Marker.ANCHOR_BOTTOM);
        map.getOverlays().add(endMarker);

        // Draw the route in between start and destination
        RoadManager roadManager = new OSRMRoadManager(this);
        ArrayList<GeoPoint> waypoints = new ArrayList<GeoPoint>();
        waypoints.add(start.getGeoLocation());
        waypoints.add(end.getGeoLocation());
        Road road = roadManager.getRoad(waypoints);
        Polyline roadOverlay = RoadManager.buildRoadOverlay(road);
        map.getOverlays().add(roadOverlay);
    }

    /**
     * Responsible for moving to different activities
     */
    public void move() {
        acceptOfferButton = (Button) findViewById(R.id.view_Accept_Button);
        if (getIntent().getBooleanExtra("source", false)) {
            acceptOfferButton.setText("Decline");
        }
        backButton = (Button) findViewById(R.id.view_Back_Button);
        /*builds the notification
        final NotificationCompat.Builder Abuilder =
                new NotificationCompat.Builder(this).setSmallIcon(R.drawable.test).
                        setContentTitle("Rider ready").setContentText("The Rider is ready to be picked up.");
        final int Anotificationid = 1;
        final NotificationManager Anotifymang =
                (NotificationManager) getSystemService(NOTIFICATION_SERVICE);*/


        //Should add the request to the list, and return you to Driver screen
        acceptOfferButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Driver.getInstance().getRequestInProgress() != null) {
                    Toast.makeText(getApplicationContext(), "Could not accept request. Perhaps a request is already in progress.",
                            Toast.LENGTH_LONG).show();
                    return;
                }
                if (getIntent().getBooleanExtra("source", false)) {
                    Driver.getInstance().removeAcceptedRequest(request);
                } else {
                    Driver.getInstance().acceptRequest(request);
                }
                //Anotifymang.notify(Anotificationid, Abuilder.build());
                finish();
            }
        });
        //returns you to the search results screen
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        //profile ID search is not working, so will use username for now
        riderInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ViewOfferActivity.this, ViewProfileActivity.class);
                intent.putExtra("profileId", request.getRiderProfile().getId());
                intent.putExtra("name", request.getRiderProfile().getUsername());
                startActivity(intent);
            }
        });
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_offer);

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        if (getIntent() != null && request == null)
        {
            if (getIntent().getBooleanExtra("source", false)) {
                request = Driver.getInstance().getAcceptedRequests().get(getIntent().getIntExtra("requestId", -1));
            } else {
                request = Driver.getInstance().getSearchedRequests().get(getIntent().getIntExtra("requestId", -1));
            }
        }
        write();
        move();
    }
}
