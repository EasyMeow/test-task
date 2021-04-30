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
import com.vaadin.flow.data.binder.ValidationResult;
import com.vaadin.flow.data.renderer.ComponentRenderer;
import com.vaadin.flow.data.value.ValueChangeMode;
import com.vaadin.flow.router.Route;
import org.apache.logging.log4j.util.Strings;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.vaadin.gatanaso.MultiselectComboBox;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;

@Scope("prototype")
@Route("songs")
public class SongsPage extends VerticalLayout {
    private final static Logger log = LoggerFactory.getLogger(SongsPage.class);
    private final Studio studio;
    private final MusicianService musicianService;

    private String filter;
    private Button create;
    private Button update;
    private Song buffer;
    private final Binder<Song> binder = new Binder<>(Song.class);
    private final TextField name = new TextField("Song Name");
    private final Grid<Song> grid = new Grid<>();
    private final List<Song> items = new ArrayList<>();
    private final MultiselectComboBox<Musician> musician = new MultiselectComboBox<>("Musicians");
    private List<Song> songs;

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

    private void initFormLayout() {
        // TODO move to dialog

        VerticalLayout form = new VerticalLayout();

        form.getStyle().set("overflow-y", "auto");

        binder.forField(name)
                .asRequired("Name can't be null")
                .withValidator((s, valueContext) -> {
                    if (s.length() < 3) {
                        return ValidationResult.error("<3");
                    }

                    return ValidationResult.ok();
                })
                // TODO research .withConverter()
                .bind(song -> song.getName(), (song, str) -> song.setName(str));

        // TODO research binder.withValidator()

        binder.forField(musician)
                .asRequired("Song must have a performer")
                .bind(song -> new HashSet<>(song.getMusicians()), (song, set) -> song.setMusicians(new ArrayList<>(set)));

        musician.setItems(musicianService.getAll());
        musician.setRenderer(new ComponentRenderer<>(m -> new Label(m.getName())));

        create = new Button("Create", event -> {
//            binder.writeBean();
//            binder.validate()
            if (binder.writeBeanIfValid(buffer)) {
                createSong(buffer.getName(), buffer.getMusicians().toArray(new Musician[0]));
            }
        });

        update = new Button("Update", event -> {
            if (binder.writeBeanIfValid(buffer)) {
                updateSong(buffer);
            }
        });

        name.setRequired(true);
        name.setRequiredIndicatorVisible(true);
        musician.setRequired(true);
        musician.setRequiredIndicatorVisible(true);

        form.add(name, musician, create, update);

        add(form);
    }

    private void switchFormToUpdating(Song song) {
        buffer = song;
        binder.readBean(buffer);

        update.setVisible(true);
        create.setVisible(false);
    }

    private void switchFormToCreation() {
        buffer = new Song();
        binder.readBean(buffer);

        update.setVisible(false);
        create.setVisible(true);
    }

    private void notifyError(String message) {
        Notification notification = Notification.show(message);
        notification.setPosition(Notification.Position.TOP_END);
        notification.addThemeVariants(NotificationVariant.LUMO_ERROR);
    }

    private void createSong(String songName, Musician... musician) {
        log.info(">> createSong");
        studio.record(songName, musician);
        switchFormToCreation();
        refreshSongs();
    }

    private void updateSong(Song song) {
        studio.updateSong(song);
        refreshSongs();

        // TODO search this song in songs list
        // TODO grid.select(found);
    }

    private void filter(String title) {
        filter = title;
        log.info("Set filter: " + title);
        refreshSongs();
    }

    private void refreshSongs() {
        songs = studio.getAllSongsByName(filter);
        grid.setItems(songs);
//        items.clear();
//        items.addAll(songs);
//        grid.getDataProvider().refreshAll();
    }
}
