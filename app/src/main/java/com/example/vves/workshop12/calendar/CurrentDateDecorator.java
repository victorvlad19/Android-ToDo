package com.example.vves.workshop12.calendar;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.PaintDrawable;
import android.text.style.BackgroundColorSpan;
import android.text.style.BulletSpan;
import android.text.style.ForegroundColorSpan;
import android.text.style.LineBackgroundSpan;
import android.text.style.RelativeSizeSpan;
import android.text.style.StyleSpan;
import android.text.style.UnderlineSpan;

import com.example.vves.workshop12.R;
import com.prolificinteractive.materialcalendarview.CalendarDay;
import com.prolificinteractive.materialcalendarview.DayViewDecorator;
import com.prolificinteractive.materialcalendarview.DayViewFacade;
import com.prolificinteractive.materialcalendarview.spans.DotSpan;

/**
 * Created by vves on 01.11.2017.
 */

public class CurrentDateDecorator implements DayViewDecorator {

    private Drawable highlightDrawable;
    private Context context;
    Drawable drawable;
    public CurrentDateDecorator( Context context) {
        this.context = context;
        highlightDrawable = this.context.getResources().getDrawable(R.drawable.circlebackground);
         drawable = context.getResources().getDrawable(R.drawable.ic_calendar);
    }
    @Override
    public boolean shouldDecorate(CalendarDay day) {
        return day.equals(CalendarDay.today());
    }

    @Override
    public void decorate(DayViewFacade view) {
        //view.addSpan(new BackgroundColorSpan(Color.BLACK));
        view.addSpan(new DotSpan(6, Color.WHITE));
        //view.setBackgroundDrawable(highlightDrawable);
        //view.setSelectionDrawable(drawable);
        //view.addSpan(new PaintDrawable(highlightDrawable));
        //view.addSpan(new ForegroundColorSpan(Color.parseColor("#b4dcff")));
        view.addSpan(new StyleSpan(Typeface.BOLD));
        //view.addSpan(new RelativeSizeSpan(1.2f));

    }
}