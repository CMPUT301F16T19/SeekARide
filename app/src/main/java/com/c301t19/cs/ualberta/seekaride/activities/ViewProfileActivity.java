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

    public Button editP;
    public Button Back;
    //private TextView username;
    private TextView phoneNumber;
    private TextView email;
    private TextView car;

    //sets up the text boxes and lets you fill them in.
    public void write() {

        /* someone fix this plz
        username = (TextView) findViewById(R.id.view_User_Text);
        phoneNumber = (TextView) findViewById(R.id.view_Phone_Text);
        email = (TextView) findViewById(R.id.view_Email_Text);
        car = (TextView) findViewById(R.id.view_Car_Text);
        */

        //The correct variables just need to go into the brackets. Til then it's commented out.
        /*
        username.setText();
        phoneNumber.setText();
        email.setText();
        car.setText();
        */


    }
    public void move(){
        editP = (Button) findViewById(R.id.view_Changes_Button);
        Back = (Button) findViewById(R.id.view_Logout_Button);

        //Moves you to the edit profile screen
        editP.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent Eswitch = new Intent(ViewProfileActivity.this, EditAccountActivity.class);
                startActivity(Eswitch);
            }
        });
        //Moves you back to the login screen.
        Back.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                Intent Bswitch = new Intent(ViewProfileActivity.this, LoginActivity.class);
                startActivity(Bswitch);
            }
        });
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_profile);
    }

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
}
