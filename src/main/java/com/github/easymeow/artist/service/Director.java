package com.github.easymeow.artist.service;

import com.github.easymeow.artist.entity.*;

import java.util.*;
import java.util.logging.Logger;
import java.util.stream.Collectors;

public class Director implements Producer, RecordManager {

    private static Logger LOG = Logger.getLogger(Director.class.getName());

    private Map<Musician, List<Release>> musicians = new HashMap<>();

    public Director() {
    }


    @Override
    public void release(Musician artist, Release album) {
        if (artist == null || album == null) {
            LOG.info("Musician and album can't be null");
            throw new NullPointerException();
        }

        if (artist.getAlbums().contains(album)) {
            LOG.info("Album already exists");
            throw new IllegalStateException();
        }

        if (!musicians.containsKey(artist)) {
            musicians.put(artist, new ArrayList<>());
        }

        album.addMusician(artist);

        List<Release> releases = musicians.get(artist);
        releases.add(album);

        artist.getAlbums().clear();
        artist.getAlbums().addAll(releases);


    }

    @Override
    public void addSong(Release album, Song song) {
        if (song == null || album == null) {
            LOG.warning("Song and album can't be null");
            throw new NullPointerException();
        }
        album.getSongList().add(song);
    }


    @Override
    public void deleteAlbum(AbstractPerformer artist, Release album) {
        if (artist == null || album == null) {
            LOG.info("Artist and album can't be null");
            throw new NullPointerException();
        }
        artist.getAlbums().remove(album);
    }
    // TODO add method with some filters

    @Override
    public List<Release> getReleases(Musician musician) {
        if (musician == null) {
            LOG.info("Musician can't be null");
            throw new NullPointerException();
        }
        return musician.getAlbums();
    }

    @Override
    public Musician getMusicians(Release album) {
        if (album == null) {
            LOG.info("Album can't be null");
            throw new NullPointerException();
        }
        return album.getMusician();
    }

//    @Override
//    public List<Musician> getMusicians(Release album) {
//        if (album == null) {
//            LOG.info("Album can't be null");
//            throw new NullPointerException();
//        }
//        return album.getMusician();
//    }

//    @Override
//    public List<Single> getAllSinglesByPerformer(Musician musician) {
//        List<Release> releases = musicians.get(musician);
//        List<Single> singles = new ArrayList<>();
//        for (Release release : releases) {
//            if (release instanceof Single) {
//                singles.add((Single) release);
//            }
//        }
//        return singles;
//    }

    @Override
    public List<Single> getAllSinglesByPerformer(Musician musician) {
        List<Release> releases = musicians.get(musician);
        return releases.stream()
                .filter(Single.class::isInstance)
                .map(Single.class::cast)
                .collect(Collectors.toList());
    }

    @Override
    public List<Song> getAllSongsByName(String name) { //все одинаковые песни разных исполнителей
        return musicians.values().stream()
                .flatMap(Collection::stream)
                .map(Release::getSongList)
                .flatMap(Collection::stream)
                .filter(s -> s.getName().equals(name))
                .map(Song.class::cast)
                .collect(Collectors.toList());
    }


}
