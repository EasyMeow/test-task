package com.github.easymeow.artist.entity;

public interface HasInstrument<INSTRUMENT extends Instrument> {
    INSTRUMENT getInstrument();
}
