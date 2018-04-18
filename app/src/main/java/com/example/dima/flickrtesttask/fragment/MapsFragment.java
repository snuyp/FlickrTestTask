package com.example.dima.flickrtesttask.fragment;

import android.Manifest;
import android.app.AlertDialog;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.SearchView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.example.dima.flickrtesttask.Interface.FlickrService;
import com.example.dima.flickrtesttask.R;
import com.example.dima.flickrtesttask.common.Common;
import com.example.dima.flickrtesttask.model.Location;
import com.example.dima.flickrtesttask.model.PhotoGallery;
import com.example.dima.flickrtesttask.model.PhotoLocation;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import dmax.dialog.SpotsDialog;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MapsFragment extends Fragment implements OnMapReadyCallback {
    private static final int PERMISSIONS_REQUEST_CODE = 100;
    private GoogleMap mMap;
    private MapView mapView;
    private FlickrService service;
    private AlertDialog dialog;
    private static MapsFragment sGoogleMapsFragment = null;

    public static MapsFragment getInstance() {

        if (sGoogleMapsFragment == null) {
            sGoogleMapsFragment = new MapsFragment();
        }
        return sGoogleMapsFragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setUpLocation();
        Gson gson = new GsonBuilder().setLenient().create();
        service = Common.getFlcikrService(gson);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        setHasOptionsMenu(true);
        dialog = new SpotsDialog(getActivity());
        return inflater.inflate(R.layout.fragment_google_maps, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mapView = view.findViewById(R.id.map);
        if (mapView != null) {
            mapView.onCreate(null);
            mapView.onResume();

            mapView.getMapAsync(this);

            mapView.onStop();
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mapView.onDestroy();
    }

    public void setMyLocation(boolean isLocation) {
        if (ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            mMap.setMyLocationEnabled(isLocation);
        }
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {

        mMap = googleMap;
        MapsInitializer.initialize(getContext());
        mMap = googleMap;

        setMyLocation(true);
        mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
        mMap.getUiSettings().isMapToolbarEnabled();
        LatLng minsk = new LatLng(53.9008552, 27.5413905);
        mMap.addMarker(new MarkerOptions().position(minsk).title("Hello Minsk!"));

        CameraPosition cameraPosition = CameraPosition.builder().
                target(minsk).zoom(5).bearing(0).build();
        mMap.moveCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));


    }

    public void addGeoMarker() {
        if (Common.photos != null) {
            mMap.clear();
            for (int i = 0; i < Common.photos.size(); i++) {
                final int finalI = i;
                service.getLocation(Common.API_KEY, Common.photos.get(i).getId(), Common.extras).enqueue(new Callback<PhotoLocation>() {
                    @Override
                    public void onResponse(Call<PhotoLocation> call, Response<PhotoLocation> response) {

                        Location location = response.body().getPhoto().getLocation();

                        LatLng latLng = new LatLng(
                                Double.parseDouble(location.getLatitude()),
                                Double.parseDouble(location.getLongitude()));
                        String title = "";
                        if (Common.photos.get(finalI).getTitle().length() > 20) {
                            title = Common.photos.get(finalI).getTitle().substring(0, 20);
                        } else {
                            title = Common.photos.get(finalI).getTitle();
                        }
                        mMap.addMarker(new MarkerOptions().position(latLng).title(title)).isVisible();
                        dialog.dismiss();
                    }

                    @Override
                    public void onFailure(Call<PhotoLocation> call, Throwable t) {

                    }
                });
            }
        }

    }

    @Override
    public void onCreateOptionsMenu(final Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.toolbar_menu, menu);
        super.onCreateOptionsMenu(menu, inflater);
        final MenuItem item = menu.findItem(R.id.action_search);
        SearchView searchView = (SearchView) item.getActionView();
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                dialog.show();
                Common.photos = null;
                if (query != "") {
                    String geo = String.format("geo, %s", query);
                    service.getSearchPhoto(Common.API_KEY, Common.extras, Common.hasGeo, geo).enqueue(new Callback<PhotoGallery>() {
                        @Override
                        public void onResponse(Call<PhotoGallery> call, Response<PhotoGallery> response) {
                            Common.photos = response.body().getPhotos().getPhoto();
                            addGeoMarker();
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


    private void setUpLocation() {
        if (ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(getActivity(), new String[]{
                    Manifest.permission.ACCESS_COARSE_LOCATION,
                    Manifest.permission.ACCESS_FINE_LOCATION}, PERMISSIONS_REQUEST_CODE);
        } else {

        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(getActivity(), new String[]{
                            Manifest.permission.ACCESS_COARSE_LOCATION,
                            Manifest.permission.ACCESS_FINE_LOCATION},
                    PERMISSIONS_REQUEST_CODE);
        } else {

        }

    }
}
