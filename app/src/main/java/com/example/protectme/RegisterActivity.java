package com.example.protectme;

import androidx.appcompat.app.AppCompatActivity;

import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

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

public class RegisterActivity extends AppCompatActivity {

    EditText username;
    EditText firstname;
    EditText surname;
    Spinner spinnerBloodgroup;
    EditText password;

    SharedPreferences prefs;
    SharedPreferences.Editor e;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        getSupportActionBar().hide(); //hide the title bar
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        ImageView imgFavorite = (ImageView) findViewById(R.id.imageViewBack);
        imgFavorite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        String[] arraySpinner = new String[] {
                "Bloodgroup", "A+", "A-", "AB+", "AB-", "B+", "B-", "0+", "0-", "Don't know!"};
        spinnerBloodgroup = (Spinner) findViewById(R.id.spinnerBloodgroup);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, arraySpinner){
            @Override
            public boolean isEnabled(int position){
                if(position == 0)
                {
                    // Disables the first item from Spinner - will be used for hint
                    return false;
                }
                else
                {
                    return true;
                }
            }
            @Override
            public View getDropDownView(int position, View convertView,
                                        ViewGroup parent) {
                View view = super.getDropDownView(position, convertView, parent);
                TextView tv = (TextView) view;
                if(position == 0){
                    tv.setTextColor(Color.GRAY); // Set the hint text color gray
                }
                else {
                    tv.setTextColor(Color.BLACK);
                }
                return view;
            }
        };
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerBloodgroup.setAdapter(adapter);
    }

    public void onRegister(View view){
        username = (EditText) findViewById(R.id.tvUsername);
        firstname = (EditText) findViewById(R.id.tvFirstname);
        surname = (EditText) findViewById(R.id.tvSurname);
        spinnerBloodgroup = (Spinner) findViewById(R.id.spinnerBloodgroup);
        password = (EditText) findViewById(R.id.tvPassword);

        Thread thread = new Thread(new Runnable() {

            @Override
            public void run() {
                HttpURLConnection urlConnection=null;
                String bloodgroup=null;
                if(spinnerBloodgroup.getSelectedItem().toString().equals("Don't know!")){
                    bloodgroup = "None";
                }else{
                    bloodgroup = spinnerBloodgroup.getSelectedItem().toString();
                }
                try  {
                    String jsonString = new JSONObject()
                            .put("username", username.getText().toString())
                            .put("firstname", firstname.getText().toString())
                            .put("surname", surname.getText().toString())
                            .put("bloodgroup", bloodgroup)
                            .put("password", password.getText().toString())
                            .toString();
                    URL url = new URL("https://protectme.the-rothley.de/users/register");
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
                                Toast toast = Toast.makeText(RegisterActivity.this,
                                        "Error while registration.",
                                        Toast.LENGTH_SHORT);
                                toast.show();
                            }
                        });
                        throw new IOException("Invalid response from server: " + code);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
                finally {
                    if (urlConnection != null) {
                        urlConnection.disconnect();
                    }
                }
            }
        });
        if(username.getText().toString()==null||firstname.getText().toString()==null||surname.getText().toString()==null||spinnerBloodgroup.getSelectedItem().toString().equals("Bloodgroup")||password.getText().toString()==null){
            Toast toast = Toast.makeText(getApplicationContext(),
                    "Fill out all fields",
                    Toast.LENGTH_SHORT);
            toast.show();
        }else{
            thread.start();
            onBackPressed();
        }
    }
}