package com.c301t19.cs.ualberta.seekaride.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.c301t19.cs.ualberta.seekaride.R;

import io.searchbox.core.Search;

public class SearchRequestsActivity extends Activity {
    public Button Search;

    public void move(){
        Search = (Button) findViewById(R.id.search_Search_Button);

        //takes the information from the map, and moves you to the requests fround screen
        Search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent Nswitch = new Intent(SearchRequestsActivity.this, SearchRequestsActivity.class);
                startActivity(Nswitch);
            }
        });

    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_requests);
    }
}
