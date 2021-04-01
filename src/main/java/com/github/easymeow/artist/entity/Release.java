package com.github.easymeow.artist.entity;

import java.util.List;

public interface Release {

    void addSong(Song songName);

    void deleteSong(Song songName);

    public String getName();

    public List<Song> getSongList();

}
