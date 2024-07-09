package com.example.organaizer.data.db.classes;

import androidx.room.Database;
import androidx.room.RoomDatabase;

import com.example.organaizer.data.db.interfaces.AimDao;
import com.example.organaizer.data.db.interfaces.TaskDao;
import com.example.organaizer.data.db.interfaces.UserDao;

@Database(entities = {User.class, Task.class, Aim.class}, version = 1, exportSchema = false)
public abstract class AppDatabase extends RoomDatabase {
    public abstract UserDao userDao();
    public abstract TaskDao taskDao();
    public abstract AimDao aimDao();
}
