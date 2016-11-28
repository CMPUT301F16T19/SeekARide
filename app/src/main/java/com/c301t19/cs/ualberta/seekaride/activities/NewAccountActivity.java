package com.c301t19.cs.ualberta.seekaride.activities;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.c301t19.cs.ualberta.seekaride.R;
import com.c301t19.cs.ualberta.seekaride.core.AccountController;
import com.c301t19.cs.ualberta.seekaride.core.Profile;

/**
 * Activity to create a new account.
 */
public class NewAccountActivity extends Activity {

    AccountController accountController;

    public Button createA;
    public Button cancelA;
    private EditText username;
    private EditText phoneNumber;
    private EditText email;
    private EditText car;

    String usernameText;
    String phoneNumberText;
    String emailText;
    String carText;

    //sets up the text boxes and lets you fill them in.

    /**
     * Get user input.
     */
    public void write() {
        username = (EditText) findViewById(R.id.new_Username_Text);
        phoneNumber = (EditText) findViewById(R.id.new_Phone_Text);
        email = (EditText) findViewById(R.id.new_Email_Text);
        car = (EditText) findViewById(R.id.new_Car_Text);

        usernameText = username.getText().toString();
        phoneNumberText = phoneNumber.getText().toString();
        emailText = email.getText().toString();
        carText = car.getText().toString();
    }

    /**
     * Set up buttons
     */
    public void move(){
        createA = (Button) findViewById(R.id.newA_Account_Button);
        cancelA = (Button) findViewById(R.id.newA_Cancel_Button);

        //Creates an account, moves you to the main screen
        createA.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                write();
                if (usernameText == null || usernameText.length() < 5) {
                    Toast.makeText(getApplicationContext(), "Username must be at least 5 characters long.",
                            Toast.LENGTH_LONG).show();
                    return;
                }
                if (!accountController.createNewAccount(new Profile(usernameText, phoneNumberText, emailText, carText))) {
                    Toast.makeText(getApplicationContext(), "User already exists.",
                            Toast.LENGTH_LONG).show();
                    //unnecessary return function.
                }
                else {
                     finish();
                }
            }
        });
        //moves you to the main screen without making an account.
        cancelA.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                finish();
            }
        });

    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_account);
        accountController = new AccountController();
        write();
        move();
    }
}
