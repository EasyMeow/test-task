package com.github.easymeow.artist;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

@EnableConfigurationProperties
@SpringBootApplication
public class ArtistApplication {
    private final static Logger log = LoggerFactory.getLogger(ArtistApplication.class);

    public static void main(String[] args) {
        SpringApplication.run(ArtistApplication.class, args);
    }
}
