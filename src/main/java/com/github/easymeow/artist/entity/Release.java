package com.github.easymeow.artist.entity;

import java.util.List;

public interface Release {

    void addMusician(Musician musician);

    Musician getMusician();

    List<Song> getSongList();

    void deleteSong(Song songName);

}
