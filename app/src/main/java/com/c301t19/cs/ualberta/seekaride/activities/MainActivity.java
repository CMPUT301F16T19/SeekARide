package com.c301t19.cs.ualberta.seekaride.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.c301t19.cs.ualberta.seekaride.R;
import com.c301t19.cs.ualberta.seekaride.core.LoginController;

public class MainActivity extends Activity {

    EditText username;
    LoginController loginController;
    Button newAccount;

    public void move() {
        newAccount = (Button) findViewById(R.id.newA_Button);
        //Moves you to the search for requests screen.
        newAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent Nswitch = new Intent(MainActivity.this, NewAccountActivity.class);
                startActivity(Nswitch);
            }
        });
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        username = (EditText) findViewById(R.id.username_Text);
        move();
        loginController = new LoginController();
    }

    public void onNewAccountClick(View v) {
        Intent intent = new Intent(this, NewAccountActivity.class);
        startActivity(intent);
    }

    public void onLoginClick(View v) {
        String name = username.getText().toString();
        if (!loginController.login(name))
        {
            Toast.makeText(getApplicationContext(), "Account does not exist.",
                    Toast.LENGTH_LONG).show();
            return;
        }
        Intent intent = new Intent(this, RiderActivity.class);
        startActivity(intent);
    }

    //remove before final build
    public void testScreenButton(View v) {
        Intent intent = new Intent(this, AddRequestActivity.class);
        startActivity(intent);
    }
}
