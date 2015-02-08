package com.amine.torf;

import java.util.HashMap;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

public class Setting_preference {
	SharedPreferences pref;
	Editor editor;


	Context _context;
	int PRIVATE_MODE = 0;
	private static final String PREF_NAME = "QUIZ";

	private static final String IS_LOGIN = "IsLoggedIn";
	public static final String KEY_USERNAME = "username";
	private static final String IS_UPDATE= "isupdate";

	public Setting_preference(Context context){
		this._context = context;
		pref = _context.getSharedPreferences(PREF_NAME, PRIVATE_MODE);
		editor = pref.edit();
	}
	
	
	public void entername(String name)
	{
		editor.putBoolean(IS_LOGIN, true);
		editor.putString(KEY_USERNAME, name);


		editor.commit();
		
	}
	
	public void updated()
	{
		editor.putBoolean(IS_UPDATE, true);
		

		editor.commit();
		
	}
	
	public boolean isLoggedIn(){		
		return pref.getBoolean(IS_LOGIN, false);
	}
	
	public boolean isUpdate(){		
		return pref.getBoolean(IS_UPDATE, false);
	}
	
	public HashMap<String, String> getUserDetails(){
		HashMap<String, String> user = new HashMap<String, String>();


		

		user.put(KEY_USERNAME, pref.getString(KEY_USERNAME, null));

		return user;
	}
	
	public Editor getEditor() {
		return editor;
	}


	public void setEditor(Editor editor) {
		this.editor = editor;
	}

}