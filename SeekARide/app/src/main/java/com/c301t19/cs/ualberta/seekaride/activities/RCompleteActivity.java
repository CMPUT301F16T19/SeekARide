package com.c301t19.cs.ualberta.seekaride.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.c301t19.cs.ualberta.seekaride.R;

public class RCompleteActivity extends Activity {
    private Button confirmP;
    private EditText review;

    //handles getting the review, but doesn't save it anywhere.
    public void write(){
        review = (EditText) findViewById(R.id.complete_Review_Text);

        String reviewText = review.getText().toString();
    }

    public void move(){
        confirmP = (Button) findViewById(R.id.complete_Confirm_Button);

        //moves you to the login screen, after saving your review.
        confirmP.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent Nswitch = new Intent(RCompleteActivity.this, LoginActivity.class);
                startActivity(Nswitch);
            }
        });

    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rcomplete);
        move();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_rcomplete, menu);
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
