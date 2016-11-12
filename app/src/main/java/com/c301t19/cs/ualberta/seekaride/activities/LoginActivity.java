package com.c301t19.cs.ualberta.seekaride.activities;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import com.c301t19.cs.ualberta.seekaride.R;

public class LoginActivity extends Activity {

    public Button vAccount;
    public Button useR;
    public Button useD;
    public MenuItem menu;


    public void move(){
        vAccount = (Button) findViewById(R.id.login_View_Button);
        useR = (Button) findViewById(R.id.login_Rider_Button);
        useD = (Button) findViewById(R.id.login_Driver_Button);

        //Lets you view your account information, moves you to the view account screen.
        vAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent Vswitch = new Intent(LoginActivity.this, ViewProfileActivity.class);
                startActivity(Vswitch);
            }
        });
        //Moves you to the rider screen. Makes you a Rider.
        useR.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent RSwitch = new Intent(LoginActivity.this, RiderActivity.class);
                startActivity(RSwitch);
            }
        });
        //moves you to the driver screen. Makes you a Driver
        useD.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                Intent DSwitch = new Intent(LoginActivity.this, DriverActivity.class);
                startActivity((DSwitch));
            }

        });
    }

    public void UseRider (MenuItem menu){
        Intent RiderSwitch = new Intent(LoginActivity.this, RiderActivity.class);
        startActivity(RiderSwitch);
    }
    public void UseView (MenuItem menu){
        Intent EditSwitch = new Intent(LoginActivity.this, ViewProfileActivity.class);
        startActivity(EditSwitch);
    }
    public void UseDriver (MenuItem menu){
        Intent DriverSwitch = new Intent(LoginActivity.this, DriverActivity.class);
        startActivity(DriverSwitch);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        move();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_login, menu);
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
