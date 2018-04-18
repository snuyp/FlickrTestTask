package com.example.dima.flickrtesttask.common;

import com.example.dima.flickrtesttask.Interface.FlickrService;
import com.example.dima.flickrtesttask.model.Location;
import com.example.dima.flickrtesttask.model.Photo;
import com.example.dima.flickrtesttask.remote.FlickrClient;
import com.google.gson.Gson;

import java.util.List;

/**
 * Created by Dima on 17.04.2018.
 */

public class Common {
    private static final String BASE_URL = "https://www.flickr.com";

    public static final String API_KEY = "40650639388c5e49bd0781b9f46cf4dc";//for flickr

    public static String extras = "url_s";
    public static String hasGeo = "true";
    public static int perPage = 100;
    public static int page = 1;

    public static List<Photo> photos = null; // feature to use in RecentFragment (no added)


    public static FlickrService getFlcikrService(Gson gson)
    {
        return FlickrClient.getClient(BASE_URL,gson).create(FlickrService.class);
    }
}
