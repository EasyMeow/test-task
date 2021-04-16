package com.github.easymeow.artist.service;

import com.github.easymeow.artist.entity.Musician;
import com.github.easymeow.artist.entity.Song;

import java.util.ArrayList;
import java.util.List;

public class StudioImpl implements Studio {
    private final List<SongListener> listeners = new ArrayList<>();

    /**
     * @return запись песни
     */
    @Override
    public Song record(String songName, Musician... musician) {
        Song song = new Song(songName, musician);
        listeners.forEach(listener -> listener.onSongRecorded(song));
        return song;
    }

    public void subcribe(SongListener songListener) {
        listeners.add(songListener);
    }
}
