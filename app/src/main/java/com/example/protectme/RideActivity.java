package com.example.protectme;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.media.MediaPlayer;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

public class RideActivity extends AppCompatActivity implements SensorEventListener {

    long mLastTimeStamp;
    long mCrashTimeStamp;

    double mVelocity=0; //m per s
    double mLocation=0; //m

    SharedPreferences prefs;
    SharedPreferences.Editor e;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        getSupportActionBar().hide(); //hide the title bar
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ride);

        ImageView imgFavorite = (ImageView) findViewById(R.id.imageViewBack);//for navigation back, due to use of imageView
        imgFavorite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });


        //Sensor Manager & Listener for measuring speed and acceleration
        SensorManager manager = (SensorManager)getSystemService(Context.SENSOR_SERVICE);
        Sensor accSensor=manager.getDefaultSensor(Sensor.TYPE_LINEAR_ACCELERATION);

        manager.registerListener(this, accSensor, SensorManager.SENSOR_DELAY_NORMAL);
    }

    public void onStopRide(View view){
        onBackPressed();
    }

    public void onStartEmergency(View view){
        Intent intent = new Intent(RideActivity.this, EmergencyActivity.class);
        startActivity(intent);
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        if(mLastTimeStamp>0) {
            long diffT = event.timestamp - mLastTimeStamp;
            mLastTimeStamp = event.timestamp;
            double diffT_sec = diffT / 1000000000d;
            double diff_vel = event.values[0] * diffT_sec;
            mVelocity = mVelocity + diff_vel;
            mVelocity=mVelocity*0.95;

            //Distance:
            mLocation=mLocation+mVelocity*diffT_sec;

            if((((mVelocity*100)>138.9)||((mVelocity*100)<-138.9))&&((event.values[0]*100)>10)){ //crash will be detected if Velocity is greater than 5 km/h (negative and positive) and acceleration is greater than 10 cm/s*s
                if(mCrashTimeStamp==0||((System.currentTimeMillis()-mCrashTimeStamp)>5000)){ //crash can be detected every five seconds
                    mCrashTimeStamp = System.currentTimeMillis();
                    Intent intent = new Intent(RideActivity.this, EmergencyActivity.class);
                    prefs = this.getSharedPreferences("prefs", MODE_PRIVATE);
                    e=prefs.edit();
                    e.putBoolean("autoAlarm", true);
                    e.commit();
                    startActivity(intent);
                }
            }

        }else
        {
            mLastTimeStamp=event.timestamp;
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }
}
