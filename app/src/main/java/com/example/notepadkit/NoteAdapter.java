package com.example.notepadkit;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class NoteAdapter extends RecyclerView.Adapter<NoteAdapter.NoteViewHolder> {

    private LayoutInflater mInflater;
    private ArrayList<Record> record;
    private Context context;
    private NoteClickListener noteClickListener;

    NoteAdapter(Context context, ArrayList<Record> record, NoteClickListener noteClickListener) {
        mInflater = LayoutInflater.from(context);
        this.context = context;
        this.record = record;
        this.noteClickListener = noteClickListener;
    }

    @NonNull
    @Override
    public NoteAdapter.NoteViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new NoteViewHolder(mInflater.inflate(R.layout.list_item, parent, false), noteClickListener);
    }

    @Override
    public void onBindViewHolder(@NonNull NoteAdapter.NoteViewHolder holder, int position) {
        holder.setData(record.get(position)); // set data for each holder
    }

    @Override
    public int getItemCount() {
        return record.size(); // must know how many note in the arraylist
    }

    public interface NoteClickListener {
        void recordClicked(int position, int choice);
    }

    public class NoteViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private final TextView listTitle;
        private final TextView listContent;
        private final TextView listDate;
        private final View itemView;
        private final Button share;
        private final Button delete;
        private NoteClickListener noteClickListener;

        public NoteViewHolder(@NonNull View itemView, NoteClickListener noteClickListener) {
            super(itemView);
            this.itemView = itemView;
            this.noteClickListener = noteClickListener;
            itemView.setOnClickListener(this);
            listTitle = itemView.findViewById(R.id.list_title);
            listContent = itemView.findViewById(R.id.list_content);
            listDate = itemView.findViewById(R.id.list_date);
            share = itemView.findViewById(R.id.share);
            delete = itemView.findViewById(R.id.delete);
            share.setOnClickListener(this);
            delete.setOnClickListener(this);
        }

        //Set data for corresponding holder
        public void setData(Record record) {
            listTitle.setText(record.getTitle());
            listContent.setText(record.getContent());
            listDate.setText(record.getDate().substring(0, 12));
        }

        //When the item in recycler view is clicked
        @Override
        public void onClick(View view) {
            int choice;

            if (view.getId() == R.id.share) {
                choice = 1;
                noteClickListener.recordClicked(getBindingAdapterPosition(), choice);
            } else if (view.getId() == R.id.delete) {
                choice = 2;
                noteClickListener.recordClicked(getBindingAdapterPosition(), choice);
            } else {
                choice = 3;
                noteClickListener.recordClicked(getBindingAdapterPosition(), choice);
            }

        }
    }

}
