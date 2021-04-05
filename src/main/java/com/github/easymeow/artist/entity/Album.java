package com.github.easymeow.artist.entity;

import java.util.ArrayList;
import java.util.List;

public class Album implements Release {
    private String name;

    private List<Song> songList = new ArrayList<>();

    Musician musician;

    public Album(String name) {
        this.name = name;
    }

    public void setSongList(List<Song> songList) {
        this.songList = songList;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    @Override
    public List<Song> getSongList() {
        return songList;
    }

    @Override
    public Musician getMusician() {
        return musician;
    }

    @Override
    public void addMusician(Musician musicians) {
        this.musician=musicians;
    }


    @Override
    public String toString() {

        return "Название альбома:" + name  +"("+musician.toString() +")";
    }

    @Override
    public void deleteSong(Song songName) {
        songList.remove(songName);
    }

}
