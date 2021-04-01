package com.github.easymeow.artist.entity;

import java.util.Collections;
import java.util.List;

public class Single extends Song implements Release {

    public Single(String name) {
        super(name);
    }

    @Override
    public void addSong(Song songName) {
        throw new IllegalStateException("Нельзя так делать!");
    }

    @Override
    public void deleteSong(Song songName) {
        throw new IllegalStateException("Нельзя так делать!");
    }

    @Override
    public List<Song> getSongList() {
        return Collections.singletonList(this);
    }
}
