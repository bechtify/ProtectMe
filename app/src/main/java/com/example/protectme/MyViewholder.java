package com.example.protectme;

import android.content.Context;
import android.content.SharedPreferences;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import static android.content.Context.MODE_PRIVATE;

public class MyViewholder extends RecyclerView.ViewHolder implements View.OnClickListener {

    TextView tvUsername;
    TextView tvRelationship;
    TextView tvDisplayName;
    TextView tvPhone;
    TextView tvAddress;
    CheckBox checkBox;

    public MyViewholder(@NonNull View itemView) {
        super(itemView);
        tvUsername=itemView.findViewById(R.id.tvUsername);
        tvRelationship=itemView.findViewById(R.id.tvRelationship);
        tvDisplayName=itemView.findViewById(R.id.tvDisplayName);
        tvPhone=itemView.findViewById(R.id.tvPhone);
        tvAddress=itemView.findViewById(R.id.tvAddress);
        checkBox=itemView.findViewById(R.id.checkBox);
        checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                                       @Override
                                       public void onCheckedChanged(CompoundButton buttonView,boolean isChecked) {
                                           if(checkBox.isChecked()==true){

                                           }else {

                                           }
                                       }
                                   }
        );
        itemView.setOnClickListener(this);
    }



    @Override
    public void onClick(View v) {

        System.out.println("Clicked"+getAdapterPosition());
    }

    public boolean isChecked(){
        return checkBox.isChecked();
    }
}

