package com.example.organaizer;

import android.app.Application;

import androidx.room.Room;

import com.example.organaizer.data.db.classes.AppDatabase;
import com.example.organaizer.data.db.classes.User;

public class App extends Application {
    public static App instance;
    public static User user;
    private AppDatabase database;
    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;
        database = Room.databaseBuilder(this, AppDatabase.class, "database")
                .allowMainThreadQueries()
                .build();
    }

    public static User getUser() { return user; }
    public static void setUser(User user) { App.user = user; }

    public static App getInstance() {
        return instance;
    }
    public AppDatabase getDatabase() {
        return database;
    }
}
