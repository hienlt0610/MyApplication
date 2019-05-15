package com.example.myapplication;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.util.List;

public class AddItemAdapter extends RecyclerView.Adapter<AddItemAdapter.ViewHolder> {
    private List<NoteItem> noteItems;
    private OnItemListListener onItemListListener;

    public AddItemAdapter(List<NoteItem> noteItems) {
        this.noteItems = noteItems;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        return new ViewHolder(LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_add, viewGroup, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int position) {
        NoteItem noteItem = noteItems.get(position);
        viewHolder.bindITem(noteItem);
    }

    public NoteItem newITem() {
        NoteItem noteItem = new NoteItem();
        this.noteItems.add(noteItem);
        notifyItemInserted(getItemCount());
        return noteItem;
    }

    @Override
    public int getItemCount() {
        return noteItems.size();
    }

    public void setOnItemListListener(OnItemListListener onItemListListener) {
        this.onItemListListener = onItemListListener;
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        Button btnDelete, btnEdit;
        EditText edtDescription;
        TextView tvId;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            btnDelete = itemView.findViewById(R.id.btn_delete);
            btnEdit = itemView.findViewById(R.id.btn_edit);
            edtDescription = itemView.findViewById(R.id.edt_description);
            tvId = itemView.findViewById(R.id.tv_id);
            btnDelete.setOnClickListener(this);
            btnEdit.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.btn_delete:
                    if (onItemListListener != null) {
                        onItemListListener.onItemDelete(getAdapterPosition());
                    }
                    break;
                case R.id.btn_edit:
                    NoteItem noteItem = noteItems.get(getAdapterPosition());
                    noteItem.setDescription(edtDescription.getText().toString().trim());
                    if (onItemListListener != null) {
                        onItemListListener.onItemUpdate(noteItem, getAdapterPosition());
                    }
                    break;
            }
        }

        public void bindITem(NoteItem noteItem) {
            tvId.setText(String.valueOf(noteItem.getId()));
            edtDescription.setText(noteItem.getDescription());
        }
    }

    public NoteItem remove(int pos) {
        NoteItem remove = noteItems.remove(pos);
        notifyItemRemoved(pos);
        return remove;
    }

    interface OnItemListListener {
        void onItemDelete(int position);

        void onItemUpdate(NoteItem noteItem, int position);
    }
}
