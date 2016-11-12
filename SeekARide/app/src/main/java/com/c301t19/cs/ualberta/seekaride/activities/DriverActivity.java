package com.c301t19.cs.ualberta.seekaride.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;

import com.c301t19.cs.ualberta.seekaride.R;
import com.c301t19.cs.ualberta.seekaride.core.Request;

public class DriverActivity extends Activity {
    public Button Search;
    private ListView requests;
    private Request selectedRequest;

    public void move(){
        Search = (Button) findViewById(R.id.driver_Search_Button);
        requests = (ListView) findViewById(R.id.rider_Requests);
        //Moves you to the search for requests screen.
        Search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent Nswitch = new Intent(DriverActivity.this, SearchRequestsActivity.class);
                startActivity(Nswitch);
            }
        });
        //Choose a request from the list to see more details about it.
        requests.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick (AdapterView<?> parent, View view, int position, long id){
                selectedRequest = (Request) requests.getItemAtPosition(position);
            }
        });

    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_driver);
        move();
    }
}
