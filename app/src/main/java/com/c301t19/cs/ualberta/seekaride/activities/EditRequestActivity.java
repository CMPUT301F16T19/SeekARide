package com.c301t19.cs.ualberta.seekaride.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.c301t19.cs.ualberta.seekaride.R;
import com.c301t19.cs.ualberta.seekaride.core.Location;
import com.c301t19.cs.ualberta.seekaride.core.Request;
import com.c301t19.cs.ualberta.seekaride.core.Rider;
import com.google.gson.Gson;

import org.osmdroid.bonuspack.routing.OSRMRoadManager;
import org.osmdroid.bonuspack.routing.Road;
import org.osmdroid.bonuspack.routing.RoadManager;
import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.overlay.Marker;
import org.osmdroid.views.overlay.Polyline;

import java.util.ArrayList;

public class EditRequestActivity extends Activity {

    Request request;

    private Button editR;
    private Button deleteR;
    private Button Cancel;
    private EditText descriptionText;
    private TextView startLocationText;
    private TextView endLocationText;
    private TextView fare;
    private TextView recommendedFare;
    private Location startLocation;
    private Location endLocation;
    private String fareString;

    private DriversAdapter adapter;
    private ListView driversList;

    //takes the filled in information sets variables to it.
    public void write() {
        descriptionText = (EditText) findViewById(R.id.edit_Description_Text);

        startLocation = request.getStart();
        startLocationText = (TextView) findViewById(R.id.edit_Slocation_Text);
        endLocation = request.getDestination();
        endLocationText = (TextView) findViewById(R.id.edit_Elocation_Text);

        fare = (EditText) findViewById(R.id.edit_Fare_Text);
        fareString = String.valueOf(request.getPrice());
        recommendedFare = (TextView) findViewById(R.id.editRecommendedFareNumber);

        descriptionText.setText(request.getDescription());
        startLocationText.setText(request.getStart().getAddress());
        endLocationText.setText(request.getDestination().getAddress());
        fare.setText(fareString);

        recommendedFare.setText(request.getStart().calculateFare(
                request.getDestination()));

        startLocationText.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(),
                        ChooseLocationActivity.class);
                intent.putExtra("key", "start");
                intent.putExtra("callingActivity", "edit");
                startActivityForResult(intent, RESULT_OK);
            }
        });

        endLocationText.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(),
                        ChooseLocationActivity.class);
                intent.putExtra("key", "end");
                intent.putExtra("callingActivity", "edit");
                startActivityForResult(intent, RESULT_OK);
            }
        });

        //desciptText = descriptionText.getText().toString();
        //String startText = startLocationText.getText().toString();
        //String endText = endLocationText.getText().toString();
        //fareText = fare.getText().toString();
        // I don't know how we're setting our recommended fares, so it's commented out.
        //recommendedFare.setText();
    }

    public void move() {
        editR = (Button) findViewById(R.id.edit_Edit_Button);
        deleteR = (Button) findViewById(R.id.edit_Delete_Button);
        Cancel = (Button) findViewById(R.id.edit_Cancel_Button);
        //edits the request and moves you back to the rider screen.
        editR.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //write();
                Request edited = new Request(request);
                edited.setDescription(descriptionText.getText().toString());
                edited.setStart(startLocation);
                edited.setDestination(endLocation);
                try {
                    edited.setPrice(Double.parseDouble(fare.getText().toString()));
                } catch (NumberFormatException e) {
                    Toast.makeText(getApplicationContext(), "invalid fare format",
                            Toast.LENGTH_LONG).show();
                    return;
                }
                Rider.getInstance().editRequest(edited);
                Rider.getInstance().updateOpenRequests();
                finish();
            }
        });
        //deletes the request entirely, and moves you back to the rider screen.
        deleteR.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Rider.getInstance().deleteRequest(request);
                Rider.getInstance().updateOpenRequests();
                finish();
            }
        });
        //just moves you back to the rider screen.
        Cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    @Deprecated
    public void load() {
        descriptionText = (EditText) findViewById(R.id.edit_Description_Text);
        descriptionText.setText(request.getDescription());
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_request);
        driversList = (ListView) findViewById(R.id.edit_Drivers_List);
        request = Rider.getInstance().getOpenRequests().get(getIntent().getIntExtra("requestId", -1));
        adapter = new DriversAdapter(this,
                R.layout.request_list_item, request.getAcceptedDriverProfiles(), getLayoutInflater());
        driversList.setAdapter(adapter);
        move();
        write();
        getLocations();
        //load();
    }

    public void getLocations() {
        Intent intent = getIntent();
        Gson gson = new Gson();

        if (intent.getStringExtra("start") != null) {
            startLocation = new Location("");
            startLocation = gson.fromJson(intent.getStringExtra("start"), startLocation.getClass());
            startLocationText.setText(startLocation.getAddress());
        }
        if (startLocation == null) {
            startLocationText.setText("");
        }

        if (intent.getStringExtra("end") != null) {
            endLocation = new Location("");
            endLocation = gson.fromJson(intent.getStringExtra("end"), endLocation.getClass());
            endLocationText.setText(endLocation.getAddress());
        }
        if (endLocation == null)  {
            endLocationText.setText("");
        }
        recommendedFare.setText(startLocation.calculateFare(endLocation));
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        setIntent(intent);
        getLocations();
    }
}
