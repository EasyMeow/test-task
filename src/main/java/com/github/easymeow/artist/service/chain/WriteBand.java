package com.github.easymeow.artist.service.chain;

import com.github.easymeow.artist.entity.Band;
import com.github.easymeow.artist.entity.Musician;
import org.springframework.stereotype.Service;

@Service
public class WriteBand extends ArtistChain {
    private final String status = "Band";

    public WriteBand() {
    }


    @Override
    public boolean addStatus(Musician artist) {
        if (artist instanceof Band) {
            System.out.println("Исполнителю " + artist.getName().toString());
            artist.setName(artist.getName().toString() + " [" + status + "]");
            System.out.println("выдан статус " + status);
            System.out.println(artist.toString());
        }

        return checkNext(artist);
    }
}
