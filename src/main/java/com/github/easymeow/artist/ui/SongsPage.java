package com.github.easymeow.artist.ui;

import com.github.easymeow.artist.entity.Musician;
import com.github.easymeow.artist.entity.Song;
import com.github.easymeow.artist.exceptions.ArtistException;
import com.github.easymeow.artist.service.MusicianService;
import com.github.easymeow.artist.service.Studio;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.listbox.MultiSelectListBox;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.notification.NotificationVariant;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.data.renderer.ComponentRenderer;
import com.vaadin.flow.data.value.ValueChangeMode;
import com.vaadin.flow.router.Route;
import org.apache.logging.log4j.util.Strings;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;


@Route("songs")
public class SongsPage extends VerticalLayout {
    private final static Logger log = LoggerFactory.getLogger(SongsPage.class);
    private final Grid<Song> grid = new Grid<>();
    private final List<Song> items = new ArrayList<>();
    private final Studio studio;
    private final MusicianService musicianService;
    private String filter;
    private Button create;
    private Button update;
    private Song buffer;

    Binder<Song> binder = new Binder<>(Song.class);
    TextField name = new TextField("Song Name");
    MultiSelectListBox<Musician> musician = new MultiSelectListBox<>();

    @Autowired
    public SongsPage(Studio studio, MusicianService musicianService) {
        this.studio = studio;
        this.musicianService = musicianService;

        initTableLayout();
        initFormLayout();

        switchFormToCreation();

        refreshSongs();
    }

    private void initTableLayout() {
        TextField filter = new TextField();
        filter.setPlaceholder("Song Name");
        filter.setValueChangeMode(ValueChangeMode.EAGER);
        filter.addValueChangeListener(event -> {
            filter(event.getValue());
        });

        grid.addColumn(Song::getName)
                .setHeader("Name")
                .setSortable(true);

        grid.addComponentColumn(song -> {
            HorizontalLayout hl = new HorizontalLayout();
            for (Musician musician : song.getMusicians()) {
                hl.add(new Label(musician.getName()));
            }
            return hl;
        })
                .setHeader("Musicians")
                .setSortable(true)
                .setComparator(Comparator.comparing(Song::getName));


        grid.addSelectionListener(event -> {
            if (event.getFirstSelectedItem().isPresent()) {
                switchFormToUpdating(event.getFirstSelectedItem().get());
            } else {
                switchFormToCreation();
            }
        });

        grid.setItems(items);

        add(filter, grid);
    }

    private void switchFormToUpdating(Song song) {
        // TODO Put musician data to form


        buffer = song;
//        try {
//            binder.writeBean(song);
//        } catch (ValidationException e) {
//            e.printStackTrace();
//        }
//        //musician.setValue((Set<Musician>) song.getMusicians());
//        name.setValue(binder.readBean(song));
        name.setValue(song.getName());
        update.setVisible(true);
        create.setVisible(false);

    }

    private void switchFormToCreation() {

        musician.setItems(musicianService.getAll());
        name.setValue("");
        update.setVisible(false);
        create.setVisible(true);
    }

    private void initFormLayout() {
        VerticalLayout form = new VerticalLayout();

        form.getStyle().set("overflow-y", "auto");


        musician.setItems(musicianService.getAll());
        musician.setRenderer(new ComponentRenderer<>(m -> new Label(m.getName())));

        create = new Button("Create", event -> {
            try {
                if (Strings.isBlank(name.getValue())) {
                    // TODO use Vaadin Binder for client validation
//                    binder.forField(name)
//                            .bind(Song::getName,
//                               Song::setName);
//                    binder.forField(musician)
//                            .bind(Song::getMusicians,
//                                    Song::setMusicians);
                    name.setErrorMessage("Name can't be null");
                    name.setInvalid(true);
                    return;
                }
                if (musician.getValue().isEmpty()) {
                    Notification notification = Notification.show("Song must have a performer");
                    notification.setPosition(Notification.Position.TOP_END);
                    notification.addThemeVariants(NotificationVariant.LUMO_ERROR);
                    return;
                }
            } catch (ArtistException ex) {
                Notification notification = Notification.show(ex.getMessage());
                notification.setPosition(Notification.Position.TOP_END);
                notification.addThemeVariants(NotificationVariant.LUMO_ERROR);
            }
                createSong(name.getValue(), musician.getValue().toArray(new Musician[0]));
                refreshSongs();

        });

        update = new Button("Update", event -> {
            try {
                if (Strings.isBlank(name.getValue())) {
                    // TODO use Vaadin Binder for client validation
                    name.setErrorMessage("Name can't be null");
                    name.setInvalid(true);
                    return;
                }
                if (musician.getValue().isEmpty()) {
                    Notification notification = Notification.show("Song must have a performer");
                    notification.setPosition(Notification.Position.TOP_END);
                    notification.addThemeVariants(NotificationVariant.LUMO_ERROR);
                    return;
                }
            } catch (ArtistException ex) {
                Notification notification = Notification.show(ex.getMessage());
                notification.setPosition(Notification.Position.TOP_END);
                notification.addThemeVariants(NotificationVariant.LUMO_ERROR);
            }
            updateSong(buffer);
            refreshSongs();

        });


        form.add(name, musician, create, update);

        add(form);
    }

    private void createSong(String songName, Musician... musician) {
        log.info(">> createSong");
        studio.record(songName, musician);
        refreshSongs();
    }

    private void updateSong(Song song) {
        song.setName(name.getValue());
        song.setMusicians(musician.getValue().toArray(new Musician[0]));
    }

    private void filter(String title) {
        filter = title;
        log.info("Set filter: " + title);
        refreshSongs();
    }

    private void refreshSongs() {
        List<Song> songs = studio.getAllSongsByName(filter);
        items.clear();
        items.addAll(songs);

        grid.getDataProvider().refreshAll();
    }
}
