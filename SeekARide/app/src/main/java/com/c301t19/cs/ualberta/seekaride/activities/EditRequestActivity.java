package com.c301t19.cs.ualberta.seekaride.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.c301t19.cs.ualberta.seekaride.R;

public class EditRequestActivity extends Activity {
    private Button editR;
    private Button deleteR;
    private Button Cancel;
    private TextView description;
    private TextView sLocation;
    private TextView eLocation;
    private TextView fare;
    private TextView recommendedFare;


    //takes the filled in information sets variables to it.
    public void write() {
        description = (TextView) findViewById(R.id.edit_Description_Text);
        sLocation = (TextView) findViewById(R.id.edit_SLocation_Text);
        eLocation = (TextView) findViewById(R.id.edit_ELocation_Text);
        fare = (TextView) findViewById(R.id.edit_Fare_Text);
        recommendedFare = (TextView) findViewById(R.id.edit_RecFare_Text);

        String desciptText = description.getText().toString();
        String startText = sLocation.getText().toString();
        String endText = eLocation.getText().toString();
        String fareText = fare.getText().toString();
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
                Intent Eswitch = new Intent(EditRequestActivity.this, RiderActivity.class);
                startActivity(Eswitch);
            }
        });
        //deletes the request entirely, and moves you back to the rider screen.
        deleteR.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent Dswitch = new Intent(EditRequestActivity.this, RiderActivity.class);
                startActivity(Dswitch);
            }
        });
        //just moves you back to the rider screen.
        Cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent Cswitch = new Intent(EditRequestActivity.this, RiderActivity.class);
                startActivity(Cswitch);
            }
        });
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_request);
        move();
        write();
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
