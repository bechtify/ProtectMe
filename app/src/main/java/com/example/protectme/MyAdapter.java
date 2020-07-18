package com.example.protectme;

import android.content.SharedPreferences;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;


public class MyAdapter extends RecyclerView.Adapter<MyViewholder> {

        ArrayList<EmergencyContact> mData;
    SparseBooleanArray itemStateArray= new SparseBooleanArray();
    public MyAdapter(ArrayList<EmergencyContact> aData)
    {
        mData=aData;
    }

    SharedPreferences prefs;
    SharedPreferences.Editor e;

    private List<MyViewholder> viewHolder = new ArrayList<MyViewholder>();

    @NonNull
    @Override
    public MyViewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater=LayoutInflater.from(parent.getContext());
        View neueView=inflater.inflate(R.layout.emergency_contact,parent,false);
        MyViewholder viewholder=new MyViewholder(neueView);
        this.viewHolder.add(viewholder);
        return viewholder;
    }

    public void deleteChecked(){
        ArrayList<Integer> toDelete = new ArrayList<Integer>();
        for(MyViewholder mvw : viewHolder){ //marks every entry which has to be deleted
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

    public void onClick(){

    }
}

