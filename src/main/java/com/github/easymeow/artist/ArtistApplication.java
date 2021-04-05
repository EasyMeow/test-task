package com.github.easymeow.artist;

import com.github.easymeow.artist.entity.Album;
import com.github.easymeow.artist.entity.Artist;
import com.github.easymeow.artist.entity.Single;
import com.github.easymeow.artist.entity.Song;
import com.github.easymeow.artist.service.Director;
import com.github.easymeow.artist.service.StudioImpl;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class ArtistApplication {

    public static void main(String[] args) {
        SpringApplication.run(ArtistApplication.class, args);
        Director director = new Director();
        StudioImpl studio = new StudioImpl();

        Artist strykalo=new Artist("Валентин Стрыкало");
        Artist deftones=new Artist("Deftones");

        Song firstSong= studio.record("От заката до рассвета",strykalo,deftones);
        Song secondSong=studio.record("Знаешь, Таня",strykalo);
        Song thirdSong=studio.record("Sextape",deftones);
        Song forthSong= studio.record("Знаешь, Таня", deftones);

        Single firstSingle=new Single("Some single");
        Album firstAlbum=new Album("Развлечение");
        Album secondAlbum=new Album("Some album");

        director.addSong(firstAlbum,firstSong);
        director.addSong(secondAlbum,thirdSong);
        director.addSong(firstSingle,secondSong);
        director.addSong(secondAlbum,forthSong);

        director.release(strykalo,firstAlbum);
        director.release(strykalo,firstSingle);
        director.release(deftones,secondAlbum);

        System.out.println("Все синглы по Стрыкало");
        System.out.println("====================================================");
        System.out.println(director.getAllSinglesByPerformer(strykalo).toString());
        System.out.println("====================================================");
        System.out.println("Все музыканты по firstAlbum");
        System.out.println("====================================================");
        System.out.println(director.getMusicians(firstAlbum).toString());
        System.out.println("====================================================");
        System.out.println("Все песни разных музыкантов с переданным названием");
        System.out.println("====================================================");
        System.out.println(director.getAllSongsByName("Знаешь, Таня").toString());
        System.out.println("====================================================");
        System.out.println("Все релизы Стрыкало");
        System.out.println("====================================================");
        System.out.println(director.getReleases(strykalo).toString());
        System.out.println("====================================================");




    }


}
