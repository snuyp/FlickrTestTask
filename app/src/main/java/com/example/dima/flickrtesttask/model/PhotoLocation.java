package com.example.dima.flickrtesttask.model;

/**
 * Created by Dima on 18.04.2018.
 */

public class PhotoLocation {
    private Photo photo;

    public Photo getPhoto ()
    {
        return photo;
    }

    public void setPhoto (Photo photo)
    {
        this.photo = photo;
    }

    @Override
    public String toString()
    {
        return "ClassPojo [photo = "+photo+"]";
    }
}
