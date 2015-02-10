package com.amine.torf.helpers;


import android.os.Bundle;
import android.preference.PreferenceActivity;

import com.amine.torf.R;

public class PrefsActivity extends PreferenceActivity{
	 
@SuppressWarnings("deprecation")
@Override
protected void onCreate(Bundle savedInstanceState) {
   super.onCreate(savedInstanceState);
   addPreferencesFromResource(R.xml.prefs);
}
}
