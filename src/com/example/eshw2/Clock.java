package com.example.eshw2;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Paint.Align;
import android.graphics.PaintFlagsDrawFilter;
import android.graphics.Point;
import android.graphics.Typeface;
import android.os.Handler;
import android.util.AttributeSet;
import android.view.View;

public class Clock extends View{
	// private ShapeDrawable mDrawable;
	public static final int MEG_INVALIDATE = 9527;
    private Paint mPaint;
    private Calendar calendar = Calendar.getInstance();
    private Handler handler = new Handler();
    private SimpleDateFormat format = new java.text.SimpleDateFormat("yyyy-MM-dd-hh-mm-ss");
    private Date d = new Date(calendar.getTimeInMillis()); 
    private int ampm, hour, min, sec;
    private int year, month, date;
    private int hr_m_size = 60;
    private int date_size = 35;
    private int sec_size = 7;
    
	public Clock(Context context, AttributeSet attributeSet) {
		super(context, attributeSet);
		
        d.setTime(calendar.getTimeInMillis());
		String[] tt = format.format(d).split("-");
		year  = Integer.valueOf(tt[0]);
		month = Integer.valueOf(tt[1]);
		date  = Integer.valueOf(tt[2]);
		ampm  = calendar.get(Calendar.AM_PM);
		hour  = Integer.valueOf(tt[3]);
		min   = Integer.valueOf(tt[4]);
		sec   = Integer.valueOf(tt[5]);
		
		handler.removeCallbacks(redrawTimer);
		handler.postDelayed(redrawTimer, 1000);
		
		mPaint = new Paint();
		mPaint.setTypeface(Typeface.create("fonts/Ubuntu.ttf", Typeface.BOLD));
	}
	
	@Override
    protected void onDraw(Canvas canvas) {
		canvas.setDrawFilter(new PaintFlagsDrawFilter(0, Paint.ANTI_ALIAS_FLAG | Paint.FILTER_BITMAP_FLAG));
        mPaint.setColor(0xAAFF0000);
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setStrokeWidth(3);
        int w = this.getWidth();
        int h = this.getHeight();
        Point mid = new Point(w/2, h/2);
        int r = (w < h) ? (w / 2) : (h / 2);
        
        canvas.drawCircle(mid.x, mid.y, r, mPaint);
        // draw hour min ampm
        mPaint.setTextSize(hr_m_size); mPaint.setTextAlign(Align.LEFT);
        canvas.drawText(fix(hour), mid.x - 80, mid.y - 60, mPaint);
        canvas.drawLine(mid.x, mid.y + 100, mid.x, mid.y - 100, mPaint);
        canvas.drawText(fix(min), mid.x + 70 - hr_m_size, mid.y - 60, mPaint);
        canvas.drawText(am_pm(ampm), mid.x + 70 - hr_m_size, mid.y + 20, mPaint);
        // draw year month date
        mPaint.setTextSize(date_size); mPaint.setStyle(Paint.Style.FILL);
        canvas.drawText("" + year, mid.x - 90, mid.y + 90, mPaint);
        canvas.drawText(fix(month) + "/" + fix(date), mid.x + 10, mid.y + 90, mPaint);
        // draw second
        mPaint.setTextSize(sec_size + 30);
        int xx = mid.x + (int)(r * Math.cos((double)(sec - 15.0) * 6.0 * 3.14 / 180.0));
        int yy = mid.y + (int)(r * Math.sin((double)(sec - 15.0) * 6.0 * 3.14 / 180.0));
   
        if(sec == 0 || sec == 30) {
        	mPaint.setTextAlign(Align.CENTER);
        }
        else if(sec < 30) {
        	mPaint.setTextAlign(Align.RIGHT);
        }
        else if(sec > 30){
        	mPaint.setTextAlign(Align.LEFT);
        }

        canvas.drawText(fix(sec), xx, yy, mPaint);
        canvas.drawPoint(xx, yy, mPaint);
    }
	
	private Runnable redrawTimer = new Runnable() {
    	public void run() {	   		
    		calendar.setTimeInMillis(calendar.getTimeInMillis() + 1000);
    		d.setTime(calendar.getTimeInMillis());
    		
    		String[] tt = format.format(d).split("-");    		
    		year  = Integer.valueOf(tt[0]);
    		month = Integer.valueOf(tt[1]);
    		date  = Integer.valueOf(tt[2]);
    		ampm  = calendar.get(Calendar.AM_PM);
    		hour  = Integer.valueOf(tt[3]);
    		min   = Integer.valueOf(tt[4]);
    		sec   = Integer.valueOf(tt[5]);
    		// refresh per sec
    		handler.postDelayed(this, 1000);
    		invalidate();
    	}
    };
    
    public void setCalendar(Calendar cal) {
    	this.calendar = cal;
    }
    
	public String fix(int n) {
		if(n < 10) return "0" + n; 
		else return "" + n;
	}
	
	private String am_pm(int a) {
		return (a == Calendar.AM) ? "A.M." : "P.M.";
	}
}
