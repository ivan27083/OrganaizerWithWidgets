package com.example.organaizer.ui.fragments;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import android.util.Log;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.example.organaizer.App;
import com.example.organaizer.R;
import com.example.organaizer.data.db.classes.Aim;
import com.example.organaizer.data.db.classes.Task;
import com.example.organaizer.domain.Adapter.AimAdapter;
import com.example.organaizer.domain.Adapter.TaskAdapter;
import com.example.organaizer.domain.ViewModels.AimViewModel;
import com.example.organaizer.domain.ViewModels.TaskViewModel;
import com.example.organaizer.ui.activities.AddAimActivity;
import com.example.organaizer.ui.activities.AddTaskActivity;
import com.example.organaizer.ui.activities.AimActivity;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;

public class PurposesFragment extends Fragment {
    private List<Aim> aimsList;
    private ListView listView;
    private AimViewModel viewModel;
    private FloatingActionButton addButton;
    private TaskViewModel taskViewModel;
    private AimAdapter adapter;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_purposes, container, false);

        viewModel = new ViewModelProvider(this).get(AimViewModel.class);
        taskViewModel = new ViewModelProvider(this).get(TaskViewModel.class);
        listView = (ListView) view.findViewById(R.id.PurposesList);
        addButton = (FloatingActionButton) view.findViewById(R.id.floatingActionButton);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                try {
                    Intent intent = new Intent(getContext(), AimActivity.class);
                    intent.putExtra(Aim.class.getSimpleName(), aimsList.get(i));
                    startActivity(intent);
                }
                catch (Exception e) {
                    Log.e("PurposesFragment", "Error starting AimActivity", e);
                }
            }
        });
        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity().getBaseContext(), AddAimActivity.class);
                startActivity(intent);
            }
        });
        return view;
    }
    @Override
    public void onCreateContextMenu(ContextMenu menu, View v,
                                    ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        MenuInflater inflater = getActivity().getMenuInflater();
        inflater.inflate(R.menu.context_menu, menu);
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
        if (item.getItemId() == R.id.edit){
            Intent intent = new Intent(getContext(), AddAimActivity.class);
            intent.putExtra(Aim.class.getSimpleName(), aimsList.get(info.position));
            startActivity(intent);
            return true;
        }
        else if (item.getItemId() == R.id.delete){
            viewModel.deleteAim(aimsList.get(info.position).getId());
            aimsList = viewModel.getAims(App.getUser());

            adapter = new AimAdapter(getContext(), R.layout.purpose_item, aimsList);
            listView.setAdapter(adapter);
            registerForContextMenu(listView);
            return true;
        }
        else return super.onContextItemSelected(item);
    }

    @Override
    public void onStart() {
        super.onStart();
        aimsList = viewModel.getAims(App.getUser());

        adapter = new AimAdapter(this.getContext(), R.layout.purpose_item, aimsList);
        listView.setAdapter(adapter);
        registerForContextMenu(listView);

        for (int j = 0; j < aimsList.size(); j++) {
            List<Task> taskList = taskViewModel.getTasks(App.getUser());
            List<Task> tasksForAim = new ArrayList<Task>();
            double counter = 0;
            for (int i = 0; i < taskList.size(); i++) {
                if (taskList.get(i).getAimId() == aimsList.get(j).getId()) {
                    tasksForAim.add(taskList.get(i));
                    if (taskList.get(i).getCompleted()) counter++;
                }
            }
            aimsList.get(j).setProgress((int) (counter / tasksForAim.size() * 100));
            viewModel.updateAim(aimsList.get(j));
        }
    }
}