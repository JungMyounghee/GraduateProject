package com.example.myhomefood;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class SettingAdapter extends RecyclerView.Adapter<SettingAdapter.Holder> {
    private ArrayList<SettingItemInfo> items;

    public SettingAdapter(ArrayList<SettingItemInfo> items) {
        this.items = items;
    }

    @NonNull
    @Override
    public Holder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.setting_list_item, parent, false);
        return new Holder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull Holder holder, int position) {
        SettingItemInfo item = items.get(position);
        holder.item.setText(item.getSettingItem());

    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public class Holder extends RecyclerView.ViewHolder {
        private TextView item;

        public Holder(@NonNull View itemView) {
            super(itemView);

            item = itemView.findViewById(R.id.setting_item);
        }
    }
}

class SettingItemInfo {
    private String SettingItem;

    public SettingItemInfo() {}

    public SettingItemInfo(String settingItem) {
        SettingItem = settingItem;
    }

    public String getSettingItem() {
        return SettingItem;
    }

    public void setSettingItem(String settingItem) {
        SettingItem = settingItem;
    }
}
