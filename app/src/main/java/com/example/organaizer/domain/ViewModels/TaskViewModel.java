package com.example.organaizer.domain.ViewModels;

import android.content.Context;

import androidx.lifecycle.ViewModel;

import com.example.organaizer.App;
import com.example.organaizer.MyRepository;
import com.example.organaizer.data.db.classes.AppDatabase;
import com.example.organaizer.data.db.classes.Task;
import com.example.organaizer.data.db.classes.User;
import com.example.organaizer.data.db.interfaces.TaskDao;

import java.util.List;

public class TaskViewModel extends ViewModel {
    AppDatabase db;
    TaskDao taskDao;
    private MyRepository repository;
    public TaskViewModel(){
        repository = new MyRepository(this);
        db = App.getInstance().getDatabase();
        taskDao = db.taskDao();
    }
    public List<Task> getTasks(User user){
        return taskDao.getTasks(user.getId());
    }
    public Task getTask(User user, int id){
        return taskDao.getTask(user.getId(), id);
    }
    public void addTask(Task t){
        taskDao.addTask(t);
    }
    public void deleteTask(int id){
        taskDao.deleteTask(id);
    }
    public void updateTask(Task t){
        taskDao.updateTask(t.getId(), t.getText(), t.getDescription(), t.getCompleted(), t.getDate());
    }
    private static TaskViewModel instance;

    public static TaskViewModel getInstance(Context context) {
        if (instance == null) {
            instance = new TaskViewModel();
        }
        return instance;
    }
    public void setData(User user) {
        repository.setData(getTasks(App.getUser()));
    }
    public List<Task> getData(User user) {
        // Retrieve the data from the repository
        return repository.getList();
    }
}
