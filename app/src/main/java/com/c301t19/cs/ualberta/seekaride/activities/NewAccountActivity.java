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

public class NewAccountActivity extends Activity {

    public Button createA;
    public Button cancelA;
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
        username = (EditText) findViewById(R.id.new_Username_Text);
        password = (EditText) findViewById(R.id.new_Password_Text);
        cPassword = (EditText) findViewById(R.id.new_ConfirmP_Text);
        phoneNumber = (EditText) findViewById(R.id.new_Phone_Text);
        email = (EditText) findViewById(R.id.new_Email_Text);
        car = (EditText) findViewById(R.id.new_Car_Text);

        String usernameText = username.getText().toString();
        String passwordText = password.getText().toString();
        String confirmPassword = cPassword.getText().toString();
        String phoneNumberText = phoneNumber.getText().toString();
        String emailText = email.getText().toString();
        String carText = car.getText().toString();
    }
    public void move(){
        createA = (Button) findViewById(R.id.newA_Account_Button);
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
                finish();
            }
        });

    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_account);
        write();
        move();
    }
}
