package com.github.easymeow.artist.entity;

public interface Producer {

    void release(AlbumGroup artist,Release album);

    void deleteAlbum(AlbumGroup artist,Release album);
}
