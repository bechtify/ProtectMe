package com.example.protectme;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TimePicker;

public class EmergencySettingsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        getSupportActionBar().hide(); //hide the title bar
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_emergency_settings);
        ImageView imgFavorite = (ImageView) findViewById(R.id.imageViewBack);
        imgFavorite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(EmergencySettingsActivity.this, MenuActivity.class);
                startActivity(intent);
            }
        });
        TimePicker picker=(TimePicker)findViewById(R.id.timePicker);
        picker.setIs24HourView(true);
        picker.setHour(0);
        picker.setMinute(30);
    }

    public void onSave(View view){
        Intent intent = new Intent(EmergencySettingsActivity.this, MenuActivity.class);
        startActivity(intent);
    }
}
