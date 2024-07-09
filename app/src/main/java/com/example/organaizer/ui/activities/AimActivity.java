package com.example.organaizer.ui.activities;

import android.Manifest;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.view.ContextMenu;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.lifecycle.ViewModelProvider;

import com.example.organaizer.App;
import com.example.organaizer.R;
import com.example.organaizer.data.db.classes.Aim;
import com.example.organaizer.data.db.classes.Task;
import com.example.organaizer.domain.Adapter.TaskAdapter;
import com.example.organaizer.domain.ViewModels.AimViewModel;
import com.example.organaizer.domain.ViewModels.TaskViewModel;

import java.util.ArrayList;
import java.util.List;

public class AimActivity extends AppCompatActivity implements TaskAdapter.OnCheckBoxClickListener{
    private List<Task> taskList;
    private ListView listView;
    private TaskViewModel viewModel;
    private Button button;
    private Button backButton;
    private Button editButton;
    private Bundle arguments;
    private Aim aim;
    private AimViewModel aimViewModel;
    private List<Task> tasksForAim;
    private TaskAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_aim);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }
    @Override
    public void onCreateContextMenu(ContextMenu menu, View v,
                                    ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.context_menu, menu);
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
        if (item.getItemId() == R.id.edit){
            Intent intent = new Intent(getBaseContext(), AddTaskActivity.class);
            intent.putExtra(Aim.class.getSimpleName(), aim);
            intent.putExtra(Task.class.getSimpleName(), tasksForAim.get(info.position));
            startActivity(intent);
            return true;
        }
        else if (item.getItemId() == R.id.delete){
            viewModel.deleteTask(tasksForAim.get(info.position).getId());
            List<Task> newList = viewModel.getTasks(App.getUser());
            tasksForAim.clear();
            for (int i = 0; i < newList.size(); i++){
                if(newList.get(i).getAimId() == aim.getId()) {
                    tasksForAim.add(newList.get(i));
                }
            }
            adapter = new TaskAdapter(getBaseContext(), R.layout.task_item, tasksForAim, viewModel);
            adapter.setOnCheckBoxClickListener(this);
            listView.setAdapter(adapter);
            registerForContextMenu(listView);
            return true;
        }
        else return super.onContextItemSelected(item);
    }

    @Override
    protected void onStart() {
        super.onStart();
        TextView text = findViewById(R.id.taskText);
        TextView description = findViewById(R.id.taskDescription);
        arguments = getIntent().getExtras();
        if (arguments != null) {
            aim = (Aim) arguments.get(Aim.class.getSimpleName());
            text.setText(aim.getText());
            description.setText(aim.getDescription());
        }

        viewModel = new ViewModelProvider(this).get(TaskViewModel.class);
        aimViewModel = new ViewModelProvider(this).get(AimViewModel.class);
        button = findViewById(R.id.addTaskButton);
        backButton = findViewById(R.id.backButton);
        editButton = findViewById(R.id.editButton);
        listView = findViewById(R.id.listView);

        taskList = viewModel.getTasks(App.getUser());
        tasksForAim = new ArrayList<Task>();
        for (int i = 0; i < taskList.size(); i++){
            if(taskList.get(i).getAimId() == aim.getId()) {
                tasksForAim.add(taskList.get(i));
            }
        }
        adapter = new TaskAdapter(getBaseContext(), R.layout.task_item, tasksForAim, viewModel);
        adapter.setOnCheckBoxClickListener(this);
        listView.setAdapter(adapter);
        registerForContextMenu(listView);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent = new Intent(getBaseContext(), AddTaskActivity.class);
                intent.putExtra(Aim.class.getSimpleName(), aim);
                intent.putExtra(Task.class.getSimpleName(), tasksForAim.get(i));
                startActivity(intent);
            }
        });
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getBaseContext(), AddTaskActivity.class);
                intent.putExtra(Aim.class.getSimpleName(), aim);
                startActivity(intent);
            }
        });
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getBaseContext(), MainUserActivity.class);
                startActivity(intent);
            }
        });
        editButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getBaseContext(), AddAimActivity.class);
                intent.putExtra(Aim.class.getSimpleName(), aim);
                startActivity(intent);
            }
        });
    }
    @Override
    public void onCheckBoxClick(Task task, boolean isChecked) {
        Aim aim = aimViewModel.getAim(App.getUser(), task.getAimId());
        taskList = viewModel.getTasks(App.getUser());
        List<Task> tasksForAim = new ArrayList<Task>();
        double counter = 0;
        for (int i = 0; i < taskList.size(); i++){
            if(taskList.get(i).getAimId() == aim.getId()){
                tasksForAim.add(taskList.get(i));
                if(taskList.get(i).getCompleted()) counter++;
            }
        }
        aim.setProgress((int) (counter/tasksForAim.size()*100));

        if (aim.getProgress()==100)
        {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                NotificationChannel channel = new NotificationChannel("channel_id_3", "Channel Name", NotificationManager.IMPORTANCE_DEFAULT);
                NotificationManager notificationManager = getSystemService(NotificationManager.class);
                notificationManager.createNotificationChannel(channel);
            }
            NotificationManagerCompat notificationManagerCompat;
            Notification notification;
            Intent intent = new Intent(this, AimActivity.class);
            PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_IMMUTABLE);


            NotificationCompat.Builder builder = new NotificationCompat.Builder(this, "channel_id_3")
                    .setSmallIcon(R.drawable.user_img_z)
                    .setContentTitle("Позравляем!")
                    .setContentText("Цель "+aim.getText()+" достигнута!")
                    .setContentIntent(pendingIntent)
                    .setAutoCancel(true);

            notification = builder.build();
            notificationManagerCompat = NotificationManagerCompat.from(this);

            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.POST_NOTIFICATIONS) == PackageManager.PERMISSION_GRANTED) {
                notificationManagerCompat.notify(1, notification);
            }
        }
        String mess="";
        if (isChecked==true) { mess = "Задача " + task.getText() + " выполнена.\n";}
        mess+="Цель "+aim.getText()+ " завершена на "+aim.getProgress()+"%";
        Toast.makeText(this, mess, Toast.LENGTH_SHORT).show();

        aimViewModel.updateAim(aim);
        Log.d("CalendarFragment", "Checkbox clicked: " + task.getText() + " - " + isChecked);
    }
}