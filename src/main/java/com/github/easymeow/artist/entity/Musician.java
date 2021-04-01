package com.github.easymeow.artist.entity;

import java.util.List;

public interface Musician {

    void addAlbum(Release album);

    void deleteAlbum(Release album);

    public String getName();

    public List<Release> getAlbums();


}
