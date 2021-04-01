package com.github.easymeow.artist.entity;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public abstract class AlbumGroup implements Musician {
    private final String name;
    private List<Release> albums = new ArrayList<>();

    public AlbumGroup(String name) {
        this.name = name;
    }

    public void setAlbums(List<Release> albums) {
        this.albums = albums;
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
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        AlbumGroup that = (AlbumGroup) o;
        return name.equals(that.name);
    }

    @Override
    public int hashCode() {
//        return name.hashCode();
        return Objects.hash(name);
    }

    @Override
    public String toString() {
        return name + '\n' + albums.toString();
    }
}
