package com.example.myapplication;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class AddDeleteItemActivity extends AppCompatActivity implements View.OnClickListener, AddItemAdapter.OnItemListListener {
    private AddItemAdapter addItemAdapter;
    private Databasehelper databasehelper;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_addedit);
        databasehelper = new Databasehelper(this);
        Button btnAdd = findViewById(R.id.btnAdd);
        btnAdd.setOnClickListener(this);

        RecyclerView recyclerView = findViewById(R.id.recyclerview);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        addItemAdapter = new AddItemAdapter(databasehelper.getListNote());
        addItemAdapter.setOnItemListListener(this);
        recyclerView.setAdapter(addItemAdapter);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnAdd:
                NoteItem noteItem = addItemAdapter.newITem();
                long _id = databasehelper.addNote(noteItem);
                noteItem.setId((int) _id);
                Toast.makeText(this, "Add item success!!!", Toast.LENGTH_SHORT).show();
                break;
        }
    }

    @Override
    public void onItemDelete(int position) {
        NoteItem noteItem = addItemAdapter.remove(position);
        databasehelper.deleteItem(noteItem.getId());
        Toast.makeText(this, "Delete item success!!!", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onItemUpdate(NoteItem noteItem, int position) {
        databasehelper.updateNote(noteItem);
        addItemAdapter.notifyItemChanged(position);
        Toast.makeText(this, "Update item success!!!", Toast.LENGTH_SHORT).show();

    }
}
