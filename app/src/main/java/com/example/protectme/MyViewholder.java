package com.example.protectme;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class MyViewholder extends RecyclerView.ViewHolder implements View.OnClickListener {

    TextView tvUsername;
    TextView tvRelationship;
    TextView tvDisplayName;
    TextView tvPhone;
    TextView tvAddress;

    public MyViewholder(@NonNull View itemView) {
        super(itemView);
        tvUsername=itemView.findViewById(R.id.tvUsername);
        tvRelationship=itemView.findViewById(R.id.tvRelationship);
        tvDisplayName=itemView.findViewById(R.id.tvDisplayName);
        tvPhone=itemView.findViewById(R.id.tvPhone);
        tvAddress=itemView.findViewById(R.id.tvAddress);
        itemView.setOnClickListener(this);
    }



    @Override
    public void onClick(View v) {

        //System.out.println(((TextView)v.findViewById(R.id.nameTV)).getText());
    }
}

