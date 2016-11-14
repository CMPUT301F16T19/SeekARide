package com.c301t19.cs.ualberta.seekaride.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.c301t19.cs.ualberta.seekaride.R;


public class MainActivity extends Activity implements View.OnClickListener {

    Button testScreenButton; // remove before final build

    private EditText id;
    private EditText pass;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // remove before final build
        testScreenButton = (Button) findViewById(R.id.testingButton);
        testScreenButton.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {
                setResult(RESULT_OK);
                startActivity(new Intent(getParent(), TestingActivity.class));
                finish();
            }
        });

        Button newAccount = (Button)findViewById(R.id.newA_Button);
        newAccount.setOnClickListener(this);

        Button logIn = (Button)findViewById(R.id.login_Button);
        logIn.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.newA_Button:
                Intent intent = new Intent(this, NewAccountActivity.class);
                startActivityForResult(intent, 4);
                break;
            case R.id.login_Button:
                String Id = id.getText().toString();
                String password = pass.getText().toString();
                //log in ?
                break;
            default:
                break;
        }
    }
}
