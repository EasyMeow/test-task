package com.github.easymeow.artist;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties("artist")
public class AppProperties {
    private String studioTitle;
    private String lang;

    public String getStudioTitle() {
        return studioTitle;
    }

    public void setStudioTitle(String studioTitle) {
        this.studioTitle = studioTitle;
    }

    public String getLang() {
        return lang;
    }

    public void setLang(String lang) {
        this.lang = lang;
    }
}
