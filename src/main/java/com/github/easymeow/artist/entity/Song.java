package com.github.easymeow.artist.entity;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

public class Song {
    private String name;
    private final List<Musician> musicians = new ArrayList<>();

    public Song() {
    }

    public Song(String name, Musician... musicians) { //У песни может быть много исполнителей, в том числе feat.
        this.name = name;                            //У альбома или сингла все-таки один исполнитель
        this.musicians.addAll(Arrays.asList(musicians));
    }

    public String getName() {
        return name;
    }

    public List<Musician> getMusicians() {
        return musicians;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Song that = (Song) o;
        return name.equals(that.name) && musicians.equals(that.musicians);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, musicians);
    }

    @Override
    public String toString() {
        return "Название: " + name + "( " + musicians.toString() + ")";
    }

    public void setMusicians(Musician... musicians) {
        this.musicians.clear();
        this.musicians.addAll(Arrays.asList(musicians));
    }
}
