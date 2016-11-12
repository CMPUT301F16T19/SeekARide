package com.c301t19.cs.ualberta.seekaride.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.c301t19.cs.ualberta.seekaride.R;
import com.c301t19.cs.ualberta.seekaride.core.Location;

public class AddRequestActivity extends Activity {
    public Button createR;
    public Button Back;
    private TextView description;
    private TextView sLocation;
    private TextView eLocation;
    private TextView fare;
    private TextView recommendedFare;


    //takes the filled in information sets variables to it.
    public void write() {
        description = (TextView) findViewById(R.id.add_Description_Text);
        /* someone fix this plz
        sLocation = (TextView) findViewById(R.id.add_Start_Location);
        eLocation = (TextView) findViewById(R.id.add_ELocation_Text);
        */
        fare = (TextView) findViewById(R.id.add_Fare_Text);
        recommendedFare = (TextView) findViewById(R.id.add_RecFare_Text);

        String desciptText = description.getText().toString();
        String startText = sLocation.getText().toString();
        String endText = eLocation.getText().toString();
        String fareText = fare.getText().toString();
        // I don't know how we're setting our recommended fares, so it's commented out.
        //recommendedFare.setText();
    }

    public void move(){
        createR = (Button) findViewById(R.id.add_Create_Button);
        Back = (Button) findViewById(R.id.add_Back_Button);
        //creates the request and moves you back to the initial rider screen
        createR.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent Cswitch = new Intent(AddRequestActivity.this, RiderActivity.class);
                startActivity(Cswitch);
            }
        });
        //moves you back to the initial rider screen without creating a request.
        Back.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                Intent Bswitch = new Intent(AddRequestActivity.this, RiderActivity.class);
                startActivity(Bswitch);
            }
        });
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_request);
        write();
        move();
    }
}
