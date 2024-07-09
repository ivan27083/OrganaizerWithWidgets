package com.example.organaizer.data.db.classes;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.io.Serializable;

@Entity
public class Aim implements Serializable {
    @PrimaryKey(autoGenerate = true)
    private int Id;
    private String Text;
    private String Description;
    private int Progress;
    private int UserId;
    public Aim(){
        Text = "";
        Description = "";
        Progress = 0;
    }
    public Aim(String Text, String Description){
        this.Text = Text;
        this.Description = Description;
        Progress = 0;
    }

    public int getId() {return Id;}
    public void setId(int id) {Id = id;}

    public String getText() {return Text;}
    public void setText(String text) {Text = text;}

    public String getDescription() {return Description;}
    public void setDescription(String description) {Description = description;}

    public int getProgress() {return Progress;}
    public void setProgress(int progress) {
        Progress = progress;}

    public int getUserId() {return UserId;}
    public void setUserId(int userId) {UserId = userId;}
}
