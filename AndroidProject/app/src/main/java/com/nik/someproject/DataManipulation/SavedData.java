package com.nik.someproject.DataManipulation;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import androidx.annotation.LayoutRes;
import androidx.annotation.StringRes;

import com.nik.someproject.R;

public class SavedData {
    Activity activity;
    SharedPreferences sharedPreferences;

    String hash;

    public SavedData(Activity activity) {
        this.activity = activity;
        sharedPreferences = activity.getSharedPreferences(String.valueOf(R.string.SHARED_PREFS), Context.MODE_PRIVATE);
    }

    public boolean contains(String key) {
        return activity.getSharedPreferences(String.valueOf(R.string.SHARED_PREFS), Context.MODE_PRIVATE).contains(key);
    }

    public boolean contains(@StringRes int stringContentId) {
        return activity.getSharedPreferences(String.valueOf(R.string.SHARED_PREFS), Context.MODE_PRIVATE).contains(String.valueOf(stringContentId));
    }

    public String get(String key) {
        return activity.getSharedPreferences(String.valueOf(R.string.SHARED_PREFS), Context.MODE_PRIVATE).getString(key, null);
    }

    public String get(@StringRes int stringContentId) {
        return activity.getSharedPreferences(String.valueOf(R.string.SHARED_PREFS), Context.MODE_PRIVATE).getString(String.valueOf(stringContentId), null);
    }

    public void save(String key, String value) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(key, value);
        editor.apply();
    }

    public void save(@StringRes int stringContentId, String value) {
        if (value == null)
            return;
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(String.valueOf(stringContentId), value);
        editor.apply();
    }

    public void delete(@StringRes int stringContentId) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.remove(String.valueOf(stringContentId));
        editor.apply();
    }

    public void delete(String key) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.remove(key);
        editor.apply();
    }
}
