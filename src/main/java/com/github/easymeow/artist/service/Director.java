package com.github.easymeow.artist.service;

import com.github.easymeow.artist.entity.Musician;
import com.github.easymeow.artist.entity.Release;
import com.github.easymeow.artist.entity.Song;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;
import java.util.stream.Collectors;

public class Director implements Producer {
    private static final Logger LOG = LoggerFactory.getLogger(Director.class.getName());
    private final Map<Musician, List<Release>> musicians = new HashMap<>();

    public Director() {
    }

    @Override
    public void createRelease(Musician artist, Release album) {
        Objects.requireNonNull(artist);
        Objects.requireNonNull(album);

        if (artist.getAlbums().contains(album)) {
            throw new IllegalStateException("Album already exists");
        }

        if (album.getMusician() != null) {
            throw new IllegalStateException("Musician already exists");
        }

        if (!album.getState().equals(Release.State.CREATED)) {
            throw new IllegalStateException("ALbum is not created");
        }

        if (!musicians.containsKey(artist)) {
            musicians.put(artist, new ArrayList<>());
        }

        album.setMusician(artist);
        album.setState(Release.State.ASSIGNED);

        List<Release> releases = musicians.get(artist);
        releases.add(album);

        artist.getAlbums().clear();
        artist.getAlbums().addAll(releases);
    }

    @Override
    public void release(Release album) {
        Objects.requireNonNull(album);

        if (!album.getState().equals(Release.State.ASSIGNED)) {
            throw new IllegalStateException("ALbum is not assigned");
        }

        album.setState(Release.State.RELEASED);
    }

    @Override
    public void addSong(Release album, Song song) {
        Objects.requireNonNull(album);
        Objects.requireNonNull(song);

        if (album.isReleased()) {
            throw new IllegalStateException("ALbum is released");
        }

        album.getSongList().add(song);
    }

    @Override
    public void deleteAlbum(Release album) {
        Objects.requireNonNull(album);
        Objects.requireNonNull(album.getMusician());

        if (album.isReleased()) {
            throw new IllegalStateException("Album is released");
        }

        List<Release> releases = musicians.get(album.getMusician());
        releases.remove(album);
        album.getMusician().getAlbums().clear();
        album.getMusician().getAlbums().addAll(releases);
    }

    @Override
    public List<Release> getReleases(Musician musician) {
        Objects.requireNonNull(musician);

        return musicians.get(musician);
    }

    @Override
    public Musician getMusicians(Release album) {
        Objects.requireNonNull(album);

        return album.getMusician();
    }

    @Override
    public List<Release> getAllSinglesByPerformer(Musician musician) {
        List<Release> releases = musicians.get(musician);
        return releases.stream()
                .filter(Release::isSingle)
                .collect(Collectors.toList());
    }

    /**
     * @return все одинаковые песни разных исполнителей
     */
    @Override
    public List<Song> getAllSongsByName(String name) { //
        return musicians.values().stream()
                .flatMap(Collection::stream)
                .map(Release::getSongList)
                .flatMap(Collection::stream)
                .filter(s -> s.getName().equals(name))
                .map(Song.class::cast)
                .collect(Collectors.toList());
    }
}
