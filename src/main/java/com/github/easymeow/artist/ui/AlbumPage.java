package com.github.easymeow.artist.ui;

import com.github.easymeow.artist.entity.Album;
import com.github.easymeow.artist.entity.Musician;
import com.github.easymeow.artist.entity.Song;
import com.github.easymeow.artist.service.Director;
import com.github.easymeow.artist.service.MusicianService;
import com.github.easymeow.artist.service.Studio;
import com.github.easymeow.artist.service.StudioImpl;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.vaadin.gatanaso.MultiselectComboBox;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.function.Consumer;

@PageTitle("Albums")
@Route(value = "albums", layout = RootLayout.class)
public class AlbumPage extends VerticalLayout {
    private final Grid<Album> grid = new Grid<>();
    private final Director director;
    private final StudioImpl studio;
    private final MusicianService musicianService;
    private Button create;
    private final MasterDetailView master;
    private final static Logger log = LoggerFactory.getLogger(AlbumPage.class);


    AlbumPage(Director director, StudioImpl studio, MusicianService musicianService) {
        this.studio = studio;
        this.director = director;
        this.musicianService = musicianService;
        master = new MasterDetailView(musicianService, studio);
        setPadding(false);
        master.addClassName("master-detail");
        master.setVisible(false);
        initTable();
        initLayout();
        refreshAlbums();
    }

    public void initTable() {
        grid.addColumn(Album::getName)
                .setHeader("Album name")
                .setSortable(true);
        grid.addColumn(Album::getMusician)
                .setHeader("Musicians")
                .setSortable(true);

        grid.addItemDoubleClickListener(event -> {
            master.openUpdate(event.getItem(), this::updateAlbum);
            create.setVisible(false);
        });
        add(grid, master);
    }

    public void initLayout() {
        create = new Button("Create Album", e -> {
            master.openCreate(new Album(), a ->
                    createAlbum(a.getMusician(), a.getName(), a.getSongList()));
            create.setVisible(false);
        });
        add(create);

    }

    public void refreshAlbums() {
        grid.setItems(new ArrayList<>(director.getAll()));
    }

    public void createAlbum(Musician musician, String name, List<Song> songs) {
        log.info("Album is created");
        Album album = new Album(name);
        album.setSongList(songs);
        director.createRelease(musician, album);
        director.release(album);
        refreshAlbums();
        create.setVisible(true);
    }

    public void updateAlbum(Album album) {
        log.info("Album is updated");
        director.updateAlbum(album);
        refreshAlbums();
        create.setVisible(true);
    }

    //TODO
    // Optionally(!!) add grid detail with songs list

    public static class MasterDetailView extends FormLayout {
        private final Button save = new Button("save");
        private final Button cancel = new Button("cancel");
        private final TextField name = new TextField("Album name");
        private final ComboBox<Musician> musicians = new ComboBox<>("Musicians");
        private final MultiselectComboBox<Song> songs = new MultiselectComboBox<>("Songs");
        private final HorizontalLayout hLayout = new HorizontalLayout(save, cancel);
        private final HorizontalLayout hlayout1 = new HorizontalLayout(name, musicians);
        private final VerticalLayout layout = new VerticalLayout(hlayout1, songs, hLayout);


        private Binder<Album> binder = new Binder<>(Album.class);
        private Album buffer;
        private Consumer<Album> action;
        private final MusicianService musicianService;
        private final Studio studio;


        public MasterDetailView(MusicianService musicianService, Studio studio) {
            this.musicianService = musicianService;
            this.studio = studio;

            layout.addClassName("master-detail");

            cancel.addClassName("cancel");
            cancel.addClickListener(e ->
                    this.setVisible(false));

            binder.forField(name)
                    .asRequired("name should be not null")
                    .bind(Album::getName, Album::setName);

            binder.forField(musicians)
                    .asRequired("musician list should be not null")
                    .bind(Album::getMusician, Album::setMusician
                    );

            binder.forField(songs)
                    .bind(a -> new HashSet<>(a.getSongList()), (a, songs) ->
                            a.setSongList(new ArrayList<>(songs))
                    );

            musicians.addValueChangeListener(e -> {
                songs.setItems(studio.getAllSongsByMusician(musicians.getValue()));
                binder.forField(songs)
                        .bind(a -> new HashSet<>(a.getSongList()), (a, songs) ->
                                a.setSongList(new ArrayList<>(songs))
                        );
            });


            save.addClickListener(event -> {
                if (binder.writeBeanIfValid(buffer)) {
                    action.accept(buffer);
                    musicians.setItems(Collections.emptyList());
                    name.setValue("");
                    this.setVisible(false);
                }
            });
            add(layout);
        }


        public void open(Album album, Consumer<Album> action) {
            buffer = album;
            musicians.setItems(musicianService.getAll());
            this.action = action;
            binder.readBean(buffer);

        }

        public void openCreate(Album album, Consumer<Album> action) {
            this.setVisible(true);
            save.setText("create");
            open(album, action);
        }

        public void openUpdate(Album album, Consumer<Album> action) {

            this.setVisible(true);
            save.setText("update");
            open(album, action);
        }

    }

}
