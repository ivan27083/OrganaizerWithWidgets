package com.example.organaizer;

import static android.graphics.Color.BLUE;

import android.graphics.drawable.Drawable;

import com.prolificinteractive.materialcalendarview.CalendarDay;
import com.prolificinteractive.materialcalendarview.DayViewDecorator;
import com.prolificinteractive.materialcalendarview.DayViewFacade;
import com.prolificinteractive.materialcalendarview.spans.DotSpan;

public class DotDecorator implements DayViewDecorator {
    private final Drawable drawable;
    private final CalendarDay date;

    public DotDecorator(Drawable drawable, CalendarDay date) {
        this.drawable = drawable;
        this.date = date;
    }

    @Override
    public boolean shouldDecorate(CalendarDay day) {
        return day.equals(date);
    }

    @Override
    public void decorate(DayViewFacade view) {
        view.addSpan(new DotSpan(10,BLUE));
        //view.setBackgroundDrawable(drawable);
    }
}

