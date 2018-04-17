package com.example.dima.flickrtesttask.model;

/**
 * Created by Dima on 17.04.2018.
 */

public class PhotoGallery {

    private Photos photos;
    private String stat;

    public PhotoGallery() {
    }

    public PhotoGallery(Photos photos, String stat) {
        this.photos = photos;
        this.stat = stat;
    }

    public Photos getPhotos() {
        return photos;
    }

    public void setPhotos(Photos photos) {
        this.photos = photos;
    }

    public String getStat() {
        return stat;
    }

    public void setStat(String stat) {
        this.stat = stat;
    }
}
