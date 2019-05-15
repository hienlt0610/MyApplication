package com.example.myapplication;

import java.util.ArrayList;
import java.util.List;

public class SectionItem {
    private List<ImageItem> imageItems = new ArrayList<>();

    public List<ImageItem> getImageItems() {
        return imageItems;
    }

    public void setImageItems(List<ImageItem> imageItems) {
        this.imageItems = imageItems;
    }
}
