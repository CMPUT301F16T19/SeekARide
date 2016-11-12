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

public class ViewOfferActivity extends Activity {
    private Button acceptO;
    private Button Back;
    private TextView description;
    private TextView sLocation;
    private TextView eLocation;
    private TextView fare;
    private TextView riderInfo;

    //fills in the blank text views with the relavent information from the request.
    public void write(){
        description = (TextView) findViewById(R.id.view_Description_Text);
        /* someone fix this plz
        sLocation = (TextView) findViewById(R.id.view_SLocation_Text);
        eLocation = (TextView) findViewById(R.id.view_ELocation_Text);
        */
        fare = (TextView) findViewById(R.id.view_Fare_Text);
        riderInfo = (TextView) findViewById(R.id.view_Info_Text);

        //The commands to fill the text, just needs the proper variable in the brackets.
        /*
        description.setText();
        sLocation.setText();
        eLocation.setText();
        fare.setText();
        riderInfo.setText();
         */
    }
    public void move(){
        acceptO = (Button) findViewById(R.id.view_Accept_Button);
        Back = (Button) findViewById(R.id.view_Back_Button);

        //Should add the request to the list, and return you to Driver screen
        acceptO.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent Aswitch = new Intent(ViewOfferActivity.this, DriverActivity.class);
                startActivity(Aswitch);
            }
        });
        //returns you to the search results screen
        Back.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                Intent Bswitch = new Intent(ViewOfferActivity.this, SearchRequestsActivity.class);
                startActivity(Bswitch);
            }
        });
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_offer);
        write();
        move();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_view_offer, menu);
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
