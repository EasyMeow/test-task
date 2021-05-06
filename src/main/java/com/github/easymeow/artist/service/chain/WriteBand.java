package com.github.easymeow.artist.service.chain;

import com.github.easymeow.artist.entity.Band;
import com.github.easymeow.artist.entity.Musician;
import org.springframework.context.annotation.Profile;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

@Profile("!prod")
@Scope("singleton")
@Service
public class WriteBand extends ArtistChain {

    public WriteBand() {
    }

    @Override
    public boolean addStatus(Musician artist) {
        if (artist instanceof Band) {
            artist.setStatus("Band");
        }
        return checkNext(artist);
    }
}
