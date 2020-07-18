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


public class FirstAidAdapter extends RecyclerView.Adapter<FirstAidViewholder> {

    ArrayList<FirstAidText> mData;
    SparseBooleanArray itemStateArray= new SparseBooleanArray();
    public FirstAidAdapter(ArrayList<FirstAidText> aData)
    {
        mData=aData;
    }

    SharedPreferences prefs;
    SharedPreferences.Editor e;

    private List<FirstAidViewholder> viewHolder = new ArrayList<FirstAidViewholder>();

    @NonNull
    @Override
    public FirstAidViewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater=LayoutInflater.from(parent.getContext());
        View neueView=inflater.inflate(R.layout.first_aid_text,parent,false);
        FirstAidViewholder viewholder=new FirstAidViewholder(neueView);
        this.viewHolder.add(viewholder);
        return viewholder;
    }



    @Override
    public void onBindViewHolder(@NonNull FirstAidViewholder holder, int position) {
        FirstAidText text=mData.get(position);

        holder.tvHeading.setText(text.mHeading);
        holder.tvText.setText(text.mText);
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    public void onClick(){

    }
}

