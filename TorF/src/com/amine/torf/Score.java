package com.amine.torf;

import java.util.HashMap;

import android.annotation.SuppressLint;
import android.app.ActionBar.LayoutParams;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.amine.torf.helpers.DataManager;
import com.amine.torf.helpers.DbHighestScore;
import com.amine.torf.pojo.Scoredata;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.InterstitialAd;

public class Score extends Activity {

	TextView txtright, txtheader;
	String rightans = null;
	String totalquestions = null;
	Setting_preference pref;
	Button btnMainMenu, btnShare, btnHighscore;
	int numberques, rightanswer;
	int result;
	String category, standard;
	String score, name;
	SharedPreferences prefs;
	boolean cbonline;
	Setting_preference setuser;
	DbHighestScore db;
	Typeface normal, bold;
	final private static int DIALOG_LOGIN = 1;
	private InterstitialAd interstitial;
	private AdView adView;

	/* Your ad unit id. Replace with your actual ad unit id. */
	private static final String AD_UNIT_ID = DataManager.admobid;

	@SuppressWarnings("deprecation")
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_score);

		adView = new AdView(this);

		adView.setAdSize(AdSize.BANNER);
		adView.setAdUnitId(AD_UNIT_ID);
		normal = Typeface.createFromAsset(getAssets(), "normal.ttf");
		bold = Typeface.createFromAsset(getAssets(), "bold.ttf");
		LinearLayout ll = (LinearLayout) findViewById(R.id.ad);
		ll.addView(adView);
		AdRequest adRequest = new AdRequest.Builder().addTestDevice(
				DataManager.deviceId).build();

		adView.loadAd(adRequest);

		// Create the interstitial.
		interstitial = new InterstitialAd(this);
		interstitial.setAdUnitId(DataManager.interstitialid);

		// Create ad request.
		AdRequest adRequest1 =  new AdRequest.Builder().addTestDevice(
				DataManager.deviceId).build();

		// Begin loading your interstitial.
		interstitial.loadAd(adRequest1);

		txtright = (TextView) findViewById(R.id.txtright);
		setuser = new Setting_preference(this);

		pref = new Setting_preference(this);
		db = new DbHighestScore(this);

		prefs = PreferenceManager.getDefaultSharedPreferences(this);

		txtheader = (TextView) findViewById(R.id.textView);

		txtheader.setTypeface(bold);
		txtright.setTypeface(normal);

		rightans = getIntent().getSerializableExtra("rightans").toString();
		totalquestions = getIntent().getSerializableExtra("totalques")
				.toString();

		numberques = Integer.parseInt(totalquestions);

		rightanswer = Integer.parseInt(rightans);

		result = (rightanswer);

		HashMap<String, String> user = pref.getUserDetails();
		name = user.get(Setting_preference.KEY_USERNAME);

		score = String.valueOf(result);

		btnMainMenu = (Button) findViewById(R.id.btnMainMenu);
		btnHighscore = (Button) findViewById(R.id.btnHighestscore);
		btnShare = (Button) findViewById(R.id.btnShare);

		txtright.setText("" + result);

		btnMainMenu.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View view) {

				Intent iMainMenu = new Intent(Score.this, MainActivity.class);
				finish();
				startActivity(iMainMenu);

			}
		});

		btnHighscore.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {

				Intent iMainMenu1 = new Intent(Score.this, Highest_Score.class);
				startActivity(iMainMenu1);
			}
		});

		btnShare.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				Intent sharingIntent = new Intent(Intent.ACTION_SEND);
				sharingIntent.setType("text/plain");
				sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT,
						DataManager.share);
				// sharingIntent.putExtra(android.content.Intent.EXTRA_SUBJECT,
				// share);
				startActivity(Intent
						.createChooser(sharingIntent, "Share using"));

			}
		});

		showDialog(DIALOG_LOGIN);

	}

	public void displayInterstitial() {
		if (interstitial.isLoaded()) {
			interstitial.show();
		}
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

		Intent i = new Intent(Score.this, MainActivity.class);
		finish();
		startActivity(i);

	}

	@Override
	protected Dialog onCreateDialog(int id) {

		AlertDialog dialogDetails = null;

		switch (id) {
		case DIALOG_LOGIN:
			LayoutInflater inflater = LayoutInflater.from(this);

			View dialogview = inflater.inflate(
					R.layout.custom_dialog_entername, null);

			AlertDialog.Builder dialogbuilder = new AlertDialog.Builder(this);
			dialogbuilder.setView(dialogview);

			dialogDetails = dialogbuilder.create();

			break;
		}

		return dialogDetails;
	}

	@SuppressLint("InlinedApi")
	@Override
	protected void onPrepareDialog(int id, Dialog dialog) {
		final HashMap<String, String> user = setuser.getUserDetails();
		switch (id) {
		case DIALOG_LOGIN:
			final AlertDialog myDialog = (AlertDialog) dialog;
			myDialog.getWindow().setLayout(LayoutParams.MATCH_PARENT,
					LayoutParams.WRAP_CONTENT);

			Button login = (Button) myDialog.findViewById(R.id.btnOk);
			Button createAccount = (Button) myDialog
					.findViewById(R.id.btncancel);
			final EditText input = (EditText) myDialog
					.findViewById(R.id.etname);
			input.setTypeface(bold);
			input.setText(user.get(Setting_preference.KEY_USERNAME));
			TextView text = (TextView) myDialog.findViewById(R.id.txtname);

			text.setTypeface(bold);

			login.setOnClickListener(new OnClickListener() {
				public void onClick(View v) {

					name = user.get(Setting_preference.KEY_USERNAME);

					String name = input.getText().toString();

					setuser.entername(name);
					db.addContact(new Scoredata(name, score));
					displayInterstitial();
					myDialog.cancel();

				}
			});

			createAccount.setOnClickListener(new OnClickListener() {
				public void onClick(View v) {

					displayInterstitial();
					myDialog.cancel();
				}
			});
			break;
		}
	}
}
