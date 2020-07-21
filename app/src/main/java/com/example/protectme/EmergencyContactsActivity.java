package com.example.protectme;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;


public class EmergencyContactsActivity extends AppCompatActivity {
    static ArrayList<EmergencyContact> mData;
    static EmergencyContactAdapter mAdapter;
    static RecyclerView mRecyclerView;

    SharedPreferences prefs;
    SharedPreferences.Editor e;
    Context test;

    static EmergencyContactsActivity mThis;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        getSupportActionBar().hide(); //hide the title bar
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_emergency_contacts);
        ImageView imgFavorite = (ImageView) findViewById(R.id.imageViewBack);
        imgFavorite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
    }

    public void onAdd(View view){
        Intent intent = new Intent(EmergencyContactsActivity.this, AddContactActivity.class);
        startActivity(intent);
        onPause();
    }

    public void onResume() { //reloads Data --> shows newly added input
        super.onResume();
        addDatatoView();

    }

    public void addDatatoView(){
        final SharedPreferences prefs = this.getSharedPreferences("prefs", MODE_PRIVATE);
        mRecyclerView=findViewById(R.id.recyclerView);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        String userID = prefs.getString("userID", null);
        String stringURL = "https://protectme.the-rothley.de/contacts/user/"+userID;
        try {
            URL url = new URL(stringURL);
            DataHandler dataHandler = new DataHandler(mData, mAdapter, mRecyclerView);
            dataHandler.execute(url, this);
        }catch (Exception e){}
    }

    public void onDelete(View view){
        mAdapter.deleteChecked();
        prefs = this.getSharedPreferences("prefs", MODE_PRIVATE);
        e=prefs.edit();
        int i =0;
        while(prefs.getString(Integer.toString((i)), null)!=null) {//Deletes every contact in shared prefs
            e.remove(Integer.toString(i));
            i++;
        }
        e.commit();
        e=prefs.edit();
        e.putInt("contactNumber", mData.size());//adjusts number of contacts --> when a new contact gets added, this int is used as index for adding the new contact to sharedPrefs
        for(i=0; i<mData.size(); i++){//adds all remaining contacts from mData to shared prefs
            //EmergencyContact myObject = new EmergencyContact(mData.get(i).username, mData.get(i).relationship, mData.get(i).display_name, mData.get(i).phone, mData.get(i).address);
            Gson gson = new Gson();
            //String json = gson.toJson(myObject);
            //e.putString(Integer.toString(i), json);
        }
        e.commit();
    }

    public void addData(String response){
        try {
            JSONArray jsonArray = new JSONArray(response);
            mData = new ArrayList<EmergencyContact>();
            for (int i = 0; i < jsonArray.length(); i++) {
                Gson gson = new Gson();
                EmergencyContact contact = gson.fromJson(jsonArray.getString(i), EmergencyContact.class);
                mData.add(contact);
            }
            mAdapter = new EmergencyContactAdapter(mData);
            mAdapter.notifyDataSetChanged();

            runOnUiThread(new Runnable() {

                @Override
                public void run() {

                    mRecyclerView.setAdapter(mAdapter);

                }
            });

        }catch (Exception e){
            e.printStackTrace();
        }
    }

}
