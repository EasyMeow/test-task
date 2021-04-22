package com.github.easymeow.artist.service;

import com.github.easymeow.artist.entity.Musician;
import com.github.easymeow.artist.entity.Release;
import com.github.easymeow.artist.entity.Song;
import com.github.easymeow.artist.exceptions.ExistingException;
import com.github.easymeow.artist.service.chain.ArtistChain;
import com.github.easymeow.artist.service.chain.WriteBand;
import com.github.easymeow.artist.service.chain.WriteSolo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class Director implements Producer {
    private static final Logger LOG = LoggerFactory.getLogger(Director.class.getName());
    private final Map<Musician, List<Release>> musicians = new HashMap<>();

    public Director() {
    }

    /**
     * создание альбома
     * @param artist artist
     * @param album  album
     */
    @Override
    public void createRelease(Musician artist, Release album) {
        Objects.requireNonNull(artist, "artist mustn't be null");
        Objects.requireNonNull(album, "album mustn't be null");

        if (artist.getAlbums().contains(album)) {
            throw new ExistingException("Album already exists") {
            };
        }

        if (album.getMusician() != null) {
            throw new ExistingException("Musician already exists");
        }


        if (!musicians.containsKey(artist)) {
            musicians.put(artist, new ArrayList<>());
        }

        album.setMusician(artist);
        album.setState(Release.State.ASSIGNED);
        //LOG.info("Album: "+album.toString()+"is ASSIGNED");

        List<Release> releases = musicians.get(artist);
        releases.add(album);

        artist.getAlbums().clear();
        artist.getAlbums().addAll(releases);
    }

    /**
     * релиз альбома
     */
    @Override
    public void release(Release album) {
        Objects.requireNonNull(album, "album mustn't be null");

        if (!album.getState().equals(Release.State.ASSIGNED)) {
            throw new ExistingException("ALbum is not assigned");
        }
        if (album.getState().equals(Release.State.RELEASED)) {
            throw new ExistingException("ALbum is already released");
        }

        album.setState(Release.State.RELEASED);
        LOG.info("Album: " + album.getName().toString() + " is RELEASED");
    }

    /**
     * добавление песни в релиз
     */
    @Override
    public void addSong(Release album, Song song) {
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
    public void deleteAlbum(Release album) {
        Objects.requireNonNull(album, "album mustn't be null");
        Objects.requireNonNull(album.getMusician(), "musician mustn't be null");

        if (album.isReleased()) {
            throw new ExistingException("Album is released");
        }

        List<Release> releases = musicians.get(album.getMusician());
        releases.remove(album);
        album.getMusician().getAlbums().clear();
        album.getMusician().getAlbums().addAll(releases);
    }

    /**
     * @return получение списка релизов по музыканту
     */
    @Override
    public List<Release> getReleases(Musician musician) {
        Objects.requireNonNull(musician, "musician mustn't be null");
        return musicians.get(musician);
    }

    /**
     * @return получение музыканта по релизу
     */
    @Override
    public Musician getMusicians(Release album) {
        Objects.requireNonNull(album, "album mustn't be null");
        return album.getMusician();
    }

    /**
     * @return получение всех синглов по заданному исполнителю
     */
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

    public void addStatus(Musician musician) {
        ArtistChain artistChain = new WriteSolo();
        artistChain.linkWith(new WriteBand());
        artistChain.addStatus(musician);
    }
}
