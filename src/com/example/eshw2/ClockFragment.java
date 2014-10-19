package com.example.eshw2;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import android.app.Activity;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.InputFilter;
import android.text.Spanned;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.TextView;

public class ClockFragment extends Fragment {
	private SimpleDateFormat dateFormat, timeFormat;
	
	private TextView clock_tv_warning;
	private EditText clock_et_date, clock_et_time;
	private Button clock_btn_set, clock_btn_system;
	private RadioGroup clock_rg_ampm;
	private Clock clock_view_display;
	private String currentDate, currentTime;
	private int AMPM;
	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {	
		return inflater.inflate(R.layout.clock_frag, container, false);
	}
	
	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		View v = this.getView();
	    TextView tv = (TextView) v.findViewById(R.id.textView1);
	    tv.setTypeface(Typeface.createFromAsset(v.getContext().getAssets(), "fonts/Ubuntu.ttf"));
	    
	    clock_view_display = (Clock) v.findViewById(R.id.clock_view_display);
	    
	    dateFormat = new SimpleDateFormat("yyyy-MM-dd");
	    dateFormat.setLenient(false);
	    timeFormat = new SimpleDateFormat("hh:mm:ss");
	    timeFormat.setLenient(false);
	    clock_rg_ampm = (RadioGroup) v.findViewById(R.id.clock_rg_ampm);
	    Date date = new Date();
	    currentDate = dateFormat.format(date);
	    currentTime = timeFormat.format(date);
	    	
	    clock_et_date = (EditText) v.findViewById(R.id.clock_et_date);
	    clock_et_date.setText(currentDate); 
	    clock_et_time = (EditText) v.findViewById(R.id.clock_et_time);
	    clock_et_time.setText(currentTime); 
	    
	    AMPM = dateFormat.getCalendar().get(Calendar.AM_PM);
	    if(AMPM == Calendar.PM) {
	    	clock_rg_ampm.check(R.id.clock_rd_pm);
	    }
	     
	    clock_tv_warning = (TextView) v.findViewById(R.id.clock_tv_warning);
	    clock_btn_set = (Button) v.findViewById(R.id.clock_btn_set); 
	    clock_btn_set.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				boolean fine = true;
				String msg = "";
				
				try {
					doDateCheck();
					String[] s = clock_et_date.getText().toString().split("-");
					
					if(s[0].length() <= 2) {
						s[0] = "19" + s[0];
					}
					if(Integer.valueOf(s[0]) < 1900 || Integer.valueOf(s[0]) > 2100) {
						fine &= false;
						msg = "only for 1900 ~ 2100";
					}	
					if(s[1].length() <= 1)
						s[1] = "0" + s[1];
					if(s[2].length() <= 1)
						s[2] = "0" + s[2];
					clock_et_date.setText(s[0] + "-" + s[1] + "-" + s[2]);
				} catch (java.text.ParseException e) {
					msg = "invalid date";
					fine &= false;
				}
				
				try {
					doTimeCheck();
					String[] s = clock_et_time.getText().toString().split(":");
					
					if(s[0].length() <= 1)
						s[0] = "0" + s[0];
					if(s[0].equals("00"))
						s[0] = "12";
					if(s[1].length() <= 1)
						s[1] = "0" + s[1];
					if(s[2].length() <= 1)
						s[2] = "0" + s[2];
					clock_et_time.setText(s[0] + ":" + s[1] + ":" + s[2]);
				} catch(java.text.ParseException e) {
					fine &= false;
					if(msg.length() == 0) 
						msg = "invalid time";
					else
						msg += ", invalid time";
				}
				
				if(fine) {
					clock_tv_warning.setTextColor(Color.parseColor("#AA00FF00"));
					clock_tv_warning.setText("Success");
					currentDate = clock_et_date.getText().toString();
					currentTime = clock_et_time.getText().toString();
					
					String[] dd = clock_et_date.getText().toString().split("-");
					String[] tt = clock_et_time.getText().toString().split(":");
					
					Calendar cal = Calendar.getInstance();
					cal.set(Calendar.YEAR, str2Int(dd[0]));
					cal.set(Calendar.MONTH, str2Int(dd[1])- 1);
					cal.set(Calendar.DAY_OF_MONTH, str2Int(dd[2]));
					cal.set(Calendar.HOUR, str2Int(tt[0]));
					cal.set(Calendar.MINUTE, str2Int(tt[1]));
					cal.set(Calendar.SECOND, str2Int(tt[2]));
					
					if(clock_rg_ampm.getCheckedRadioButtonId() == R.id.clock_rd_pm) {
						cal.set(Calendar.AM_PM, Calendar.PM);
						clock_tv_warning.setText(currentDate + " " + currentTime + " PM");
					}
					else {
						//cal.set(str2Int(dd[0]), str2Int(dd[1]) - 1, str2Int(dd[2]), str2Int(tt[0]), str2Int(tt[1]), str2Int(tt[2]));
						cal.set(Calendar.AM_PM, Calendar.AM);
						clock_tv_warning.setText(currentDate + " " + currentTime + " AM");
					}
					clock_view_display.setCalendar(cal);
				}
				else {
					clock_tv_warning.setTextColor(Color.parseColor("#AAFF0000"));
					clock_tv_warning.setText(msg);
				}
			}	
	    });
	    
	    clock_btn_system = (Button) v.findViewById(R.id.clock_btn_system);
	    clock_btn_system.setSelected(true);
	    clock_btn_system.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				clock_view_display.setCalendar(Calendar.getInstance());
				clock_tv_warning.setTextColor(Color.parseColor("#AA00FF00"));
				clock_tv_warning.setText("System time");
			}
	    });
	}
	
	private void doDateCheck() throws java.text.ParseException {
		dateFormat.parse(clock_et_date.getText().toString());
	}
	
	private void doTimeCheck() throws java.text.ParseException {
		timeFormat.parse(clock_et_time.getText().toString());
	}
	
	private int str2Int(String str) {
		return Integer.valueOf(str);
	}
}
