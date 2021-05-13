package com.github.easymeow.artist.service;

import com.github.easymeow.artist.entity.Artist;
import com.github.easymeow.artist.entity.Band;
import com.github.easymeow.artist.entity.Musician;
import com.github.easymeow.artist.service.chain.ArtistChain;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class MusicianImpl implements MusicianService {
    private final List<Musician> musicians = new ArrayList<>();
    private final ArtistChain chain;
    private Musician buffer;

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
    public Band createBand(String Name, List<Artist> artists) {
        Band band = new Band(Name);
        addStatus(band);
        band.setArtist(artists);
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
    public List<Artist> getAvailableArtists() {
        return musicians.stream()
                .filter(m -> m instanceof Artist)
                .filter(m -> !((Artist) m).getIsMember())
                .map(m -> (Artist) m)
                .collect(Collectors.toList());

    }

    @Override
    public List<Artist> getUnavailableArtists() {
        return musicians.stream()
                .filter(m -> m instanceof Artist)
                .filter(m -> ((Artist) m).getIsMember())
                .map(m -> (Artist) m)
                .collect(Collectors.toList());

    }

    @Override
    public List<Musician> getHierarchy() {
        List<Musician> m = new ArrayList<>();
        m.addAll(getBands());
        m.addAll(getAvailableArtists());
        return m;
    }

    @Override
    public void updateAvailableArtists(ArrayList<Artist> before, Set<Artist> selectedItems) {
        for (Artist a : before) {
            if (!selectedItems.contains(a)) {
                a.setMember(false);
            }
        }
        for (Artist a : selectedItems) {
            a.setMember(true);
        }
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
        Band band = new Band("band");
        musicians.add(band);

        Artist artist2 = createArtist("artist2");

        Artist artist3 = createArtist("bbbbbb");

        Artist artist4 = createArtist("cccccccc");

    }
}
