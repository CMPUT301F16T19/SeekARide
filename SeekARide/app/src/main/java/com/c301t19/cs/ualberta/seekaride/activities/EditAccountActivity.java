package com.c301t19.cs.ualberta.seekaride.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import com.c301t19.cs.ualberta.seekaride.R;

public class EditAccountActivity extends Activity {
    public Button Save;
    public Button Cancel;

    public void move(){
        Save = (Button) findViewById(R.id.edit_Changes_Button);
        Cancel = (Button) findViewById(R.id.edit_Cancel_Button);

        //saves your changes and moves you back to the view profile screen.
        Save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent Sswitch = new Intent(EditAccountActivity.this, ViewProfileActivity.class);
                startActivity(Sswitch);
            }
        });
        //just moves you back to the view profile screen.
        Cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent CSwitch = new Intent(EditAccountActivity.this, ViewProfileActivity.class);
                startActivity(CSwitch);
            }
        });
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_account);
        move();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_edit_account, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
