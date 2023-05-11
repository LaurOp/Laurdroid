package com.example.laurdroid.Repos;

import android.app.Application;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.laurdroid.services.AppDatabase;
import com.example.laurdroid.DAO.UserDao;
import com.example.laurdroid.Models.User;

public class UserRepository {
    private final UserDao userDao;

    public UserRepository(Application application) {
        AppDatabase db = AppDatabase.getDatabase(application);
        userDao = db.userDao();
    }

    public LiveData<User> getUserByEmailAndPass(String email, String hashedPass) {
        return userDao.getUserByEmailAndPass(email, hashedPass);
    }

    public LiveData<Boolean> insert(User user) {
        MutableLiveData<Boolean> insertResult = new MutableLiveData<>();
        AppDatabase.databaseWriteExecutor.execute(() -> {
            try {
                userDao.insert(user);
                insertResult.postValue(true);
            } catch (Exception e) {
                insertResult.postValue(false);
            }
        });
        return insertResult;
    }


    public LiveData<Boolean> loginUser(String email, String hashedPass) {
        MutableLiveData<Boolean> loginResult = new MutableLiveData<>();

        getUserByEmailAndPass(email, hashedPass).observeForever(user -> {
            if (user == null) {
                loginResult.postValue(false);
            } else {
                loginResult.postValue(true);
            }
        });

        return loginResult;
    }

}
