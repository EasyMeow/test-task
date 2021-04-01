package com.github.easymeow.artist.entity;

public class Showman implements Producer{
    @Override
    public void release(AlbumGroup artist, Release album) {
        artist.addAlbum(album);
    }

    @Override
    public void deleteAlbum(AlbumGroup artist, Release album) {
        artist.deleteAlbum(album);
    }
}
