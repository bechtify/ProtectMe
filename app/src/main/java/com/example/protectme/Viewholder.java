package com.example.protectme;

import android.content.Intent;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;


public class Viewholder extends RecyclerView.ViewHolder implements View.OnClickListener {

    TextView tvUsername;
    TextView tvRelationship;
    TextView tvDisplayName;
    TextView tvPhone;
    TextView tvAddress;
    CheckBox checkBox;

    public Viewholder(@NonNull View itemView) {
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
        Intent myIntent = new Intent(v.getContext(), EditContactActivity.class);
        myIntent.putExtra("contactNumber", getAdapterPosition());//get number of item which has been clicked
        v.getContext().startActivity(myIntent);
    }

    public boolean isChecked(){
        return checkBox.isChecked();
    }
}

