package com.example.protectme;

import androidx.appcompat.app.AppCompatActivity;

import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

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
                "Relationship", "Family", "Friend", "Colleague"};
        Spinner s = (Spinner) findViewById(R.id.spinner);
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

        if(mUsername.getText().toString().equals("")||mRelationship.getSelectedItem().toString().equals("Relationship")||mDisplayName.getText().toString().equals("")||mPhone.getText().toString().equals("")||mAddress.getText().toString().equals("")) {
            Toast toast = Toast.makeText(getApplicationContext(),
                    "Fill out all fields",
                    Toast.LENGTH_SHORT);
            toast.show();
        }else {
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
            onBackPressed();
        }
    }

}
