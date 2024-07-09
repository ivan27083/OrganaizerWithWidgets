package com.example.organaizer.ui.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.lifecycle.ViewModelProvider;

import com.example.organaizer.App;
import com.example.organaizer.R;
import com.example.organaizer.data.db.classes.Aim;
import com.example.organaizer.data.db.classes.Task;
import com.example.organaizer.data.db.classes.User;
import com.example.organaizer.domain.ViewModels.TaskViewModel;
import com.example.organaizer.ui.dialogFragments.CustomDialogFragment;
import com.google.android.material.textfield.TextInputEditText;

import java.sql.Date;

public class AddTaskActivity extends AppCompatActivity {
    private TaskViewModel viewModel;
    private TextInputEditText textInput;
    private TextInputEditText descriptionInput;
    private DatePicker datePicker;
    private Button button;
    private Button backButton;
    private Aim aim;
    private Task task;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_add_task);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        Bundle arguments = getIntent().getExtras();
        if (arguments != null) {
            aim = (Aim) arguments.get(Aim.class.getSimpleName());
            task = (Task) arguments.get(Task.class.getSimpleName());
        }
        viewModel = new ViewModelProvider(this).get(TaskViewModel.class);
        textInput = findViewById(R.id.taskTextInput);
        descriptionInput = findViewById(R.id.taskDescriptionInput);
        button = findViewById(R.id.saveTaskButton);
        backButton = findViewById(R.id.backButton);
        datePicker = findViewById(R.id.datePicker);

        if (task != null){
            textInput.setText(task.getText());
            descriptionInput.setText(task.getDescription());
            String[] date = task.getDate().split("-");
            datePicker.init(Integer.parseInt(date[0]), Integer.parseInt(date[1])-1, Integer.parseInt(date[2]), null);
        }

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(task != null) {
                    if (textInput.getText() != null) {
                        task.setText(textInput.getText().toString());
                        task.setDescription(descriptionInput.getText().toString());
                        task.setDate(new Date(datePicker.getYear()-1900, datePicker.getMonth(), datePicker.getDayOfMonth()).toString());
                        viewModel.updateTask(task);
                        Intent intent = new Intent(getBaseContext(), AimActivity.class);
                        intent.putExtra(Aim.class.getSimpleName(), aim);
                        startActivity(intent);
                    } else {
                        CustomDialogFragment dialog = new CustomDialogFragment("Внимание", "Укажите название задачи");
                        dialog.show(getSupportFragmentManager(), "custom");
                    }
                }
                else {
                    Task task = new Task(textInput.getText().toString(), descriptionInput.getText().toString(), new Date(datePicker.getYear()-1900, datePicker.getMonth(), datePicker.getDayOfMonth()));
                    task.setUserId(App.getUser().getId());
                    task.setAimId(aim.getId());
                    if (textInput.getText() != null) {
                        viewModel.addTask(task);
                        Intent intent = new Intent(getBaseContext(), AimActivity.class);
                        intent.putExtra(Aim.class.getSimpleName(), aim);
                        startActivity(intent);
                    } else {
                        CustomDialogFragment dialog = new CustomDialogFragment("Внимание", "Укажите название задачи");
                        dialog.show(getSupportFragmentManager(), "custom");
                    }
                }
            }
        });
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (aim != null){
                    Intent intent = new Intent(getBaseContext(), AimActivity.class);
                    intent.putExtra(Aim.class.getSimpleName(), aim);
                    startActivity(intent);
                }
                else{
                    Intent intent = new Intent(getBaseContext(), MainUserActivity.class);
                    startActivity(intent);
                }
            }
        });
    }
}