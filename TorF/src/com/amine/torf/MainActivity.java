package com.amine.torf;

import android.app.ActionBar.LayoutParams;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.amine.torf.helpers.DataBaseHelper;
import com.amine.torf.helpers.DataManager;
import com.amine.torf.helpers.PrefsActivity;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.GoogleApiClient.ConnectionCallbacks;
import com.google.android.gms.common.api.GoogleApiClient.OnConnectionFailedListener;
import com.google.android.gms.games.Games;
import com.google.android.gms.plus.Plus;
import com.google.example.games.basegameutils.BaseGameUtils;

public class MainActivity extends Activity implements ConnectionCallbacks,
		OnConnectionFailedListener {
	String mGreeting = "Hello, anonymous user (not signed in)";

	TextView txtplay, txtfeedback, txthighscore, txtheader;
	Button btnexit;
	final private static int DIALOG_LOGIN = 1;
	Setting_preference setuser;
	// ArrayList<CategoryList> categorylist = new ArrayList<CategoryList>();
	SharedPreferences prefs;
	DataBaseHelper db;
	Button btnsetting;
	/* Your ad unit id. Replace with your actual ad unit id. */
	SharedPreferences myPrefs;
	SharedPreferences.Editor prefsEditor;
	private final String TAG_NAME = "tagname";
	Typeface normal, bold;
	int ratecounter;

	private GoogleApiClient mGoogleApiClient;
	private View signInButton, signOutButton;
	private boolean mResolvingConnectionFailure = false;

	// Has the user clicked the sign-in button?
	private boolean mSignInClicked = false;

	// Automatically start the sign-in flow when the Activity starts
	private boolean mAutoStartSignInFlow = true;

	// request codes we use when invoking an external activity
	private static final int RC_RESOLVE = 5000;
	private static final int RC_UNUSED = 5001;
	private static final int RC_SIGN_IN = 9001;

	// achievements and scores we're pending to push to the cloud
	// (waiting for the user to sign in, for instance)
	AccomplishmentsOutbox mOutbox = new AccomplishmentsOutbox();
	final String TAG = "TorF";
	boolean mShowSignIn = true;

	@SuppressWarnings("deprecation")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		// Create the Google API Client with access to Plus and Games
		mGoogleApiClient = new GoogleApiClient.Builder(this)
				.addConnectionCallbacks(this)
				.addOnConnectionFailedListener(this).addApi(Plus.API)
				.addScope(Plus.SCOPE_PLUS_LOGIN).addApi(Games.API)
				.addScope(Games.SCOPE_GAMES).build();

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
		signInButton = (View) findViewById(R.id.sign_in_button);
		signOutButton = (View) findViewById(R.id.sign_out_button);

		txtheader.setTypeface(bold);
		btnsetting = (Button) findViewById(R.id.btnsetting);

		mOutbox.loadLocal(this);

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

		signInButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				mSignInClicked = true;
				mGoogleApiClient.connect();
			}
		});

		signOutButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				mSignInClicked = false;
				Games.signOut(mGoogleApiClient);
				if (mGoogleApiClient.isConnected()) {
					mGoogleApiClient.disconnect();
				}

				setShowSignInButton(true);
			}
		});

		mOutbox.loadLocal(this);

	}

	private boolean isSignedIn() {
		return (mGoogleApiClient != null && mGoogleApiClient.isConnected());
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

	@Override
	public void onConnectionFailed(ConnectionResult connectionResult) {
		Log.d(TAG, "onConnectionFailed(): attempting to resolve");
		if (mResolvingConnectionFailure) {
			Log.d(TAG, "onConnectionFailed(): already resolving");
			return;
		}

		if (mSignInClicked || mAutoStartSignInFlow) {
			mAutoStartSignInFlow = false;
			mSignInClicked = false;
			mResolvingConnectionFailure = true;
			if (!BaseGameUtils.resolveConnectionFailure(this, mGoogleApiClient,
					connectionResult, RC_SIGN_IN,
					getString(R.string.signin_other_error))) {
				mResolvingConnectionFailure = false;
			}
		}

		// TODO Auto-generated method stub
		// Sign-in failed, so show sign-in button on main menu
	}

	@Override
	public void onConnected(Bundle arg0) {
		Log.d(TAG, "onConnected(): connected to Google APIs");
		// Show sign-out button on main menu
		setShowSignInButton(false);

		/*
		 * Player p = Games.Players.getCurrentPlayer(mGoogleApiClient); String
		 * displayName; if (p == null) { Log.w(TAG,
		 * "mGamesClient.getCurrentPlayer() is NULL!"); displayName = "???"; }
		 * else { displayName = p.getDisplayName(); }
		 */
		// TODO

		// if we have accomplishments to push, push them
		if (!mOutbox.isEmpty()) {
			pushAccomplishments();
			Toast.makeText(this,
					getString(R.string.your_progress_will_be_uploaded),
					Toast.LENGTH_LONG).show();
		}

	}

	@Override
	public void onConnectionSuspended(int arg0) {
		Log.d(TAG, "onConnectionSuspended(): attempting to connect");
		mGoogleApiClient.connect();
	}

	public void setShowSignInButton(boolean showSignIn) {
		mShowSignIn = showSignIn;
		updateUi();
	}

	@Override
	public void onStart() {
		super.onStart();
		mGoogleApiClient.connect();
		updateUi();
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
		Log.d(TAG, "onStop(): disconnecting");
		if (mGoogleApiClient.isConnected()) {
			mGoogleApiClient.disconnect();
		}

	}

	public void setGreeting(String greeting) {
		mGreeting = greeting;
		updateUi();
	}

	void updateUi() {
		if (getApplication() == null)
			return;

		this.findViewById(R.id.sign_in_bar).setVisibility(
				mShowSignIn ? View.VISIBLE : View.GONE);
		this.findViewById(R.id.sign_out_bar).setVisibility(
				mShowSignIn ? View.GONE : View.VISIBLE);
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode,
			Intent intent) {
		super.onActivityResult(requestCode, resultCode, intent);
		if (requestCode == RC_SIGN_IN) {
			mSignInClicked = false;
			mResolvingConnectionFailure = false;
			if (resultCode == RESULT_OK) {
				mGoogleApiClient.connect();
			} else {
				BaseGameUtils.showActivityResultError(this, requestCode,
						resultCode, R.string.signin_other_error);
			}
		}
	}
	
	
	//******************************Achievements***********************//
	
	public void onShowAchievementsRequested() {
		if (isSignedIn()) {
			startActivityForResult(
					Games.Achievements.getAchievementsIntent(mGoogleApiClient),
					RC_UNUSED);
		} else {
			BaseGameUtils.makeSimpleDialog(this,
					getString(R.string.achievements_not_available)).show();
		}
	}

	public void onShowLeaderboardsRequested() {
		if (isSignedIn()) {
			startActivityForResult(
					Games.Leaderboards
							.getAllLeaderboardsIntent(mGoogleApiClient),
					RC_UNUSED);
		} else {
			BaseGameUtils.makeSimpleDialog(this,
					getString(R.string.leaderboards_not_available)).show();
		}
	}

	void pushAccomplishments() {
		if (!isSignedIn()) {
			// can't push to the cloud, so save locally
			mOutbox.saveLocal(this);
			return;
		}

		if (mOutbox.mArrogantAchievement) {
			Games.Achievements.unlock(mGoogleApiClient,
					getString(R.string.amazing));
			mOutbox.mArrogantAchievement = false;
		}

		/*
		 * if (mOutbox.mPrimeAchievement) {
		 * Games.Achievements.unlock(mGoogleApiClient,
		 * getString(R.string.amazing)); mOutbox.mPrimeAchievement = false; } if
		 * (mOutbox.mHumbleAchievement) {
		 * Games.Achievements.unlock(mGoogleApiClient,
		 * getString(R.string.achievement_humble)); mOutbox.mHumbleAchievement =
		 * false; } if (mOutbox.mLeetAchievement) {
		 * Games.Achievements.unlock(mGoogleApiClient,
		 * getString(R.string.achievement_leet)); mOutbox.mLeetAchievement =
		 * false; } if (mOutbox.mBoredSteps > 0) {
		 * Games.Achievements.increment(mGoogleApiClient,
		 * getString(R.string.achievement_really_bored), mOutbox.mBoredSteps);
		 * Games.Achievements.increment(mGoogleApiClient,
		 * getString(R.string.achievement_bored), mOutbox.mBoredSteps); } if
		 * (mOutbox.mEasyModeScore >= 0) {
		 * Games.Leaderboards.submitScore(mGoogleApiClient,
		 * getString(R.string.leaderboard_easy), mOutbox.mEasyModeScore);
		 * mOutbox.mEasyModeScore = -1; } if (mOutbox.mHardModeScore >= 0) {
		 * Games.Leaderboards.submitScore(mGoogleApiClient,
		 * getString(R.string.leaderboard_hard), mOutbox.mHardModeScore);
		 * mOutbox.mHardModeScore = -1; }
		 */
		mOutbox.saveLocal(this);
	}

	class AccomplishmentsOutbox {
		boolean mPrimeAchievement = false;
		boolean mHumbleAchievement = false;
		boolean mLeetAchievement = false;
		boolean mArrogantAchievement = false;
		int mBoredSteps = 0;
		int mEasyModeScore = -1;
		int mHardModeScore = -1;

		boolean isEmpty() {
			return !mPrimeAchievement && !mHumbleAchievement
					&& !mLeetAchievement && !mArrogantAchievement
					&& mBoredSteps == 0 && mEasyModeScore < 0
					&& mHardModeScore < 0;
		}

		public void saveLocal(Context ctx) {
			/*
			 * TODO: This is left as an exercise. To make it more difficult to
			 * cheat, this data should be stored in an encrypted file! And
			 * remember not to expose your encryption key (obfuscate it by
			 * building it from bits and pieces and/or XORing with another
			 * string, for instance).
			 */
		}

		public void loadLocal(Context ctx) {
			/*
			 * TODO: This is left as an exercise. Write code here that loads
			 * data from the file you wrote in saveLocal().
			 */
		}
	}
}