package com.github.easymeow.artist.service;

import com.github.easymeow.artist.entity.Artist;
import com.github.easymeow.artist.entity.Band;
import com.github.easymeow.artist.entity.Musician;

import java.util.List;
import java.util.Set;

public interface MusicianService {
    Artist createArtist(String name);

    Band createBand(String Name);

    List<Musician> getAll();

    List<Musician> getArtists();

    List<Musician> getBands();

    void updateArtist(Musician artist);

    void updateBand(Musician band, Set<Musician> artists);
}
