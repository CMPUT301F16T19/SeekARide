package com.c301t19.cs.ualberta.seekaride.activities;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Paint;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

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

    private Request request;

    //sets up the text boxes and lets you fill them in.
    public void write() {

        username = (TextView) findViewById(R.id.View_user_text);
        phoneNumber = (TextView) findViewById(R.id.edit_Phone_Text);
        email = (TextView) findViewById(R.id.edit_Email_Text);
        rating = (TextView) findViewById(R.id.view_Rating);
        car = (TextView) findViewById(R.id.edit_Car_Text);

        username.setText("Username: " + aProfile.getUsername());
        phoneNumber.setText("Phone: " + aProfile.getPhoneNumber());
        email.setText("Email: " + aProfile.getEmail());
        car.setText("Car: " + aProfile.getCar());
        rating.setText("Average rating: " + ((Float)aProfile.getRating()).toString());

        // http://stackoverflow.com/questions/5645789/how-to-set-underline-text-on-textview 2016-11-23, 8:39 PM, author toxa_xa
        phoneNumber.setPaintFlags(phoneNumber.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
        email.setPaintFlags(email.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
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

                /*if (!request.riderAccept(aProfile)) {
                    return;
                }
                int requestIndex = Rider.getInstance().getOpenRequests().indexOf(request);
                Rider.getInstance().editRequest(request);*/
                if (!Rider.getInstance().acceptDriverOffer(request, aProfile)) {
                    Toast.makeText(getApplicationContext(), "Could not accept driver. Perhaps a request is already in progress.",
                            Toast.LENGTH_LONG).show();
                    return;
                }

                Intent intent = new Intent(ViewProfileActivity.this, RCompleteActivity.class);
                //intent.putExtra("requestIndex", requestIndex);
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

        int requestIndex = getIntent().getIntExtra("requestIndex", -1);
        if (requestIndex >= 0) {
            request = Rider.getInstance().getRequest(requestIndex);
        }

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
