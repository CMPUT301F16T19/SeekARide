package com.c301t19.cs.ualberta.seekaride.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.c301t19.cs.ualberta.seekaride.R;


public class MainActivity extends Activity {

    EditText username;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        username = (EditText) findViewById(R.id.username_Text);
    }

    public void onNewAccountClick(View v) {
        Intent intent = new Intent(this, NewAccountActivity.class);
        startActivity(intent);
    }

    public void onLoginClick(View v) {
        String name = username.getText().toString();
        if (name.length() <  5)
        {
            return;
        }
        Intent intent = new Intent(this, RiderActivity.class);
        intent.putExtra("username", name);
        startActivity(intent);
    }

    //remove before final build
    public void testScreenButton(View v) {
        Intent intent = new Intent(this, TestingActivity.class);
        startActivity(intent);
    }
}
