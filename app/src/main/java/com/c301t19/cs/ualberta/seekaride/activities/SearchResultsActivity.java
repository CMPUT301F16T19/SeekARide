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
import com.c301t19.cs.ualberta.seekaride.core.Rider;

import java.util.List;

public class SearchResultsActivity extends Activity {

    private Button Back;
    private ListView results;
    private RequestsAdapter adapter;
    private Request selectedRequest;

    public void move(){
        Back = (Button) findViewById(R.id.results_Back_Button);

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
                startActivity(intent);
            }
        });
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_results);
        results = (ListView) findViewById(R.id.result_List);
        Intent intent = getIntent();
        Driver.getInstance().searchRequestsByKeyword(intent.getStringExtra("keywords"),
                intent.getStringExtra("radius"));
        adapter = new RequestsAdapter(this,
                R.layout.request_list_item, Driver.getInstance().getSearchedRequests(), getLayoutInflater());
        Log.i("results", ((Boolean)(results==null)).toString());
        Log.i("adapter", ((Boolean)(adapter==null)).toString());
        results.setAdapter(adapter);
        move();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_search_results, menu);
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
