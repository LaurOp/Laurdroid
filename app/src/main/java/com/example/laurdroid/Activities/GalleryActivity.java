package com.example.laurdroid.Activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.net.Uri;
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
import java.util.stream.Collectors;

import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.example.laurdroid.Models.GalleryImage;
import com.example.laurdroid.R;
import com.example.laurdroid.Repos.GalleryRepo;
import com.example.laurdroid.ViewAuxiliaries.GalleryItemAdapter;

public class GalleryActivity extends AppCompatActivity {

    private List<String> imagePathList = new ArrayList<>();
    private static final int REQUEST_IMAGE_CAPTURE = 1;
    private File photoFile;
    private GalleryRepo galleryRepo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gallery);
        GalleryRepo.OnImagesLoadedListener listener = images -> {};

        galleryRepo = new GalleryRepo(getApplication());
        galleryRepo.getAllImages(listener).observe(this, result ->{
            imagePathList = result.stream().map(GalleryImage::getPath).collect(Collectors.toList());

            GalleryItemAdapter adapter = new GalleryItemAdapter(GalleryActivity.this, imagePathList);
            RecyclerView recyclerView = findViewById(R.id.recycler_view);
            recyclerView.setLayoutManager(new LinearLayoutManager(GalleryActivity.this));
            recyclerView.setAdapter(adapter);
        });

        GalleryItemAdapter adapter = new GalleryItemAdapter(this, imagePathList);
        RecyclerView recyclerView = findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);


        Button cameraButton = findViewById(R.id.camera_button);
        cameraButton.setOnClickListener(v -> {
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
        });
    }

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


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            String imagePath = photoFile.getAbsolutePath();

            GalleryImage image = new GalleryImage(imagePath);
            galleryRepo.insertImage(image);

            RecyclerView recyclerView = findViewById(R.id.recycler_view);
            GalleryItemAdapter adapter = (GalleryItemAdapter) recyclerView.getAdapter();
            assert adapter != null;
            adapter.addImage(imagePath);

        }
    }



}