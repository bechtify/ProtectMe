package com.example.protectme;

import android.view.View;

public class EmergencyContact {
    String mUsername;
    String mRelationship;
    String mDisplayName;
    String mPhone;
    String mAddress;

    public EmergencyContact(String mUsername, String mRelationship, String mDisplayName, String mPhone, String mAddress) {
        this.mUsername = mUsername;
        this.mRelationship = mRelationship;
        this.mDisplayName = mDisplayName;
        this.mPhone = mPhone;
        this.mAddress = mAddress;
    }

    public void onContact(View view){

    }

}