package com.example.laurdroid.DAO;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import com.example.laurdroid.Models.GalleryImage;

import java.util.List;

@Dao
public interface GalleryImageDAO {

    @Insert
    void insert(GalleryImage image);

    @Query("SELECT * FROM images")
    List<GalleryImage> getAllImages();
}

