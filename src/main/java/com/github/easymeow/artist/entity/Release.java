package com.github.easymeow.artist.entity;

import java.util.List;

public interface Release {

    void setMusician(Musician musician);

    Musician getMusician();

    List<Song> getSongList();

    State getState();

    void setState(State state);

    default boolean isSingle() {
        return getSongList().size() == 1;
    }

    default boolean isReleased() {
        return getState().equals(State.RELEASED);
    }

    enum State {
        CREATED, ASSIGNED, RELEASED
    }
}
