package com.example.notepadkit;

import androidx.annotation.NonNull;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class Record {

    public static ArrayList<Record> record = new ArrayList<>();
    private static DatabaseReference ref;
    private String id;
    private String title, content, date;

    public Record(String id, String title, String content, String date) {
        this.id = id;
        this.title = title;
        this.content = content;
        this.date = date;
    }

    public Record() {
    }

    public static ArrayList<Record> getRecord() {
        return record;
    }

    public static void setRecord(ArrayList<Record> record) {
        Record.record = record;
    }

    //Search the selected note by ID
    public static Record searchRecordById(String id) {
        for (Record r : record) {
            if (r.getId().equals(id))
                return r;
        }
        return null;
    }

    //Load the data for recycler view
    public static void loadRecord(final NoteAdapter na, final ArrayList<Record> re) {
        record.clear();
        ref = FirebaseDatabase.getInstance().getReference("Record");

        ref.orderByKey().addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                //Get all records, and set the unique key as ID
                record.clear();
                for (DataSnapshot dsp : dataSnapshot.getChildren()) {
                    Record r = dsp.getValue(Record.class);
                    r.setId(dsp.getKey());

                    record.add(r);
                }

                for (Record records : record) {
                    re.add(records);
                }

                if (na != null)
                    na.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }
}
