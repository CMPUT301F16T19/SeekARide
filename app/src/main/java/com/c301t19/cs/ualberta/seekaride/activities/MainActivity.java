package com.c301t19.cs.ualberta.seekaride.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.c301t19.cs.ualberta.seekaride.R;


public class MainActivity extends Activity {

    Button testScreenButton; // remove before final build

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // remove before final build
        testScreenButton = (Button) findViewById(R.id.testingButton);
        testScreenButton.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {
                setResult(RESULT_OK);
                startActivity(new Intent(getParent(), TestingActivity.class));
                finish();
            }
        });
    }
}
