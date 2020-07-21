package com.example.protectme;

import android.view.View;

public class EmergencyContact {
    String contact_id;
    String username;
    String display_name;
    String phone;
    String address;
    String relationship;

    public EmergencyContact(String contact_id, String username, String display_name, String phone, String address, String relationship) {
        this.contact_id = contact_id;
        this.username = username;
        this.display_name = display_name;
        this.phone = phone;
        this.address = address;
        this.relationship = relationship;
    }

    public void onContact(View view){

    }

}