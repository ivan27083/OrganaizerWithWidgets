package com.example.organaizer;

import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.icu.text.SimpleDateFormat;
import android.widget.RemoteViews;
import android.app.PendingIntent;
import android.content.Intent;
import android.widget.TextView;

import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelStoreOwner;

import com.example.organaizer.data.db.classes.Task;
import com.example.organaizer.domain.ViewModels.TaskViewModel;
import com.example.organaizer.ui.activities.AddAimActivity;
import com.example.organaizer.ui.activities.AimActivity;
import com.example.organaizer.ui.activities.MainActivity;

import java.text.ParseException;
import java.util.Calendar;
import java.util.Date;
import java.util.List;


/**
 * Implementation of App Widget functionality.
 */
public class WidgetReminder extends AppWidgetProvider {
    private MyRepository repository;
    private TaskViewModel viewModel;
    private List<Task> task_list;
    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Date date1 = null;
        Date currentDate = new Date();
        Date closestDate = null;
        String closestTask = "", closestDateString="";
        String s = "", d = "";
        CharSequence widgetText="У Вас нет невыполненнх задач.", widgetText_date="";
        if (App.getUser()!=null) {
            repository = new MyRepository(viewModel.getInstance(context));
            task_list = repository.getData();

            for (int i = 0; i < task_list.size(); i++) {
                if (task_list.get(i).getCompleted()) continue;
                s = task_list.get(i).getText();
                d = task_list.get(i).getDate();
                try {
                    date1 = sdf.parse(d);
                    if (date1.after(currentDate) && (closestDate == null || date1.before(closestDate))) {
                        closestDate = date1; closestTask = s;
                        closestDateString=d;
                    }
                } catch (ParseException e) {
                    throw new RuntimeException(e);
                }
            }
        }
        else {widgetText="Авторизуйтесь, чтобы получить информацию о ближайшей задаче."; widgetText_date="";}

        if (closestTask!="" && closestDate!=null)
        {
            //String selectedDate = String.format("%04d-%02d-%02d", closestDate.getYear()+1900, closestDate.getMonth(), closestDate.getDay());
            widgetText = "Ближайшая задача:\n" + closestTask;
            widgetText_date = "Срок выполнения: " + closestDateString;
        }
        // Construct the RemoteViews object
        RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.widget_reminder);
        views.setTextViewText(R.id.appwidget_text_task, widgetText);
        views.setTextViewText(R.id.appwidget_text_date, widgetText_date);
        appWidgetManager.updateAppWidget(appWidgetIds[0], views);

        //task_list=viewModel.getTasks(App.getUser());
        // There may be multiple widgets active, so update all of them
        for (int appWidgetId : appWidgetIds) {
            updateAppWidget(context, appWidgetManager, appWidgetId);
        }
    }

    @Override
    public void onEnabled(Context context) {
        // Enter relevant functionality for when the first widget is created
    }

    @Override
    public void onDisabled(Context context) {
        // Enter relevant functionality for when the last widget is disabled
    }

    static void updateAppWidget(Context context, AppWidgetManager appWidgetManager,
                                int appWidgetId) {
        RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.widget_reminder);
        //views.setTextViewText(R.id.appwidget_text_task);
        //views.setTextViewText(R.id.appwidget_text_date);
        // Instruct the widget manager to update the widget
        //appWidgetManager.updateAppWidget(appWidgetId, views);
        Intent intent = new Intent(context, AimActivity.class); //запускаем главную Активность (можно другую)
        PendingIntent pIntentMainActivity = PendingIntent.getActivity(context, 0, intent, PendingIntent.FLAG_IMMUTABLE);
        views.setOnClickPendingIntent(R.id.appwidget_text_task, pIntentMainActivity);

        //appWidgetManager.updateAppWidget(appWidgetId, views);
    }
}