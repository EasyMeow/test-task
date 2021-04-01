package com.github.easymeow.artist.entity;

public class Song  {
    private String name;
//    private Album albumName;

    public Song() {
    }

    public Song(String name) {
        this.name = name;
    }

//    public Album getAlbumName() {
//        return albumName;
//    }
//
//    public void setAlbumName(Album albumName) {
//        this.albumName = albumName;
//    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }


    @Override
    public String toString() {
        return name;
    }

}
