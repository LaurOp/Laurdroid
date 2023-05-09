package com.example.laurdroid;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Application;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import java.util.stream.Collectors;

import android.content.pm.PackageManager;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Gallery;

import com.example.laurdroid.DAO.GalleryImageDAO;
import com.example.laurdroid.Models.GalleryImage;
import com.example.laurdroid.Repos.GalleryRepo;
import com.example.laurdroid.services.GalleryItemAdapter;

public class GalleryActivity extends AppCompatActivity {

    private List<String> imagePathList = new ArrayList<>();
    private static final int REQUEST_IMAGE_CAPTURE = 1;
    private File photoFile;
    private GalleryRepo galleryRepo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gallery);
        GalleryRepo.OnImagesLoadedListener listener = new GalleryRepo.OnImagesLoadedListener() {
            @Override
            public void onImagesLoaded(List<GalleryImage> images) {
            }
        };

        galleryRepo = new GalleryRepo(getApplication());
        galleryRepo.getAllImages(listener).observe(this, result ->{
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
                imagePathList = result.stream().map(GalleryImage::getPath).collect(Collectors.toList());

                GalleryItemAdapter adapter = new GalleryItemAdapter(GalleryActivity.this, imagePathList);
                RecyclerView recyclerView = findViewById(R.id.recycler_view);
                recyclerView.setLayoutManager(new LinearLayoutManager(GalleryActivity.this));
                recyclerView.setAdapter(adapter);
            }
        });

        GalleryItemAdapter adapter = new GalleryItemAdapter(this, imagePathList);
        RecyclerView recyclerView = findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);


        Button cameraButton = findViewById(R.id.camera_button);
        cameraButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Open the camera app
                Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
                    try {
                        Log.v("Camera", "in try1");
                        photoFile = createImageFile();
                        Log.v("Camera", "in try2");
                    } catch (IOException ex) {
                        Log.v("Camera", "in catch");
                    }
                    if (photoFile != null) {
                        Uri photoURI = FileProvider.getUriForFile(GalleryActivity.this, "com.example.android.fileprovider", photoFile);
                        takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                        startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
                    }
                }
                else{
                    Log.e("Camera","failed to open");
                }
            }
        });
    }

    // Create a file to save the captured image
    private File createImageFile() throws IOException {
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        return File.createTempFile(
                imageFileName, /* prefix */
                ".jpg", /* suffix */
                storageDir /* directory */
        );
    }


    // Handle the captured image
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            String imagePath = photoFile.getAbsolutePath();
            // imagePathList.add(imagePath);

            GalleryImage image = new GalleryImage(imagePath);
            galleryRepo.insertImage(image);

            // Update the RecyclerView adapter
            RecyclerView recyclerView = findViewById(R.id.recycler_view);
            GalleryItemAdapter adapter = (GalleryItemAdapter) recyclerView.getAdapter();
            assert adapter != null;
            adapter.addImage(imagePath);

        }
    }



}