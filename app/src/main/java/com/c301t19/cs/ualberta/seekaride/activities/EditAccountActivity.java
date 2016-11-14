package com.c301t19.cs.ualberta.seekaride.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.c301t19.cs.ualberta.seekaride.R;

public class EditAccountActivity extends Activity implements View.OnClickListener {

    private EditText userName;
    private EditText passWord;
    private EditText confirm;
    private EditText phoneNum;
    private EditText email;
    private EditText carType;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_account);

        userName = (EditText)findViewById(R.id.edit_User_Text);
        passWord = (EditText)findViewById(R.id.edit_Password_Text);
        confirm = (EditText)findViewById(R.id.edit_ConfirmP_Text);
        phoneNum = (EditText)findViewById(R.id.view_Phone_Text);
        email = (EditText)findViewById(R.id.view_Email_Text);
        carType = (EditText)findViewById(R.id.view_Car_Text);

        Button saveChange = (Button)findViewById(R.id.edit_Changes_Button);
        saveChange.setOnClickListener(this);
        Button cancel = (Button)findViewById(R.id.edit_Cancel_Button);
        cancel.setOnClickListener(this);
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

    @Override
    public void onClick(View view) {
        switch(view.getId()){
            case R.id.edit_Cancel_Button:
                finish();
                break;
            case R.id.edit_Changes_Button:
                Intent intent = new Intent();
                if(passWord.getText()==confirm.getText()){
                    String uid = userName.getText().toString();
                    String password = passWord.getText().toString();
                    String phone = phoneNum.getText().toString();
                    String emaill = email.getText().toString();
                    String car = carType.getText().toString();

                    intent.putExtra("uid", uid);
                    intent.putExtra("password", password);
                    intent.putExtra("phone", phone);
                    intent.putExtra("email", emaill);
                    intent.putExtra("car", car);
                    setResult(6, intent);
                }
                finish();
                break;
            default:
                break;
        }
    }
}
