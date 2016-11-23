package com.c301t19.cs.ualberta.seekaride.activities;

import android.app.Activity;
import android.app.NotificationManager;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.c301t19.cs.ualberta.seekaride.R;

public class AcceptDriverActivity extends Activity {
    private Button acceptD;
    private Button editR;
    private Button deleteR;
    private TextView description;
    private TextView sLocation;
    private TextView eLocation;
    private TextView fare;
    private TextView driverInfo;

    //fills in the blank text views with the relavent information from the request.
    public void write(){
        description = (TextView) findViewById(R.id.accept_Description_Text);
        sLocation = (TextView) findViewById(R.id.accept_Start_Location);
        eLocation = (TextView) findViewById(R.id.accept_ELocation_Text);
        fare = (TextView) findViewById(R.id.accept_Fare_Text);
        driverInfo = (TextView) findViewById(R.id.accept_Info_Text);


        //The commands to fill the text, just needs the proper variable in the brackets.
        /*
        description.setText();
        sLocation.setText();
        eLocation.setText();
        fare.setText();
        driverInfo.setText();
         */
    }

    public void move(){
        acceptD = (Button) findViewById(R.id.accept_Accept_Button);
        editR = (Button) findViewById(R.id.accept_Edit_Button);
        deleteR = (Button) findViewById(R.id.accept_Delete_Button);
        //builds the notification
        final NotificationCompat.Builder Abuilder =
                new NotificationCompat.Builder(this).setSmallIcon(R.drawable.test).
                        setContentTitle("Rider ready").setContentText("The Rider is ready to be picked up.");
        final int Anotificationid = 1;
        final NotificationManager Anotifymang =
                (NotificationManager) getSystemService(NOTIFICATION_SERVICE);

        //Accepts the driver, and moves you back to the rider screen.
        acceptD.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent Aswitch = new Intent(AcceptDriverActivity.this, RiderActivity.class);
                //should send the notification.
                Anotifymang.notify(Anotificationid, Abuilder.build());
                startActivity(Aswitch);
            }
        });
        //Moves you to the edit request screen.
        editR.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                Intent Eswitch = new Intent(AcceptDriverActivity.this, EditRequestActivity.class);
                startActivity(Eswitch);
            }
        });
        //Deletes the request, and moves you back to the rider screen.
        deleteR.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                Intent Dswitch = new Intent(AcceptDriverActivity.this, RiderActivity.class);
                startActivity(Dswitch);
            }
        });
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_accept_driver);
        write();
        move();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_accept_driver, menu);
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
