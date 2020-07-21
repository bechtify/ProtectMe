package com.example.protectme;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

public class RideActivity extends AppCompatActivity implements SensorEventListener, LocationListener {

    long mLastTimeStamp;
    long mCrashTimeStamp;

    double mVelocity = 0; //m per s
    double mLocation = 0; //m
    String longitude;
    String latitude;

    TextView tvLocation;
    SharedPreferences prefs;
    SharedPreferences.Editor e;

    FusedLocationProviderClient fusedLocationProviderClient;

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
        SensorManager manager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        Sensor accSensor = manager.getDefaultSensor(Sensor.TYPE_LINEAR_ACCELERATION);

        manager.registerListener(this, accSensor, SensorManager.SENSOR_DELAY_NORMAL);

        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);
        if (ActivityCompat.checkSelfPermission(RideActivity.this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            getLocation();
        } else {
            ActivityCompat.requestPermissions(RideActivity.this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 44);
        }

        final LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

        CountDownTimer timer;
        timer = new CountDownTimer(10000, 1000) {
            public void onTick(long millisUntilFinished) {
                    try {
                        if (ActivityCompat.checkSelfPermission(RideActivity.this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(RideActivity.this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                        }
                        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, RideActivity.this);
                    } catch (Exception e) {

                    }
            }

            public void onFinish() {
            }
        }.start();
    }

    @Override
    public void onLocationChanged(Location location) {
        tvLocation = (TextView) findViewById(R.id.tvLocation);
        tvLocation.setText("Location: "+Double.toString(location.getLatitude())+" "+Double.toString(location.getLongitude()));
    }

    @Override
    public void onProviderDisabled(String provider) {

    }

    @Override
    public void onProviderEnabled(String provider) {

    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }


    private void getLocation() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
        }
        fusedLocationProviderClient.getLastLocation()
                .addOnSuccessListener(this, new OnSuccessListener<Location>() {
                    @Override
                    public void onSuccess(Location location) {
                if (location != null) {
                    try {
                        Geocoder geocoder = new Geocoder(RideActivity.this, Locale.getDefault());
                        List<Address> addresses = geocoder.getFromLocation(
                                location.getLatitude(), location.getLongitude(), 1
                        );
                        tvLocation = (TextView) findViewById(R.id.tvLocation);
                        longitude = Double.toString(addresses.get(0).getLongitude());
                        latitude = Double.toString(addresses.get(0).getLatitude());
                        tvLocation.setText("Location: "+Double.toString(addresses.get(0).getLatitude())+" "+Double.toString(addresses.get(0).getLongitude()));
                    } catch (IOException ex) {
                        ex.printStackTrace();
                    }
                }
            }
        });
    }


    public void onStopRide(View view){
        onBackPressed();
    }

    public void onStartEmergency(View view){
        Intent intent = new Intent(RideActivity.this, EmergencyActivity.class);
        intent.putExtra("longitude", longitude);
        intent.putExtra("latitude", latitude);
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
                    intent.putExtra("alarmtype", "automatic");
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
