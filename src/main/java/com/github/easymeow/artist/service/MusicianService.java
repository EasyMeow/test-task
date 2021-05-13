package com.github.easymeow.artist.service;

import com.github.easymeow.artist.entity.Artist;
import com.github.easymeow.artist.entity.Band;
import com.github.easymeow.artist.entity.Musician;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public interface MusicianService {
    Artist createArtist(String name);

    Band createBand(String Name, List<Artist> artists);

    List<Musician> getAll();

    List<Artist> getArtists();

    List<Band> getBands();

    void updateArtist(Musician artist);

    void updateBand(Band band);

    List<Musician> getHierarchy();

    void updateAvailableArtists(ArrayList<Artist> before, Set<Artist> selectedItems);

    List<Artist> getAvailableArtists();

    List<Artist> getUnavailableArtists();
}
