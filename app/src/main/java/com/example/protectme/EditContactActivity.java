package com.example.protectme;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;

public class EditContactActivity extends AppCompatActivity {

    EditText mUsername;
    EditText mDisplayName;
    EditText mPhone;
    EditText mAddress;
    Spinner mRelationship;

    SharedPreferences prefs;
    SharedPreferences.Editor e;

    int contactNumber;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        getSupportActionBar().hide(); //hide the title bar
        Intent intent = getIntent();
        contactNumber = intent.getIntExtra("contactNumber", 0);//get number of item which has been clicked
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_contact);

        ImageView imgFavorite = (ImageView) findViewById(R.id.imageViewBack);
        imgFavorite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        mUsername = (EditText) findViewById(R.id.tvUsername);
        mDisplayName = (EditText) findViewById(R.id.tvDisplayName);
        mPhone = (EditText) findViewById(R.id.tvPhone);
        mAddress = (EditText) findViewById(R.id.tvAddress);
        mRelationship = (Spinner) findViewById(R.id.spinnerRelationship);

        prefs = this.getSharedPreferences("prefs", MODE_PRIVATE);
        int contactsNumber = (prefs.getInt("contactNumber", 0)-1);
        contactNumber =Math.abs(contactNumber-contactsNumber); //inverts contact order
        Gson gson = new Gson();
        String json=prefs.getString(Integer.toString(contactNumber), null);
        EmergencyContact contact = gson.fromJson(json, EmergencyContact.class);

        String[] arraySpinner = new String[] {
                "Relationship", "Family", "Friends", "Colleagues"};
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, arraySpinner){
            @Override
            public boolean isEnabled(int position){
                if(position == 0)
                {
                    // Disables the first item from Spinner - will be used for hint
                    return false;
                }
                else
                {
                    return true;
                }
            }
            @Override
            public View getDropDownView(int position, View convertView,
                                        ViewGroup parent) {
                View view = super.getDropDownView(position, convertView, parent);
                TextView tv = (TextView) view;
                if(position == 0){
                    tv.setTextColor(Color.GRAY); // Set the hint text color gray
                }
                else {
                    tv.setTextColor(Color.BLACK);
                }
                return view;
            }
        };
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mRelationship.setAdapter(adapter);

        mUsername.setText(contact.mUsername);
        mDisplayName.setText(contact.mDisplayName);
        mPhone.setText(contact.mPhone);
        mAddress.setText(contact.mAddress);
        if(contact.mRelationship.equals("Family")){
            mRelationship.setSelection(1);
        } else if(contact.mRelationship.equals("Friends")){
            mRelationship.setSelection(2);
        } else if(contact.mRelationship.equals("Colleagues")){
            mRelationship.setSelection(3);
        }
    }

    public void onSave(View view){
        if(mUsername.getText().toString().equals("")||mRelationship.getSelectedItem().toString().equals("Relationship")||mDisplayName.getText().toString().equals("")||mPhone.getText().toString().equals("")||mAddress.getText().toString().equals("")) {
            Toast toast = Toast.makeText(getApplicationContext(),
                    "Fill out all fields",
                    Toast.LENGTH_SHORT);
            toast.show();
        }else {
            EmergencyContact myObject = new EmergencyContact(mUsername.getText().toString(), mRelationship.getSelectedItem().toString(), mDisplayName.getText().toString(), mPhone.getText().toString(), mAddress.getText().toString());
            Gson gson = new Gson();
            String json = gson.toJson(myObject);//Object gets casted to String in order to save it in SharedPrefs
            e = prefs.edit();
            e.putString(Integer.toString(contactNumber), json);
            e.commit();
            onBackPressed();
        }
    }
}