package com.c301t19.cs.ualberta.seekaride.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.StrictMode;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.c301t19.cs.ualberta.seekaride.R;
import com.c301t19.cs.ualberta.seekaride.core.Driver;
import com.c301t19.cs.ualberta.seekaride.core.Location;
import com.c301t19.cs.ualberta.seekaride.core.Request;
import com.c301t19.cs.ualberta.seekaride.core.Rider;
import com.google.gson.Gson;

import java.util.List;

public class SearchResultsActivity extends Activity {

    private Button Back;
    private ListView results;
    private EditText filterPrice;
    private Button filterButton;
    private RequestsAdapter adapter;
    private Request selectedRequest;

    public void move(){
        filterButton = (Button) findViewById(R.id.results_Filter_Price_Button);
        Back = (Button) findViewById(R.id.results_Back_Button);
        filterPrice = (EditText) findViewById(R.id.results_Filter_Price_Text);

        //moves you back to the Driver screen without doing anything else.
        Back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent Nswitch = new Intent(SearchResultsActivity.this, DriverActivity.class);
                startActivity(Nswitch);
            }
        });
        //Choose a request from the list to see more details about it.
        results.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick (AdapterView<?> parent, View view, int position, long id){
                selectedRequest = (Request) adapter.getItem(position);
                int requestIndex = Driver.getInstance().getSearchedRequests().indexOf(selectedRequest);
                if (requestIndex < 0) {
                    return;
                }
                Intent intent = new Intent(SearchResultsActivity.this, ViewOfferActivity.class);
                intent.putExtra("requestId", requestIndex);
                intent.putExtra("source", false);
                startActivity(intent);
            }
        });

        filterButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(filterPrice.getText().length() > 0)
                {
                    double price = Double.parseDouble(filterPrice.getText().toString());
                    Driver.getInstance().filterRequestsByPrice(price);
                    adapter.clear();
                    adapter.addAll(Driver.getInstance().getSearchedRequests());
                }
            }
        });
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_results);

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        results = (ListView) findViewById(R.id.result_List);
        Intent intent = getIntent();
        if (intent.getStringExtra("queryLocation") != null) {

            Gson gson = new Gson();
            Location queryLocation = new Location("");
            queryLocation = gson.fromJson(intent.getStringExtra("queryLocation"), queryLocation.getClass());

            // multiplied by 1000 to get meters
            double radiusDouble = Double.parseDouble(intent.getStringExtra("radius")) * 1000;
            //Toast.makeText(getApplicationContext(), String.valueOf(radiusDouble),
            //        Toast.LENGTH_LONG).show();
            Driver.getInstance().searchRequestsByLocation(queryLocation, radiusDouble);
        }
        else {
            Driver.getInstance().searchRequestsByKeyword(intent.getStringExtra("keywords"),
                    intent.getStringExtra("radius"));
        }
        adapter = new RequestsAdapter(this,
                R.layout.request_list_item, Driver.getInstance().getSearchedRequests(), getLayoutInflater());
        results.setAdapter(adapter);
        move();
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
