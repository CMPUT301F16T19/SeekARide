package com.c301t19.cs.ualberta.seekaride.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.c301t19.cs.ualberta.seekaride.R;

public class ViewProfileActivity extends Activity {

    public Button Back;
    //private TextView username;
    private TextView phoneNumber;
    private TextView email;
    //private TextView car;

    //sets up the text boxes and lets you fill them in.
    public void write() {

        //username = (TextView) findViewById(R.id.view_User_Text);
        phoneNumber = (TextView) findViewById(R.id.edit_Phone_Text);
        email = (TextView) findViewById(R.id.edit_Email_Text);
        //car = (TextView) findViewById(R.id.view_Car_Text);
    }

    public void move(){
        Back = (Button) findViewById(R.id.view_Profile_Back_Button);

        //Moves you back to the login screen.
        Back.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                finish();
            }
        });
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_profile);
    }

    /*
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_view_profile, menu);
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
    */
}
