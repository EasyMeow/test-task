package com.github.easymeow.artist.service;

import com.github.easymeow.artist.entity.*;
import com.github.easymeow.artist.exceptions.ExistingException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

@Component
public class Initialiser {
    private final static Logger log = LoggerFactory.getLogger(Initialiser.class);
    private final Director director;
    private final MusicianImpl musician;
    @Qualifier("asia")
    private StudioImpl studio;

    @Autowired
    public Initialiser(Director director, MusicianImpl musician, StudioImpl studio) {
        this.director = director;
        this.musician = musician;
        this.studio = studio;
    }

    Song createSong(String songName, Musician... musician) {
        log.info(">> createSong");
        return studio.record(songName, musician);
    }

    void createReleaseByDirector(Musician musician, Album album) {
        director.createRelease(musician, album);
    }

    void releaseByDirector(Album album) {
        director.release(album);
    }

    void addSongByDirector(Album album, Song song) {
        director.addSong(album, song);
    }

    Artist createArtist(String name) {
        return musician.createArtist(name);
    }

    Band createBand(String name) {
        return musician.createBand(name);
    }


    @PostConstruct
    public void init() {
        Artist strykalo = createArtist("Валентин Стрыкало");
        log.info("New Artist " + strykalo.getName() + " is created");
        Artist deftones = createArtist("Deftones");
        log.info("New Artist " + deftones.getName() + " is created");
        Band band1 = createBand("BANDana");
        log.info("New Band " + band1.getName() + " is created");


        Song firstSong = createSong("От заката до рассвета", strykalo, deftones);
        Song secondSong = createSong("Знаешь, Таня", strykalo);

        Song thirdSong = createSong("Sextape", deftones);
        Song forthSong = createSong("Знаешь, Таня", deftones);

        Album firstSingle = new Album("Some single");
        Album firstAlbum = new Album("Развлечение");
        Album secondAlbum = new Album("Some album");

        try {
            createReleaseByDirector(strykalo, firstAlbum);
        } catch (ExistingException ex) {
            log.error("Release creation error", ex);
        }
        try {
            createReleaseByDirector(strykalo, firstSingle);
        } catch (ExistingException ex) {
            log.error("Release creation error", ex);
        }
        try {
            createReleaseByDirector(deftones, secondAlbum);
        } catch (ExistingException ex) {
            log.error("Release creation error", ex);
        }


        try {
            addSongByDirector(firstAlbum, firstSong);
        } catch (ExistingException ex) {
            log.error("Song adding error", ex);
        }
        try {
            addSongByDirector(secondAlbum, thirdSong);
        } catch (ExistingException ex) {
            log.error("Song adding error", ex);
        }
        try {
            addSongByDirector(firstSingle, secondSong);
        } catch (ExistingException ex) {
            log.error("Song adding error", ex);
        }
        try {
            addSongByDirector(secondAlbum, forthSong);
        } catch (ExistingException ex) {
            log.error("Song adding error", ex);
        }


        try {
            releaseByDirector(firstAlbum);
        } catch (ExistingException ex) {
            log.error("Album releasing error", ex);
        }
        try {
            releaseByDirector(secondAlbum);
        } catch (ExistingException ex) {
            log.error("Album releasing error", ex);
        }
        try {
            releaseByDirector(firstSingle);
        } catch (ExistingException ex) {
            log.error("Album releasing error", ex);
        }


        System.out.println("Все синглы по Стрыкало");
        System.out.println("====================================================");
        System.out.println(director.getAllSinglesByPerformer(strykalo).toString());
        System.out.println("====================================================");
        System.out.println("Все музыканты по firstAlbum");
        System.out.println("====================================================");
        System.out.println(director.getMusicians(firstAlbum).toString());
        System.out.println("====================================================");
        System.out.println("Все песни разных музыкантов с переданным названием");
        System.out.println("====================================================");
        System.out.println(studio.getAllSongsByName("От заката до рассвета").toString());
        System.out.println("Все песни из базы студии");
        System.out.println("====================================================");
        System.out.println(studio.getSongs().toString());
        System.out.println("====================================================");
        System.out.println("Все релизы Стрыкало");
        System.out.println("====================================================");
        System.out.println(director.getReleases(strykalo).toString());
        System.out.println("====================================================");
        System.out.println("Все музыканты ");
        System.out.println(musician.getAll().toString());
        System.out.println("====================================================");
        System.out.println("Все артисты ");
        System.out.println(musician.getArtists().toString());
        System.out.println("====================================================");
        System.out.println("Все бэнды ");
        System.out.println(musician.getBands().toString());

        Band band = new Band("Аэрокосовcкий dungeon");
        log.info("New Artist " + band.getName() + " is created");
        musician.addStatus(band);

        musician.addStatus(strykalo);


        log.info("End of working");

    }
}
