package com.github.easymeow.artist.service.chain;

import com.github.easymeow.artist.entity.Musician;

public abstract class ArtistChain {
    private ArtistChain next;

    public ArtistChain linkWith(ArtistChain next) {
        this.next = next;
        return next;
    }

    protected boolean checkNext(Musician artist) {
        if (next == null) {
            return true;
        }
        return next.addStatus(artist);
    }


    public abstract boolean addStatus(Musician artist);


}
