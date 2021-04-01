package com.github.easymeow.artist.service;

import com.github.easymeow.artist.entity.AlbumGroup;
import com.github.easymeow.artist.entity.Musician;
import com.github.easymeow.artist.entity.Release;

import java.util.List;

public interface Producer {

    void release(Musician artist, Release album);

    void deleteAlbum(AlbumGroup artist, Release album);

    List<Release> getReleases(Musician album);

    List<Musician> getReleases(Release album);
}
