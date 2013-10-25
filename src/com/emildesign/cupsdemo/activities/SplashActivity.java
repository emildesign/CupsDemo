package com.emildesign.cupsdemo.activities;

import com.emildesign.cupsdemo.R;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class SplashActivity extends Activity {
	private Button bGetStarted;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		initComponents();
	}

	@Override
	protected void onResume() {
		super.onResume();
	}

	public void initComponents() {
		setContentView(R.layout.activity_splash);

		// Creating a thread and running it for 2 second and the starting the MainActivity activity.
		Thread logoTimer = new Thread() 
		{
			public void run() 
			{
				try {
					int logoTimer = 0;
					// Making the thread sleep for 2000 milliseconds = 2 seconds.
					while (logoTimer < 2000) 
					{
						sleep(100);
						logoTimer = logoTimer + 100;
					}
					startActivity(new Intent(SplashActivity.this, MainActivity.class));
				} catch (InterruptedException e) {
					e.printStackTrace();
				}

				finally {
					finish();
				}
			}

		};
		logoTimer.start();
	}
}
