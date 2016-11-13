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
        if(intent.getStringExtra("start") != null) {
            startLoc = new Location("");
            startLoc = gson.fromJson(intent.getStringExtra("start"), startLoc.getClass());
            Toast.makeText(getApplicationContext(), startLoc.getAddress(),
                    Toast.LENGTH_LONG).show();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_request);
        write();
        move();
        getLocations();
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        setIntent(intent);
        getLocations();
    }
}
