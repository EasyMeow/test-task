package com.github.easymeow.artist.entity;

import java.util.ArrayList;
import java.util.List;

public class Band extends AbstractPerformer {
    private final List<Artist> artists = new ArrayList<>();

    public Band(String name) {
        super(name);
    }

    public List<Artist> getArtists() {
        return artists;
    }

    public void deleteArtist(Artist artist) {
        artists.remove(artist);
    }

    public void addArtist(Artist artist) {
        artists.add(artist);
    }
}
