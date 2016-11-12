package com.c301t19.cs.ualberta.seekaride.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import com.c301t19.cs.ualberta.seekaride.R;

public class EditRequestActivity extends Activity {
    public Button editR;
    public Button deleteR;
    public Button Cancel;

    public void move(){
        editR = (Button) findViewById(R.id.edit_Edit_Button);
        deleteR = (Button) findViewById(R.id.edit_Delete_Button);
        Cancel = (Button) findViewById(R.id.edit_Cancel_Button);
        //edits the request and moves you back to the rider screen.
        editR.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent Eswitch = new Intent(EditRequestActivity.this, RiderActivity.class);
                startActivity(Eswitch);
            }
        });
        //deletes the request entirely, and moves you back to the rider screen.
        deleteR.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                Intent Dswitch = new Intent(EditRequestActivity.this, RiderActivity.class);
                startActivity(Dswitch);
            }
        });
        //just moves you back to the rider screen.
        Cancel.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                Intent Cswitch = new Intent(EditRequestActivity.this, RiderActivity.class);
                startActivity(Cswitch);
            }
        });
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_request);
            move();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_edit_request, menu);
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
