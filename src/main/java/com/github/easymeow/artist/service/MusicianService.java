package com.github.easymeow.artist.service;

import com.github.easymeow.artist.entity.Artist;
import com.github.easymeow.artist.entity.Band;
import com.github.easymeow.artist.entity.Musician;

import java.util.List;

public interface MusicianService {
    Artist createArtist(String name);

    Band createBand(String Name);

    List<Musician> getAll();

    List<Artist> getArtists();

    List<Band> getBands();

    void updateArtist(Musician artist);

    void updateBand(Band band);
}
