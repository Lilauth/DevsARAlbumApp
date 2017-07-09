package com.devsar.albumsapp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import com.devsar.albumsapp.albumSupport.Album;
import java.util.List;

/**
 * Created by lilauth on 7/6/17.
 */

public class AlbumListAdapter extends BaseAdapter {
    private Context myContext;
    private List<Album> albums;
    private AlbumListAdapterListener mListener;

    public AlbumListAdapter(Context context, AlbumListAdapterListener listener, List<Album> albums){
        this.myContext = context;
        this.albums = albums;
        this.mListener = listener;
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
        void onAlbumClick(Album a);
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
