package com.example.protectme;

import android.content.SharedPreferences;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;


public class EmergencyContactAdapter extends RecyclerView.Adapter<EmergencyContactViewholder> {

        ArrayList<EmergencyContact> mData;
    SparseBooleanArray itemStateArray= new SparseBooleanArray();
    public EmergencyContactAdapter(ArrayList<EmergencyContact> aData)
    {
        mData=aData;
    }

    SharedPreferences prefs;
    SharedPreferences.Editor e;

    private List<EmergencyContactViewholder> viewHolder = new ArrayList<EmergencyContactViewholder>();

    @NonNull
    @Override
    public EmergencyContactViewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater=LayoutInflater.from(parent.getContext());
        View neueView=inflater.inflate(R.layout.emergency_contact,parent,false);
        EmergencyContactViewholder emergencyContactViewholder =new EmergencyContactViewholder(neueView);
        this.viewHolder.add(emergencyContactViewholder);
        return emergencyContactViewholder;
    }

    public void deleteChecked(){
        ArrayList<Integer> toDelete = new ArrayList<Integer>();
        for(EmergencyContactViewholder mvw : viewHolder){ //marks every entry which has to be deleted
            if(mvw.isChecked()){
                toDelete.add(mvw.getAdapterPosition());
            }
            mvw.checkBox.setChecked(false);
        }
        for (int i=toDelete.size()-1; i>=0; i--){ //deletes every marked entry
            int j=toDelete.get(i);
            mData.remove(j);
        }
        this.notifyDataSetChanged();
    }

    @Override
    public void onBindViewHolder(@NonNull EmergencyContactViewholder holder, int position) {
        EmergencyContact contact=mData.get(position);

        holder.tvAddress.setText(contact.address);
        holder.tvDisplayName.setText(contact.display_name);
        holder.tvPhone.setText(contact.phone);
        holder.tvRelationship.setText(contact.relationship);
        holder.tvUsername.setText(contact.username);
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    public void onClick(){

    }
}

