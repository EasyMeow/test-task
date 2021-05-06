package com.github.easymeow.artist.entity;

public class Artist extends AbstractPerformer {

    public Artist(String name) {
        super(name);
    }

    public Artist() {
    }

    @Override
    public String getStatus() {
        return "Solo Artist";
    }
}
