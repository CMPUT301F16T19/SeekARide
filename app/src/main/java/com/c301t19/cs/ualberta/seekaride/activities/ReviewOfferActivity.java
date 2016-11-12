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

public class ReviewOfferActivity extends Activity {
    private Button cOffer;
    private Button Back;
    private TextView description;
    private TextView sLocation;
    private TextView eLocation;
    private TextView riderInfo;

    //fills in the blank text views with the relavent information from the request.
    public void write(){
        description = (TextView) findViewById(R.id.review_Description_Text);
        sLocation = (TextView) findViewById(R.id.review_SLocation_Text);
        eLocation = (TextView) findViewById(R.id.review_ELocation_Text);
        riderInfo = (TextView) findViewById(R.id.review_Info_Text);

        //The commands to fill the text, just needs the proper variable in the brackets.
        /*
        description.setText();
        sLocation.setText();
        eLocation.setText();
        riderInfo.setText();
         */
    }


    public void move(){
        cOffer = (Button) findViewById(R.id.review_Cancel_Button);
        Back = (Button) findViewById(R.id.review_Back_Button);

        //deletes the request being viewed and returns you to the driver screen
        cOffer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent Cswitch = new Intent(ReviewOfferActivity.this, DriverActivity.class);
                startActivity(Cswitch);
            }
        });
        //Returns you to the initial driver screen with no changes
        Back.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                Intent Bswitch = new Intent(ReviewOfferActivity.this, DriverActivity.class);
                startActivity(Bswitch);
            }
        });

    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_review_offer);
        write();
        move();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_review_offer, menu);
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
