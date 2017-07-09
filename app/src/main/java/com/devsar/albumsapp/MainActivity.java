package com.devsar.albumsapp;

import android.net.Uri;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import com.devsar.albumsapp.albumSupport.Album;

public class MainActivity extends AppCompatActivity implements AlbumList.OnAlbumListInteractionListener,
                                                               AlbumDetailFragment.OnFragmentInteractionListener{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //on create  start albumFragment with a list
        //fragment_container = R.id.fragment_container;
        //load albums and passes to fragment
        if (savedInstanceState == null) { // First execution
            start();
        }
        Log.e("MainActivity", "onCreate");
    }

    private void start(){
        Fragment fragment = new AlbumList();
        android.support.v4.app.FragmentTransaction fm = getSupportFragmentManager().beginTransaction();
        fm.replace(R.id.fragment_container, fragment);
        // fm.addToBackStack(null);
        fm.commit();
    }

    @Override
    public void onFragmentInteraction(Uri uri) {
        //
    }

    @Override
    public void showAlbumInformation(Album album) {
        Fragment fragment = AlbumDetailFragment.newInstance(album);
        android.support.v4.app.FragmentTransaction fm = getSupportFragmentManager().beginTransaction();
        //fm.addToBackStack("Detail Fragment");
        fm.replace(R.id.fragment_container, fragment);
        fm.addToBackStack(null);
        fm.commit();
    }

}
