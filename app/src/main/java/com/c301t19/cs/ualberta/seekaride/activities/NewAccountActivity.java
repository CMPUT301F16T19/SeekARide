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

public class NewAccountActivity extends Activity implements View.OnClickListener {

    private EditText userName;
    private EditText passWord;
    private EditText confirm;
    private EditText phoneNum;
    private EditText email;
    private EditText carType;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_account);

        userName = (EditText)findViewById(R.id.new_Username_Text);
        passWord = (EditText)findViewById(R.id.new_Password_Text);
        confirm = (EditText)findViewById(R.id.confirm_Password_Text);
        phoneNum = (EditText)findViewById(R.id.new_Phone_Text);
        email = (EditText)findViewById(R.id.new_Email_Text);
        carType = (EditText)findViewById(R.id.new_Car_Text);

        Button newAccount = (Button)findViewById(R.id.create_Account_Button);
        newAccount.setOnClickListener(this);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_new_account, menu);
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
            case R.id.create_Account_Button:
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
                    setResult(4, intent);
                }
                finish();
            default:
                break;

        }
    }
}
