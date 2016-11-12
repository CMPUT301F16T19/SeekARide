package com.c301t19.cs.ualberta.seekaride.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.c301t19.cs.ualberta.seekaride.R;

public class AddRequestActivity extends Activity {
    public Button createR;
    public Button Back;


    public void move(){
        createR = (Button) findViewById(R.id.add_Create_Button);
        Back = (Button) findViewById(R.id.add_Back_Button);
        //creates the request and moves you back to the initial rider screen
        createR.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent Cswitch = new Intent(AddRequestActivity.this, RiderActivity.class);
                startActivity(Cswitch);
            }
        });
        //moves you back to the initial rider screen without creating a request.
        Back.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                Intent Bswitch = new Intent(AddRequestActivity.this, RiderActivity.class);
                startActivity(Bswitch);
            }
        });
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_request);
        move();
    }
}
