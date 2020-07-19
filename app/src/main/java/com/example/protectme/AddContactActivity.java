package com.example.protectme;

import androidx.appcompat.app.AppCompatActivity;

import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;

import org.json.JSONObject;

import java.io.BufferedOutputStream;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;


public class AddContactActivity extends AppCompatActivity {

    EditText mUsername;
    EditText mDisplayName;
    EditText mPhone;
    EditText mAddress;
    Spinner mRelationship;
    SharedPreferences prefs;
    SharedPreferences.Editor e;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        getSupportActionBar().hide(); //hide the title bar
        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN); //hides keyboard
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_contact);
        String[] arraySpinner = new String[] {
                "Relationship", "Family", "Friend", "Colleague"};
        Spinner s = (Spinner) findViewById(R.id.spinner);
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
        s.setAdapter(adapter);

        ImageView imgFavorite = (ImageView) findViewById(R.id.imageViewBack); //for navigation back, due to use of imageView
        imgFavorite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
    }

    public void onAdd(View v){
        mUsername = (EditText) findViewById(R.id.tvUsername);
        mDisplayName = (EditText) findViewById(R.id.tvDisplayName);
        mPhone = (EditText) findViewById(R.id.tvPhone);
        mAddress = (EditText) findViewById(R.id.tvAddress);
        mRelationship = (Spinner) findViewById(R.id.spinner);

        final SharedPreferences prefs = this.getSharedPreferences("prefs", MODE_PRIVATE);
        Thread thread = new Thread(){

            @Override
            public void run() {
                HttpURLConnection urlConnection=null;
                try  {
                    String jsonString = new JSONObject()
                            .put("username", mUsername.getText().toString())
                            .put("display_name", mDisplayName.getText().toString())
                            .put("phone", mPhone.getText().toString())
                            .put("address", mAddress.getText().toString())
                            .put("relationship", mRelationship.getSelectedItem().toString())
                            .toString();
                    String userID = prefs.getString("userID", null);
                    URL url = new URL("https://protectme.the-rothley.de/contacts/"+userID);
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
                    if (code ==  400) {
                        runOnUiThread(new Runnable() {
                            public void run() {
                                Toast toast = Toast.makeText(AddContactActivity.this,
                                        "Could not find Username.",
                                        Toast.LENGTH_SHORT);
                                toast.show();
                            }
                        });
                        throw new IOException("Invalid response from server: " + code);
                    }else if (code !=  200 && code !=  201) {
                        runOnUiThread(new Runnable() {
                            public void run() {
                                Toast toast = Toast.makeText(AddContactActivity.this,
                                        "Could not add contact.",
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
        };
        if(mUsername.getText().toString()==null||mRelationship.getSelectedItem().toString().equals("Relationship")||mDisplayName.getText().toString()==null||mPhone.getText().toString()==null||mAddress.getText().toString()==null){
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
