package com.kidsdynamic.swing.view;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.support.annotation.NonNull;
import android.text.style.LineBackgroundSpan;
import android.util.Log;

import com.prolificinteractive.materialcalendarview.CalendarDay;
import com.prolificinteractive.materialcalendarview.DayViewDecorator;
import com.prolificinteractive.materialcalendarview.DayViewFacade;

import java.util.Collection;
import java.util.HashSet;

/**
 * date:   2017/10/23 19:43 <br/>
 */

public class EventDecorator implements DayViewDecorator {
    protected String color = "#FF4081";
    protected float radius = 6;
    protected int offSize = 0;
    private HashSet<CalendarDay> calendarDays;

    public EventDecorator(@NonNull Collection<CalendarDay> dates){
        calendarDays = new HashSet<>(dates);
    }

    @Override
    public boolean shouldDecorate(CalendarDay day) {
        return calendarDays.contains(day);
    }

    @Override
    public void decorate(DayViewFacade view) {
        view.addSpan(new AnnulusSpan(radius, Color.parseColor(color),offSize));
    }

    public class AnnulusSpan implements LineBackgroundSpan {
        float radius;
        int color;

        public AnnulusSpan(float radius, int color, int offSize){
            this.radius = radius;
            this.color = color;
        }

        @Override
        public void drawBackground(Canvas canvas, Paint paint, int left, int right, int top, int baseline,
                                   int bottom, CharSequence charSequence, int i5, int i6, int i7) {

            int oldColor = paint.getColor();
            if(color != 0){
                paint.setColor(color);
            }

            Log.w("TestCalendar","(left+right) " + (left+right));

            canvas.drawCircle((left+right)/2 - offSize, bottom+radius,radius, paint);
            paint.setColor(oldColor);

        }
    }
}
