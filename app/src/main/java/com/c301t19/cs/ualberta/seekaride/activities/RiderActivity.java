package com.c301t19.cs.ualberta.seekaride.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.c301t19.cs.ualberta.seekaride.R;
import com.c301t19.cs.ualberta.seekaride.core.Request;

import java.util.ArrayList;

public class RiderActivity extends Activity implements View.OnClickListener {

    private ArrayList<Request> reqList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rider);

        Button addReq = (Button)findViewById(R.id.request_Ride_Button);
        addReq.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.request_Ride_Button:
                Intent intent = new Intent(this, AddRequestActivity.class);
                startActivityForResult(intent, 2);
                break;
            default:
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        //receiving results in page AddRequestActivity

    }
}
