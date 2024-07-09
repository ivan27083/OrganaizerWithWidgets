package com.example.organaizer;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelStore;
import androidx.lifecycle.ViewModelStoreOwner;

import com.example.organaizer.data.db.classes.Task;
import com.example.organaizer.domain.ViewModels.AimViewModel;
import com.example.organaizer.domain.ViewModels.TaskViewModel;

import java.util.List;

public class MyRepository{
    private TaskViewModel viewModel;
    List<Task> tasks;
    int counter=0;
    public MyRepository(TaskViewModel viewModel) {
        this.viewModel = viewModel;
        counter=0;
        //viewModel.setData(App.getUser());
    }

    public void setData(List<Task> data) {
        if (data!=null && tasks==null)
        {
            if (!data.isEmpty()) {
                tasks = data;
            }
        }
    }
    public List<Task> getData() {
        if (counter==0) {viewModel.setData(App.getUser());}
        counter+=1;
        return viewModel.getData(App.getUser());
    }
    public List<Task> getList() {
        return tasks;
    }
}