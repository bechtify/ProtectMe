package com.example.protectme;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.media.MediaPlayer;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.widget.TextView;

public class CrashDetectionDevelopmentActivity extends AppCompatActivity implements SensorEventListener {

    protected TextView tvVelocity;
    protected  TextView tvAcceleration;
    protected TextView tvCrashDetected;

    long mLastTimeStamp;
    long mCrashTimeStamp;

    double mVelocity=0; //m per s
    double mLocation=0; //m

    int crash=0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        getSupportActionBar().hide(); //hide the title bar
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_crash_detection_development);
        initView();



    }

    private void initView() {
        tvVelocity = (TextView) findViewById(R.id.tvVelocity);
        tvAcceleration = (TextView) findViewById(R.id.tvAcceleration);
        tvCrashDetected = (TextView) findViewById(R.id.tvCrashDetected);

        SensorManager manager = (SensorManager)getSystemService(Context.SENSOR_SERVICE);
        Sensor accSensor=manager.getDefaultSensor(Sensor.TYPE_LINEAR_ACCELERATION);

        manager.registerListener(this, accSensor, SensorManager.SENSOR_DELAY_NORMAL);
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        if(mLastTimeStamp>0) {
            long diffT = event.timestamp - mLastTimeStamp;
            mLastTimeStamp = event.timestamp;
            double diffT_sec = diffT / 1000000000d;
            //System.out.println(event.values[0] + " time_difference=" + diffT / 1000000d + "ns");


            double diff_vel = event.values[0] * diffT_sec;
            mVelocity = mVelocity + diff_vel;
            mVelocity=mVelocity*0.95;
            tvVelocity.setText("Velocity: "+Double.toString(mVelocity * 100) + " cm per s");

            //Weg:
            mLocation=mLocation+mVelocity*diffT_sec;
            //wegTV.setText(Double.toString(mLocation*100)+" cm");
            tvAcceleration.setText("Acceleration: "+Double.toString(event.values[0]*100)+" cm / s*s");
            if((((mVelocity*100)>138.9)||((mVelocity*100)<-138.9))&&((event.values[0]*100)>10)){ //crash will be detected if Velocity is greater than 5 km/h (negative and positive) and acceleration is greater than 10 cm/s*s
                if(mCrashTimeStamp==0||((System.currentTimeMillis()-mCrashTimeStamp)>5000)){ //crash can be detected every five seconds
                    crash++;
                    tvCrashDetected.setText(Integer.toString(crash));
                    mCrashTimeStamp = System.currentTimeMillis();
                    Uri alarmSound =
                            RingtoneManager. getDefaultUri (RingtoneManager. TYPE_ALARM );
                    MediaPlayer mp = MediaPlayer. create (getApplicationContext(), alarmSound);
                    mp.start();
                }
            }

        }else
        {
            mLastTimeStamp=event.timestamp;
        }
    }

    @Override
    protected void onDestroy() {
        System.out.println("onDestroy");
        super.onDestroy();
    }

    @Override
    protected void onStop() {
        System.out.println("onStop");
        super.onStop();
    }

    @Override
    protected void onResume() {
        System.out.println("onResume");
        super.onResume();
    }

    @Override
    protected void onStart() {
        System.out.println("onStart");
        super.onStart();
    }

    @Override
    protected void onPause() {
        System.out.println("onPause");
        super.onPause();
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }
}