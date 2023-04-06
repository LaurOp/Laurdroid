package com.example.laurdroid.DAO;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.example.laurdroid.Models.User;

import java.util.List;

@Dao
public interface UserDao {

    @Query("SELECT * FROM users")
    List<User> getAll();

    @Query("SELECT * FROM users WHERE id = :id")
    User getById(String id);

    @Query("SELECT * FROM users WHERE id IN (:userIds)")
    List<User> loadAllByIds(int[] userIds);

    @Query("SELECT * FROM users WHERE name LIKE :username")
    User findByName(String username);

    @Insert
    void insertAll(User... users);

    @Delete
    void delete(User user);

    @Query("DELETE FROM users WHERE id = :id")
    void deleteById(int id);

    @Query("SELECT * FROM users WHERE email = :email AND hashedPass = :hashedPass LIMIT 1")
    LiveData<User> getUserByEmailAndPass(String email, String hashedPass);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(User user);

}

