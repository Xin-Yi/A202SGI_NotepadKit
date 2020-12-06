package com.example.notepadkit;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.SimpleDateFormat;
import java.util.Date;

public class EditDetail extends AppCompatActivity {

    private EditText mTitle, mContent;
    private TextView mDate;
    private DatabaseReference ref;

    private Record record;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        mTitle = findViewById(R.id.title);
        mContent = findViewById(R.id.content);
        mDate = findViewById(R.id.date);

        Intent edit = getIntent();
        String id = edit.getStringExtra(MainActivity.EXTRA_ID);
        record = Record.searchRecordById(id);

        if (record == null) {
            finish();
        }

        mTitle.setText(record.getTitle());
        mContent.setText(record.getContent());
        mDate.setText(record.getDate());

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.detail_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            //Save edited note
            case R.id.menu_save:
                if (validate()) {
                    try {
                        //Update records details
                        record.setTitle(mTitle.getText().toString());
                        record.setContent(mContent.getText().toString());
                        SimpleDateFormat dateFormat = new SimpleDateFormat("MMM dd, yyyy hh:mma"); //Get current edit date
                        String currentDateTime = dateFormat.format(new Date());
                        mDate.setText(currentDateTime);
                        record.setDate(mDate.getText().toString());
                        ref = FirebaseDatabase.getInstance().getReference("Record").child(record.getId());
                        ref.setValue(record);
                        Record.record.clear();
                        finish();

                    } catch (Exception ex) {
                        ex.printStackTrace();
                    }
                }
                break;
            case android.R.id.home: {
                finish(); // close this activity and return to preview activity (if there is any)
            }
        }
        return true;
    }

    //Validate required field
    private boolean validate() {
        boolean isValid = true;

        if (mTitle.getText().toString().equals("")) {
            mTitle.setError("Required");
            isValid = false;
        } else {
            mTitle.setError(null);
        }

        return isValid;
    }
}
