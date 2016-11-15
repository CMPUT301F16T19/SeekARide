package com.c301t19.cs.ualberta.seekaride.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.c301t19.cs.ualberta.seekaride.R;
import com.c301t19.cs.ualberta.seekaride.core.LoginController;
import com.c301t19.cs.ualberta.seekaride.core.Profile;
import com.c301t19.cs.ualberta.seekaride.core.Rider;

public class EditAccountActivity extends Activity {
    public Button Save;
    public Button Cancel;

    private EditText username;
    private EditText password;
    private EditText cPassword;
    private EditText phoneNumber;
    private EditText email;
    private EditText car;

    String usernameText;
    String passwordText;
    String confirmPassword;
    String phoneNumberText;
    String emailText;
    String carText;

    //sets up the text boxes and lets you fill them in.
    public void write() {
        username = (EditText) findViewById(R.id.edit_User_Text);
        password = (EditText) findViewById(R.id.edit_Password_Text);
        cPassword = (EditText) findViewById(R.id.edit_ConfirmP_Text);
        phoneNumber = (EditText) findViewById(R.id.edit_Phone_Text);
        email = (EditText) findViewById(R.id.edit_Email_Text);
        car = (EditText) findViewById(R.id.edit_Car_Text);

        usernameText = username.getText().toString();
        passwordText = password.getText().toString();
        confirmPassword = cPassword.getText().toString();
        phoneNumberText = phoneNumber.getText().toString();
        emailText = email.getText().toString();
        carText = car.getText().toString();

    }

    public void move(){
        Save = (Button) findViewById(R.id.edit_Changes_Button);
        Cancel = (Button) findViewById(R.id.edit_Cancel_Button);

        //saves your changes and moves you back to the view profile screen.
        Save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                write();
                Profile newProfile = new Profile(Rider.getInstance().getProfile());
                newProfile.setEmail(emailText);
                newProfile.setPhoneNumber(phoneNumberText);
                newProfile.setUsername(usernameText);
                new LoginController().editAccount(Rider.getInstance().getProfile(), newProfile);
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
