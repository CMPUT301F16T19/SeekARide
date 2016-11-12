package com.c301t19.cs.ualberta.seekaride.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import com.c301t19.cs.ualberta.seekaride.R;

public class AcceptDriverActivity extends Activity {
    public Button acceptD;
    public Button editR;
    public Button deleteR;


    public void move(){
        acceptD = (Button) findViewById(R.id.accept_Accept_Button);
        editR = (Button) findViewById(R.id.accept_Edit_Button);
        deleteR = (Button) findViewById(R.id.accept_Delete_Button);
        //Accepts the driver, and moves you back to the rider screen.
        acceptD.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent Aswitch = new Intent(AcceptDriverActivity.this, RiderActivity.class);
                startActivity(Aswitch);
            }
        });
        //Moves you to the edit request screen.
        editR.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                Intent Eswitch = new Intent(AcceptDriverActivity.this, EditRequestActivity.class);
                startActivity(Eswitch);
            }
        });
        //Deletes the request, and moves you back to the rider screen.
        deleteR.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                Intent Dswitch = new Intent(AcceptDriverActivity.this, RiderActivity.class);
                startActivity(Dswitch);
            }
        });
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_accept_driver);
        move();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_accept_driver, menu);
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
