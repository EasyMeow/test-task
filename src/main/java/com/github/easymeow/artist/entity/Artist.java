package com.github.easymeow.artist.entity;

import java.util.ArrayList;
import java.util.List;

public class Artist implements ArtistInterface {
    private String name;
    private List<Album> albums = new ArrayList<>();

    public Artist(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<Album> getAlbums() {
        return albums;
    }

    public void setAlbums(List<Album> albums) {
        this.albums = albums;
    }

    public void addAlbumName(Album albumName) {
        albums.add(albumName);
    }

    public void deleteAlbum(Album albumName) {
        albums.remove(albumName);

    }

    public String toString() {
        return name + '\n' + albums.toString();
    }
}
