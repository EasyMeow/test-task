package com.github.easymeow.artist.entity;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Artist extends AbstractPerformer {

    private final List<Musician> artists = new ArrayList<>();

    public Artist(String name) {
        super(name);
    }

    public Artist() {
    }

    @Override
    public List<Musician> getArtists() {
        return artists;
    }

    @Override
    public void setArtist(List<Musician> musicianList) {
        artists.addAll(Collections.emptyList());
    }
}
