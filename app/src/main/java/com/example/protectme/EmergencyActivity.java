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

import org.json.JSONObject;

import java.io.BufferedOutputStream;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;

public class EmergencyActivity extends AppCompatActivity {

    public MediaPlayer mp;
    protected TextView mTime;
    Button abort;

    SharedPreferences prefs;
    SharedPreferences.Editor e;

    CountDownTimer timer;

    String longitude;
    String latitude;

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
        if(minutes==00&&seconds==00){
            seconds=30;//in case user has never changed settings-->default value is 30 sec
        }

        Intent intent = getIntent();
        longitude = intent.getStringExtra("longitude");
        latitude = intent.getStringExtra("latitude");

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
                raiseAlarm("automatic");
                onBackPressed();
            }
        }.start();
    }

    public void abort(View view){
        RideActivity.activeAlarm=false;
        mp.stop();//stops the alarm tone
        onBackPressed();//navigates back to ride
    }

    public void raiseAlarm(final String alarmtype){
        mTime.setText("Emergency Services have been called!");
        Toast toast = Toast.makeText(getApplicationContext(),
                "Emergency Services have been called!",
                Toast.LENGTH_SHORT);
        toast.show();
        //final SharedPreferences prefs = this.getSharedPreferences("prefs", MODE_PRIVATE);
        Thread thread = new Thread(){

            @Override
            public void run() {
                HttpURLConnection urlConnection=null;
                try  {
                    String selectedTypeOfMobility = prefs.getString("selectedTypeOfMobility", null);
                    String jsonString = new JSONObject()
                            .put("longitude", longitude)
                            .put("latitude", latitude)
                            .put("vehicle", selectedTypeOfMobility)
                            .put("alarmtype", alarmtype)
                            .toString();
                    String userID = prefs.getString("userID", null);
                    URL url = new URL("https://protectme.the-rothley.de/emergencies/"+userID);
                    urlConnection  = (HttpURLConnection) url.openConnection();
                    urlConnection.setRequestProperty("Content-Type", "application/json");
                    urlConnection.setRequestMethod("POST");
                    urlConnection.setDoOutput(true);
                    urlConnection.setDoInput(true);
                    urlConnection.setChunkedStreamingMode(0);
                    OutputStream out = new BufferedOutputStream(urlConnection.getOutputStream());
                    BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(
                            out, "UTF-8"));
                    writer.write(jsonString);
                    writer.flush();

                    int code = urlConnection.getResponseCode();
                    if (code ==  400||code !=  200 && code !=  201) {
                        runOnUiThread(new Runnable() {
                            public void run() {
                                Toast toast = Toast.makeText(EmergencyActivity.this,
                                        "Emergency Services could not be called.",
                                        Toast.LENGTH_SHORT);
                                toast.show();
                            }
                        });
                        throw new IOException("Invalid response from server: " + code);
                    }
                    mTime = (TextView) findViewById(R.id.tvTime);
                    mTime.setText("Emergency Services have been called!");
                    Toast toast = Toast.makeText(getApplicationContext(),
                            "Emergency Services have been called!",
                            Toast.LENGTH_SHORT);
                    toast.show();
                } catch (Exception e) {
                    e.printStackTrace();
                }
                finally {
                    if (urlConnection != null) {
                        urlConnection.disconnect();
                        RideActivity.activeAlarm=false;
                    }
                }
            }
        };
        thread.start();
    }

    public void onCallHelp(View view){
        abort = findViewById(R.id.btAbort);
        abort.setText("Go Back");
        timer.cancel();
        raiseAlarm("manual");
        mp.stop();//stops the alarm tone
    }

    public void onBackPressed(){
        RideActivity.activeAlarm=false;
        timer.cancel();
        mp.stop();//stops the alarm tone
        super.onBackPressed();
    }
}
