package com.github.easymeow.artist.entity;

public interface ArtistInterface {
    String toString();

    void addAlbumName(Album album);

    void deleteAlbum(Album album);
}
