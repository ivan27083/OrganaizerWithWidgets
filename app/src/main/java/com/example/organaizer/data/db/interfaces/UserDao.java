package com.example.organaizer.data.db.interfaces;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import com.example.organaizer.data.db.classes.User;

@Dao
public interface UserDao {
    @Query("SELECT * FROM User WHERE username = :username AND password = :password")
    User authenticateUser(String username, String password);
    @Insert
    void registerUser(User... user);

}
