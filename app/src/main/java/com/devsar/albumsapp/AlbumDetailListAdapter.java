package com.devsar.albumsapp;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.HttpHeaderParser;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;
import com.devsar.albumsapp.albumSupport.Album;
import com.devsar.albumsapp.albumSupport.AlbumPicture;
import com.devsar.albumsapp.albumSupport.Connector;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by lilauth on 7/6/17.
 */

public class AlbumDetailListAdapter extends BaseAdapter {
    private Context myContext;
    private Album album;
    private String base_url = "http://jsonplaceholder.typicode.com/albums/";
    private RequestQueue requestQueue;
    private ImageLoader imageLoader;

    public AlbumDetailListAdapter(Context context, Album al){
        this.myContext = context;
        this.album = al;

        requestQueue = Connector.getRequestQueue(context);
        imageLoader = Connector.getImageLoader();
        //load data
        String url = base_url + String.valueOf(album.getId())+"/photos";
        Request jsonObjectRequest = new Request(Request.Method.GET, url, new AlbumDetailListAdapter.ResponseErrorListener()) {
            @Override
            protected Response<List<AlbumPicture>> parseNetworkResponse(NetworkResponse response) {
                try {
                    String json = new String(response.data, HttpHeaderParser.parseCharset(response.headers));
                    Gson gson = new Gson();
                    Type listType = new TypeToken<List<AlbumPicture>>(){}.getType();
                    ArrayList<AlbumPicture> extraData = new ArrayList<>();
                    extraData = gson.fromJson(json, listType);
                    album.setAlbumPictureAndExtraData(extraData);
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
        requestQueue.add(jsonObjectRequest);
    }

    @Override
    public int getCount() {
        Log.e("getCount()", String.valueOf(album.getAlbumPictureAndExtraData().size()));
        return album.getAlbumPictureAndExtraData().size();
    }

    @Override
    public AlbumPicture getItem(int position) {
        return album.getAlbumPictureAndExtraData().get(position);
    }

    @Override
    public long getItemId(int position) {
        return album.getAlbumPictureAndExtraData().get(position).getId();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View resultView;
        AlbumDetailListAdapterViewHolder viewHolder;
        Log.e("view", "creating detail list view adapter");
        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) myContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            resultView =  inflater.inflate(R.layout.list_adapter_details, parent, false);

            viewHolder = new AlbumDetailListAdapterViewHolder();
            viewHolder.picture_title = (TextView) resultView.findViewById(R.id.lb_picture_title);
            viewHolder.icon = (NetworkImageView) resultView.findViewById(R.id.album_icon);

            resultView.setTag(viewHolder);
        }
        else{
            // Reuse a created view
            resultView = convertView;
            viewHolder = (AlbumDetailListAdapterViewHolder) resultView.getTag();
        }

        AlbumPicture albumPicture = album.getAlbumPictureAndExtraData().get(position);

        viewHolder.picture_title.setText(albumPicture.getTitle());
        //photo load here
        viewHolder.icon.setImageUrl(albumPicture.getThumbnailUrl(), imageLoader);
        return resultView;
    }

    private static class AlbumDetailListAdapterViewHolder {
        TextView picture_title;
        NetworkImageView icon;

    }

    private class ResponseErrorListener implements Response.ErrorListener{

        @Override
        public void onErrorResponse(VolleyError error) {
            Log.e("error", error.getMessage());
        }
    }

}
