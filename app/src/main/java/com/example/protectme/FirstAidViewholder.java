package com.example.protectme;

import android.content.Intent;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;


public class FirstAidViewholder extends RecyclerView.ViewHolder {

    TextView tvHeading;
    TextView tvText;


    public FirstAidViewholder(@NonNull View itemView) {
        super(itemView);
        tvHeading=itemView.findViewById(R.id.tvHeading);
        tvText=itemView.findViewById(R.id.tvText);
    }

}

