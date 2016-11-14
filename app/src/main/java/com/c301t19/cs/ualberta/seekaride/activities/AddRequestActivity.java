package com.c301t19.cs.ualberta.seekaride.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.c301t19.cs.ualberta.seekaride.R;

public class AddRequestActivity extends Activity implements View.OnClickListener {

    private Button CreReq;
    private EditText description;
    private EditText startLoc;
    private EditText endLoc;
    private EditText Fare;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_request);

        CreReq = (Button)findViewById(R.id.add_Create_Button);
        description = (EditText)findViewById(R.id.add_Description_Text);
        startLoc = (EditText)findViewById(R.id.add_Slocation_Text);
        endLoc = (EditText)findViewById(R.id.add_Elocation_Text);
        Fare = (EditText)findViewById(R.id.add_Fare_Text);

        CreReq.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
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
                setResult(1, intent);
                finish();
            default:
                break;
        }
    }
}
