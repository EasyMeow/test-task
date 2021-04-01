package com.github.easymeow.artist.service;

import com.github.easymeow.artist.entity.AlbumGroup;
import com.github.easymeow.artist.entity.Musician;
import com.github.easymeow.artist.entity.Release;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Showman implements Producer {
    HashMap<Musician, List<Release>> musicians = new HashMap<>();

    @Override
    public void release(Musician artist, Release album) {
        // TODO check for null
        // TODO check album duplicates

        if (!musicians.containsKey(artist)) {
            musicians.put(artist, new ArrayList<>());
        }

        List<Release> releases = musicians.get(artist);
        releases.add(album);

        artist.getAlbums().clear();
        artist.getAlbums().addAll(releases);
    }

    @Override
    public void deleteAlbum(AlbumGroup artist, Release album) {
        // TODO
    }

    // TODO add method with some filters
    @Override
    public List<Release> getReleases(Musician musician) {
        // TODO
        return null;
    }

    @Override
    public List<Musician> getReleases(Release album) {
        // TODO
        return null;
    }
}
