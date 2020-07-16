package com.example.protectme;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;


public class MyAdapter extends RecyclerView.Adapter<MyViewholder> {

    ArrayList<EmergencyContact> mData;
    public MyAdapter(ArrayList<EmergencyContact> aData)
    {
        mData=aData;
    }

    @NonNull
    @Override
    public MyViewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater=LayoutInflater.from(parent.getContext());
        View neueView=inflater.inflate(R.layout.emergency_contact,parent,false);
        MyViewholder viewholder=new MyViewholder(neueView);
        return viewholder;
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewholder holder, int position) {
        EmergencyContact contact=mData.get(position);

        holder.tvAddress.setText(contact.mAddress);
        holder.tvDisplayName.setText(contact.mDisplayName);
        holder.tvPhone.setText(contact.mPhone);
        holder.tvRelationship.setText(contact.mRelationship);
        holder.tvUsername.setText(contact.mUsername);
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }
}

