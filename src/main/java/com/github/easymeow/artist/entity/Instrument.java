package com.github.easymeow.artist.entity;

public abstract class Instrument {
    private final String brand;

    public Instrument(String brand) {
        this.brand = brand;
    }

    public String getBrand() {
        return brand;
    }

    @Override
    public String toString() {
        return "Instrument{" +
                "brand='" + brand + '\'' +
                '}';
    }
}
