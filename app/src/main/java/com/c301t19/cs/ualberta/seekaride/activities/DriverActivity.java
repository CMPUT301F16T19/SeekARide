package com.c301t19.cs.ualberta.seekaride.activities;

import android.app.Activity;
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
import com.c301t19.cs.ualberta.seekaride.core.Driver;
import com.c301t19.cs.ualberta.seekaride.core.Request;

public class DriverActivity extends Activity {

    private Button search;
    private ListView requests;
    private RequestsAdapter adapter;
    private Request selectedRequest;

    public void move(){
        search = (Button) findViewById(R.id.driver_Search_Button);
        requests = (ListView) findViewById(R.id.driver_Requests);
        //Moves you to the search for requests screen.
        search.setOnClickListener(new View.OnClickListener() {
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
                //selectedRequest = (Request) requests.getItemAtPosition(position);
                selectedRequest = adapter.getItem(position);
                int requestIndex = Driver.getInstance().getAcceptedRequests().indexOf(selectedRequest);
                if (requestIndex < 0) {
                    return;
                }
                if (selectedRequest.getAcceptedDriverProfile()!= null && selectedRequest.getAcceptedDriverProfile().equals(Driver.getInstance().getProfile())) {
                    Intent intent = new Intent(DriverActivity.this, RCompleteActivity.class);
                    intent.putExtra("requestIndex", requestIndex);
                    intent.putExtra("isRider", false);
                    intent.putExtra("theirID", selectedRequest.getRiderProfile().getId());
                    startActivity(intent);
                }
                else {
                    Intent intent = new Intent(DriverActivity.this, ViewOfferActivity.class);
                    intent.putExtra("requestId", requestIndex);
                    intent.putExtra("source", true);
                    startActivity(intent);
                }
            }
        });

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_driver);
        requests = (ListView) findViewById(R.id.driver_Requests);
        //adapter = new RequestsAdapter(this,
        //        R.layout.request_list_item, Driver.getInstance().getAcceptedRequests(), getLayoutInflater());
        //Log.i("adapter", ((Boolean)(adapter==null)).toString());
        //Log.i("requests", ((Boolean)(requests==null)).toString());
        //requests.setAdapter(adapter);
        move();
    }

    @Override
    protected void onResume() {
        super.onResume();
        // refresh data
        Driver.getInstance().updateAcceptedRequests();
        /*try {
            TimeUnit.SECONDS.sleep(5);
        }
        catch (InterruptedException e) {

        }*/
        adapter = new RequestsAdapter(this,
                R.layout.request_list_item, Driver.getInstance().getAcceptedRequests(), getLayoutInflater());
        requests.setAdapter(adapter);
        adapter.notifyDataSetChanged();
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
            case R.id.mainMenuDriver:
                return false;
            case R.id.mainMenuRider:
                startActivity(new Intent(this, RiderActivity.class));
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
