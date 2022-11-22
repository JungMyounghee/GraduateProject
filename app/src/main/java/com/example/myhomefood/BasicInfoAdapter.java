package com.example.myhomefood;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class BasicInfoAdapter extends RecyclerView.Adapter<BasicInfoAdapter.Holder> {
    private ArrayList<BasicItemInfo> items;

    public BasicInfoAdapter(ArrayList<BasicItemInfo> items) {
        this.items = items;
    }

    @NonNull
    @Override
    public Holder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.progress_item, parent, false);
        return new Holder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull Holder holder, int position) {
        BasicItemInfo item = items.get(position);
        holder.item.setText(item.getBasicItem());

    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public class Holder extends RecyclerView.ViewHolder {
        private TextView item;

        public Holder(@NonNull View itemView) {
            super(itemView);

            item = itemView.findViewById(R.id.item);
        }
    }
}

class BasicItemInfo {
    private String basicItem;

    public BasicItemInfo() {}
    public BasicItemInfo(String basicItem) {
        this.basicItem = basicItem;
    }

    public String getBasicItem() {
        return basicItem;
    }

    public void setBasicItem(String basicItem) {
        this.basicItem = basicItem;
    }
}
