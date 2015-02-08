package com.amine.torf.ui;

import java.io.IOException;
import java.util.ArrayList;

import android.app.Activity;
import android.content.Intent;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.view.Window;
import android.widget.ImageView;

import com.amine.torf.MainActivity;
import com.amine.torf.R;
import com.amine.torf.Setting_preference;
import com.amine.torf.helpers.DataBaseHelper;
import com.amine.torf.pojo.CategoryList;

public class Splashscreen extends Activity {

	DataBaseHelper db;
	Setting_preference pref;
	private boolean mIsBackButtonPressed;
	private static final int SPLASH_DURATION = 1500; // 3 seconds
	ArrayList<CategoryList> categorylist = new ArrayList<CategoryList>();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_splashscreen);
		
		ImageView gyroView = (ImageView) findViewById(R.id.splash);
		gyroView.setBackgroundResource(R.drawable.splash_animation);
		AnimationDrawable gyroAnimation = (AnimationDrawable) gyroView.getBackground();
		gyroAnimation.start();
		
		db = new DataBaseHelper(this);
		pref = new Setting_preference(this);

		final DataBaseHelper dbHelper = new DataBaseHelper(this);

		try {
			dbHelper.createDataBase();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		Handler handler = new Handler();

		// run a thread after 2 seconds to start the home screen
		handler.postDelayed(new Runnable() {

			@Override
			public void run() {

				if (!mIsBackButtonPressed) {

					pref.getEditor().putBoolean("sound", true);
					pref.getEditor().putBoolean("vibrate", true);

					Intent i = new Intent(Splashscreen.this, MainActivity.class);
					finish();
					startActivity(i);

				}
			}

		}, SPLASH_DURATION);

	}

	@Override
	public void onBackPressed() {

		// set the flag to true so the next activity won't start up
		mIsBackButtonPressed = true;
		super.onBackPressed();

	}

}