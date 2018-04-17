package com.example.dima.flickrtesttask;
import com.example.dima.flickrtesttask.adapter.FlickrFragmentAdapter;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;


public class MainActivity extends AppCompatActivity {
    private ViewPager mViewPager;
    private TabLayout mTabLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
//        Toolbar toolbar = findViewById(R.id.toolbar);
//        toolbar.setTitle("Flickr Fetch");
//        setSupportActionBar(toolbar);

        mViewPager = findViewById(R.id.viewPager);
        FlickrFragmentAdapter adapter = new FlickrFragmentAdapter(getSupportFragmentManager(),this);
        mViewPager.setAdapter(adapter);
        mTabLayout = findViewById(R.id.tabLayout);
        mTabLayout.setupWithViewPager(mViewPager);
    }

}
