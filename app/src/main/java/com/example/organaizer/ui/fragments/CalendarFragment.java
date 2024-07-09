package com.example.organaizer.ui.fragments;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;
import android.icu.text.SimpleDateFormat;
import android.icu.util.ULocale;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModelProvider;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.CalendarView;

import com.example.organaizer.App;
import com.example.organaizer.R;
import com.example.organaizer.data.db.classes.Aim;
import com.example.organaizer.data.db.classes.Task;
import com.example.organaizer.domain.Adapter.TaskAdapter;
import com.example.organaizer.domain.ViewModels.AimViewModel;
import com.example.organaizer.domain.ViewModels.TaskViewModel;
import com.example.organaizer.ui.activities.AddTaskActivity;
import com.example.organaizer.ui.activities.AimActivity;
import com.google.android.material.datepicker.DayViewDecorator;

import android.widget.Toast;

import java.text.ParseException;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import com.prolificinteractive.materialcalendarview.CalendarDay;
import com.prolificinteractive.materialcalendarview.DayViewFacade;


import com.example.organaizer.DotDecorator;
import com.prolificinteractive.materialcalendarview.MaterialCalendarView;
import com.prolificinteractive.materialcalendarview.OnDateSelectedListener;


public class CalendarFragment extends Fragment implements TaskAdapter.OnCheckBoxClickListener{
    private List<Task> taskList;
    private ListView listView;
    private TaskViewModel viewModel;
    private AimViewModel aimViewModel;
    private MaterialCalendarView calendarView;
    private List<Task> tasksForDay;
    TaskAdapter adapter;

    @SuppressLint("ResourceType")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_calendar, container, false);
        listView = (ListView) view.findViewById(R.id.listView);
        calendarView = view.findViewById(R.id.calendarView);
        tasksForDay = new ArrayList<Task>();
        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        viewModel = new ViewModelProvider(this).get(TaskViewModel.class);
        aimViewModel = new ViewModelProvider(this).get(AimViewModel.class);
        taskList = viewModel.getTasks(App.getUser());

        SimpleDateFormat sdf;
        Date date1 = null;
        for (int i = 0; i < taskList.size(); i++){
            String myDate = taskList.get(i).getDate();
            sdf = new SimpleDateFormat("yyyy-MM-dd");
            try {
                date1 = sdf.parse(myDate);
            }
            catch (ParseException e) { throw new RuntimeException(e); }

            long dateInMillis = date1.getTime();

            Drawable drawable = getResources().getDrawable(R.drawable.calendar_dot);

            Date d = new Date(dateInMillis);
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O)
            {
                LocalDate date = d.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
                DotDecorator dotDecorator = new DotDecorator(drawable, CalendarDay.from(date));
                calendarView.addDecorator(dotDecorator);
            }
        }
        calendarView.setOnDateChangedListener(new OnDateSelectedListener() {
            @Override
            public void onDateSelected(@NonNull MaterialCalendarView widget, @NonNull CalendarDay date, boolean selected) {
                tasksForDay = new ArrayList<Task>();
                Calendar calendar = Calendar.getInstance(ULocale.getDefault().toLocale());
                calendar.set(date.getYear(), date.getMonth(), date.getDay());
                String selectedDate = String.format("%04d-%02d-%02d", calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));
                for (int i = 0; i < taskList.size(); i++){
                    if(taskList.get(i).getDate().equals(selectedDate)) {
                        tasksForDay.add(taskList.get(i));
                    }
                }
                adapter = new TaskAdapter(getContext(), R.layout.task_item, tasksForDay, viewModel);
                adapter.setOnCheckBoxClickListener(CalendarFragment.this);
                listView.setAdapter(adapter);
            }
        });

        adapter = new TaskAdapter(getContext(), R.layout.task_item, tasksForDay, viewModel);
        adapter.setOnCheckBoxClickListener(this);
        listView.setAdapter(adapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent = new Intent(getContext(), AddTaskActivity.class);
                intent.putExtra(Task.class.getSimpleName(), taskList.get(i));
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
                NotificationChannel channel = new NotificationChannel("channel_id_2", "Channel Name", NotificationManager.IMPORTANCE_DEFAULT);
                NotificationManager notificationManager = (NotificationManager)getActivity().getSystemService(NotificationManager.class);
                notificationManager.createNotificationChannel(channel);
            }
            NotificationManagerCompat notificationManagerCompat;
            Notification notification;
            Intent intent = new Intent(getContext(), AimActivity.class);
            PendingIntent pendingIntent = PendingIntent.getActivity(getContext(), 0, intent, PendingIntent.FLAG_IMMUTABLE);


            NotificationCompat.Builder builder = new NotificationCompat.Builder(getContext(), "channel_id_2")
                    .setSmallIcon(R.drawable.user_img_z)
                    .setContentTitle("Позравляем!")
                    .setContentText("Цель "+aim.getText()+" достигнута!")
                    .setContentIntent(pendingIntent)
                    .setAutoCancel(true);

            notification = builder.build();
            notificationManagerCompat = NotificationManagerCompat.from(getContext());

            if (ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.POST_NOTIFICATIONS) == PackageManager.PERMISSION_GRANTED) {
                notificationManagerCompat.notify(1, notification);
            }

        }

        String mess="";
        if (isChecked==true) { mess = "Задача " + task.getText() + " выполнена.\n";}
        mess+="Цель "+aim.getText()+ " завершена на "+aim.getProgress()+"%";
        Toast.makeText(getContext(), mess, Toast.LENGTH_SHORT).show();


        aimViewModel.updateAim(aim);
        Log.d("CalendarFragment", "Checkbox clicked: " + task.getText() + " - " + isChecked);
    }
}