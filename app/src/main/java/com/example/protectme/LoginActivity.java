package com.example.protectme;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;
import android.view.View;

import android.os.Bundle;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;
import com.google.gson.Gson;

import org.json.JSONObject;

import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;

public class LoginActivity extends AppCompatActivity {

    EditText username;
    EditText password;

    SharedPreferences prefs;
    SharedPreferences.Editor e;
    String token;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        getSupportActionBar().hide(); //hide the title bar
        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN); //hides keyboard
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        username = (EditText) findViewById(R.id.tvUsername);
        password  = (EditText) findViewById(R.id.tvPassword);
        prefs = this.getSharedPreferences("prefs", MODE_PRIVATE);
        String usernameValue = prefs.getString("username", null);
        String passwordValue = prefs.getString("password", null);
        Intent intent = getIntent();
        if(intent.getStringExtra("name")!=null){
            Intent emergencyIntent = new Intent(this, EmergencyNotificationActivity.class);
            emergencyIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            emergencyIntent.putExtra("name", intent.getStringExtra("name"));
            emergencyIntent.putExtra("latitude", intent.getStringExtra("latitude"));
            emergencyIntent.putExtra("longitude", intent.getStringExtra("longitude"));
            startActivity(emergencyIntent);
        }

        if(usernameValue!=null){
            username.setText(usernameValue);
        }
        if(passwordValue!=null){
            password.setText(passwordValue);
        }

        FirebaseInstanceId.getInstance().getInstanceId()
                .addOnCompleteListener(new OnCompleteListener<InstanceIdResult>() {
                    @Override
                    public void onComplete(@NonNull Task<InstanceIdResult> task) {
                        if (!task.isSuccessful()) {
                            return;
                        }
                        // Get new Instance ID token
                        token = task.getResult().getToken();
                    }
                });
    }

    public void onLogin(View view){
        username = (EditText) findViewById(R.id.tvUsername);
        password  = (EditText) findViewById(R.id.tvPassword);
        prefs = this.getSharedPreferences("prefs", MODE_PRIVATE);


        Thread thread = new Thread(){
            public void run(){
                HttpURLConnection urlConnection=null;
                try  {
                    e =prefs.edit();
                    e.putString("username", username.getText().toString());
                    e.putString("password", password.getText().toString());
                    e.commit();
                    String jsonString = new JSONObject()
                            .put("username", username.getText().toString())
                            .put("password", password.getText().toString())
                            .put("push_token", token)
                            .toString();
                    URL url = new URL("https://protectme.the-rothley.de/users/login");
                    urlConnection  = (HttpURLConnection) url.openConnection();
                    urlConnection.setRequestProperty("Content-Type", "application/json");
                    urlConnection.setRequestMethod("POST");
                    urlConnection.setDoOutput(true);
                    urlConnection.setDoInput(true);
                    urlConnection.setChunkedStreamingMode(0);
                    OutputStream out = new BufferedOutputStream(urlConnection.getOutputStream());
                    BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(
                            out, "UTF-8"));
                    writer.write(jsonString);
                    writer.flush();

                    int code = urlConnection.getResponseCode();
                    if (code !=  200 && code !=  201) {
                        runOnUiThread(new Runnable() {
                            public void run() {
                                Toast toast = Toast.makeText(LoginActivity.this,
                                        "Invalid credentials.",
                                        Toast.LENGTH_SHORT);
                                toast.show();
                            }
                        });
                        throw new IOException("Invalid response from server: " + code);
                    }

                    BufferedReader rd = new BufferedReader(new InputStreamReader(
                            urlConnection.getInputStream()));

                    String userID = rd.readLine();
                    e=prefs.edit();
                    e.putString("userID", userID);
                    e.commit();
                    Intent intent = new Intent(LoginActivity.this, MenuActivity.class);
                    startActivity(intent);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                finally {
                    if (urlConnection != null) {
                        urlConnection.disconnect();
                    }
                }
            }
        };
        if(username.getText().toString().equals("")||password.getText().toString().equals("")){
            Toast toast = Toast.makeText(getApplicationContext(),
                    "Please fill out all fields.",
                    Toast.LENGTH_SHORT);
            toast.show();
        }else{
            thread.start();
        }
    }

    public void onRegister(View view){
        Intent intent = new Intent(LoginActivity.this, EmergencyNotificationActivity.class);
        startActivity(intent);
    }

    public void onDev(View view){
        Intent intent = new Intent(LoginActivity.this, CrashDetectionDevelopmentActivity.class);
        startActivity(intent);
    }
}
