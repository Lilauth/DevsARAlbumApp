package com.devsar.albumsapp.albumSupport;

import android.content.Context;
import android.graphics.Bitmap;
import android.support.annotation.NonNull;
import android.support.v4.util.LruCache;
import android.util.Log;

import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.HttpHeaderParser;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.Volley;
import com.devsar.albumsapp.AlbumDetailListAdapter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by laura on 08/07/17.
 */

public class Connector {
    private String base_url = "http://jsonplaceholder.typicode.com/albums/";

    private static RequestQueue requestQueue = null;
    private static ImageLoader imageLoader = null;

    public static RequestQueue getRequestQueue(Context context){
        if(requestQueue == null){
            requestQueue = Volley.newRequestQueue(context);
        }
        return requestQueue;
    }

    public static ImageLoader getImageLoader(){
        //you should never call getImageLoader() before getRequestLoader(Context c)
        if(imageLoader == null){
            imageLoader = new ImageLoader(requestQueue, new ImageLoader.ImageCache(){
                private final LruCache<String, Bitmap> mCache = new LruCache<String, Bitmap>(10);
                @Override
                public Bitmap getBitmap(String url) {
                    return mCache.get(url);
                }

                @Override
                public void putBitmap(String url, Bitmap bitmap) {
                    mCache.put(url, bitmap);
                }
            });
        }
        return imageLoader;
    }

}
