package com.github.easymeow.artist.entity;

import java.util.List;

public interface Musician {
    String getName();

    List<Release> getAlbums();

    void setName(String s);
}
