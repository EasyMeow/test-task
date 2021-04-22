package com.github.easymeow.artist.entity;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public abstract class AbstractPerformer implements Musician {
    private String name;
    private List<Release> albums = new ArrayList<>();

    public AbstractPerformer(String name) {
        this.name = name;
    }

    public void setName(String name) {
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
        AbstractPerformer that = (AbstractPerformer) o;
        return name.equals(that.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name);
    }

    @Override
    public String toString() {
        return name ;
    }
}
