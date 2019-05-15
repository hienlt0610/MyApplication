package com.example.myapplication;

import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ImageSectionAdapter extends RecyclerView.Adapter<ImageSectionAdapter.ViewHolder> {
    private static final String TAG = ImageSectionAdapter.class.getSimpleName();
    private LayoutInflater layoutInflater;
    private List<SectionItem> sectionItemList;
    private Map<Integer, Integer> mapScrollPos;
    private LinearLayoutManager parentLayoutManager;
    private int scrollX;
    private RecyclerView masterView = null;


    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
        this.parentLayoutManager = (LinearLayoutManager) recyclerView.getLayoutManager();
    }

    public ImageSectionAdapter(Context context) {
        layoutInflater = LayoutInflater.from(context);
        sectionItemList = new ArrayList<>();
        mapScrollPos = new HashMap<>();
    }

    public void setSectionItemList(List<SectionItem> sectionItemList, boolean hasUpdate) {
        this.sectionItemList = sectionItemList;
        if (hasUpdate) {
            notifyDataSetChanged();
        }
    }

    @Override
    public ImageSectionAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(layoutInflater.inflate(R.layout.item_section, parent, false));
    }

    @Override
    public void onBindViewHolder(ImageSectionAdapter.ViewHolder holder, int position) {
        SectionItem sectionItem = sectionItemList.get(position);
        holder.imageAdapter.setSectionItemList(sectionItem.getImageItems(), true);
        holder.recyclerView.removeOnScrollListener(onScrollListener);
        holder.recyclerView.scrollToPosition(0);
        holder.recyclerView.scrollBy(scrollX, 0);

        holder.recyclerView.addOnScrollListener(onScrollListener);
    }

    @Override
    public int getItemCount() {
        return sectionItemList.size();
    }

    @Override
    public void onViewRecycled(ViewHolder holder) {
        super.onViewRecycled(holder);
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        RecyclerView recyclerView;
        ImageAdapter imageAdapter;

        public ViewHolder(View itemView) {
            super(itemView);
            recyclerView = itemView.findViewById(R.id.rv_section);
            recyclerView.setLayoutManager(new LinearLayoutManager(itemView.getContext(), LinearLayoutManager.HORIZONTAL, false));
            imageAdapter = new ImageAdapter(itemView.getContext());
            recyclerView.setAdapter(imageAdapter);
            recyclerView.addOnScrollListener(onScrollListener);
        }
    }

    RecyclerView.OnScrollListener onScrollListener = new RecyclerView.OnScrollListener() {
        @Override
        public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
            super.onScrollStateChanged(recyclerView, newState);
            if (newState == RecyclerView.SCROLL_STATE_DRAGGING && masterView == null) {
                masterView = recyclerView;
            } else if (newState == RecyclerView.SCROLL_STATE_IDLE) {
                masterView = null;
            }
        }

        @Override
        public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
            super.onScrolled(recyclerView, dx, dy);
            if (masterView != null) {
                scrollX = masterView.computeHorizontalScrollOffset();
            }
            for (int i = 0; i < parentLayoutManager.getChildCount(); i++) {
                View childAt = parentLayoutManager.getChildAt(i);
                if (childAt != null) {
                    RecyclerView horizontalRcv = childAt.findViewById(R.id.rv_section);
                    if (horizontalRcv != null && horizontalRcv != recyclerView) {
                        horizontalRcv.removeOnScrollListener(this);
                        horizontalRcv.scrollBy(dx, 0);
                        horizontalRcv.addOnScrollListener(this);
                    }
                }

            }
        }
    };
}
