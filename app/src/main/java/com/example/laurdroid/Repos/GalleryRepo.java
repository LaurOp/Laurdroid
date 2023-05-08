package com.example.laurdroid.Repos;

import android.app.Application;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.laurdroid.AppDatabase;
import com.example.laurdroid.DAO.GalleryImageDAO;
import com.example.laurdroid.Models.GalleryImage;

import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class GalleryRepo {
    private GalleryImageDAO galleryImageDAO;

    public GalleryRepo(Application application) {
        AppDatabase db = AppDatabase.getDatabase(application);
        galleryImageDAO = db.galleryDao();
    }


    private final Executor executor = Executors.newSingleThreadExecutor();

    public void insertImage(final GalleryImage image) {
        executor.execute(() -> galleryImageDAO.insert(image));
    }

    // Get all images from the database
    public LiveData<List<GalleryImage>> getAllImages(final OnImagesLoadedListener listener) {
        MutableLiveData<List<GalleryImage>> imagesLiveData = new MutableLiveData<>();
        executor.execute(() -> {
            List<GalleryImage> images = galleryImageDAO.getAllImages();
            listener.onImagesLoaded(images);
            imagesLiveData.postValue(images);
        });
        return imagesLiveData;
    }

    // Listener interface for getting images from the database
    public interface OnImagesLoadedListener {
        void onImagesLoaded(List<GalleryImage> images);
    }


}
