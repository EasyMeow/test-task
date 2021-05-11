package com.github.easymeow.artist.entity;

public class Artist extends AbstractPerformer {

    boolean isMember = false;

    public Artist(String name) {
        super(name);
    }

    public Artist() {
    }

    public void setMember(boolean isMember) {
        this.isMember = isMember;
    }

    public boolean getIsMember() {
        return isMember;
    }

    @Override
    public String getStatus() {
        return "Solo Artist";
    }
}
