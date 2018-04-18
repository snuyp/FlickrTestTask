package com.example.dima.flickrtesttask.Interface;

import com.example.dima.flickrtesttask.model.PhotoGallery;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * Created by Dima on 17.04.2018.
 */

public interface FlickrService {
    @GET("/services/rest/?method=flickr.photos.getRecent&format=json&nojsoncallback=1")
    Call<PhotoGallery>
    getInfo(
            @Query("api_key") String apiKey,
            @Query("extras") String extras,
            @Query("per_page") int per_page,
            @Query("page") int page
    );

    @GET("services/rest/?method=flickr.photos.search&format=json&nojsoncallback=1")
    Call<PhotoGallery>
    getSearchPhoto(
            @Query("api_key") String apiKey,
            @Query("extras") String extras,
            @Query("text") String search
    );

//    @GET("/services/rest/?method=flickr.photos.geo.getLocation&format=json&nojsoncallback=1")
//    Call<>
//    getInfo(
//            @Query("api_key") String apiKey,
//            @Query("")
//            @Query("extras") String extras
//    );
}
