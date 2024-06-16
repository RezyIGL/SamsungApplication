package com.rezyjs.samsungapplication;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class MyViewHolder extends RecyclerView.ViewHolder {

    TextView operation, money;

    public MyViewHolder(@NonNull View itemView) {
        super(itemView);
        operation = itemView.findViewById(R.id.operation);
        money = itemView.findViewById(R.id.money);
    }
}
