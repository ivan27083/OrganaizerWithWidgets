package com.example.organaizer.data.db.classes;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.io.Serializable;
import java.sql.Date;
import java.sql.Timestamp;
@Entity
public class Task implements Serializable {
    @PrimaryKey(autoGenerate = true)
    private int Id;
    private String Text;
    private String Description;
    private Boolean Completed;
    private String Date;
    private int AimId;
    private int UserId;
    public Task(){
        Text = "";
        Description = "";
        Completed = false;
        Date = null;
    }
    public Task(String Text, String Description){
        this.Text = Text;
        this.Description = Description;
        Completed = false;
        Date = null;
    }
    public Task(String Text, String Description, Date Date){
        this.Text = Text;
        this.Description = Description;
        Completed = false;
        this.Date = Date.toString();
    }

    public int getId() {return Id;}
    public void setId(int id) {Id = id;}

    public String getText() {return Text;}
    public void setText(String text) {Text = text;}

    public String getDescription() {return Description;}
    public void setDescription(String description) {Description = description;}

    public Boolean getCompleted() {return Completed;}
    public void setCompleted(Boolean completed) {Completed = completed;}

    public String getDate() {return Date;}
    public void setDate(String date) {Date = date;}

    public int getUserId() {return UserId;}
    public void setUserId(int userId) {UserId = userId;}

    public int getAimId() {return AimId;}
    public void setAimId(int aimId) {AimId = aimId;}
}
