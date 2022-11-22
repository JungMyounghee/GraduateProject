package com.example.myhomefood;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class ProcessAdapter extends RecyclerView.Adapter<ProcessAdapter.Holder> {
    private ArrayList<ProcessInfo> items;

    public ProcessAdapter(ArrayList<ProcessInfo> items) {
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
        ProcessInfo item = items.get(position);
        holder.item.setText(item.getProcess());

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

class ProcessInfo {
    private String process;

    public ProcessInfo() {}
    public ProcessInfo(String process) {
        this.process = process;
    }

    public String getProcess() {
        return process;
    }

    public void setProcess(String process) {
        this.process = process;
    }
}
