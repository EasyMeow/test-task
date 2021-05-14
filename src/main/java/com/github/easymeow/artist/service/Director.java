package com.github.easymeow.artist.service;

import com.github.easymeow.artist.entity.*;
import com.github.easymeow.artist.exceptions.ExistingException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;


@Service
public class Director implements Producer {
    private static final Logger LOG = LoggerFactory.getLogger(Director.class.getName());
    private final Map<Musician, List<Album>> musicians = new HashMap<>();

    public Director() {

        Album album = new Album("album");
        Artist artist = new Artist("artist");
        musicians.put(artist, Collections.singletonList(album));
    }

    /**
     * создание альбома
     *
     * @param artist artist
     * @param album  album
     */
    @Override
    public void createRelease(Musician artist, Album album) {
        Objects.requireNonNull(artist, "artist mustn't be null");
        Objects.requireNonNull(album, "album mustn't be null");

        if (album.getMusician() != null) {
            throw new ExistingException("Musician already exists");
        }

        if (!musicians.containsKey(artist)) {
            musicians.put(artist, new ArrayList<>());
        } else {
            if (musicians.get(artist).contains(album)) {
                throw new ExistingException("Album already exists");
            }
        }

        album.setMusician(artist);
        album.setState(Release.State.ASSIGNED);
        //LOG.info("Album: "+album.toString()+"is ASSIGNED");

        List<Album> releases = musicians.get(artist);
        releases.add(album);
    }


    /**
     * релиз альбома
     */
    @Override
    public void release(Album album) {
        Objects.requireNonNull(album, "album mustn't be null");

        if (!album.getState().equals(Album.State.ASSIGNED)) {
            throw new ExistingException("ALbum is not assigned");
        }
        if (album.getState().equals(Album.State.RELEASED)) {
            throw new ExistingException("ALbum is already released");
        }

        album.setState(Album.State.RELEASED);
        LOG.info("Album: " + album.getName() + " is RELEASED");
    }

    public List<Album> getAll() {
        return musicians.values().stream()
                .flatMap(m -> m.stream())
                .collect(Collectors.toList());
    }

    /**
     * добавление песни в релиз
     */
    @Override
    public void addSong(Album album, Song song) {
        Objects.requireNonNull(album, "album mustn't be null");
        Objects.requireNonNull(song, "song mustn't be null");

        if (album.isReleased()) {
            throw new ExistingException("ALbum is released");
        }

        album.getSongList().add(song);
    }

    /**
     * удаление релиза
     */
    @Override
    public void deleteAlbum(Album album) {
        Objects.requireNonNull(album, "album mustn't be null");
        Objects.requireNonNull(album.getMusician(), "musician mustn't be null");

        if (album.isReleased()) {
            throw new ExistingException("Album is released");
        }

        List<Album> releases = musicians.get(album.getMusician());
        releases.remove(album);
    }

    /**
     * @return получение списка релизов по музыканту
     */
    @Override
    public List<Album> getReleases(Musician musician) {
        Objects.requireNonNull(musician, "musician mustn't be null");
        return musicians.get(musician);
    }

    /**
     * @return получение музыканта по релизу
     */
    @Override
    public Musician getMusicians(Album album) {
        Objects.requireNonNull(album, "album mustn't be null");
        return album.getMusician();
    }

    /**
     * @return получение всех синглов по заданному исполнителю
     */
    @Override
    public List<Album> getAllSinglesByPerformer(Musician musician) {
        List<Album> releases = musicians.get(musician);
        return releases.stream()
                .filter(Release::isSingle)
                .collect(Collectors.toList());
    }

    @Override
    public void updateAlbum(Album album) {
        //update in database
    }


}
