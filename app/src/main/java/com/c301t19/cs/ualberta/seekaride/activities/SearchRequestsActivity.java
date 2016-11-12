package com.c301t19.cs.ualberta.seekaride.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.c301t19.cs.ualberta.seekaride.R;

import io.searchbox.core.Search;

public class SearchRequestsActivity extends Activity {
    private Button search;
    private EditText keywords;
    private EditText radius;

    String keywordsText;
    String radiusText;

    //gets the keywords used and the radius inputted.
    public void write(){
        keywords = (EditText) findViewById(R.id.search_Keywords_Text);
        radius = (EditText) findViewById(R.id.search_Radius_Text);

        //we don't always need keywords, so what should we do to handle cases when it isn't used?
        keywordsText = keywords.getText().toString();
        radiusText = radius.getText().toString();
    }

    public void move(){
        search = (Button) findViewById(R.id.search_Search_Button);

        //takes the information from the map, and moves you to the requests found screen
        search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                write();
                Intent Nswitch = new Intent(SearchRequestsActivity.this, SearchResultsActivity.class);
                Nswitch.putExtra("keywords", keywordsText);
                Nswitch.putExtra("radius", radiusText);
                startActivity(Nswitch);
            }
        });

    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_requests);
        write();
        move();
    }
}
