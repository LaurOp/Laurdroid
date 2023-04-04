package com.example.laurdroid.services;

import android.content.Context;
import android.content.SharedPreferences;

public class Session {
    SharedPreferences prefs;
    SharedPreferences.Editor editor;
    Context _context;
    int isprivate = 0;

    private static final String sessionPref = "sessionPref";
    private static final String loggedIn = "IsLoggedIn";
    public static final String email = "email";
    public static final String pass = "password";

    public Session(Context context){
        this._context = context;
        prefs = _context.getSharedPreferences(sessionPref, isprivate);
        editor = prefs.edit();
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
