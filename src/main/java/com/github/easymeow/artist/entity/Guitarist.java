package com.github.easymeow.artist.entity;

public class Guitarist extends Artist implements HasInstrument<Guitar> {
    private final Guitar guitar;

    public Guitarist(String name) {
        super(name);
        guitar = new Guitar("les paul");
    }

    @Override
    public Guitar getInstrument() {
        return guitar;
    }
}
