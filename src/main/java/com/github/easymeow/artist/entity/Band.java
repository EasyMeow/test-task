package com.github.easymeow.artist.entity;

import java.util.ArrayList;
import java.util.List;

public class Band extends AbstractPerformer {
    private final List<Musician> artists = new ArrayList<>();

    public Band(String name) {
        super(name);
    }

    public Band() {
    }

    public List<Musician> getArtists() {
        return artists;
    }

    public void deleteArtist(Artist artist) {
        artists.remove(artist);
    }

    public void setArtist(List<Musician> artists) {
        this.artists.addAll(artists);
    }
}
