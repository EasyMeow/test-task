package com.github.easymeow.artist.entity;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Song {
    private String name;
    private final List<Musician> musicians=new ArrayList<>();

    public Song() {
    }

    public Song(String name,Musician... musicians) { //У песни может быть много исполнителей, в том числе feat.
        this.name = name;                            //У альбома или сингла все-таки один исполнитель
        this.musicians.addAll(Arrays.asList(musicians));
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }


    @Override
    public String toString() {
        return "Название: "+name+"( "+musicians.toString()+")";
    }

}
