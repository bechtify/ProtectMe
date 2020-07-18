package com.example.protectme;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.media.MediaPlayer;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class EmergencyActivity extends AppCompatActivity {

    public MediaPlayer mp;
    protected TextView mTime;
    Button abort;

    SharedPreferences prefs;
    SharedPreferences.Editor e;

    CountDownTimer timer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        getSupportActionBar().hide(); //hide the title bar
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_emergency);

        prefs = this.getSharedPreferences("prefs", MODE_PRIVATE);
        boolean autoAlarm = prefs.getBoolean("autoAlarm", false);//checks if emergency was triggered automatically @TODO: not used
        Uri alarmSound =
                RingtoneManager.getDefaultUri(RingtoneManager.TYPE_ALARM);
        mp = MediaPlayer.create(getApplicationContext(), alarmSound);
        mp.start();//alarm gets raised

        mTime = (TextView) findViewById(R.id.tvTime);
        int minutes=prefs.getInt("npMinutes", 00);
        int seconds=prefs.getInt("npSeconds", 00);

        timer = new CountDownTimer(minutes*60000+seconds*1000, 1000) {

            public void onTick(long millisUntilFinished) {
                long seconds=millisUntilFinished%60000;
                long minutes=millisUntilFinished/60000;
                if(Long.toString(seconds).length()==5) {
                    mTime.setText(minutes + " : " + Long.toString(seconds).substring(0, 2));
                }
                if(Long.toString(seconds).length()==4) {
                    mTime.setText(minutes + " : " +0+ Long.toString(seconds).substring(0, 1));
                }
                if(Long.toString(seconds).length()==3) {
                    mTime.setText(minutes + " : " +0+0);
                }
            }

            public void onFinish() {
                mTime.setText("Emergency Services have been called!");
                Toast toast = Toast.makeText(getApplicationContext(),
                        "Emergency Services have been called!",
                        Toast.LENGTH_SHORT);
                toast.show();
            }
        }.start();

    }

    public void abort(View view){
        mp.stop();//stops the alarm tone
        prefs = this.getSharedPreferences("prefs", MODE_PRIVATE);
        e=prefs.edit();
        e.putBoolean("autoAlarm", false);//resets alarm type
        e.commit();
        onBackPressed();//navigates back to ride
    }

    public void onCallHelp(View view){
        abort = findViewById(R.id.btAbort);
        abort.setText("Go Back");
        timer.cancel();
        mTime = (TextView) findViewById(R.id.tvTime);
        mTime.setText("Emergency Services have been called!");
        Toast toast = Toast.makeText(getApplicationContext(),
                "Emergency Services have been called!",
                Toast.LENGTH_SHORT);
        toast.show();
        mp.stop();//stops the alarm tone
    }
}
