package com.example.dima.flickrtesttask.adapter;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.example.dima.flickrtesttask.R;
import com.example.dima.flickrtesttask.fragment.MapsFragment;
import com.example.dima.flickrtesttask.fragment.RecentsFragment;

public class FlickrFragmentAdapter extends FragmentPagerAdapter {
    private Context context;

    public FlickrFragmentAdapter(FragmentManager fm, Context context) {
        super(fm);
        this.context = context;
    }

    @Override
    public Fragment getItem(int position) {
        if (position == 0) {
            return RecentsFragment.getInstance();
        } else if (position == 1) {
            return MapsFragment.getInstance();
        } else return null;
    }

    @Override
    public int getCount() {
        return 2;
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        switch (position) {
            case 0:
                return context.getString(R.string.recents);
            case 1:
                return context.getString(R.string.map);
        }
        return "";
    }
}
