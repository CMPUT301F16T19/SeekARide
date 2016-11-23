package com.c301t19.cs.ualberta.seekaride.activities;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;

import com.c301t19.cs.ualberta.seekaride.R;
import com.c301t19.cs.ualberta.seekaride.core.*;

import java.util.Calendar;
import java.util.TimeZone;
import java.util.concurrent.TimeUnit;

public class RiderActivity extends Activity {

    private Button addRequest;
    private ListView requests;
    private Request selectedRequest;
    private RequestsAdapter adapter;

    private Rider rider;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rider);
        move();
    }

    @Override
    protected void onResume() {
        super.onResume();
        // refresh data
        Rider.getInstance().updateOpenRequests();
        /*try {
            TimeUnit.SECONDS.sleep(5);
        }
        catch (InterruptedException e) {

        }*/
        adapter = new RequestsAdapter(this,
                R.layout.request_list_item, Rider.getInstance().getOpenRequests(), getLayoutInflater());
        requests.setAdapter(adapter);
        adapter.notifyDataSetChanged();
    }

    public void move(){
        addRequest = (Button) findViewById(R.id.request_Ride_Button);
        requests = (ListView) findViewById(R.id.current_Requests);
        //Moves you to the addRequest screen.
        addRequest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent Nswitch = new Intent(RiderActivity.this, AddRequestActivity.class);
                startActivity(Nswitch);
            }
        });
        //Choose a request from the list to see more details about it.
        requests.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick (AdapterView<?> parent, View view, int position, long id){
                selectedRequest = adapter.getItem(position);
                int requestIndex = Rider.getInstance().getOpenRequests().indexOf(selectedRequest);
                if (requestIndex < 0) {
                    return;
                }
                Intent intent = new Intent(RiderActivity.this, EditRequestActivity.class);
                intent.putExtra("requestId", requestIndex);
                startActivity(intent);
            }
        });

    }

    @Override
    // dropdown menu
    public boolean onCreateOptionsMenu(Menu m) {
        getMenuInflater().inflate(R.menu.main, m);
        return true;
    }

    // dropdown menu
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.mainMenuRider:
                return false;
            case R.id.mainMenuDriver:
                startActivity(new Intent(this, DriverActivity.class));
                return true;
            case R.id.mainMenuProfile:
                startActivity(new Intent(this, EditAccountActivity.class));
                return true;
            default:
                return false;
            //return super.onOptionsItemSelected(item);
        }
    }
}
