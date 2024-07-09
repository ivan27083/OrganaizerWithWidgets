package com.example.organaizer.data.db.interfaces;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import com.example.organaizer.data.db.classes.Aim;

import java.util.List;
@Dao
public interface AimDao {
    @Query("SELECT a.* FROM Aim a JOIN User u ON u.id = a.UserId WHERE u.id = :uid")
    List<Aim> getAims(int uid);

    @Query("SELECT a.* FROM Aim a JOIN User u ON u.id = a.UserId WHERE u.id = :uid AND a.id = :id")
    Aim getAim(int uid, int id);

    @Query("UPDATE Aim SET Text = :text, Description = :description, Progress = :progress WHERE Id = :id")
    void updateAim(int id, String text, String description, int progress);

    @Insert
    void addAim(Aim aim);

    @Query("DELETE FROM Aim WHERE Id = :id")
    void deleteAim(int id);
}
