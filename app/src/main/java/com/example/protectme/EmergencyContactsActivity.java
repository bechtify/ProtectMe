package com.example.protectme;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.gson.Gson;

import org.json.JSONObject;

import java.io.BufferedOutputStream;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;


public class EmergencyContactsActivity extends AppCompatActivity {
    public ArrayList<EmergencyContact> mData;
    EmergencyContactAdapter mAdapter;

    SharedPreferences prefs;
    SharedPreferences.Editor e;

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

        addDatatoView();
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
        Thread thread = new Thread(new Runnable() {

            @Override
            public void run() {
                HttpURLConnection urlConnection=null;
                try  {
                    String jsonString = new JSONObject()
                            /*.put("firstname", .getText().toString())
                            .put("firstname", m.getText().toString())
                            .put("surname", surname.getText().toString())
                            .put("bloodgroup", bloodgroup)
                            .put("password", password.getText().toString())*/
                            .toString();
                    String userID = prefs.getString("userID", null);
                    URL url = new URL("https://protectme.the-rothley.de/contacts/"+userID);
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
                    if (code !=  200 && code !=  201) {
                        runOnUiThread(new Runnable() {
                            public void run() {
                                Toast toast = Toast.makeText(EmergencyContactsActivity.this,
                                        "Could not add contact.",
                                        Toast.LENGTH_SHORT);
                                toast.show();
                            }
                        });
                        throw new IOException("Invalid response from server: " + code);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
                finally {
                    if (urlConnection != null) {
                        urlConnection.disconnect();
                    }
                }
            }
        });
        //thread.start();
        //onBackPressed();
        /*mData=new ArrayList<>();
        prefs = this.getSharedPreferences("prefs", MODE_PRIVATE);

        Gson gson = new Gson();
        int contactNumber=prefs.getInt("contactNumber", -1);

        while(contactNumber>=0){
            String json=prefs.getString(Integer.toString(contactNumber), null);
            EmergencyContact contact = gson.fromJson(json, EmergencyContact.class);
            if(contact!=null){
                mData.add(contact);
            }
            contactNumber--;
        }
        mAdapter =new EmergencyContactAdapter(mData);
        RecyclerView recyclerView=findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(mAdapter);*/
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
            EmergencyContact myObject = new EmergencyContact(mData.get(i).mUsername, mData.get(i).mRelationship, mData.get(i).mDisplayName, mData.get(i).mPhone, mData.get(i).mAddress);
            Gson gson = new Gson();
            String json = gson.toJson(myObject);
            e.putString(Integer.toString(i), json);
        }
        e.commit();
    }
}
