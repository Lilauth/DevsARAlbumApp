package com.devsar.albumsapp.albumSupport;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by lilauth on 7/6/17.
 */

public class Album implements Serializable{

    private int userID;
    private int id;
    private String title;
    private List<AlbumPicture> albumPictureAndExtraData;

    public Album(int userID, int id, String title) {
        this.userID = userID;
        this.id = id;
        this.title = title;
    }

    public int getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public void setAlbumPictureAndExtraData(List<AlbumPicture> extraData){
        albumPictureAndExtraData = extraData;
    }

    public List<AlbumPicture> getAlbumPictureAndExtraData(){
        if(albumPictureAndExtraData == null){albumPictureAndExtraData = new ArrayList<>();}
        return albumPictureAndExtraData;}
}
