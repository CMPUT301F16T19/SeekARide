package com.c301t19.cs.ualberta.seekaride.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.c301t19.cs.ualberta.seekaride.R;
import com.c301t19.cs.ualberta.seekaride.core.AccountController;
import com.c301t19.cs.ualberta.seekaride.core.Profile;
import com.c301t19.cs.ualberta.seekaride.core.Rider;

/**
 * Activity to let a user change their account info
 */
public class EditAccountActivity extends Activity {
    public Button Save;
    public Button Cancel;

    private EditText password;
    private EditText cPassword;
    private EditText phoneNumber;
    private EditText email;
    private EditText car;
    private TextView username;

    String passwordText;
    String confirmPassword;
    String phoneNumberText;
    String emailText;
    String carText;

    private TextView rating;
    private ReviewsAdapter adapter;
    private ListView reviewList;

    //sets up the text boxes and lets you fill them in.

    /**
     * Get user input.
     */
    public void write() {
        password = (EditText) findViewById(R.id.edit_Password_Text);
        cPassword = (EditText) findViewById(R.id.edit_ConfirmP_Text);
        phoneNumber = (EditText) findViewById(R.id.edit_Phone_Text);
        email = (EditText) findViewById(R.id.edit_Email_Text);
        car = (EditText) findViewById(R.id.edit_Car_Text);
        rating = (TextView) findViewById(R.id.edit_Account_Rating);
        username = (TextView) findViewById(R.id.edit_User_Text);

        passwordText = password.getText().toString();
        confirmPassword = cPassword.getText().toString();
        phoneNumberText = phoneNumber.getText().toString();
        emailText = email.getText().toString();
        carText = car.getText().toString();
        rating.setText("Average rating: " + ((Float)Rider.getInstance().getProfile().getRating()).toString());
        username.setText(Rider.getInstance().getProfile().getUsername());
    }

    /**
     * Set up buttons
     */
    public void move(){
        Save = (Button) findViewById(R.id.edit_Changes_Button);
        Cancel = (Button) findViewById(R.id.edit_Cancel_Button);

        phoneNumber.setText(Rider.getInstance().getProfile().getPhoneNumber());
        email.setText(Rider.getInstance().getProfile().getEmail());
        car.setText(Rider.getInstance().getProfile().getCar());

        //saves your changes and moves you back to the view profile screen.
        Save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                write();
                Profile newProfile = new Profile(Rider.getInstance().getProfile());
                newProfile.setEmail(emailText);
                newProfile.setPhoneNumber(phoneNumberText);
                newProfile.setCar(carText);
                (new AccountController()).editAccount(Rider.getInstance().getProfile(), newProfile);
                finish();
            }
        });
        //just moves you back to the view profile screen.
        Cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_account);
        write();
        move();
        reviewList = (ListView) findViewById(R.id.edit_Account_Reviews);
        adapter = new ReviewsAdapter(this,
                R.layout.request_list_item, Rider.getInstance().getProfile().getReviews(), getLayoutInflater());
        reviewList.setAdapter(adapter);
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
                startActivity(new Intent(this, RiderActivity.class));
                return true;
            case R.id.mainMenuDriver:
                startActivity(new Intent(this, DriverActivity.class));
                return true;
            case R.id.mainMenuProfile:
                return false;
            default:
                return false;
            //return super.onOptionsItemSelected(item);
        }
    }
}
