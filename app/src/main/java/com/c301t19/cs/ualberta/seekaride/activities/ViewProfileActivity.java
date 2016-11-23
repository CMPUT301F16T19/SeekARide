package com.c301t19.cs.ualberta.seekaride.activities;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
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
import com.c301t19.cs.ualberta.seekaride.core.Request;
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

                int requestIndex = getIntent().getIntExtra("requestIndex", -1);
                Request edited = Rider.getInstance().getRequest(requestIndex);
                if (!edited.riderAccept(aProfile)) {
                    return;
                }
                Rider.getInstance().editRequest(edited);

                Intent intent = new Intent(ViewProfileActivity.this, RCompleteActivity.class);
                intent.putExtra("requestIndex", requestIndex);
                intent.putExtra("isRider", true);
                intent.putExtra("theirID", aProfile.getId());

                startActivity(intent);
                // somehow make the driver start the activity
                finish();
            }
        });

        phoneNumber.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                //Rider.getInstance().contactByPhone(phoneNumber.getText().toString());
                //Here's a different way to do that I think. No need to use another method.
                //Found at 'www.tutorialspoint.com/android/android_phone_calls.htm'
                Intent phoneIntent = new Intent(Intent.ACTION_CALL);
                phoneIntent.setData(Uri.parse("tel:" + aProfile.getPhoneNumber()));
                startActivity(phoneIntent);
                finish();
            }
        });

        email.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                //Rider.getInstance().contactByPhone(email.getText().toString());
                //Same deal as above
                //found at stackoverflow.com/questions/2734749/opening-an-email-client-on-clicking-a-button
                Intent emailIntent = new Intent(Intent.ACTION_SEND);
                emailIntent.setType("plain/text");
                emailIntent.putExtra(Intent.EXTRA_EMAIL, new String[] {aProfile.getEmail()});
                emailIntent.putExtra(Intent.EXTRA_SUBJECT, "SeekARide");
                emailIntent.putExtra(Intent.EXTRA_TEXT, "Hello " + aProfile.getUsername() + " !");
                startActivity(Intent.createChooser(emailIntent, ""));
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
        String profileId = getIntent().getStringExtra("profileId");
        Log.i("ProfileID", profileId);
        ElasticsearchController.GetUserTask getUserTask = new ElasticsearchController.GetUserTask(
                ElasticsearchController.UserField.ID, profileId);
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
        write();
        move();
        reviewList = (ListView) findViewById(R.id.view_Profile_ListView);
        adapter = new ReviewsAdapter(this,
                R.layout.request_list_item, aProfile.getReviews(), getLayoutInflater());
        reviewList.setAdapter(adapter);
    }
}
