package com.github.easymeow.artist.entity;

import java.util.ArrayList;
import java.util.List;

public class Band extends AbstractPerformer {
    private List<Artist> artists = new ArrayList<>();

    public Band(String name) {
        super(name);
    }

    public Band() {
    }

    public List<Artist> getArtists() {
        return artists;
    }

    public void deleteArtist(Artist artist) {
        artists.remove(artist);
    }

    @Override
    public String getStatus() {
        return "Band";
    }

    public void setArtist(List<Artist> artists) {
        this.artists = artists;
    }
}
