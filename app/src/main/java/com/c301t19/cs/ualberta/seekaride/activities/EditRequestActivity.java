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

public class EditRequestActivity extends Activity implements View.OnClickListener {

    private Button editReq;
    private Button delete;
    private Button cancel;
    private EditText description;
    private EditText startLoc;
    private EditText endLoc;
    private EditText Fare;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_request);

        editReq = (Button)findViewById(R.id.edit_Edit_Button);
        delete = (Button)findViewById(R.id.edit_Delete_Button);
        cancel = (Button)findViewById(R.id.edit_Cancel_Button);
        description = (EditText)findViewById(R.id.edit_Description_Text);
        startLoc = (EditText)findViewById(R.id.edit_Slocation_Text);
        endLoc = (EditText)findViewById(R.id.edit_Elocation_Text);
        Fare = (EditText)findViewById(R.id.editText);

        editReq.setOnClickListener(this);
        delete.setOnClickListener(this);
        cancel.setOnClickListener(this);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_edit_request, menu);
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

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.add_Create_Button:
                Intent intent = new Intent();

                String desc = description.getText().toString();
                String Slocation = startLoc.getText().toString();
                String Elocation = endLoc.getText().toString();
                String fare = Fare.getText().toString();
                intent.putExtra("description", desc);
                intent.putExtra("Slocation", Slocation);
                intent.putExtra("Elocation", Elocation);
                intent.putExtra("fare", fare);
                setResult(8, intent);
                finish();
                break;
            case R.id.edit_Delete_Button:
                //
                break;
            case R.id.edit_Cancel_Button:
                finish();
                break;
            default:
                break;
        }
    }
}
