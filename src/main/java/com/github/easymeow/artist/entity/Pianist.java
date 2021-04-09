package com.github.easymeow.artist.entity;

public class Pianist extends Artist implements HasInstrument<Piano> {
    private final Piano piano;

    public Pianist(String name) {
        super(name);
        piano = new Piano("yamaha");
    }


    @Override
    public Piano getInstrument() {
        return piano;
    }
}
