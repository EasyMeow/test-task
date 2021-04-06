package com.github.easymeow.artist.service;

import com.github.easymeow.artist.entity.Musician;
import com.github.easymeow.artist.entity.Release;
import com.github.easymeow.artist.entity.Song;

public interface Producer extends RecordManager {

    void createRelease(Musician artist, Release album);

    void release(Release album);

    void addSong(Release album, Song song);

    void deleteAlbum(Release album);
}
