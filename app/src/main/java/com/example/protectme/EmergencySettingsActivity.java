package com.example.protectme;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.NumberPicker;

public class EmergencySettingsActivity extends AppCompatActivity {

    SharedPreferences prefs;
    SharedPreferences.Editor e;

    public NumberPicker minutesPicker;
    public NumberPicker secondsPicker;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        getSupportActionBar().hide(); //hide the title bar
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_emergency_settings);
        ImageView imgFavorite = (ImageView) findViewById(R.id.imageViewBack);
        imgFavorite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        //Changes Format of Number Pickers
        minutesPicker=(NumberPicker) findViewById(R.id.npMinutes);
        minutesPicker.setMaxValue(59);
        minutesPicker.setMinValue(0);
        minutesPicker.setFormatter(new NumberPicker.Formatter() {
            @Override
            public String format(int i) {
                return String.format("%02d", i);
            }
        });
        secondsPicker=(NumberPicker) findViewById(R.id.npSeconds);
        secondsPicker.setMaxValue(59);
        secondsPicker.setMinValue(0);
        secondsPicker.setFormatter(new NumberPicker.Formatter() {
            @Override
            public String format(int i) {
                return String.format("%02d", i);
            }
        });

        //Sets values of numberpickers
        prefs = this.getSharedPreferences("prefs", MODE_PRIVATE);
        int minutes = prefs.getInt("npMinutes", 0);
        int seconds = prefs.getInt("npSeconds", 30);
        minutesPicker.setValue(minutes);
        secondsPicker.setValue(seconds);

    }

    public void onSave(View view){
        //gets values of number pickers and saves them
        prefs = this.getSharedPreferences("prefs", MODE_PRIVATE);
        e=prefs.edit();
        e.putInt("npMinutes", minutesPicker.getValue());
        e.putInt("npSeconds", secondsPicker.getValue());
        e.commit();
        onBackPressed();
    }
}
