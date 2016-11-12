package com.c301t19.cs.ualberta.seekaride.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.c301t19.cs.ualberta.seekaride.R;


public class MainActivity extends Activity {

    private EditText username;
    private EditText password;
    private Button NewA;
    private Button Login;

    public void move(){
        NewA = (Button) findViewById(R.id.newA_Button);
        Login = (Button) findViewById(R.id.login_Button);

        //moves you to the new account screen, to make a new account.
        NewA.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent Nswitch = new Intent(MainActivity.this, NewAccountActivity.class);
                startActivity(Nswitch);
            }
        });
        //Lets you login if the account info matches. Moves you to the Login screen.
        Login.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                Intent LSwitch = new Intent(MainActivity.this, LoginActivity.class);
                startActivity(LSwitch);
            }
        });
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        username = (EditText) findViewById(R.id.username_Text);
        //are we even doing anything with passwords?
        password = (EditText) findViewById(R.id.password_Text);
        move();
    }

    public void onNewAccountClick(View v) {
        Intent intent = new Intent(this, NewAccountActivity.class);
        startActivity(intent);
    }

    public void onLoginClick(View v) {
        String name = username.getText().toString();
        if (name.length() <  5)
        {
            return;
        }
        Intent intent = new Intent(this, RiderActivity.class);
        intent.putExtra("username", name);
        startActivity(intent);
    }

    //remove before final build
    public void testScreenButton(View v) {
        Intent intent = new Intent(this, TestingActivity.class);
        startActivity(intent);
    }
}
