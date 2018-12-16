package com.pm.historyjki;

import com.pm.historyjki.db.StoriesDatabase;

import android.app.Application;
import android.arch.persistence.room.Room;

public class App extends Application {

    private static final String DB_NAME = "StoriesDatabase";

    private StoriesDatabase storiesDatabase;

    private static App instance;

    public static App get() {
        return instance;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        storiesDatabase = Room.databaseBuilder(getApplicationContext(), StoriesDatabase.class, DB_NAME)
                .allowMainThreadQueries()
                .build();
        setInstance(this);
    }

    private static void setInstance(App app) {
        instance = app;
    }

    public StoriesDatabase getDb() {
        return storiesDatabase;
    }
}
