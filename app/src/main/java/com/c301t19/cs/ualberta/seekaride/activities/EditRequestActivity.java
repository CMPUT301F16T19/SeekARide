package com.c301t19.cs.ualberta.seekaride.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.c301t19.cs.ualberta.seekaride.R;
import com.c301t19.cs.ualberta.seekaride.core.Request;
import com.c301t19.cs.ualberta.seekaride.core.Rider;

public class EditRequestActivity extends Activity {

    Request request;

    private Button editR;
    private Button deleteR;
    private Button Cancel;
    private TextView description;
    private TextView sLocation;
    private TextView eLocation;
    private TextView fare;
    private TextView recommendedFare;

    private DriversAdapter adapter;
    private ListView driversList;

    String desciptText;
    String fareText;

    //takes the filled in information sets variables to it.
    public void write() {
        description = (TextView) findViewById(R.id.edit_Description_Text);

        sLocation = (TextView) findViewById(R.id.edit_Slocation_Text);
        eLocation = (TextView) findViewById(R.id.edit_Elocation_Text);

        fare = (TextView) findViewById(R.id.edit_Fare_Text);
        recommendedFare = (TextView) findViewById(R.id.editRecommendedFareNumber);

        description.setText(request.getDescription());
        sLocation.setText(request.getStart().getAddress());
        eLocation.setText(request.getDestination().getAddress());
        fare.setText(String.valueOf("$" + String.valueOf(request.getPrice())));

        // THIS NEEDS TO BE WORKED ON TO WRITE AFTER EDITING INDIVIDUAL PARAMS

        recommendedFare.setText(request.getStart().calculateFare(
                request.getDestination()));

        //desciptText = description.getText().toString();
        //String startText = sLocation.getText().toString();
        //String endText = eLocation.getText().toString();
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
                write();
                Request edited = new Request(request);
                edited.setDescription(desciptText);
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

    public void load() {
        description = (TextView) findViewById(R.id.edit_Description_Text);
        description.setText(request.getDescription());
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
        //load();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_edit_request, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
