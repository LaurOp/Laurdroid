package com.example.laurdroid.ViewAux;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.bumptech.glide.Glide;
import com.example.laurdroid.R;

import java.util.List;

public class GalleryItemAdapter extends RecyclerView.Adapter<GalleryItemAdapter.ImageViewHolder> {

    private Context mContext;
    private List<String> mImagePathList;

    public GalleryItemAdapter(Context context, List<String> imagePathList) {
        mContext = context;
        mImagePathList = imagePathList;
    }

    @NonNull
    @Override
    public ImageViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.gallery_item, parent, false);
        return new ImageViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ImageViewHolder holder, int position) {
        String imagePath = mImagePathList.get(position);
        Glide.with(mContext).load(imagePath).into(holder.imageView);
    }

    // add this method to implement the abstract method in RecyclerView.Adapter
    @Override
    public void onBindViewHolder(@NonNull GalleryItemAdapter.ImageViewHolder holder, int position, @NonNull List<Object> payloads) {
        super.onBindViewHolder(holder, position, payloads);
        onBindViewHolder(holder, position);
    }

    @Override
    public int getItemCount() {
        return mImagePathList.size();
    }

    public void addImage(String imagePath) {
        mImagePathList.add(imagePath);
        notifyItemInserted(mImagePathList.size() - 1);
    }

    public static class ImageViewHolder extends RecyclerView.ViewHolder {

        ImageView imageView;

        public ImageViewHolder(View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.image_view);
        }
    }
}
