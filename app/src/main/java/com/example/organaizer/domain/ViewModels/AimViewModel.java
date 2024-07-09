package com.example.organaizer.domain.ViewModels;

import androidx.lifecycle.ViewModel;

import com.example.organaizer.App;
import com.example.organaizer.data.db.classes.Aim;
import com.example.organaizer.data.db.classes.AppDatabase;
import com.example.organaizer.data.db.classes.User;
import com.example.organaizer.data.db.interfaces.AimDao;

import java.util.List;

public class AimViewModel extends ViewModel {
    AppDatabase db;
    AimDao aimDao;
    public AimViewModel(){
        db = App.getInstance().getDatabase();
        aimDao = db.aimDao();
    }
    public List<Aim> getAims(User user){
        return aimDao.getAims(user.getId());
    }
    public Aim getAim(User user, int id){
        return aimDao.getAim(user.getId(), id);
    }
    public void addAim(Aim a){
        aimDao.addAim(a);
    }
    public void deleteAim(int id){
        aimDao.deleteAim(id);
    }
    public void updateAim(Aim a){
        aimDao.updateAim(a.getId(), a.getText(), a.getDescription(), a.getProgress());
    }
}
