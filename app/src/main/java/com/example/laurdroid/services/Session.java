package com.example.laurdroid.services;

import android.content.Context;
import android.content.SharedPreferences;

public class Session {
    private static Session instance = null;
    private SharedPreferences prefs;
    private SharedPreferences.Editor editor;
    private Context _context;
    private int isprivate = 0;

    private static final String sessionPref = "sessionPref";
    private static final String loggedIn = "IsLoggedIn";
    public static final String email = "email";
    public static final String pass = "password";

    private Session(Context context){
        this._context = context;
        prefs = _context.getSharedPreferences(sessionPref, isprivate);
        editor = prefs.edit();
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
    }

    public boolean isLoggedIn(){
        return prefs.getBoolean(loggedIn, false);
    }

    public void logoutUser(){
        editor.clear();
        editor.commit();
    }
}
