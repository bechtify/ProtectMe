package com.example.protectme;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class EmergencyNotificationActivity extends AppCompatActivity {

    TextView tvName;
    TextView tvLocation;
    Intent intent;
    Button btnMaps;
    Button btnClose;
    String latitude;
    String longitude;
    String name;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_emergency_notification);
        tvName = (TextView) findViewById(R.id.tvNotifName);
        tvLocation = (TextView) findViewById(R.id.tvNotifLocation);
        btnMaps = (Button) findViewById(R.id.btnOpenMaps);
        btnClose = (Button) findViewById(R.id.btnCloseNotification);
        intent = getIntent();
        name = intent.getStringExtra("name");
        latitude = intent.getStringExtra("latitude");
        longitude = intent.getStringExtra("longitude");
        tvName.setText(name);
        tvLocation.setText("Latitude:" + latitude + " Longitude:" + longitude);
    }

    public void onMaps(View view) {
//        // Create a Uri from an intent string. Use the result to create an Intent.
//        Uri gmmIntentUri = Uri.parse("geo:"+latitude+","+longitude);
//
//        // Create an Intent from gmmIntentUri. Set the action to ACTION_VIEW
//        Intent mapIntent = new Intent(Intent.ACTION_VIEW, gmmIntentUri);
//        // Make the Intent explicit by setting the Google Maps package
//        mapIntent.setPackage("com.google.android.apps.maps");
//
//        // Attempt to start an activity that can handle the Intent
//        startActivity(mapIntent);

        Intent intent = new Intent(android.content.Intent.ACTION_VIEW,
                Uri.parse("http://maps.google.com/maps?daddr="+latitude+","+longitude));
        startActivity(intent);
    }

    public void onClose(View view){
        finish();
    }


}