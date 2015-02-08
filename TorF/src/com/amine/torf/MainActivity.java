package com.amine.torf;

import android.app.ActionBar.LayoutParams;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.amine.torf.helpers.DataBaseHelper;
import com.amine.torf.helpers.DataManager;
import com.amine.torf.helpers.PrefsActivity;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.AdView;

public class MainActivity extends Activity {

	TextView txtplay, txtfeedback, txthighscore, txtheader;
	Button btnexit;
	final private static int DIALOG_LOGIN = 1;
	Setting_preference setuser;
	// ArrayList<CategoryList> categorylist = new ArrayList<CategoryList>();
	SharedPreferences prefs;
	DataBaseHelper db;
	Button btnsetting;
	/* Your ad unit id. Replace with your actual ad unit id. */
	private static final String AD_UNIT_ID = DataManager.admobid;
	SharedPreferences myPrefs;
	SharedPreferences.Editor prefsEditor;
	private final String TAG_NAME = "tagname";
	Typeface normal, bold;
	int ratecounter;
	private AdView adView;

	@SuppressWarnings("deprecation")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		normal = Typeface.createFromAsset(getAssets(), "normal.ttf");
		bold = Typeface.createFromAsset(getAssets(), "bold.ttf");
		myPrefs = this.getSharedPreferences("myPrefs", MODE_WORLD_READABLE);
		prefsEditor = myPrefs.edit();
		ratecounter = myPrefs.getInt(TAG_NAME, 0);
		prefs = PreferenceManager.getDefaultSharedPreferences(this);

		setuser = new Setting_preference(this);

		db = new DataBaseHelper(this);

		txtplay = (TextView) findViewById(R.id.txtplay1);

		txtfeedback = (TextView) findViewById(R.id.txtfeedback1);
		txthighscore = (TextView) findViewById(R.id.txthighscore);
		txtheader = (TextView) findViewById(R.id.txtheader);

		txtheader.setTypeface(bold);
		btnsetting = (Button) findViewById(R.id.btnsetting);

		adView = new AdView(this);

		adView.setAdSize(AdSize.BANNER);
		adView.setAdUnitId(AD_UNIT_ID);
		AdRequest adRequest = new AdRequest.Builder().build();

		adView.loadAd(adRequest);
		LinearLayout ll = (LinearLayout) findViewById(R.id.ad);
		ll.addView(adView);

		txtplay.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				ratecounter++;
				updaterateCounter();

			}

		});

		txthighscore.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				Intent k = new Intent(MainActivity.this, Highest_Score.class);

				startActivity(k);

			}
		});

		btnsetting.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent i = new Intent(MainActivity.this, PrefsActivity.class);
				startActivity(i);
			}
		});

		txtfeedback.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				showDialog(DIALOG_LOGIN);
			}
		});

	}

	@Override
	public void onBackPressed() {
		new AlertDialog.Builder(this)
				.setTitle("Really Exit?")
				.setMessage("Are you sure you want to exit?")
				.setNegativeButton(android.R.string.no, null)
				.setPositiveButton(android.R.string.yes,
						new DialogInterface.OnClickListener() {

							public void onClick(DialogInterface arg0, int arg1) {
								Intent intent = new Intent(Intent.ACTION_MAIN);
								intent.addCategory(Intent.CATEGORY_HOME);
								intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
								finish();
								startActivity(intent);
							}
						}).create().show();
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

	public void updaterateCounter() {
		prefsEditor.putInt(TAG_NAME, ratecounter);
		prefsEditor.commit();

		if (ratecounter == DataManager.ratecounter) {
			ratealert();

		} else {
			Intent i = new Intent(MainActivity.this, Category.class);
			startActivity(i);
		}

	}

	public void ratealert() {
		AlertDialog.Builder alert = new AlertDialog.Builder(MainActivity.this);
		alert.setTitle("Rate My App"); // Set Alert dialog title
										// here
		alert.setCancelable(false);
		alert.setPositiveButton("Rate Now",
				new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int whichButton) {

						String url = DataManager.appurl;

						startActivity(new Intent(Intent.ACTION_VIEW, Uri
								.parse(url)));
						prefsEditor.putInt(TAG_NAME, 200);
						prefsEditor.commit();
						Intent i = new Intent(MainActivity.this, Category.class);
						startActivity(i);

					}

				});
		alert.setNeutralButton("Later", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int whichButton) {

				prefsEditor.putInt(TAG_NAME, 0);
				prefsEditor.commit();
				Intent i = new Intent(MainActivity.this, Category.class);
				startActivity(i);

			}
		});

		alert.setNegativeButton("Never", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int whichButton) {

				prefsEditor.putInt(TAG_NAME, 200);
				prefsEditor.commit();
				Intent i = new Intent(MainActivity.this, Category.class);
				startActivity(i);

			}
		});
		AlertDialog alertDialog = alert.create();
		alertDialog.show();
	}

	@Override
	protected Dialog onCreateDialog(int id) {

		AlertDialog dialogDetails = null;

		switch (id) {
		case DIALOG_LOGIN:
			LayoutInflater inflater = LayoutInflater.from(this);

			View dialogview = inflater.inflate(R.layout.custom_dialog_rateapp,
					null);

			AlertDialog.Builder dialogbuilder = new AlertDialog.Builder(this);
			dialogbuilder.setView(dialogview);

			dialogDetails = dialogbuilder.create();

			break;
		}

		return dialogDetails;
	}

	@Override
	protected void onPrepareDialog(int id, Dialog dialog) {

		switch (id) {
		case DIALOG_LOGIN:
			final AlertDialog myDialog = (AlertDialog) dialog;
			myDialog.getWindow().setLayout(LayoutParams.MATCH_PARENT,
					LayoutParams.WRAP_CONTENT);

			Button ratebtn = (Button) myDialog.findViewById(R.id.btnOk);
			Button suggestionbtn = (Button) myDialog
					.findViewById(R.id.btncancel);
			ratebtn.setTypeface(bold);
			suggestionbtn.setTypeface(bold);
			final TextView input = (TextView) myDialog
					.findViewById(R.id.txttitle);
			input.setTypeface(bold);

			TextView text = (TextView) myDialog.findViewById(R.id.txtheader);

			text.setTypeface(bold);

			ratebtn.setOnClickListener(new OnClickListener() {
				public void onClick(View v) {

					String str = DataManager.appurl;

					startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(str)));
					myDialog.cancel();
				}
			});

			suggestionbtn.setOnClickListener(new OnClickListener() {
				public void onClick(View v) {

					Intent emailIntent = new Intent(Intent.ACTION_SEND);
					emailIntent.setType("Text/plain");
					emailIntent.putExtra(android.content.Intent.EXTRA_EMAIL,
							new String[] { DataManager.email });
					emailIntent.putExtra(android.content.Intent.EXTRA_SUBJECT,
							"Quiz Feedback");
					startActivity(Intent.createChooser(emailIntent,
							"Send mail..."));
					myDialog.cancel();
				}
			});
			break;
		}
	}
}