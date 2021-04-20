package com.github.easymeow.artist.service;

import com.github.easymeow.artist.AppProperties;
import com.github.easymeow.artist.entity.Musician;
import com.github.easymeow.artist.entity.Song;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

@Qualifier("asia")
@Service
public class StudioImpl implements Studio {
    private static StudioImpl instance;
    private final List<SongListener> listeners = new ArrayList<>();
    private final AppProperties properties;
    private final MessageSource messageSource;

    @Autowired
    public StudioImpl(AppProperties properties, MessageSource messageSource) {
        this.properties = properties;
        this.messageSource = messageSource;
    }

    /**
     * @return запись песни
     */
    @Override
    public Song record(String songName, Musician... musician) {
        String title = messageSource.getMessage("song", null, Locale.forLanguageTag(properties.getLang()));
        Song song = new Song(title + " [" + properties.getStudioTitle() + "] " + songName, musician);
        listeners.forEach(listener -> listener.onSongRecorded(song));
        return song;
    }

    public void subcribe(SongListener songListener) {
        listeners.add(songListener);
    }
}
