package com.example.myhomefood;

import android.media.Image;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class likeItemAdapter extends RecyclerView.Adapter<likeItemAdapter.Holder> {
    private ArrayList<LikeInfo> likeItems;

    public likeItemAdapter(ArrayList<LikeInfo> likeItems) {
        this.likeItems = likeItems;
    }

    public interface OnItemClickEventListener {
        void onItemClick(View a_view, int a_position);
        void onImageClick(View a_view, int img_position);
    }

    private OnItemClickEventListener likeItemClickListener;

    public void setOnItemClickListener(OnItemClickEventListener a_listener) {
        likeItemClickListener = a_listener;
    }

    @NonNull
    @Override
    public Holder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.like_item, parent, false);
        return new Holder(v, likeItemClickListener);
    }

    @Override
    public void onBindViewHolder(@NonNull Holder holder, int position) {
        LikeInfo item = likeItems.get(position);
        holder.textItem.setText(item.getLikeItem());
    }

    @Override
    public int getItemCount() {
        return likeItems.size();
    }

    public class Holder extends RecyclerView.ViewHolder {
        public TextView textItem;
        public ImageView xImg;

        public Holder(@NonNull View itemView, final OnItemClickEventListener likeItemClickListener) {
            super(itemView);

            textItem = itemView.findViewById(R.id.like_item);
            xImg = itemView.findViewById(R.id.delete_button);

            //click listener
            textItem.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    final int position = getAbsoluteAdapterPosition();
                    if(position != RecyclerView.NO_POSITION) {
                        likeItemClickListener.onItemClick(v, position);
                    }
                }
            });

            //click listener
            xImg.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    final int position = getAbsoluteAdapterPosition();
                    if(position != RecyclerView.NO_POSITION) {
                        likeItemClickListener.onImageClick(v, position);
                    }
                }
            });


        }
    }
}

class LikeInfo {
    private String likeItem;

    public LikeInfo() {}
    public LikeInfo(String likeItem) {
        this.likeItem = likeItem;
    }

    public String getLikeItem() {
        return likeItem;
    }

    public void setLikeItem(String likeItem) {
        this.likeItem = likeItem;
    }
}
