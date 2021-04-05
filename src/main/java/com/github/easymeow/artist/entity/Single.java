package com.github.easymeow.artist.entity;

import java.util.ArrayList;
import java.util.List;

public class Single implements Release  {

    String name;

    Musician musician;

    private  List<Song> songList = new ArrayList<>();

    public Single(String name) {
        this.name=name;
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
    public String toString() {

        return "Название Сингла:" + name +"("+musician.toString()+")";
    }

    @Override
    public void addMusician(Musician musician) {
        this.musician = musician;
    }


    @Override
    public void deleteSong(Song songName) {
        throw new UnsupportedOperationException("Unsupported operation!");
    }

    @Override
    public List<Song> getSongList() {
        return songList;
    }

    @Override
    public Musician getMusician() {
        return musician;
    }
}
