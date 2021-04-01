package com.github.easymeow.artist.entity;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Album implements AlbumInterface{
    private String name;


    private List<Song> songList = new ArrayList<>();

    public Album(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<Song> getSongList() {
        return songList;
    }

    public void setSongList(List<Song> songList) {
        this.songList = songList;
    }

    public void addSong(Song songName) {
        songList.add(songName);
    }

    public String toString() {

        return "Название альбома:" + name + '\n' + songList.toString() + '\n';
    }

    public void deleteSong(Song songName) {
        songList.remove(songName);
    }

}
