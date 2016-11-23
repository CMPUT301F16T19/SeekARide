package com.c301t19.cs.ualberta.seekaride.activities;

import android.app.Activity;
import android.app.NotificationManager;
import android.content.Intent;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.v4.app.NotificationCompat;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.c301t19.cs.ualberta.seekaride.R;
import com.c301t19.cs.ualberta.seekaride.core.Driver;
import com.c301t19.cs.ualberta.seekaride.core.Location;
import com.c301t19.cs.ualberta.seekaride.core.NetworkManager;
import com.c301t19.cs.ualberta.seekaride.core.Request;
import com.c301t19.cs.ualberta.seekaride.core.Rider;

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
    private GeoPoint startPoint;

    private Button acceptO;
    private Button Back;
    private TextView description;
    private TextView sLocation;
    private TextView eLocation;
    private TextView fare;
    private TextView riderInfo;

    private Boolean hasInternet;

    //fills in the blank text views with the relavent information from the request.
    public void write() {
        description = (TextView) findViewById(R.id.view_Description_Text);
        sLocation = (TextView) findViewById(R.id.view_Slocation_Text);
        eLocation = (TextView) findViewById(R.id.view_Elocation_Text);
        fare = (TextView) findViewById(R.id.view_Fare_Text);
        riderInfo = (TextView) findViewById(R.id.view_Info_Text);

        //The commands to fill the text, just needs the proper variable in the brackets
        description.setText("Description: " + request.getDescription());
        sLocation.setText("From: " + request.getStart().getAddress());
        eLocation.setText("To: " + request.getDestination().getAddress());
        fare.setText("Fare: $" + ((Double) request.getPrice()).toString());
        riderInfo.setText("Rider: " + request.getRiderProfile().getUsername());

        MapView map = (MapView) findViewById(R.id.view_Offer_Map);
        map.setTileSource(TileSourceFactory.MAPNIK);
        map.setBuiltInZoomControls(true);
        map.setMultiTouchControls(true);
        startPoint = new GeoPoint(53.52676, -113.52715);
        IMapController mapController = map.getController();
        mapController.setZoom(9);
        mapController.setCenter(startPoint);

        Location start = request.getStart();
        Marker sMarker = new Marker(map);
        sMarker.setPosition(start.getGeoLocation());
        sMarker.setTitle(start.getAddress());
        sMarker.setIcon(getResources().getDrawable(R.drawable.person));
        sMarker.setAnchor(Marker.ANCHOR_CENTER, Marker.ANCHOR_BOTTOM);
        map.getOverlays().add(sMarker);

        Location end = request.getDestination();
        Marker eMarker = new Marker(map);
        eMarker.setPosition(end.getGeoLocation());
        eMarker.setTitle(end.getAddress());
        eMarker.setIcon(getResources().getDrawable(R.drawable.marker_default));
        eMarker.setAnchor(Marker.ANCHOR_CENTER, Marker.ANCHOR_BOTTOM);
        map.getOverlays().add(eMarker);

        RoadManager roadManager = new OSRMRoadManager(this);
        ArrayList<GeoPoint> waypoints = new ArrayList<GeoPoint>();
        waypoints.add(start.getGeoLocation());
        waypoints.add(end.getGeoLocation());
        Road road = roadManager.getRoad(waypoints);
        Polyline roadOverlay = RoadManager.buildRoadOverlay(road);
        map.getOverlays().add(roadOverlay);
    }

    public void move() {
        acceptO = (Button) findViewById(R.id.view_Accept_Button);
        if (getIntent().getBooleanExtra("source", false)) {
            acceptO.setText("Decline");
        }
        Back = (Button) findViewById(R.id.view_Back_Button);
        //builds the notification
        final NotificationCompat.Builder Abuilder =
                new NotificationCompat.Builder(this).setSmallIcon(R.drawable.test).
                        setContentTitle("Rider ready").setContentText("The Rider is ready to be picked up.");
        final int Anotificationid = 1;
        final NotificationManager Anotifymang =
                (NotificationManager) getSystemService(NOTIFICATION_SERVICE);


        //Should add the request to the list, and return you to Driver screen
        acceptO.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (getIntent().getBooleanExtra("source", false)) {
                    Driver.getInstance().removeAcceptedRequest(request);
                } else {
                    Driver.getInstance().acceptRequest(request);
                }
                Anotifymang.notify(Anotificationid, Abuilder.build());
                finish();
            }
        });
        //returns you to the search results screen
        Back.setOnClickListener(new View.OnClickListener() {
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

        NetworkManager.Connectivity connectivity = NetworkManager.getConnectivityStatus(getApplicationContext());
        if (connectivity == NetworkManager.Connectivity.MOBILE || connectivity == NetworkManager.Connectivity.WIFI) {
            hasInternet = true;
        }
        else
        {
            hasInternet = false;
        }

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        if (getIntent().getBooleanExtra("source", false)) {
            request = Driver.getInstance().getAcceptedRequests().get(getIntent().getIntExtra("requestId", -1));
        } else {
            request = Driver.getInstance().getSearchedRequests().get(getIntent().getIntExtra("requestId", -1));
        }
        write();
        move();
    }
}
