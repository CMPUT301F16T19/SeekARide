package com.c301t19.cs.ualberta.seekaride.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import com.c301t19.cs.ualberta.seekaride.R;
import com.c301t19.cs.ualberta.seekaride.core.*;

import java.util.concurrent.ExecutionException;

public class RiderActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rider);
    }

    @Override
    // dropdown menu
    public boolean onCreateOptionsMenu(Menu m) {
        getMenuInflater().inflate(R.menu.main, m);
        return true;
    }

    // dropdown menu
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.mainMenuRider:
                return false;
            case R.id.mainMenuDriver:
                startActivity(new Intent(this, DriverActivity.class));
                return true;
            case R.id.mainMenuProfile:
                startActivity(new Intent(this, ViewProfileActivity.class));
                return true;
            default:
                return false;
            //return super.onOptionsItemSelected(item);
        }
    }
}
