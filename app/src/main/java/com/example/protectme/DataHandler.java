package com.example.protectme;

import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.Gson;

import org.json.JSONArray;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Scanner;

public class DataHandler {
    ArrayList<EmergencyContact> mData;
    EmergencyContactAdapter mAdapter;
    RecyclerView mRecyclerView;

    public DataHandler(ArrayList<EmergencyContact> mData, EmergencyContactAdapter mAdapter, RecyclerView mRecyclerView) {
        this.mData = mData;
        this.mAdapter = mAdapter;
        this.mRecyclerView = mRecyclerView;
    }

    public void execute(final URL url, final EmergencyContactsActivity context){
        Thread thread = new Thread(new Runnable() {

            @Override
            public void run() {
        String response;
        try {
            response = new Scanner( url.openStream() ).useDelimiter( "\\Z" ).next();
            context.addData(response);
        } catch (Exception e) {
            e.printStackTrace();
        }}});
        thread.start();
    }
}
