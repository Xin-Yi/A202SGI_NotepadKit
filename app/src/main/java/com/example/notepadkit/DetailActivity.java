package com.example.notepadkit;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.SimpleDateFormat;
import java.util.Date;

public class DetailActivity extends AppCompatActivity {

    private EditText mTitle, mContent;
    private TextView mDate;
    private DatabaseReference ref;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        mTitle = findViewById(R.id.title);
        mContent = findViewById(R.id.content);
        mDate = findViewById(R.id.date);

        //Set date format for current date and time
        SimpleDateFormat dateFormat = new SimpleDateFormat("MMM dd, yyyy hh:mma");
        String currentDateTime = dateFormat.format(new Date());
        mDate.setText(currentDateTime);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.detail_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            //Save note
            case R.id.menu_save:
                if (validate()) {
                    try {
                        //Push new note to database
                        String title = mTitle.getText().toString();
                        String content = mContent.getText().toString();
                        String date = mDate.getText().toString();
                        Record record = new Record("", title, content, date);
                        ref = FirebaseDatabase.getInstance().getReference("Record");
                        ref.push().setValue(record);
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

    //Validate for required field
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
