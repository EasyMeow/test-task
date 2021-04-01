package com.github.easymeow.artist;

import com.github.easymeow.artist.entity.Album;
import com.github.easymeow.artist.entity.Artist;
import com.github.easymeow.artist.entity.Song;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class ArtistApplication {

    public static void main(String[] args) {
        SpringApplication.run(ArtistApplication.class, args);
        Artist strykalo = new Artist("Валентин Стрыкало");
        Album firstAlbum = new Album("Часть чего-то большего");
        Song firstSong = new Song("Кладбище самолетов");
        Song secondSong = new Song("Знаешь,Таня");
//        firstSong.setAlbumName(firstAlbum);
//        secondSong.setAlbumName(firstAlbum);
        firstAlbum.addSong(firstSong);
        firstAlbum.addSong(secondSong);
        strykalo.addAlbumName(firstAlbum);

//        System.out.println(firstAlbum);
//        firstAlbum.deleteSong(firstSong);
//        System.out.println("Произошло удаление песни из альбома");
//        System.out.println(firstAlbum);
//        firstSong.setName("Космос нас ждет");
//        firstAlbum.addSong(firstSong);
//        System.out.println("Произошло добавление новой песни");
//        System.out.println(firstAlbum);
        Album secondAlbum = new Album("Развлечение");
        strykalo.addAlbumName(secondAlbum);
        secondAlbum.addSong(firstSong);
        secondAlbum.addSong(secondSong);
        System.out.println("======================================================================");
        System.out.println(strykalo.toString());

        System.out.println("======================================================================");
        firstAlbum.deleteSong(firstSong);
        System.out.println("Произошло удаление песни из 1 альбома");
        System.out.println("======================================================================");
        System.out.println(strykalo.toString());
        System.out.println("======================================================================");
        strykalo.deleteAlbum(firstAlbum);
        System.out.println("Произошло удаление песни из 1 альбома");
        System.out.println("======================================================================");
        System.out.println(strykalo.toString());

    }


}
