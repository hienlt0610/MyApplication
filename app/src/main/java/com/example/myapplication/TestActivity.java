package com.example.myapplication;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class TestActivity extends AppCompatActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);
        RecyclerView rv = findViewById(R.id.recyclerview);
        rv.setLayoutManager(new LinearLayoutManager(this));
        ImageSectionAdapter adapter = new ImageSectionAdapter(this);
        adapter.setSectionItemList(generateSectionList(), false);
        rv.setAdapter(adapter);
    }

    private List<SectionItem> generateSectionList() {
        List<SectionItem> sectionItems = new ArrayList<>();
        for (int i = 0; i < 15; i++) {
            SectionItem sectionItem = new SectionItem();
            sectionItem.setImageItems(generateImageItemList());
            sectionItems.add(sectionItem);
        }
        return sectionItems;
    }

    private List<ImageItem> generateImageItemList() {
        List<ImageItem> imageItems = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            ImageItem imageItem = new ImageItem();
            imageItem.setPath("http://thuthuat123.com/uploads/2018/01/27/avatar-dep-nhat-83_112148.jpg");
            imageItems.add(imageItem);
        }
        return imageItems;
    }
}
