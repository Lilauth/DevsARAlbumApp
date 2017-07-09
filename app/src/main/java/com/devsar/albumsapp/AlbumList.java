package com.devsar.albumsapp;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.HttpHeaderParser;
import com.devsar.albumsapp.albumSupport.Album;
import com.devsar.albumsapp.albumSupport.Connector;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import java.io.Serializable;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;


public class AlbumList extends Fragment implements AlbumListAdapter.AlbumListAdapterListener{

    private static final String KEY_ALBUMS = "com.devsar.albumsapp.AlbumList.albums";

    private AlbumListAdapter adapter; // =  new AlbumListAdapter(getActivity(),this, this.albums);
    private String url = "http://jsonplaceholder.typicode.com/albums/";
    private List<Album> albums;

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

    private void retrieveData(){
        albums = new ArrayList<>();
        Request jsonObjectRequest = new Request(Request.Method.GET, url, new ResponseErrorListener()) {
            @Override
            protected Response<List<Album>> parseNetworkResponse(NetworkResponse response) {
                try {
                    String json = new String(response.data, HttpHeaderParser.parseCharset(response.headers));
                    Gson gson = new Gson();
                    Type listType = new TypeToken<List<Album>>(){}.getType();
                    albums = gson.fromJson(json, listType);
                }  catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }

                return Response.success(albums,
                        HttpHeaderParser.parseCacheHeaders(response));

            }

            @Override
            protected void deliverResponse(Object response) {
                //
            }
        };
        Connector.getRequestQueue(getActivity()).add(jsonObjectRequest);
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if(savedInstanceState == null){
            Log.e("ret", "attempting to call retrieve data");
            retrieveData();
        }
        else{
            this.albums = (List<Album>) savedInstanceState.getSerializable(KEY_ALBUMS);
        }
        adapter = new AlbumListAdapter(getActivity(),this, this.albums);
    }

    @Override
    public void onSaveInstanceState (Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putSerializable(KEY_ALBUMS,(Serializable) albums);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_album_list, container, false);
        // adapter = new AlbumListAdapter(getActivity(),this, this.albums);
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
        void showAlbumInformation(Album album);
    }

    private class ResponseErrorListener implements Response.ErrorListener{

        @Override
        public void onErrorResponse(VolleyError error) {
            Log.e("error", error.getMessage());
        }
    }

}
