package com.example.organaizer.data.db.classes;

import androidx.room.Entity;
import androidx.room.PrimaryKey;
@Entity
public class User {
    @PrimaryKey(autoGenerate = true)
    private int id;
    private String username;
    private String password;
    private static User instance;

    public User(String username, String password)
    {
        this.username = username;
        this.password = password;
    }

    public static User getInstance(String username, String password) {
        if (instance == null) {
            synchronized (User.class) {
                if (instance == null) {
                    instance = new User(username, password);
                }
            }
        }
        return instance;
    }

    public int getId() {
        return id;
    }
    public void setId(int id) {
        this.id = id;
    }
    public String getUsername() {
        return username;
    }
    public void setUsername(String username) {
        this.username = username;
    }
    public String getPassword() {
        return password;
    }
    public void setPassword(String password) {
        this.password = password;
    }
}
