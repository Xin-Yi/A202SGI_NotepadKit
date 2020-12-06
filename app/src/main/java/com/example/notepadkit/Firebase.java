package com.example.notepadkit;

import com.google.firebase.database.FirebaseDatabase;

//Store data locally on device while offline, and will update the database while online
public class Firebase extends android.app.Application {

    @Override
    public void onCreate() {
        super.onCreate();
        FirebaseDatabase.getInstance().setPersistenceEnabled(true);
        FirebaseDatabase.getInstance().getReference("Record").keepSynced(true);
    }

}
