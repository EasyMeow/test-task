package com.github.easymeow.artist.entity;

import java.util.ArrayList;
import java.util.List;

public abstract class AlbumGroup implements Musician {
    private final String name;
    private List<Release> albums = new ArrayList<>();

    public AlbumGroup(String name) {
        this.name = name;
    }

    public void addAlbum(Release album) {
        albums.add(album);
    }

    public void setAlbums(List<Release> albums) {
        this.albums = albums;
    }

    public void deleteAlbum(Release album) {
        albums.remove(album);
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public List<Release> getAlbums() {
        return albums;
    }

    @Override
    public String toString() {
        return name + '\n' + albums.toString();
    }
}
