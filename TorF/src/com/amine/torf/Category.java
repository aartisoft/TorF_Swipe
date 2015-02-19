package com.amine.torf;

import java.io.InputStream;
import java.util.ArrayList;

import org.json.JSONArray;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.amine.torf.helpers.DataBaseHelper;
import com.amine.torf.helpers.DataManager;
import com.amine.torf.ui.ExpandedListView;

public class Category extends Activity {

	TextView txtMovies, txtGeneral, txtSports, txtMusic;

	ExpandedListView listView;
	ArrayAdapter<String> arrayAdapter;
	String category;
	static InputStream is = null;
	static JSONArray jObj = null;
	static String json = "";
	int categoryid;
	private ArrayList<String> offlinecategorylist = new ArrayList<String>();
	DataBaseHelper db;
	JSONArray json1;
	SharedPreferences prefs;
	Typeface normal, bold;
	/* Your ad unit id. Replace with your actual ad unit id. */
	private static final String AD_UNIT_ID = DataManager.admobid;
	boolean cbonline;
	TextView txtheader;

	private boolean isFirstRun;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_categoery_new);
		db = new DataBaseHelper(getApplicationContext());

		isFirstRun = getSharedPreferences("PREFERENCE", MODE_PRIVATE)
				.getBoolean("isFirstRun", true);

		getSharedPreferences("PREFERENCE", MODE_PRIVATE).edit()
				.putBoolean("isFirstRun", false).commit();

		normal = Typeface.createFromAsset(getAssets(), "normal.ttf");
		bold = Typeface.createFromAsset(getAssets(), "bold.ttf");
		txtheader = (TextView) findViewById(R.id.txtheader);
		txtMovies = (TextView) findViewById(R.id.txtMovies);
		txtGeneral = (TextView) findViewById(R.id.txtGeneral);
		txtSports = (TextView) findViewById(R.id.txtSports);
		txtMusic = (TextView) findViewById(R.id.txtMusic);

		Button home = (Button) findViewById(R.id.btnback);

		home.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent i = new Intent(Category.this, MainActivity.class);
				finish();
				startActivity(i);

			}
		});

		txtMovies.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent i = new Intent();
				if (isFirstRun) {
					i = new Intent(Category.this, ShowCase.class);
				} else
					i = new Intent(Category.this, Timer_questions.class);
				i.putExtra("categoryname", "Movies & TV Shows");
				finish();
				startActivity(i);

			}

		});

		txtGeneral.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent i = new Intent();
				if (isFirstRun) {
					i = new Intent(Category.this, ShowCase.class);
				} else
					i = new Intent(Category.this, Timer_questions.class);
				i.putExtra("categoryname", "General Knowledge");
				finish();
				startActivity(i);

			}

		});

		txtMusic.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent i = new Intent();
				if (isFirstRun) {
					i = new Intent(Category.this, ShowCase.class);
				} else
					i = new Intent(Category.this, Timer_questions.class);
				i.putExtra("categoryname", "Music");
				finish();
				startActivity(i);

			}

		});

		txtSports.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent i = new Intent();
				if (isFirstRun) {
					i = new Intent(Category.this, ShowCase.class);
				} else
					i = new Intent(Category.this, Timer_questions.class);
				i.putExtra("categoryname", "Sports");
				finish();
				startActivity(i);
			}

		});
		Intent intent = getIntent();
		boolean isSigned = intent.getExtras().getBoolean("signedIn");
		if (isSigned == false)
			Toast.makeText(getApplicationContext(),
					"Please sign in first, to upload your scores", Toast.LENGTH_LONG)
					.show();
	}

	@Override
	protected void onPause() {
		super.onPause();

	}

	@Override
	protected void onResume() {
		super.onResume();

	}

	@Override
	protected void onStop() {
		super.onStop();

	}

	@Override
	public void onBackPressed() {

		Intent i = new Intent(Category.this, MainActivity.class);
		finish();
		startActivity(i);

	}

	public static String getAdUnitId() {
		return AD_UNIT_ID;
	}
}
