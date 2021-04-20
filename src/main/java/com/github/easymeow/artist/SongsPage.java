package com.github.easymeow.artist;

import com.github.easymeow.artist.entity.Artist;
import com.github.easymeow.artist.entity.Musician;
import com.github.easymeow.artist.entity.Song;
import com.github.easymeow.artist.service.Studio;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.List;

@Component
public class SongsPage {
    private final static Logger log = LoggerFactory.getLogger(SongsPage.class);
    private List<Song> songs = new ArrayList<>();
    private Studio studio;

    @Autowired
    public SongsPage(Studio studio) {
        this.studio = studio;
        log.info(this.studio.toString());
        displaySongs();
    }

    void createSong(String songName, Musician... musician) {
        log.info(">> createSong");
        Song song = studio.record(songName, musician);
        songs.add(song);
        displaySongs();
    }

    private void displaySongs() {
        log.info("Current songs:");
        int i = 1;
        for (Song song : songs) {
            log.info((i++) + ". " + song.toString());
        }
        log.info("------");

    }

    @PostConstruct
    void init() {
        Artist strykalo = new Artist("Валентин Стрыкало");
        log.info("New Artist " + strykalo.getName() + " is created");
        Artist deftones = new Artist("Deftones");
        log.info("New Artist " + deftones.getName() + " is created");

        createSong("От заката до рассвета", strykalo, deftones);
        createSong("Знаешь, Таня", strykalo);

        createSong("Sextape", deftones);
        createSong("Знаешь, Таня", deftones);
    }
}
