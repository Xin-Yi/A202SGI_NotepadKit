package com.example.notepadkit;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements NoteAdapter.NoteClickListener {

    public final static String EXTRA_ID =
            "com.example.notepadkit.id";

    private RecyclerView mRecyclerView;
    private NoteAdapter mAdapter;
    private ArrayList<Record> records;
    private DatabaseReference ref;
    private String content;

    private Record record;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        //Create new note
        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent create = new Intent(view.getContext(), DetailActivity.class);
                startActivity(create);
            }
        });

        //Recycler View
        records = new ArrayList<>();
        Record.record.clear();
        mAdapter = new NoteAdapter(this, records, this);
        mRecyclerView = (RecyclerView) findViewById(R.id.rvRecord);
        mRecyclerView.setAdapter(mAdapter);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this, RecyclerView.VERTICAL, false));
    }

    @Override
    protected void onStart() {
        super.onStart();
        records.clear(); //Clear the array list before adding new data in it
        Record.loadRecord(mAdapter, records); //Load data for recycler view
    }

    //When recycler view item is clicked
    @Override
    public void recordClicked(int position, int choice) {
        String id = records.get(position).getId(); //ID of item selected

        //Share note
        if (choice == 1) {
            record = Record.searchRecordById(id);

            if (record == null)
                finish();

            content = "TITLE:  " + record.getTitle() + "\n\nDESCRIPTION: \n" + record.getContent() +
                    "\n\n" + "Written On: " + record.getDate();

            Intent share = new Intent(Intent.ACTION_SEND);
            share.setType("text/plain"); //Sets the content of the email to plain text
            share.putExtra(Intent.EXTRA_TEXT, content);

            if (share.resolveActivity(getPackageManager()) != null) {
                startActivity(share);
            } else {
                Log.d("ImplicitIntents", "Can't handle this intent!");
            }

        }
        //Delete note
        else if (choice == 2) {
            record = Record.searchRecordById(id);

            if (record == null)
                finish();

            //Display dialog for confirmation to delete
            final AlertDialog.Builder alert = new AlertDialog.Builder(this);
            alert.setTitle("Confirmation");
            alert.setMessage("Are you sure you want to delete ? ");
            alert.setPositiveButton("Yes", new DialogInterface.OnClickListener() {      //Positive btn is for yes/ok,... , Negative btn is for no/...
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    //Delete record from database
                    ref = FirebaseDatabase.getInstance().getReference("Record").child(record.getId());
                    ref.removeValue();
                    records.clear();
                    Record.loadRecord(mAdapter, records); //Rerun the recycler view
                    Toast.makeText(MainActivity.this, "The note is deleted!", Toast.LENGTH_SHORT).show();
                }
            });
            alert.setNegativeButton("No", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    Toast.makeText(MainActivity.this, "The note is remained", Toast.LENGTH_SHORT).show();
                }
            });
            alert.show();
        }
        //Edit note
        else {
            Intent edit = new Intent(this, EditDetail.class);
            edit.putExtra(EXTRA_ID, id);
            startActivity(edit);
        }
    }
}
