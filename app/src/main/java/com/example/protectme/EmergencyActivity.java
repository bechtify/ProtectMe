package com.example.protectme;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.media.MediaPlayer;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;

import org.json.JSONObject;

import java.io.BufferedOutputStream;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;
import java.util.Locale;

public class EmergencyActivity extends AppCompatActivity implements LocationListener {

    private static final int REQUEST_CALL = 1;

    public MediaPlayer mp;
    protected TextView mTime;
    Button abort;

    SharedPreferences prefs;
    SharedPreferences.Editor e;

    CountDownTimer timer;

    String longitude;
    String latitude;
    String emergencyNumber;
    FusedLocationProviderClient fusedLocationProviderClient;

    TextView tvLocation;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        getSupportActionBar().hide(); //hide the title bar
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_emergency);

        prefs = this.getSharedPreferences("prefs", MODE_PRIVATE);
        emergencyNumber = prefs.getString("npPhone", null);
        boolean autoAlarm = prefs.getBoolean("autoAlarm", false);//checks if emergency was triggered automatically @TODO: not used
        Uri alarmSound =
                RingtoneManager.getDefaultUri(RingtoneManager.TYPE_ALARM);
        mp = MediaPlayer.create(getApplicationContext(), alarmSound);
        mp.start();//alarm gets raised

        mTime = (TextView) findViewById(R.id.tvTime);
        int minutes = prefs.getInt("npMinutes", 00);
        int seconds = prefs.getInt("npSeconds", 00);
        if (minutes == 00 && seconds == 00) {
            seconds = 30;//in case user has never changed settings-->default value is 30 sec
        }

        Intent intent = getIntent();
        longitude = intent.getStringExtra("longitude");
        latitude = intent.getStringExtra("latitude");

        timer = new CountDownTimer(minutes * 60000 + seconds * 1000, 1000) {

            public void onTick(long millisUntilFinished) {
                long seconds = millisUntilFinished % 60000;
                long minutes = millisUntilFinished / 60000;
                if (Long.toString(seconds).length() == 5) {
                    mTime.setText(minutes + " : " + Long.toString(seconds).substring(0, 2));
                }
                if (Long.toString(seconds).length() == 4) {
                    mTime.setText(minutes + " : " + 0 + Long.toString(seconds).substring(0, 1));
                }
                if (Long.toString(seconds).length() == 3) {
                    mTime.setText(minutes + " : " + 0 + 0);
                }
                if (ActivityCompat.checkSelfPermission(EmergencyActivity.this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                    getLocation();
                }
            }

            public void onFinish() {
                raiseAlarm("automatic");
                onBackPressed();
            }
        }.start();

        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);
        if (ActivityCompat.checkSelfPermission(EmergencyActivity.this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            getLocation();
        } else {
            ActivityCompat.requestPermissions(EmergencyActivity.this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 44);
        }

        final LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

        CountDownTimer timer;
        timer = new CountDownTimer(10000, 1000) {
            public void onTick(long millisUntilFinished) {
                try {
                    if (ActivityCompat.checkSelfPermission(EmergencyActivity.this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(EmergencyActivity.this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                    }
                    locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, (LocationListener) EmergencyActivity.this);
                } catch (Exception e) {

                }
            }

            public void onFinish() {
            }
        };
    }

    @Override
    public void onLocationChanged(Location location) {
        tvLocation = (TextView) findViewById(R.id.tvLocation);
        tvLocation.setText("Location: " + Double.toString(location.getLatitude()) + " " + Double.toString(location.getLongitude()));
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
                                Geocoder geocoder = new Geocoder(EmergencyActivity.this, Locale.getDefault());
                                List<Address> addresses = geocoder.getFromLocation(
                                        location.getLatitude(), location.getLongitude(), 1
                                );
                                tvLocation = (TextView) findViewById(R.id.tvLocation);
                                longitude = Double.toString(addresses.get(0).getLongitude());
                                latitude = Double.toString(addresses.get(0).getLatitude());
                                tvLocation.setText("Location: " + Double.toString(addresses.get(0).getLatitude()) + " " + Double.toString(addresses.get(0).getLongitude()));
                            } catch (IOException ex) {
                                ex.printStackTrace();
                            }
                        }
                    }
                });
    }

    public void abort(View view) {
        RideActivity.activeAlarm = false;
        mp.stop();//stops the alarm tone
        onBackPressed();//navigates back to ride
    }

    public void raiseAlarm(final String alarmtype) {
        mTime.setText("Emergency Services have been called!");
        Toast toast = Toast.makeText(getApplicationContext(),
                "Emergency Services have been called!",
                Toast.LENGTH_SHORT);
        toast.show();
        //final SharedPreferences prefs = this.getSharedPreferences("prefs", MODE_PRIVATE);
        Thread thread = new Thread() {

            @Override
            public void run() {
                HttpURLConnection urlConnection = null;
                try {
                    String selectedTypeOfMobility = prefs.getString("selectedTypeOfMobility", null);
                    String jsonString = new JSONObject()
                            .put("longitude", longitude)
                            .put("latitude", latitude)
                            .put("vehicle", selectedTypeOfMobility)
                            .put("alarmtype", alarmtype)
                            .toString();
                    String userID = prefs.getString("userID", null);
                    URL url = new URL("https://protectme.the-rothley.de/emergencies/" + userID);
                    urlConnection = (HttpURLConnection) url.openConnection();
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
                    if (code == 400 || code != 200 && code != 201) {
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
                } finally {
                    if (urlConnection != null) {
                        urlConnection.disconnect();
                        RideActivity.activeAlarm = false;
                    }
                }
            }
        };
        thread.start();
        makePhoneCall();
    }

    public void onCallHelp(View view) {
        abort = findViewById(R.id.btAbort);
        abort.setText("Go Back");
        timer.cancel();
        raiseAlarm("manual");
        mp.stop();//stops the alarm tone
    }

    public void onBackPressed() {
        RideActivity.activeAlarm = false;
        timer.cancel();
        mp.stop();//stops the alarm tone
        super.onBackPressed();
    }

    private void makePhoneCall() {
        String dial = "tel:" + emergencyNumber;
        startActivity(new Intent(Intent.ACTION_CALL, Uri.parse(dial)));

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == REQUEST_CALL) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                makePhoneCall();
            } else {
                Toast.makeText(this, "Permission DENIED", Toast.LENGTH_SHORT).show();
            }
        }
    }
}
