package com.github.easymeow.artist.service;

import com.github.easymeow.artist.entity.AbstractPerformer;
import com.github.easymeow.artist.entity.Musician;
import com.github.easymeow.artist.entity.Release;
import com.github.easymeow.artist.entity.Song;

public interface Producer {

    void release(Musician artist, Release album);

    void addSong(Release album, Song song);

    void deleteAlbum(AbstractPerformer artist, Release album);

}
