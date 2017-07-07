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
import com.android.volley.toolbox.Volley;
import com.devsar.albumsapp.albumSupport.Album;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by lilauth on 7/6/17.
 */

public class AlbumListAdapter extends BaseAdapter {
    private Context myContext;
    private List<Album> albums;
    private AlbumListAdapterListener mListener;
    private RequestQueue requestQueue;
    private String url = "http://jsonplaceholder.typicode.com/albums/";

    public AlbumListAdapter(Context context, AlbumListAdapterListener listener){
        this.myContext = context;
        //this.albums = albums;
        this.albums = new ArrayList<>();
        this.mListener = listener;

        requestQueue = Volley.newRequestQueue(myContext);
        //load data
        Request jsonObjectRequest = new Request(Request.Method.GET, url, new ResponseErrorListener()) {
            @Override
            protected Response<List<Album>> parseNetworkResponse(NetworkResponse response) {
                try {
                    String json = new String(response.data, HttpHeaderParser.parseCharset(response.headers));
                    json = "{\"albums\":" +json+"}";
                    JSONObject Jobject = new JSONObject(json);
                    JSONArray Jarray = Jobject.getJSONArray("albums");
                    for (int i = 0; i < Jarray.length(); i++) {
                        JSONObject object = Jarray.getJSONObject(i);
                        //complete album info
                        Album a = new Album(((Integer) object.get("userId")),((Integer)object.get("id")), (String) object.get("title"));
                        albums.add(a);
                    }

                } catch (JSONException e) {
                    Log.e("giving up", response.toString());
                    e.printStackTrace();
                } catch (UnsupportedEncodingException e) {
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
        requestQueue.add(jsonObjectRequest);
    }

    @Override
    public int getCount() {
        return albums.size();
    }

    @Override
    public Album getItem(int position) {
        return albums.get(position);
    }

    @Override
    public long getItemId(int position) {
        return albums.get(position).getId();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View resultView;
        AlbumsListAdapterViewHolder viewHolder;

        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) myContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            resultView =  inflater.inflate(R.layout.list_adapter_album, parent, false);

            viewHolder = new AlbumsListAdapterViewHolder();
            viewHolder.lb_id = (TextView) resultView.findViewById(R.id.lb_id);
            viewHolder.lb_title = (TextView) resultView.findViewById(R.id.lb_title);

            resultView.setTag(viewHolder);
        }
        else{
            // Reuse a created view
            resultView = convertView;
            viewHolder = (AlbumsListAdapterViewHolder) resultView.getTag();
        }

        Album album = albums.get(position);

        viewHolder.lb_id.setText(String.valueOf(album.getId()));
        viewHolder.lb_title.setText(album.getTitle());

        viewHolder.lb_title.setOnClickListener(new GetAlbumDetails(album));

        return resultView;
    }

    private static class AlbumsListAdapterViewHolder {
        TextView lb_id;
        TextView lb_title;

    }

    public interface AlbumListAdapterListener{
        public void onAlbumClick(Album a);
    }

    private class ResponseErrorListener implements Response.ErrorListener{

        @Override
        public void onErrorResponse(VolleyError error) {
            Log.e("error", error.getMessage());
        }
    }

    private class GetAlbumDetails implements  View.OnClickListener{
        private Album mAlbum;

        public GetAlbumDetails(Album album){
            mAlbum = album;
        }

        @Override
        public void onClick(View v) {
            mListener.onAlbumClick(mAlbum);
        }
    }
}
