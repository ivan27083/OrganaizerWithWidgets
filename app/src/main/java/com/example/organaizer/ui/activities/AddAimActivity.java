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
import android.view.View;
import android.widget.Button;

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
import com.example.organaizer.domain.ViewModels.AimViewModel;
import com.example.organaizer.domain.ViewModels.TaskViewModel;
import com.example.organaizer.ui.dialogFragments.CustomDialogFragment;
import com.google.android.material.textfield.TextInputEditText;

import java.util.ArrayList;
import java.util.List;

public class AddAimActivity extends AppCompatActivity {
    private AimViewModel viewModel;
    private TaskViewModel taskViewModel;
    private TextInputEditText textInput;
    private TextInputEditText descriptionInput;
    private Button button;
    private Button backButton;
    private Aim aim;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_add_aim);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        Bundle arguments = getIntent().getExtras();
        if (arguments != null) {
            aim = (Aim) arguments.get(Aim.class.getSimpleName());
        }

        viewModel = new ViewModelProvider(this).get(AimViewModel.class);
        taskViewModel = new ViewModelProvider(this).get(TaskViewModel.class);
        textInput = findViewById(R.id.aimTextInput);
        descriptionInput = findViewById(R.id.aimDescriptionInput);
        button = findViewById(R.id.saveAimButton);
        backButton = findViewById(R.id.backButton);

        if (aim != null){
            textInput.setText(aim.getText());
            descriptionInput.setText(aim.getDescription());
        }

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(aim != null){
                    if (textInput.getText() != null) {
                        aim.setText(textInput.getText().toString());
                        aim.setDescription(descriptionInput.getText().toString());
                        changeProgress(aim);
                        viewModel.updateAim(aim);
                        Intent intent = new Intent(getBaseContext(), AimActivity.class);
                        intent.putExtra(Aim.class.getSimpleName(), aim);
                        startActivity(intent);
                    } else {
                        CustomDialogFragment dialog = new CustomDialogFragment("Внимание", "Укажите название цели");
                        dialog.show(getSupportFragmentManager(), "custom");
                    }
                }
                else {
                    Aim aim = new Aim(textInput.getText().toString(), descriptionInput.getText().toString());
                    aim.setUserId(App.getUser().getId());
                    if (textInput.getText() != null) {
                        viewModel.addAim(aim);
                        Intent intent = new Intent(getBaseContext(), MainUserActivity.class);
                        startActivity(intent);
                    } else {
                        CustomDialogFragment dialog = new CustomDialogFragment("Внимание", "Укажите название цели");
                        dialog.show(getSupportFragmentManager(), "custom");
                    }
                }
            }
        });
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(aim != null){
                    Intent intent = new Intent(getBaseContext(), AimActivity.class);
                    intent.putExtra(Aim.class.getSimpleName(), aim);
                    startActivity(intent);
                }
                else {
                    Intent intent = new Intent(getBaseContext(), MainUserActivity.class);
                    startActivity(intent);
                }
            }
        });
    }
    private void changeProgress(Aim aim) {
        List<Task> taskList = taskViewModel.getTasks(App.getUser());
        List<Task> tasksForAim = new ArrayList<Task>();
        double counter = 0;
        for (int i = 0; i < taskList.size(); i++){
            if(taskList.get(i).getAimId() == aim.getId()){
                tasksForAim.add(taskList.get(i));
                if(taskList.get(i).getCompleted()) counter++;
            }
        }
        aim.setProgress((int) (counter/tasksForAim.size()*100));
        if (aim.getProgress()==100) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                NotificationChannel channel = new NotificationChannel("channel_id_1", "Channel Name", NotificationManager.IMPORTANCE_DEFAULT);
                NotificationManager notificationManager = getSystemService(NotificationManager.class);
                notificationManager.createNotificationChannel(channel);
            }
            NotificationManagerCompat notificationManagerCompat;
            Notification notification;
            Intent intent = new Intent(this, AimActivity.class);
            PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_IMMUTABLE);


            NotificationCompat.Builder builder = new NotificationCompat.Builder(this, "channel_id_1")
                    .setSmallIcon(R.drawable.user_img_z)
                    .setContentTitle("Позравляем!")
                    .setContentText("Цель " + aim.getText() + " достигнута!")
                    .setContentIntent(pendingIntent)
                    .setAutoCancel(true);

            notification = builder.build();
            notificationManagerCompat = NotificationManagerCompat.from(this);

            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.POST_NOTIFICATIONS) == PackageManager.PERMISSION_GRANTED) {
                notificationManagerCompat.notify(1, notification);
            }
        }
        viewModel.updateAim(aim);
    }
}