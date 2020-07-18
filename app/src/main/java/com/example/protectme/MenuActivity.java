package com.example.protectme;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.Spinner;

public class MenuActivity extends AppCompatActivity {

    Spinner mSpinner;

    SharedPreferences prefs;
    SharedPreferences.Editor e;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        getSupportActionBar().hide(); //hide the title bar
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);

        ImageView imgFavorite = (ImageView) findViewById(R.id.imageViewLogout);//for navigation back, due to use of imageView
        imgFavorite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
        String[] arraySpinner = new String[] {
                "Choose Mobility", "Bicycle", "Car", "Truck", "Motor Bike"
        };
        mSpinner = (Spinner) findViewById(R.id.spinner);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, arraySpinner);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mSpinner.setAdapter(adapter);
    }

    public void onStartRide(View view){
        prefs = this.getSharedPreferences("prefs", MODE_PRIVATE);
        e=prefs.edit();
        e.putString("selectedTypeOfMobility", mSpinner.getSelectedItem().toString());
        e.commit();
        Intent intent = new Intent(MenuActivity.this, RideActivity.class);
        startActivity(intent);
    }

    public void onEmergencySettings(View view){
        Intent intent = new Intent(MenuActivity.this, EmergencySettingsActivity.class);
        startActivity(intent);
    }

    public void onEmergencyContacts(View view){
        Intent intent = new Intent(MenuActivity.this, EmergencyContactsActivity.class);
        startActivity(intent);
    }

    public void onFirstAid(View view){
        Intent intent = new Intent(MenuActivity.this, CrashDetectionDevelopmentActivity.class);
        startActivity(intent);
    }


}
