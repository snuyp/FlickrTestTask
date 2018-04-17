package com.example.dima.flickrtesttask.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.support.v7.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.dima.flickrtesttask.R;
import com.example.dima.flickrtesttask.model.PhotoGallery;
import com.squareup.picasso.Picasso;

/**
 * Created by Dima on 17.04.2018.
 */

class ItemViewHolder extends RecyclerView.ViewHolder {
    TextView title;
    ImageView image;

    public ItemViewHolder(View itemView) {
        super(itemView);
        title = itemView.findViewById(R.id.title_photo_name);
        image = itemView.findViewById(R.id.image);
    }
}

 public class ListAdapter extends RecyclerView.Adapter<ItemViewHolder> {
    private Context context;
    private PhotoGallery photoGallery;

    public ListAdapter(Context context, PhotoGallery photoGallery) {
        this.context = context;
        this.photoGallery = photoGallery;
    }

    @NonNull
    @Override
    public ItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View itemView = inflater.inflate(R.layout.flickr_card_layout, parent, false);
        return new ItemViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull ItemViewHolder holder, int position) {
        if (photoGallery.getPhotos().getPhoto().get(position).getTitle().length() > 15) {
            holder.title.setText(photoGallery.getPhotos().getPhoto().get(position).getTitle().substring(0, 15) + "...");
        }
       // holder.title.setText(photoGallery.getPhotos().getPhoto().get(position).getTitle());


        Glide.with(context)
                .load(photoGallery.getPhotos().getPhoto().get(position).getUrlS())
                .apply(new RequestOptions()
                        .placeholder(R.drawable.ic_terrain_black_24dp))
                .into(holder.image);
//        Picasso.with(context)
//                .load(photoGallery.getPhotos().getPhoto().get(position).getUrlS())
//                .into(holder.image);

    }

    @Override
    public int getItemCount() {
        return photoGallery.getPhotos().getPhoto().size();
    }
}

