package com.github.easymeow.artist.entity;

import java.util.List;
import java.util.Objects;

public abstract class AbstractPerformer implements Musician {
    private String name;


    private String status;

    public AbstractPerformer() {
    }

    public AbstractPerformer(String name) {
        this.name = name;
    }


    public void setName(String name) {
        this.name = name;
    }

    @Override
    public List<Musician> getArtists() {
        return null;
    }

    @Override
    public String getName() {
        return name;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    @Override
    public String getStatus() {
        return status;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        AbstractPerformer that = (AbstractPerformer) o;
        return name.equals(that.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name);
    }

    @Override
    public String toString() {
        return name ;
    }
}
