package com.devsar.albumsapp;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.android.volley.RequestQueue;
import com.devsar.albumsapp.albumSupport.Album;

import java.io.Serializable;


public class AlbumList extends Fragment implements AlbumListAdapter.AlbumListAdapterListener{

    private static final String ARG_PARAM1 = "param1";
    private AlbumListAdapter adapter;

    private OnAlbumListInteractionListener mListener;

    public AlbumList() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment AlbumList.
     */
    // TODO: Rename and change types and number of parameters
    public static AlbumList newInstance() {
        AlbumList fragment = new AlbumList();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_album_list, container, false);
        adapter = new AlbumListAdapter(getActivity(),this);
        ListView album_list = (ListView) rootView.findViewById(R.id.album_list);
        album_list.setAdapter(adapter);
        return rootView;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnAlbumListInteractionListener) {
            mListener = (OnAlbumListInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    @Override
    public void onAlbumClick(Album album) {
        //for this album, retrieve data and images
        mListener.showAlbumInformation(album);
    }

    public interface OnAlbumListInteractionListener{
        public void showAlbumInformation(Album album);
    }
}
