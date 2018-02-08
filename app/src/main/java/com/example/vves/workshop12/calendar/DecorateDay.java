package com.example.vves.workshop12.calendar;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.support.v4.content.ContextCompat;
import android.text.format.DateFormat;
import android.text.style.ForegroundColorSpan;
import android.text.style.StyleSpan;
import android.util.Log;

import com.example.vves.workshop12.R;
import com.prolificinteractive.materialcalendarview.CalendarDay;
import com.prolificinteractive.materialcalendarview.DayViewDecorator;
import com.prolificinteractive.materialcalendarview.DayViewFacade;
import com.prolificinteractive.materialcalendarview.spans.DotSpan;

import java.util.Collection;
import java.util.Date;
import java.util.HashSet;

/**
 * Created by vves on 03.11.2017.
 */

public class DecorateDay implements DayViewDecorator {

    private final HashSet<CalendarDay> dates;
    private ColorDrawable drawable;
    private Context context;
    private Drawable highlightDrawable;

    public DecorateDay (Context context, Collection<CalendarDay> dates) {
        this.context = context;
        this.dates = new HashSet<>(dates);
        highlightDrawable = this.context.getResources().getDrawable(R.drawable.oval);
    }

    @Override
    public boolean shouldDecorate(CalendarDay day) {
        return dates.contains(day);
    }

    @Override
    public void decorate(DayViewFacade view) {
        //view.addSpan(new DotSpan(5, Color.YELLOW));
        view.setBackgroundDrawable(highlightDrawable);
        //view.addSpan(new StyleSpan(Typeface.BOLD));
        //view.addSpan(new ForegroundColorSpan(Color.WHITE));
        //view.setBackgroundDrawable(drawable);//So this code add a background
    }

}
