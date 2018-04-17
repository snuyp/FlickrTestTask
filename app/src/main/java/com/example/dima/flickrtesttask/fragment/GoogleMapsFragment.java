package com.example.dima.flickrtesttask.fragment;


import android.content.Context;
import android.support.v4.app.Fragment;
/**
 * Created by Dima on 17.04.2018.
 */

public class GoogleMapsFragment  extends Fragment {

    private static GoogleMapsFragment sGoogleMapsFragment= null;

    public static GoogleMapsFragment getInstance(Context context)
    {
        if(sGoogleMapsFragment == null)
        {
            sGoogleMapsFragment = new GoogleMapsFragment();
        }
        return sGoogleMapsFragment;
    }

}
