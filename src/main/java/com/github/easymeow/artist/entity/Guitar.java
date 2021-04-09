package com.github.easymeow.artist.entity;

public class Guitar extends Instrument {
    public Guitar(String brand) {
        super(brand);
    }

    @Override
    public String toString() {
        return "Guitar{" + super.toString() + "}";
    }
}
