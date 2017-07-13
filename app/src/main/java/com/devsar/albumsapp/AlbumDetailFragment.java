package com.devsar.albumsapp;

import android.content.Context;
import android.net.Uri;
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
import com.devsar.albumsapp.albumSupport.AlbumPicture;
import com.devsar.albumsapp.albumSupport.Connector;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.Serializable;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link AlbumDetailFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link AlbumDetailFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class AlbumDetailFragment extends Fragment {
    private AlbumDetailListAdapter adapter;

    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String KEY_ALBUM = "com.devsar.albumsapp.AlbumDetailFragment.album";

    private Album album;

    private String base_url = "http://jsonplaceholder.typicode.com/albums/";

    private OnFragmentInteractionListener mListener;

    public AlbumDetailFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment AlbumDetailFragment.
     */
    public static AlbumDetailFragment newInstance(Album album) {
        AlbumDetailFragment fragment = new AlbumDetailFragment();
        Bundle args = new Bundle();
        args.putSerializable(KEY_ALBUM, album);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            album = (Album) getArguments().getSerializable(KEY_ALBUM);
            Log.e("ret", "attempting to call retrieve data, for extra data");
            if(savedInstanceState == null){
                retrieveData();
            }
        }
        adapter = new AlbumDetailListAdapter(getActivity(), album);
    }

    @Override
    public void onSaveInstanceState (Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putSerializable(KEY_ALBUM, album);
    }

    private void retrieveData(){
        //load data
        String url = base_url + String.valueOf(album.getId())+"/photos";
        Request jsonObjectRequest = new Request(Request.Method.GET, url, new ResponseErrorListener()) {
            @Override
            protected Response<List<AlbumPicture>> parseNetworkResponse(NetworkResponse response) {
                try {
                    String json = new String(response.data, HttpHeaderParser.parseCharset(response.headers));
                    Gson gson = new Gson();
                    Type listType = new TypeToken<List<AlbumPicture>>(){}.getType();
                    ArrayList<AlbumPicture> extraData;
                    extraData = gson.fromJson(json, listType);
                    album.setAlbumPictureAndExtraData(extraData);
                    adapter.notifyDataSetChanged();
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
                return Response.success(album.getAlbumPictureAndExtraData(),
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
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_album_detail, container, false);
        ListView detail_list = (ListView) rootView.findViewById(R.id.album_detail_list);
        detail_list.setAdapter(adapter);
        return rootView;
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
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

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }

    private class ResponseErrorListener implements Response.ErrorListener{

        @Override
        public void onErrorResponse(VolleyError error) {
            Log.e("error", error.getMessage());
        }
    }
}
