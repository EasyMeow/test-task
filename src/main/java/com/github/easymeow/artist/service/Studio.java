package com.github.easymeow.artist.service;

import com.github.easymeow.artist.entity.Musician;
import com.github.easymeow.artist.entity.Song;

public interface Studio {
    Song record(String songName, Musician... musician);

    // getSongs() TODO
    // getAllSongsByName TODO
}
