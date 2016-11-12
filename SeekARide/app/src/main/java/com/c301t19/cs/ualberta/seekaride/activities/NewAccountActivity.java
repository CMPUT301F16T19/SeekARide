package com.c301t19.cs.ualberta.seekaride.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import com.c301t19.cs.ualberta.seekaride.R;

public class NewAccountActivity extends Activity {
    public Button createA;
    public Button cancelA;

    public void move(){
        createA = (Button) findViewById(R.id.newA_Button);
        cancelA = (Button) findViewById(R.id.newA_Cancel_Button);

        //Creates an account, moves you to the main screen
        createA.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent Nswitch = new Intent(NewAccountActivity.this, MainActivity.class);
                startActivity(Nswitch);
            }
        });
        //moves you to the main screen without making an account.
        cancelA.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                Intent Cswitch = new Intent(NewAccountActivity.this, MainActivity.class);
                startActivity(Cswitch);
            }
        });

    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_account);
        move();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_new_account, menu);
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
