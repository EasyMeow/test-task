package com.github.easymeow.artist.service;

import com.github.easymeow.artist.entity.Song;

@FunctionalInterface
public interface SongListener {
    void onSongRecorded(Song song);
}
