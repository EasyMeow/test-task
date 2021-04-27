package com.github.easymeow.artist.service;

import com.github.easymeow.artist.AppProperties;
import com.github.easymeow.artist.entity.Musician;
import com.github.easymeow.artist.entity.Song;
import org.apache.logging.log4j.util.Strings;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;

@Scope("singleton")
@Service
public class StudioImpl implements Studio {
    private final AppProperties properties;
    private final MessageSource messageSource;
    private List<Song> songList = new ArrayList<>();

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
        songList.add(song);
        return song;
    }

    @Override
    public List<Song> getSongs() {
        return songList;
    }

    @Override
    public List<Song> getAllSongsByName(String name) {
        if (Strings.isBlank(name)) {
            return getSongs();
        }

        return songList.stream()
                .filter(s -> s.getName().contains(name))
                .map(Song.class::cast)
                .collect(Collectors.toList());
    }
}
