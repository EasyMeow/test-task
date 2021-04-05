package com.github.easymeow.artist.service;

import com.github.easymeow.artist.entity.Musician;
import com.github.easymeow.artist.entity.Song;

public class StudioImpl implements Studio {
    @Override
    public Song record(String songName,Musician... musician) {
        return new Song(songName,musician);
    }

}
