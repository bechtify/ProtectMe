package com.example.protectme;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;
import android.view.View;

import android.os.Bundle;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.Toast;

import com.google.gson.Gson;

public class LoginActivity extends AppCompatActivity {

    EditText username;
    EditText password;

    SharedPreferences prefs;
    SharedPreferences.Editor e;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        getSupportActionBar().hide(); //hide the title bar
        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN); //hides keyboard
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
    }

    public void onLogin(View view){
        username = (EditText) findViewById(R.id.tvUsername);
        password  = (EditText) findViewById(R.id.tvPassword);
        String typedUsername = username.getText().toString();
        prefs = this.getSharedPreferences("prefs", MODE_PRIVATE);
        if (prefs.getString(typedUsername, null)!=null){
            Gson gson = new Gson();
            String user = prefs.getString(typedUsername, null);
            User selectedUser = gson.fromJson(user, User.class);
            if(selectedUser.mPassword.equals(password.getText().toString())){
                Intent intent = new Intent(LoginActivity.this, MenuActivity.class);
                startActivity(intent);
            }else{
                Toast toast = Toast.makeText(getApplicationContext(),
                        "Incorrect Password",
                        Toast.LENGTH_SHORT);
                toast.show();
            }
        }
        else{
            Toast toast = Toast.makeText(getApplicationContext(),
                    "Incorrect Username",
                    Toast.LENGTH_SHORT);
            toast.show();
        }
    }

    public void onRegister(View view){
        Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
        startActivity(intent);
    }

    public void onDev(View view){
        Intent intent = new Intent(LoginActivity.this, CrashDetectionDevelopmentActivity.class);
        startActivity(intent);
    }
}
