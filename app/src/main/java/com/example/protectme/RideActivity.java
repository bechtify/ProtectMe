package com.example.protectme;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

public class RideActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        getSupportActionBar().hide(); //hide the title bar
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ride);

        ImageView imgFavorite = (ImageView) findViewById(R.id.imageViewBack);//for navigation back, due to use of imageView
        imgFavorite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(RideActivity.this, MenuActivity.class);
                startActivity(intent);
            }
        });
    }

    public void onStopRide(View view){
        Intent intent = new Intent(RideActivity.this, MenuActivity.class);
        startActivity(intent);
    }

    public void onStartEmergency(View view){
        Intent intent = new Intent(RideActivity.this, EmergencyActivity.class);
        startActivity(intent);
    }

}
