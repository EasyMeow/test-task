package com.github.easymeow.artist;

import com.github.easymeow.artist.entity.Album;
import com.github.easymeow.artist.entity.Artist;
import com.github.easymeow.artist.entity.Song;
import com.github.easymeow.artist.exceptions.ExistingException;
import com.github.easymeow.artist.service.Director;
import com.github.easymeow.artist.service.StudioImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class ArtistApplication {
    private final static Logger log = LoggerFactory.getLogger(ArtistApplication.class);

    public static void main(String[] args) {
        SpringApplication.run(ArtistApplication.class, args);
        log.info("Application start");
        Director director = new Director();
        StudioImpl studio = new StudioImpl();

        Artist strykalo = new Artist("Валентин Стрыкало");
        log.info("New Artist " + strykalo.getName() + " is created");
        Artist deftones = new Artist("Deftones");
        log.info("New Artist " + deftones.getName() + " is created");

        Song firstSong = studio.record("От заката до рассвета", strykalo, deftones);
        Song secondSong = studio.record("Знаешь, Таня", strykalo);
        Song thirdSong = studio.record("Sextape", deftones);
        Song forthSong = studio.record("Знаешь, Таня", deftones);

        Album firstSingle = new Album("Some single");
        Album firstAlbum = new Album("Развлечение");
        Album secondAlbum = new Album("Some album");

        try {
            director.createRelease(strykalo, firstAlbum);
        } catch (ExistingException ex) {
            log.error("Release creation error", ex);
        }
        try {
            director.createRelease(strykalo, firstSingle);
        } catch (ExistingException ex) {
            log.error("Release creation error", ex);
        }
        try {
            director.createRelease(deftones, secondAlbum);
        } catch (ExistingException ex) {
            log.error("Release creation error", ex);
        }


        try {
            director.addSong(firstAlbum, firstSong);
        } catch (ExistingException ex) {
            log.error("Song adding error", ex);
        }
        try {
            director.addSong(secondAlbum, thirdSong);
        } catch (ExistingException ex) {
            log.error("Song adding error", ex);
        }
        try {
            director.addSong(firstSingle, secondSong);
        } catch (ExistingException ex) {
            log.error("Song adding error", ex);
        }
        try {
            director.addSong(secondAlbum, forthSong);
        } catch (ExistingException ex) {
            log.error("Song adding error", ex);
        }


        try {
            director.release(firstAlbum);
        } catch (ExistingException ex) {
            log.error("Album releasing error", ex);
        }
        try {
            director.release(secondAlbum);
        } catch (ExistingException ex) {
            log.error("Album releasing error", ex);
        }
        try {
            director.release(firstSingle);
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
        System.out.println(director.getAllSongsByName("Знаешь, Таня").toString());
        System.out.println("====================================================");
        System.out.println("Все релизы Стрыкало");
        System.out.println("====================================================");
        System.out.println(director.getReleases(strykalo).toString());
        System.out.println("====================================================");

        log.info("End of working");
    }
}
