package com.example.organaizer.data.db.interfaces;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;


import com.example.organaizer.data.db.classes.Task;

import java.sql.Timestamp;
import java.util.List;
@Dao
public interface TaskDao {
    @Query("SELECT t.* FROM Task t JOIN User u ON u.id = t.UserId WHERE u.id = :uid")
    List<Task> getTasks(int uid);

    @Query("SELECT t.* FROM Task t JOIN User u ON u.id = t.UserId WHERE u.id = :uid AND t.id = :id")
    Task getTask(int uid, int id);

    @Query("UPDATE Task SET Text = :text, Description = :description, Completed = :completed, Date = :date WHERE Id = :id")
    void updateTask(int id, String text, String description, boolean completed, String date);

    @Insert
    void addTask(Task task);

    @Query("DELETE FROM Task WHERE Id = :id")
    void deleteTask(int id);
}
