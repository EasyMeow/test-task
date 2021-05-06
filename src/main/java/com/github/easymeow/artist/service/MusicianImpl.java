package com.github.easymeow.artist.service;

import com.github.easymeow.artist.entity.Artist;
import com.github.easymeow.artist.entity.Band;
import com.github.easymeow.artist.entity.Musician;
import com.github.easymeow.artist.service.chain.ArtistChain;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class MusicianImpl implements MusicianService {
    private final List<Musician> musicians = new ArrayList<>();
    private final ArtistChain chain;

    public MusicianImpl(List<ArtistChain> chains) {
        if (!chains.isEmpty()) {
            ArtistChain previous = chains.get(0);
            for (int i = 1; i < chains.size(); i++) {
                previous.linkWith(chains.get(i));
                previous = chains.get(i);
            }
            chain = chains.get(0);
        } else {
            chain = null;
        }
    }

    @Override
    public Artist createArtist(String name) {
        Artist artist = new Artist(name);
        addStatus(artist);
        musicians.add(artist);
        return artist;
    }

    @Override
    public Band createBand(String Name) {
        Band band = new Band(Name);
        addStatus(band);
        musicians.add(band);
        return band;
    }

    @Override
    public List<Musician> getAll() {
        return musicians;
    }

    @Override
    public List<Artist> getArtists() {
        return musicians.stream()
                .filter(s -> s instanceof Artist)
                .map(s -> (Artist) s)
                .collect(Collectors.toList());
    }

    @Override
    public List<Band> getBands() {
        return musicians.stream()
                .filter(s -> s instanceof Band)
                .map(m -> (Band) m)
                .collect(Collectors.toList());
    }

    @Override
    public void updateArtist(Musician artist) {

    }

    @Override
    public void updateBand(Band band) {
        //add to database
    }

    public void addStatus(Musician musician) {
        if (chain != null) {
            chain.addStatus(musician);
        }
    }

    @PostConstruct
    void init() {
        Artist artist = createArtist("artist");
        createBand("band").getArtists().add(artist);

    }
}
