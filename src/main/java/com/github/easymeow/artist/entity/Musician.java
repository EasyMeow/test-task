package com.github.easymeow.artist.entity;

import java.util.List;

public interface Musician {
    String getName();

    void setName(String s);

    String getStatus();

    void setStatus(String status);

    List<Musician> getArtists();

    void setArtist(List<Musician> musicianList);
}
