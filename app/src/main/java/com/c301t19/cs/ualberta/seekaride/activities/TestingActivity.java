package com.c301t19.cs.ualberta.seekaride.activities;

import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.c301t19.cs.ualberta.seekaride.R;
import com.c301t19.cs.ualberta.seekaride.core.ElasticsearchController;
import com.c301t19.cs.ualberta.seekaride.core.Profile;

// Testing activity where you can test whatever you want. Will be removed before final build
public class TestingActivity extends ActionBarActivity {

    Button newUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_testing);

        newUser = (Button) findViewById(R.id.newUser);
        newUser.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {
                setResult(RESULT_OK);
                Profile profile = new Profile("TESTNAME", "TESTNUMBER", "TESTEMAIL" ,"TESTCAR");
                ElasticsearchController.AddUserTask aut = new ElasticsearchController.AddUserTask(profile);
                aut.execute();
                finish();
            }
        });
    }
}
