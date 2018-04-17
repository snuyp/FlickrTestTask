package com.example.dima.flickrtesttask.remote;

import com.google.gson.Gson;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by Dima on 17.04.2018.
 */

public class FlickrClient {
    private static Retrofit retrofit = null;
    public static Retrofit getClient(String baseUrl,Gson gson)
    {
        if(retrofit == null)
        {
            retrofit = new Retrofit.Builder()
                    .baseUrl(baseUrl)
                    .addConverterFactory(GsonConverterFactory.create(gson))
                    .build();
        }
        return retrofit;
    }
}
