package org.pullrequest.android.bookingnative;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences.Editor;

public class PreferencesManager {

	private static String PREFERENCES_NAME = "com.jops.android.ezdebts.controller.PreferencesController";

	public static final String PREF_LOGGED = "logged_user_login";

	private static PreferencesManager instance;

	private PreferencesManager() {
		//
	}

	public static PreferencesManager getInstance() {
		if (instance == null) {
			instance = new PreferencesManager();
		}
		return instance;
	}

	public void savePref(Context context, String name, boolean value) {
		Editor editor = context.getSharedPreferences(PREFERENCES_NAME, Activity.MODE_PRIVATE).edit();
		editor.putBoolean(name, value);
		SharedPreferencesCompat.apply(editor);
	}

	public void savePref(Context context, String name, int value) {
		Editor editor = context.getSharedPreferences(PREFERENCES_NAME, Activity.MODE_PRIVATE).edit();
		editor.putInt(name, value);
		SharedPreferencesCompat.apply(editor);
	}

	public void savePref(Context context, String name, String value) {
		Editor editor = context.getSharedPreferences(PREFERENCES_NAME, Activity.MODE_PRIVATE).edit();
		editor.putString(name, value);
		SharedPreferencesCompat.apply(editor);
	}

	public boolean getBooleanPref(Context context, String name) {
		return context.getSharedPreferences(PREFERENCES_NAME, Activity.MODE_PRIVATE).getBoolean(name, false);
	}

	public int getIntPref(Context context, String name) {
		return context.getSharedPreferences(PREFERENCES_NAME, Activity.MODE_PRIVATE).getInt(name, -1);
	}

	public String getStringPref(Context context, String name) {
		return context.getSharedPreferences(PREFERENCES_NAME, Activity.MODE_PRIVATE).getString(name, null);
	}

	public void removePref(Context context, String name) {
		Editor editor = context.getSharedPreferences(PREFERENCES_NAME, Activity.MODE_PRIVATE).edit();
		editor.remove(name);
		SharedPreferencesCompat.apply(editor);
	}
}