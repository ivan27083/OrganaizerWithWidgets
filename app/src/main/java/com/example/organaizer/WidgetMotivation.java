package com.example.organaizer;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.widget.RemoteViews;
import android.os.Bundle;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import com.example.organaizer.ui.activities.AddAimActivity;
import com.example.organaizer.ui.fragments.MotivationFragment;

import java.util.Random;
public class WidgetMotivation extends AppWidgetProvider {

    public static final String ACTION_REFRESH = "com.example.organaizer.ACTION_REFRESH"; // замените "your.package.name" на Ваше имя пакета
    private static final String CLICK_ACTION = "com.example.organaizer.CLICK_ACTION";

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
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
        String[] motivations;
        motivations=context.getResources().getStringArray(R.array.motivations_array);
        Random random = new Random();
        // Генерируем случайный индекс в пределах размера массива
        int randomIndex = random.nextInt(motivations.length);
        // Получаем элемент массива по случайному индексу
        String randomElement = motivations[randomIndex];
        CharSequence widgetText = randomElement;
        RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.widget_motivation);
        views.setTextViewText(R.id.appwidget_text, widgetText);
        // Создаем Intent, который будет запускаться при нажатии на виджет
        Intent intent = new Intent(context, MotivationFragment.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        views.setOnClickPendingIntent(R.id.appwidget_text, pendingIntent);
        appWidgetManager.updateAppWidget(appWidgetId, views);
    }
}