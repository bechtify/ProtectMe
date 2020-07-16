package com.example.protectme;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.Spinner;

import com.google.android.material.textfield.TextInputEditText;


public class AddContactActivity extends AppCompatActivity {

    TextInputEditText username;
    SharedPreferences prefs;
    SharedPreferences.Editor e;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        getSupportActionBar().hide(); //hide the title bar
        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN); //hides keyboard
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_contact);
        String[] arraySpinner = new String[] {
                "Relationship", "Family", "Friends", "Colleagues"};
        Spinner s = (Spinner) findViewById(R.id.spinner);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, arraySpinner);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        s.setAdapter(adapter);

        ImageView imgFavorite = (ImageView) findViewById(R.id.imageViewBack); //for navigation back, due to use of imageView
        imgFavorite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(AddContactActivity.this, EmergencyContactsActivity.class);
                startActivity(intent);
            }
        });


    }

    public void onAdd(View v){


    }
}
