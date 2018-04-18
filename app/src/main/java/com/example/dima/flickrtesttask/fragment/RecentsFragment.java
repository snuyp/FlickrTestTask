package com.example.dima.flickrtesttask.fragment;

import android.app.AlertDialog;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;


import com.example.dima.flickrtesttask.Interface.FlickrService;
import com.example.dima.flickrtesttask.MainActivity;
import com.example.dima.flickrtesttask.R;
import com.example.dima.flickrtesttask.adapter.ListAdapter;
import com.example.dima.flickrtesttask.common.Common;
import com.example.dima.flickrtesttask.model.PhotoGallery;
import com.example.dima.flickrtesttask.model.PhotoLocation;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import dmax.dialog.SpotsDialog;
import io.paperdb.Paper;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by Dima on 17.04.2018.
 */

public class RecentsFragment extends Fragment{
    private static RecentsFragment sRecentsFragment = null;

    private RecyclerView recyclerView;
    private RecyclerView.LayoutManager layoutManager;
    private ListAdapter adapter;
    private FlickrService flickrService;
    private AlertDialog dialog;
    private SwipeRefreshLayout swipeRefreshLayout;
    private FlickrService service;

    public static RecentsFragment getInstance() {
        if (sRecentsFragment == null) {
            sRecentsFragment = new RecentsFragment();
        }
        return sRecentsFragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Gson gson = new GsonBuilder().setLenient().create();
        service = Common.getFlcikrService(gson);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_recents, container, false);

        Paper.init(getActivity());
        setHasOptionsMenu(true);
        recyclerView = v.findViewById(R.id.recycler_recent);
        recyclerView.setHasFixedSize(true);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(getActivity(), 3);
        recyclerView.setLayoutManager(gridLayoutManager);

        swipeRefreshLayout = v.findViewById(R.id.swipe_refresh_recents);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                loadFlickrFetch(true);
            }
        });
        dialog = new SpotsDialog(getActivity());

        loadFlickrFetch(false);
        return v;

    }



    private void loadFlickrFetch(boolean isRefreshed) {
        if (!isRefreshed) {
            String cache = Paper.book().read("cache");
            if (cache != null && !cache.isEmpty() && !cache.equals("null")) {
                PhotoGallery photoGallery = new Gson().fromJson(cache, PhotoGallery.class);
                adapter = new ListAdapter(getActivity(),photoGallery);
                adapter.notifyDataSetChanged();
                recyclerView.setAdapter(adapter);
            } else {
                dialog.show();
                service.getInfo(Common.API_KEY, Common.extras, Common.perPage, Common.page).enqueue(new Callback<PhotoGallery>() {
                    @Override
                    public void onResponse(Call<PhotoGallery> call, Response<PhotoGallery> response) {
                        adapter = new ListAdapter(getContext(), response.body());
                        adapter.notifyDataSetChanged();
                        recyclerView.setAdapter(adapter);

                        //Save to cache
                        Paper.book().write("cache", new Gson().toJson(response.body()));
                        dialog.dismiss();
                    }

                    @Override
                    public void onFailure(Call<PhotoGallery> call, Throwable t) {
                        Log.e("Failure", "Failure" + t.getMessage());
                    }
                });
            }
        } else {
            swipeRefreshLayout.setRefreshing(true);
            service.getInfo(Common.API_KEY, Common.extras, Common.perPage, Common.page).enqueue(new Callback<PhotoGallery>() {
                @Override
                public void onResponse(Call<PhotoGallery> call, Response<PhotoGallery> response) {
                    adapter = new ListAdapter(getContext(), response.body());
                    adapter.notifyDataSetChanged();
                    recyclerView.setAdapter(adapter);

                    //Save to cache
                    Paper.book().write("cache", new Gson().toJson(response.body()));
                    swipeRefreshLayout.setRefreshing(false);
                }

                @Override
                public void onFailure(Call<PhotoGallery> call, Throwable t) {
                    Log.e("Failure", "Failure" + t.getMessage());
                }
            });
        }
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.toolbar_menu,menu);
        super.onCreateOptionsMenu(menu, inflater);
        final MenuItem item = menu.findItem(R.id.action_search);
        SearchView searchView = (SearchView) item.getActionView();
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                if(query != "") {
                    dialog.show();
                    service.getSearchPhoto(Common.API_KEY, Common.extras,Common.hasGeo, query).enqueue(new Callback<PhotoGallery>() {
                        @Override
                        public void onResponse(Call<PhotoGallery> call, Response<PhotoGallery> response) {
                            Common.photos = response.body().getPhotos().getPhoto();
                            adapter = new ListAdapter(getContext(), response.body());
                            adapter.notifyDataSetChanged();
                            recyclerView.setAdapter(adapter);

                            //Save to cache
                            Paper.book().write("cache", new Gson().toJson(response.body()));
                            swipeRefreshLayout.setRefreshing(false);
                            dialog.dismiss();
                        }

                        @Override
                        public void onFailure(Call<PhotoGallery> call, Throwable t) {

                        }
                    });
                }
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {

                return false;
            }
        });

    }


}

