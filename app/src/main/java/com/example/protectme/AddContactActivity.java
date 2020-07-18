package com.example.protectme;

import androidx.appcompat.app.AppCompatActivity;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;

import com.google.gson.Gson;


public class AddContactActivity extends AppCompatActivity {

    EditText mUsername;
    EditText mDisplayName;
    EditText mPhone;
    EditText mAddress;
    Spinner mRelationship;
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
                onBackPressed();
            }
        });
    }

    public void onAdd(View v){
        mUsername = (EditText) findViewById(R.id.tvUsername);
        mDisplayName = (EditText) findViewById(R.id.tvDisplayName);
        mPhone = (EditText) findViewById(R.id.tvPhone);
        mAddress = (EditText) findViewById(R.id.tvAddress);
        mRelationship = (Spinner) findViewById(R.id.spinner);

        EmergencyContact myObject = new EmergencyContact(mUsername.getText().toString(), mRelationship.getSelectedItem().toString(), mDisplayName.getText().toString(), mPhone.getText().toString(), mAddress.getText().toString());
        Gson gson = new Gson();
        String json = gson.toJson(myObject);//Object gets casted to String in order to save it in SharedPrefs
        prefs = this.getSharedPreferences("prefs", MODE_PRIVATE);
        int contactNumber = prefs.getInt("contactNumber", 0); //index for adding a contact - prevents overwriting a contact
        e=prefs.edit();
        e.putString(Integer.toString(contactNumber), json);
        contactNumber++;
        e.putInt("contactNumber", contactNumber);// will be used as kind of index for adding a contact next time - prevents overwriting a contact
        e.commit();

    }

}
