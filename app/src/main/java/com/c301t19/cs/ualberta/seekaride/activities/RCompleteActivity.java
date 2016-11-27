package com.c301t19.cs.ualberta.seekaride.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.StrictMode;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RatingBar;

import com.c301t19.cs.ualberta.seekaride.R;
import com.c301t19.cs.ualberta.seekaride.core.Driver;
import com.c301t19.cs.ualberta.seekaride.core.ElasticsearchController;
import com.c301t19.cs.ualberta.seekaride.core.Location;
import com.c301t19.cs.ualberta.seekaride.core.Request;
import com.c301t19.cs.ualberta.seekaride.core.Review;
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

public class RCompleteActivity extends Activity {

    private Button confirmP;
    private EditText review;
    private RatingBar ratingBar;

    private String reviewText;
    private float rating;

    private boolean isRider;
    //private int requestIndex;
    private Request request;

    //handles getting the review, but doesn't save it anywhere.
    public void write(){
        review = (EditText) findViewById(R.id.complete_Review_Text);

        reviewText = review.getText().toString();
        rating = ratingBar.getRating();
    }

    public void move(){
        confirmP = (Button) findViewById(R.id.complete_Confirm_Button);
        ratingBar = (RatingBar) findViewById(R.id.complete_ratingBar);
        if (isRider) {
            confirmP.setText("Pay Driver and Finish");
        }
        else {
            confirmP.setText("Receive Payment and Finish");
        }
        //moves you to the login screen, after saving your review.
        confirmP.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                write();
                Review review = new Review(reviewText, rating, getIntent().getStringExtra("theirID"));
                Rider.getInstance().leaveReview(review);

                if (isRider) {
                    Rider.getInstance().updateOpenRequests();
                    request = Rider.getInstance().getRequest(request.getId());
                    Rider.getInstance().makePayment(request);
                    Rider.getInstance().clearRequestInProgress();
                    Driver.getInstance().clearRequestInProgress();
                    Rider.getInstance().setReceivedNotification(false);
                    Driver.getInstance().setReceivedNotification(false);
                    Log.i("WOW",((Boolean)(Driver.getInstance().getRequestInProgress() == null)).toString());
                    startActivity(new Intent(RCompleteActivity.this, RiderActivity.class));
                }
                else {
                    Driver.getInstance().updateAcceptedRequests();
                    request = Driver.getInstance().getRequest(request.getId());
                    Driver.getInstance().receivePayment(request);
                    Log.i("WOW1",((Boolean)(Driver.getInstance().getRequestInProgress() == null)).toString());
                    Rider.getInstance().clearRequestInProgress();
                    Driver.getInstance().clearRequestInProgress();
                    Rider.getInstance().setReceivedNotification(false);
                    Driver.getInstance().setReceivedNotification(false);
                    Log.i("WOW2",((Boolean)(Driver.getInstance().getRequestInProgress() == null)).toString());
                    startActivity(new Intent(RCompleteActivity.this, DriverActivity.class));
                }

                finish();
            }
        });

    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rcomplete);
        isRider = getIntent().getBooleanExtra("isRider", false);
        //requestIndex = getIntent().getIntExtra("requestIndex", -1);
        if (isRider) {
            //request = Rider.getInstance().getOpenRequests().get(requestIndex);
            request = Rider.getInstance().getRequestInProgress();
        }
        else {
            //request = Driver.getInstance().getAcceptedRequests().get(requestIndex);
            request = Driver.getInstance().getRequestInProgress();
        }

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        final MapView map = (MapView) findViewById(R.id.rCompleteMap);
        map.setTileSource(TileSourceFactory.MAPNIK);
        map.setBuiltInZoomControls(true);
        map.setMultiTouchControls(true);
        IMapController mapController = map.getController();
        mapController.setZoom(11);

        Location start = request.getStart();
        Marker startMarker = new Marker(map);
        startMarker.setPosition(start.getGeoLocation());
        startMarker.setTitle(start.getAddress());
        startMarker.setIcon(getResources().getDrawable(R.drawable.person));
        startMarker.setAnchor(Marker.ANCHOR_CENTER, Marker.ANCHOR_BOTTOM);
        map.getOverlays().add(startMarker);

        mapController.setCenter(start.getGeoLocation());

        Location end = request.getDestination();
        Marker endMarker = new Marker(map);
        endMarker.setPosition(end.getGeoLocation());
        endMarker.setTitle(end.getAddress());
        endMarker.setIcon(getResources().getDrawable(R.drawable.marker_default));
        endMarker.setAnchor(Marker.ANCHOR_CENTER, Marker.ANCHOR_BOTTOM);
        map.getOverlays().add(endMarker);

        RoadManager roadManager = new OSRMRoadManager(this);
        ArrayList<GeoPoint> waypoints = new ArrayList<GeoPoint>();
        waypoints.add(start.getGeoLocation());
        waypoints.add(end.getGeoLocation());
        Road road = roadManager.getRoad(waypoints);
        Polyline roadOverlay = RoadManager.buildRoadOverlay(road);
        map.getOverlays().add(roadOverlay);

        move();
    }
}
