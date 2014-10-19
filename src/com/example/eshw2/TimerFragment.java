package com.example.eshw2;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class TimerFragment extends Fragment {
	private Dialog dialog;
	private Button timer_btn_start, timer_btn_restart;
	private TextView timer_tv_hr, timer_tv_m, timer_tv_s;
	private EditText timer_et_msg;
	private CountDownTimer countdownTimer;
	private int state = 0; // 0 = ready, 1 = running, 2 = pause;
	private int h = 0, m = 0, s = 0, ms = 0;
	private int[] saved = new int[4];
	private int[] numArray;
	
	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);  
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		return inflater.inflate(R.layout.timer_frag, container, false);
	}
	
	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		
		numArray = new int[60];
        for(int i = 0; i < 60 ; i++)
        		numArray[i] = i;
        
        saved[0] = h;
        saved[1] = m;
        saved[2] = s;
        saved[3] = ms;
        
		View v = this.getView();
		Typeface custom_font = Typeface.createFromAsset(v.getContext().getAssets(), "fonts/triple_dot_digital.ttf");

		final TouchToChange touch = new TouchToChange();
		
		timer_tv_hr = (TextView) v.findViewById(R.id.timer_tv_hr);
		timer_tv_hr.setTypeface(custom_font);
		timer_tv_hr.setOnTouchListener(touch);
		
		timer_tv_m = (TextView) v.findViewById(R.id.timer_tv_m);
		timer_tv_m.setTypeface(custom_font);
		timer_tv_m.setOnTouchListener(touch);
		
		timer_tv_s = (TextView) v.findViewById(R.id.timer_tv_s);
		timer_tv_s.setTypeface(custom_font);
		timer_tv_s.setOnTouchListener(touch);
		
		timer_btn_start = (Button) v.findViewById(R.id.timer_btn_start);
        timer_btn_restart  = (Button) v.findViewById(R.id.timer_btn_restart);  
        
        dialog = new Dialog(v.getContext());
        dialog.setContentView(R.layout.dialog);
        dialog.setTitle("Message");
        final TextView dialog_tv_msg = (TextView) dialog.findViewById(R.id.dialog_tv_msg);
        dialog_tv_msg.setTypeface(Typeface.createFromAsset(v.getContext().getAssets(), "fonts/Ubuntu.ttf"));
        Button btn = (Button) dialog.findViewById(R.id.dialog_btn_ok);
        btn.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View view) {
				dialog.dismiss();
			}
        	
        });
        
        timer_et_msg = (EditText) v.findViewById(R.id.timer_et_msg);
        timer_et_msg.addTextChangedListener(new TextWatcher() {
			@Override
			public void afterTextChanged(Editable arg0) {
				dialog_tv_msg.setText(timer_et_msg.getText());
			}
			@Override
			public void beforeTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) {}
			@Override
			public void onTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) {}
        	
        });
        
        timer_btn_start.setOnClickListener( new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if(state == 0) {	
					state = 1; // ready -> running;
					timer_btn_start.setText("pause");
					timer_btn_restart.setEnabled(true);				
					countdownTimer = new Timer((h * 3600 + m * 60 + s) * 1000 + ms, 1);
					countdownTimer.start();
				}
				else if(state == 1) {
					state = 2; // running -> pause
					touch.th = saved[0];
					touch.tm = saved[1];
					touch.ts = saved[2];
					//ms = saved[3];
					timer_btn_start.setText("continue");
					timer_btn_restart.setEnabled(true);
					countdownTimer.cancel();
				}
				else if(state == 2) {
					state = 1; // pause -> running
					timer_btn_start.setText("pause");
					timer_btn_restart.setEnabled(true);
					countdownTimer = new Timer((saved[0] * 3600 + saved[1] * 60 + saved[2]) * 1000 + saved[3], 1);
					countdownTimer.start();
				}
			}	
        });
        
        timer_btn_restart.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				countdownTimer.cancel();
				state = 0;
				timer_btn_start.setText("start");
				timer_btn_restart.setEnabled(false);
				
				timer_tv_hr.setText(fix(h, 2) + "h");
	            timer_tv_m.setText(fix(m, 2) + "m");
	            timer_tv_s.setText(fix(s, 2) + "." + fix(ms, 3) + "s");
			}	
        });
	}
	
	private class Timer extends CountDownTimer {
		public Timer(long millisInFuture, long countDownInterval) {
			super(millisInFuture, countDownInterval);
			// TODO Auto-generated constructor stub
		}

		@Override
		public void onTick(long millisUntilFinished) {
			// TODO Auto-generated method stub
			int millisecond = saved[3] = (int) (millisUntilFinished % 1000);
            int second 		= saved[2] = (int) (millisUntilFinished / 1000 % 60);
            int minute 		= saved[1] = (int) (millisUntilFinished / 60000 % 60);
            int hour	    = saved[0] = (int) (millisUntilFinished / 3600000 % 60);

            timer_tv_hr.setText(fix(hour, 2) + "h");
            timer_tv_m.setText(fix(minute, 2) + "m");
            timer_tv_s.setText(fix(second, 2) + "." + fix(millisecond, 3) + "s");
		}

		@Override
		public void onFinish() {
			// TODO Auto-generated method stub
			dialog.show();
			state = 0;
			timer_btn_start.setText("start");
			timer_btn_restart.setEnabled(false);
			
			timer_tv_hr.setText(fix(h, 2) + "h");
            timer_tv_m.setText(fix(m, 2) + "m");
            timer_tv_s.setText(fix(s, 2) + "." + fix(ms, 3) + "s");		
		}
	}	
	
	private class TouchToChange implements View.OnTouchListener {
		private int pressed = 0;
        private int origin = 0;
        private int dist = 0;
        private int th = 0, tm = 0, ts = 0;
        
		@SuppressLint("ClickableViewAccessibility") @Override
		public boolean onTouch(View v, MotionEvent event) {
			// TODO Auto-generated method stub
			if(state == 1) {
				return false;
			}
			
			if(event.getAction() == MotionEvent.ACTION_DOWN) {
				origin = (int) event.getY();
				switch(v.getId()) {
					case R.id.timer_tv_hr:
						pressed = 1;
						break;
					case R.id.timer_tv_m:
						pressed = 2;
						break;
					case R.id.timer_tv_s:
						pressed = 3;
						break;
				}
			}
			else if(event.getAction() == MotionEvent.ACTION_MOVE) {
				dist = origin - (int) event.getY();
				
				switch(pressed) {
            			case 1: //hour
            				h = th + dist / 20;
            				h = (h < 0) ? (13 - numArray[(-h) % 13]) : (numArray[h % 13]);
            				timer_tv_hr.setText(fix(h, 2) + "h");
            				break;
		            	case 2: //min
		            		m = tm + dist / 20;
		            		m = (m < 0) ? (60 - numArray[(-m) % 60]) : (numArray[m % 60]);	
		            		timer_tv_m.setText(fix(m, 2) + "m");
			            	break;
		            	case 3: //sec
		            		s = ts + dist / 20;
		            		s = (s < 0) ? (60 - numArray[(-s) % 60]) : (numArray[s % 60]);
		            		timer_tv_s.setText(fix(s, 2) + "." + fix(ms, 3) + "s");
		            		break;
		            	default:
		            		return false;
				}
			}
			else if(event.getAction() == MotionEvent.ACTION_UP) {
				if(pressed == 1)
					th = saved[0] = h; 
				else if(pressed == 2)
					tm = saved[1] = m; 
				else if(pressed == 3)
					ts = saved[2] = s;
				
				pressed = 0;
		        origin = 0;
		        dist = 0;
			}
			return true;
		}
	}
	
	public String fix(int n, int digit) {
		String str = String.valueOf(n);
		for(int i = str.length(); i < digit; i++) {
			str = "0" + str;
		}
		return str;
	}
}
