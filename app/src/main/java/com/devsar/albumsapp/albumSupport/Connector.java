package com.devsar.albumsapp.albumSupport;

import android.content.Context;
import android.graphics.Bitmap;
import android.support.v4.util.LruCache;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.Volley;

/**
 * Created by laura on 08/07/17.
 */

public class Connector {
    private static RequestQueue requestQueue = null;
    private static ImageLoader imageLoader = null;

    public static RequestQueue getRequestQueue(Context context){
        if(requestQueue == null){
            requestQueue = Volley.newRequestQueue(context);
        }
        return requestQueue;
    }

    public static ImageLoader getImageLoader(){
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
