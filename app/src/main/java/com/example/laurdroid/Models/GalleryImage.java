package com.example.laurdroid.Models;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "images")
public class GalleryImage {
    @PrimaryKey(autoGenerate = true)
    private int id;
    private String path;

    public GalleryImage(String path) {
        this.path = path;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }
}
