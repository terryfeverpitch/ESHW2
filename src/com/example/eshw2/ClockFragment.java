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
	private String[] array_date = new String[31];
	private SimpleDateFormat dateFormat, timeFormat;
	
	private TextView clock_tv_warning;
	private EditText clock_et_date, clock_et_time;
	private Button clock_btn_set, clock_btn_system;
	private RadioGroup clock_rg_ampm;
	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		
		for(int i = 0; i < 31; i++) {
			String str = String.valueOf(i);
			if(str.length() == 1) 
				array_date[i] = "0" + str;
			else 
				array_date[i] = str;
		}
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
	    	
	    dateFormat = new SimpleDateFormat("yyyy-MM-dd");
	    dateFormat.setLenient(false);
	    timeFormat = new SimpleDateFormat("hh:mm:ss a");
	    timeFormat.setLenient(false);
	    clock_rg_ampm = (RadioGroup) v.findViewById(R.id.clock_rg_ampm);
	    Date date = new Date();
	    String currentDate = dateFormat.format(date);
	    String currentTime = timeFormat.format(date);
	    	
	    clock_et_date = (EditText) v.findViewById(R.id.clock_et_date);
	    clock_et_date.setText(currentDate); 
	    
	    clock_et_time = (EditText) v.findViewById(R.id.clock_et_time);
	    clock_et_time.setText(currentTime); 
	    
	    int AMPM = dateFormat.getCalendar().get(Calendar.AM_PM);
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
				}
				else {
					clock_tv_warning.setTextColor(Color.parseColor("#AAFF0000"));
					clock_tv_warning.setText(msg);
				}
			}	
	    });
	}
	
	private void doDateCheck() throws java.text.ParseException {
		dateFormat.parse(clock_et_date.getText().toString());
	}
	
	private void doTimeCheck() throws java.text.ParseException {
		timeFormat.parse(clock_et_time.getText().toString());
	}
	
	InputFilter timeFilter = new InputFilter() {
		@Override
		public CharSequence filter(CharSequence source, int start, int end,
				Spanned dest, int dstart, int dend) {
			// TODO Auto-generated method stub
			if (source.length() == 0) {
                return null;// deleting, keep original editing
            }
            String result = "";
            result += dest.toString().substring(0, dstart);
            result += source.toString().substring(start, end);
            result += dest.toString().substring(dend, dest.length());

            if (result.length() > 5) {
                return "";// do not allow this edit
            }
            boolean allowEdit = true;
            char c;
            if (result.length() > 0) {
                c = result.charAt(0);
                allowEdit &= (c >= '0' && c <= '2');
            }
            if (result.length() > 1) {
                c = result.charAt(1);
                if(result.charAt(0) == '0' || result.charAt(0) == '1')
                    allowEdit &= (c >= '0' && c <= '9');
                else
                    allowEdit &= (c >= '0' && c <= '3');
            }
            if (result.length() > 2) {
                c = result.charAt(2);
                allowEdit &= (c == ':');
            }
            if (result.length() > 3) {
                c = result.charAt(3);
                allowEdit &= (c >= '0' && c <= '5');
            }
            if (result.length() > 4) {
                c = result.charAt(4);
                allowEdit &= (c >= '0' && c <= '9');
            }
            return allowEdit ? null : "";
		}
    };
}
