package com.github.easymeow.artist.service;

import com.github.easymeow.artist.AppProperties;
import com.github.easymeow.artist.entity.Musician;
import com.github.easymeow.artist.entity.Song;
import com.github.easymeow.artist.exceptions.ArtistException;
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
    private final List<Song> songList = new ArrayList<>();
    private Song buffer;

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
        if (Strings.isBlank(songName)) {
            throw new ArtistException("Song Name can't be empty");
        }
        String title = messageSource.getMessage("song", null, Locale.forLanguageTag(properties.getLang()));
        buffer = new Song(title + " [" + properties.getStudioTitle() + "] " + songName, musician);
        for (Song s : songList) {
            if (s.equals(buffer)) {
                throw new ArtistException("Song is already exists");
            }
        }

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
                .filter(s -> s.getName().toLowerCase().contains(name.toLowerCase()))
                .map(Song.class::cast)
                .collect(Collectors.toList());
    }
}
