package com.github.easymeow.artist.ui;

import com.github.easymeow.artist.entity.Musician;
import com.github.easymeow.artist.entity.Song;
import com.github.easymeow.artist.service.MusicianService;
import com.github.easymeow.artist.service.Studio;
import com.vaadin.flow.component.Key;
import com.vaadin.flow.component.Shortcuts;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.data.binder.ValidationResult;
import com.vaadin.flow.data.renderer.ComponentRenderer;
import com.vaadin.flow.data.value.ValueChangeMode;
import com.vaadin.flow.router.Route;
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
    private Button cancelCreateDialog;
    private Button cancelUpdateDialog;
    private Button open;
    private Song buffer;
    private Song updateSong;
    private final Binder<Song> binder = new Binder<>(Song.class);
    private final TextField name = new TextField("Song Name");
    private final Grid<Song> grid = new Grid<>();
    private final Dialog createDialog = new Dialog();
    private final Dialog updateDialog = new Dialog();
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

        grid.setSelectionMode(Grid.SelectionMode.SINGLE);


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
                open.setVisible(false);
            } else {
                open.setVisible(true);
                switchFormToCreation();
            }
        });

        grid.setItems(items);

        add(filter, grid);
    }

    private void initFormLayout() {
        VerticalLayout form = new VerticalLayout();

        form.getStyle().set("overflow-y", "auto");

        createDialog.setCloseOnEsc(false);
        createDialog.setCloseOnOutsideClick(false);

        cancelCreateDialog = new Button("Cancel", event -> {
            createDialog.close();
        });
        Shortcuts.addShortcutListener(createDialog, createDialog::close, Key.ESCAPE);

        open = new Button("create", e -> createDialog.open());


        updateDialog.setCloseOnEsc(false);
        updateDialog.setCloseOnOutsideClick(false);

        cancelUpdateDialog = new Button("Cancel", event -> {
            updateDialog.close();
        });
        Shortcuts.addShortcutListener(updateDialog, updateDialog::close, Key.ESCAPE);

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

        binder.forField(musician)
                //   .asRequired("Song must have a performer")
                .withValidator((p, valueContext) -> {
                    if (p.isEmpty()) {
                        return ValidationResult.error("Song must have a performer");
                    }
                    return ValidationResult.ok();
                })
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
                updateSong.setName(buffer.getName());
                updateSong.setMusicians(buffer.getMusicians());
                updateSong(updateSong);
            }
        });

        form.add(open);
        add(form);
    }

    private void switchFormToUpdating(Song song) {
        buffer = song;
        binder.readBean(buffer);
        updateSong = new Song();
        updateDialog.add(new Div(name, musician, update, cancelUpdateDialog));
        updateDialog.open();
    }

    private void switchFormToCreation() {
        buffer = new Song();
        binder.readBean(buffer);
        createDialog.add(new Div(name, musician, create, cancelCreateDialog));
    }

    private void createSong(String songName, Musician... musician) {
        log.info(">> createSong");
        studio.record(songName, musician);
        switchFormToCreation();
        refreshSongs();
        createDialog.close();
    }

    private void updateSong(Song song) {
        log.info(">> updateSong");
        studio.updateSong(buffer, song);
        // TODO search this song in songs list
        // TODO grid.select(found);

//        grid.asMultiSelect().addValueChangeListener(event -> {
//            studio.updateSong((Song) event.getValue(),song);
//        });
        refreshSongs();
        updateDialog.close();

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
