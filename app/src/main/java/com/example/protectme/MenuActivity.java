package com.example.protectme;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.Spinner;

public class MenuActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        getSupportActionBar().hide(); //hide the title bar
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);

        ImageView imgFavorite = (ImageView) findViewById(R.id.imageViewLogout);//for navigation back, due to use of imageView
        imgFavorite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MenuActivity.this, LoginActivity.class);
                startActivity(intent);
            }
        });
        String[] arraySpinner = new String[] {
                "Choose Mobility", "Bicycle", "Car", "Truck", "Motor Bike"
        };
        Spinner s = (Spinner) findViewById(R.id.spinner);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, arraySpinner);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        s.setAdapter(adapter);
    }

    public void onStartRide(View view){
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


}
