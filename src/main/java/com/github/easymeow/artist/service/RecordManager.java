package com.github.easymeow.artist.service;

import com.github.easymeow.artist.entity.Musician;
import com.github.easymeow.artist.entity.Release;
import com.github.easymeow.artist.entity.Song;

import java.util.List;

public interface RecordManager {
    List<Song> getAllSongsByName(String name);

    List<Release> getReleases(Musician album);

    Musician getMusicians(Release album);

    List<Release> getAllSinglesByPerformer(Musician musician);
}
