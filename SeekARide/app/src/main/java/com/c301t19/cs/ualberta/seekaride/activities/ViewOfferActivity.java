package com.c301t19.cs.ualberta.seekaride.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import com.c301t19.cs.ualberta.seekaride.R;

public class ViewOfferActivity extends Activity {
    public Button acceptO;
    public Button Back;

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
