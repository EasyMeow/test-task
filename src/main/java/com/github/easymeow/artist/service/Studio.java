package com.github.easymeow.artist.service;

import com.github.easymeow.artist.entity.Musician;
import com.github.easymeow.artist.entity.Song;

import java.util.List;

public interface Studio {
    Song record(String songName, Musician... musician);

    List<Song> getSongs();

    List<Song> getAllSongsByName(String name);
}
