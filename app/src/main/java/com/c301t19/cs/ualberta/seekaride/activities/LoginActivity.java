package com.c301t19.cs.ualberta.seekaride.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.c301t19.cs.ualberta.seekaride.R;
import com.c301t19.cs.ualberta.seekaride.core.AccountController;
import com.c301t19.cs.ualberta.seekaride.core.NetworkManager;

/**
 * Launcher activity that allows users to login in.
 */
public class LoginActivity extends Activity {

    EditText username;
    AccountController accountController;
    Button newAccount;

    /**
     * Set up buttons
     */
    public void move() {
        newAccount = (Button) findViewById(R.id.newA_Button);
        //Moves you to the search for requests screen.
        newAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent Nswitch = new Intent(LoginActivity.this, NewAccountActivity.class);
                startActivity(Nswitch);
            }
        });
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        NetworkManager.instantiate(getApplicationContext());
        username = (EditText) findViewById(R.id.username_Text);
        move();
        accountController = new AccountController();
        stopService(new Intent(this, PollingService.class));
    }

    @Override
    protected void onResume() {
        super.onResume();
        stopService(new Intent(this, PollingService.class));
    }

    public void onNewAccountClick(View v) {
        Intent intent = new Intent(this, NewAccountActivity.class);
        startActivity(intent);
    }

    public void onLoginClick(View v) {
        String name = username.getText().toString();
        if (!accountController.login(name, getApplicationContext()))
        {
            Toast.makeText(getApplicationContext(), "Account does not exist.",
                    Toast.LENGTH_LONG).show();
            return;
        }
        Intent intent = new Intent(this, RiderActivity.class);
        startActivity(intent);
    }
}
