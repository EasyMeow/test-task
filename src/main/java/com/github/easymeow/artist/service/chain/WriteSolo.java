package com.github.easymeow.artist.service.chain;

import com.github.easymeow.artist.entity.Artist;
import com.github.easymeow.artist.entity.Musician;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

@Scope("singleton")
@Service
public class WriteSolo extends ArtistChain {


    @Override
    public boolean addStatus(Musician artist) {
        if (artist instanceof Artist) {
            artist.setStatus("Solo Artist");
        }
        return checkNext(artist);
    }
}
