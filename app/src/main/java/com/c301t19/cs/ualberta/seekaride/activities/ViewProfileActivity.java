package com.c301t19.cs.ualberta.seekaride.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.c301t19.cs.ualberta.seekaride.R;
import com.c301t19.cs.ualberta.seekaride.core.Driver;
import com.c301t19.cs.ualberta.seekaride.core.ElasticsearchController;
import com.c301t19.cs.ualberta.seekaride.core.Profile;
import com.c301t19.cs.ualberta.seekaride.core.Rider;

public class ViewProfileActivity extends Activity {

    private Profile aProfile;

    private Button Back;
    private Button accept;
    private TextView username;
    private TextView phoneNumber;
    private TextView email;
    private TextView rating;
    private ReviewsAdapter adapter;
    private ListView reviewList;
    private TextView car;

    //sets up the text boxes and lets you fill them in.
    public void write() {

        username = (TextView) findViewById(R.id.View_user_text);
        phoneNumber = (TextView) findViewById(R.id.edit_Phone_Text);
        email = (TextView) findViewById(R.id.edit_Email_Text);
        rating = (TextView) findViewById(R.id.view_Rating);
        car = (TextView) findViewById(R.id.edit_Car_Text);

        username.setText(aProfile.getUsername());
        phoneNumber.setText(aProfile.getPhoneNumber());
        email.setText(aProfile.getEmail());
        car.setText(aProfile.getCar());
        rating.setText("Rating: " + ((Double)aProfile.getRating()).toString());
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

        // implement rider accept
        accept.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                Intent intent = new Intent(ViewProfileActivity.this, RCompleteActivity.class);
                intent.putExtra("isRider", true);
                intent.putExtra("theirID", aProfile.getId());
                startActivity(intent);
                // somehow make the driver start the activity
                finish();
            }
        });
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_profile);
        accept = (Button) findViewById(R.id.accept_Driver);
        if (!getIntent().getBooleanExtra("showAcceptButton", false)) {
            accept.setVisibility(View.GONE);
        }
        String profileId = getIntent().getStringExtra("name");
        Log.i("ProfileID", profileId);
        ElasticsearchController.GetUserTask getUserTask = new ElasticsearchController.GetUserTask(
                ElasticsearchController.UserField.NAME, profileId);
        getUserTask.execute();
        Profile profile;
        try {
            profile = getUserTask.get();
            if (profile == null) {
                finish();
                Log.i("Profile", "didnt get it");
            }
            else {
                aProfile = profile;
                Log.i("Profile", "got it");
            }
        }
        catch (Exception e) {

        }
        move();
        write();
        reviewList = (ListView) findViewById(R.id.view_Profile_ListView);
        adapter = new ReviewsAdapter(this,
                R.layout.request_list_item, aProfile.getReviews(), getLayoutInflater());
        reviewList.setAdapter(adapter);
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
