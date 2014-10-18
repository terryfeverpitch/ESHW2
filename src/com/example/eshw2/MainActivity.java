package com.example.eshw2;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTabHost;

public class MainActivity extends FragmentActivity  {
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		FragmentTabHost tabHost = (FragmentTabHost)findViewById(android.R.id.tabhost);

	    tabHost.setup(this, getSupportFragmentManager(), R.id.realtabcontent);

	    //tab 1
	    tabHost.addTab(tabHost.newTabSpec("Clcok").setIndicator("Clock"), ClockFragment.class, null);
	    //tab 2
	    tabHost.addTab(tabHost.newTabSpec("Timer").setIndicator("Timer"), TimerFragment.class, null);
	}
}
