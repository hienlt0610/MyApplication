package com.example.myapplication;

import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.amulyakhare.textdrawable.TextDrawable;

import java.util.ArrayList;
import java.util.List;

public class ImageAdapter extends RecyclerView.Adapter<ImageAdapter.ViewHolder> {
    private LayoutInflater layoutInflater;
    private List<ImageItem> imageItems;

    public ImageAdapter(Context context) {
        layoutInflater = LayoutInflater.from(context);
        imageItems = new ArrayList<>();
    }

    public void setSectionItemList(List<ImageItem> imageItems, boolean hasUpdate) {
        this.imageItems = imageItems;
        if (hasUpdate) {
            notifyDataSetChanged();
        }
    }

    @Override
    public ImageAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(layoutInflater.inflate(R.layout.item_image, parent, false));
    }

    @Override
    public void onBindViewHolder(ImageAdapter.ViewHolder holder, int position) {

        TextDrawable drawable = TextDrawable.builder()
                .beginConfig()
                .endConfig()
                .buildRect(String.valueOf(position), Color.RED);
        holder.imgItem.setImageDrawable(drawable);
    }

    @Override
    public int getItemCount() {
        return imageItems.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView imgItem;

        public ViewHolder(View itemView) {
            super(itemView);
            imgItem = itemView.findViewById(R.id.img_item);
        }
    }
}
