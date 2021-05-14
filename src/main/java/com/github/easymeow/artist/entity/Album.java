package com.github.easymeow.artist.entity;

import java.util.ArrayList;
import java.util.List;

public class Album implements Release {
    private String name;
    private List<Song> songList = new ArrayList<>();
    private Musician musician;
    private State state;

    public Album() {

    }

    public Album(String name) {
        this.name = name;
    }

    public void setSongList(List<Song> songList) {
        this.songList = songList;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public State getState() {
        return state;
    }

    @Override
    public void setState(State state) {
        this.state = state;
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
    public void setMusician(Musician musicians) {
        this.musician = musicians;
    }

    @Override
    public String toString() {
        return "Название альбома:" + name + " " + state.name() + " (" + musician.toString() + ")";
    }

}
