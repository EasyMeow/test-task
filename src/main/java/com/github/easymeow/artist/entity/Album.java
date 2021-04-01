package com.github.easymeow.artist.entity;

import java.util.ArrayList;
import java.util.List;

public class Album implements Release {
    private String name;

    private List<Song> songList = new ArrayList<>();

    public Album(String name) {
        this.name = name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setSongList(List<Song> songList) {
        this.songList = songList;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public List<Song> getSongList() {
        return songList;
    }

    @Override
    public void addSong(Song songName) {
        songList.add(songName);
    }

    @Override
    public String toString() {

        return "Название альбома:" + name + '\n' + songList.toString() + '\n';
    }

    @Override
    public void deleteSong(Song songName) {
        songList.remove(songName);
    }

}
