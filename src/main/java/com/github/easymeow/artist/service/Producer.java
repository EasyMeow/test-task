package com.github.easymeow.artist.service;

import com.github.easymeow.artist.entity.Album;
import com.github.easymeow.artist.entity.Musician;
import com.github.easymeow.artist.entity.Song;

import java.util.List;

public interface Producer {

    void createRelease(Musician artist, Album album);

    void release(Album album);

    void addSong(Album album, Song song);

    void deleteAlbum(Album album);

    List<Album> getReleases(Musician album);

    Musician getMusicians(Album album);

    List<Album> getAllSinglesByPerformer(Musician musician);

    void updateAlbum(Album album);
}
