package com.github.easymeow.artist.entity;

public class Piano extends Instrument {
    public Piano(String brand) {
        super(brand);
    }

    @Override
    public String toString() {
        return "Piano{" + super.toString() + "}";
    }
}
