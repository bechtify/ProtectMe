package com.example.protectme;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import java.util.ArrayList;

public class EmergencyContactsActivity extends AppCompatActivity {
    ArrayList<EmergencyContact> mData;
    MyAdapter mMyAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        getSupportActionBar().hide(); //hide the title bar
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_emergency_contacts);
        ImageView imgFavorite = (ImageView) findViewById(R.id.imageViewBack);
        imgFavorite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(EmergencyContactsActivity.this, MenuActivity.class);
                startActivity(intent);
            }
        });
        mData=new ArrayList<>();
        mData.add(new EmergencyContact("Bechtify","Friend","Simon Becht", "0123456789", "Coblitzallee 9"));
        mData.add(new EmergencyContact("Toro","Friend","Tobias Rothley", "987654321", "Coblitzallee 10"));
        mMyAdapter=new MyAdapter(mData);
        RecyclerView recyclerView=findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(mMyAdapter);
    }

    public void onAdd(View view){
        Intent intent = new Intent(EmergencyContactsActivity.this, AddContactActivity.class);
        startActivity(intent);
    }

    public void onDelete(View view){

    }
}
