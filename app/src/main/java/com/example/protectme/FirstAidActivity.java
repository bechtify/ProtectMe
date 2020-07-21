package com.example.protectme;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.gson.Gson;

import java.util.ArrayList;

public class FirstAidActivity extends AppCompatActivity {

    public ArrayList<FirstAidText> mData;
    FirstAidAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        getSupportActionBar().hide(); //hide the title bar
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_first_aid);
        ImageView imgFavorite = (ImageView) findViewById(R.id.imageViewBack); //for navigation back, due to use of imageView
        imgFavorite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        mData=new ArrayList<>();
        mData.add(new FirstAidText("\nFirst Aid for Bleeding","Steps to take if you are facing heavy bleeding right now:\n" +
                "\n" +
                "Cover the wound with a gauze or a cloth.\n" +
                "Apply direct pressure to stop the blood flow. Don't remove the cloth. Add more layers if needed. The cloth will help clots form to stop the flow.\n"));
        mData.add(new FirstAidText("First Aid for Suspected Fracture","Take these steps for a suspected fracture:\n" +
                "\n" +
                "Don't try to straighten it.\n" +
                "Stabilize the limb using a splint and padding to keep it immobile.\n" +
                "Put a cold pack on the injury, avoiding placing ice directly on the skin.\n" +
                "Elevate the extremity.\n" +
                "Give anti-inflammatory drugs like ibuprofen or naproxen.\n"));
        mData.add(new FirstAidText("First Aid for Suspected Cardiac Arrest","According to the American Heart Association and American Red Cross 2019 guidelines, the steps to take when a cardiac arrest is suspected are:\n" +
                "\n" +
                "Command someone to call 911 or the medical alert system for the locale.\n" +
                "Immediately start chest compressions regardless of your training. Compress hard and fast in the center of the chest, allowing recoil between compressions. Hand this task over to those who are trained if and when they arrive.\n" +
                "If you are trained, use chest compressions and rescue breathing.\n" +
                "An AED should be applied and used. But it is essential not to delay chest compressions, so finding one should be commanded to someone else while you are doing chest compressions.\n"));
        mAdapter =new FirstAidAdapter(mData);
        RecyclerView recyclerView=findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(mAdapter);
    }

    public void onCallHelp(View view){
        Intent intent = new Intent(FirstAidActivity.this, EmergencyActivity.class);
        startActivity(intent);
    }
}