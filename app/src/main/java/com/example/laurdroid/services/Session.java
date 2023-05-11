package com.example.laurdroid.services;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

public class Session {
    private static Session instance = null;
    private final SharedPreferences prefs;
    private final SharedPreferences.Editor editor;
    private final Context _context;
    private final int isprivate = 0;

    private static final String sessionPref = "sessionPref";
    private static final String loggedIn = "IsLoggedIn";
    public static final String email = "email";
    public static final String pass = "password";

    private Session(Context context){
        this._context = context;
        prefs = _context.getSharedPreferences(sessionPref, isprivate);
        editor = prefs.edit();
    }

    public String getEmailFromPrefs(){
        return prefs.getString("email", "ERROR");
    }

    public static Session getInstance(Context context) {
        if (instance == null) {
            instance = new Session(context);
        }
        return instance;
    }

    public void createLoginSession(String emaill, String password){
        editor.putBoolean(loggedIn, true);
        editor.putString(email, emaill);
        editor.putString(pass, password);
        editor.commit();
        //Log.v("Session","Created session "+emaill + password);
        //Log.v("Session","Prefs in session "+prefs.getString("email", "") + prefs.getString("password", ""));
    }

    public boolean isLoggedIn(){
        return prefs.getBoolean(loggedIn, false);
    }

    public void logoutUser(){
        editor.clear();
        editor.commit();
    }
}
