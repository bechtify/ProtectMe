package com.example.protectme;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
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
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

public class EditContactActivity extends AppCompatActivity {

    EditText mUsername;
    EditText mDisplayName;
    EditText mPhone;
    EditText mAddress;
    Spinner mRelationship;

    SharedPreferences prefs;
    SharedPreferences.Editor e;

    int contactNumber;
    ArrayList<EmergencyContact> oldData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        oldData = new ArrayList<>(EmergencyContactsActivity.mData);
        getSupportActionBar().hide(); //hide the title bar
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_contact);

        ImageView imgFavorite = (ImageView) findViewById(R.id.imageViewBack);
        imgFavorite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        mUsername = (EditText) findViewById(R.id.tvUsername);
        mDisplayName = (EditText) findViewById(R.id.tvDisplayName);
        mPhone = (EditText) findViewById(R.id.tvPhone);
        mAddress = (EditText) findViewById(R.id.tvAddress);
        mRelationship = (Spinner) findViewById(R.id.spinnerRelationship);

        Intent intent = getIntent();
        contactNumber = intent.getIntExtra("contactNumber", 0);

        String[] arraySpinner = new String[] {
                "Relationship", "Family", "Friend", "Colleague"};
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
        mRelationship.setAdapter(adapter);

        mUsername.setText(oldData.get(contactNumber).username);
        mDisplayName.setText(oldData.get(contactNumber).display_name);
        mPhone.setText(oldData.get(contactNumber).phone);
        mAddress.setText(oldData.get(contactNumber).address);
        if(oldData.get(contactNumber).relationship.equals("Family")){
            mRelationship.setSelection(1);
        } else if(oldData.get(contactNumber).relationship.equals("Friend")){
            mRelationship.setSelection(2);
        } else if(oldData.get(contactNumber).relationship.equals("Colleague")){
            mRelationship.setSelection(3);
        }
    }

    public void onSave(View view){
        if(mUsername.getText().toString().equals("")||mRelationship.getSelectedItem().toString().equals("Relationship")||mDisplayName.getText().toString().equals("")||mPhone.getText().toString().equals("")||mAddress.getText().toString().equals("")) {
            Toast toast = Toast.makeText(getApplicationContext(),
                    "Fill out all fields",
                    Toast.LENGTH_SHORT);
            toast.show();
        }else {
            Intent intent = getIntent();
            contactNumber = intent.getIntExtra("contactNumber", 0);//get number of item which has been clicked
            final String contactID = oldData.get(contactNumber).contact_id;
            final SharedPreferences prefs = this.getSharedPreferences("prefs", MODE_PRIVATE);
            Thread thread = new Thread(){
                @Override
                public void run() {
                    HttpURLConnection urlConnection=null;
                    try  {
                    String jsonString = new JSONObject()
                        .put("contact_id", contactID)
                        .put("username", mUsername.getText().toString())
                        .put("display_name", mDisplayName.getText().toString())
                        .put("phone", mPhone.getText().toString())
                        .put("address", mAddress.getText().toString())
                        .put("relationship", mRelationship.getSelectedItem().toString())
                        .toString();
                        String userID = prefs.getString("userID", null);
                        URL url = new URL("https://protectme.the-rothley.de/contacts/"+contactID);
                        urlConnection  = (HttpURLConnection) url.openConnection();
                        urlConnection.setRequestProperty("Content-Type", "application/json");
                        urlConnection.setRequestMethod("PUT");
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
                                    Toast toast = Toast.makeText(EditContactActivity.this,
                                            "Could not edit contact.",
                                            Toast.LENGTH_SHORT);
                                    toast.show();
                                }
                            });
                            throw new IOException("Invalid response from server: " + code);
                        }else if (code !=  200 && code !=  201) {
                            runOnUiThread(new Runnable() {
                                public void run() {
                                    Toast toast = Toast.makeText(EditContactActivity.this,
                                            "Could not edit contact.",
                                            Toast.LENGTH_SHORT);
                                    toast.show();
                                }
                            });
                            throw new IOException("Invalid response from server: " + code);
                        }
                        else if (code == 200) {
                            runOnUiThread(new Runnable() {
                                public void run() {
                                    Toast toast = Toast.makeText(EditContactActivity.this,
                                            "Contact successfully edited.",
                                            Toast.LENGTH_SHORT);
                                    toast.show();
                                }
                            });
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
            thread.start();
            onBackPressed();
        }
    }
}