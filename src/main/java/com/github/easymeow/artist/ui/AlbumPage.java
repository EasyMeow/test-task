package com.github.easymeow.artist.ui;

import com.github.easymeow.artist.entity.Album;
import com.github.easymeow.artist.entity.Musician;
import com.github.easymeow.artist.entity.Song;
import com.github.easymeow.artist.service.Director;
import com.github.easymeow.artist.service.MusicianService;
import com.github.easymeow.artist.service.StudioImpl;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.router.Route;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.vaadin.gatanaso.MultiselectComboBox;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.function.Consumer;

@Route("Albums")
public class AlbumPage extends RootPage {
    private final Grid<Album> grid = new Grid<>();
    private final Director director;
    private final StudioImpl studio;
    private final MusicianService musicianService;
    private Button create;
    private AlbumDialog dialog = new AlbumDialog();
    private final static Logger log = LoggerFactory.getLogger(AlbumPage.class);

    AlbumPage(Director director, StudioImpl studio, MusicianService musicianService) {
        this.studio = studio;
        this.director = director;
        this.musicianService = musicianService;

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
            dialog.openUpdate(event.getItem(), this::updateAlbum);
        });

        add(grid);
    }

    public void initLayout() {
        create = new Button("Create Album", e ->
                dialog.openCreate(new Album(), a ->
                        createAlbum(a.getMusician(), a.getName(), a.getSongList())));
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
    }

    public void updateAlbum(Album album) {
        log.info("Album is updated");
        director.updateAlbum(album);
        refreshAlbums();
    }


    public class AlbumDialog extends Dialog {
        private final Button save = new Button("save");
        private final Button cancel = new Button("cancel");
        private final TextField name = new TextField("Album name");
        private final ComboBox<Musician> musicians = new ComboBox<>("Musicians");
        private final MultiselectComboBox<Song> songs = new MultiselectComboBox<>("Songs");
        private final HorizontalLayout hLayout = new HorizontalLayout(save, cancel);
        private final VerticalLayout layout = new VerticalLayout(name, musicians, songs, hLayout);

        private Binder<Album> binder = new Binder<>(Album.class);
        private Album buffer;
        private Consumer<Album> action;


        public AlbumDialog() {
            add(layout);

            cancel.addClickListener(event ->
                    close());

            binder.forField(name)
                    .asRequired("name should be not null")
                    .bind(Album::getName, Album::setName);

            binder.forField(musicians)
                    .asRequired("musician list should be not null")
                    .bind(Album::getMusician, Album::setMusician
                    );


            save.addClickListener(event -> {
                if (binder.writeBeanIfValid(buffer)) {
                    action.accept(buffer);
                    close();
                }
            });
        }

        public void open(Album album, Consumer<Album> action) {
            buffer = album;
            musicians.setItems(musicianService.getAll());
            this.action = action;
            binder.removeBinding(songs);
            binder.readBean(buffer);
            if (!musicians.isEmpty()) {
                songs.setItems(studio.getAllSongsByMusician(musicians.getValue()));
                binder.forField(songs)
                        .bind(a -> new HashSet<>(a.getSongList()), (a, songs) ->
                                a.setSongList(new ArrayList<>(songs))
                        );
                binder.readBean(buffer);
                songs.setVisible(true);
            } else {
                songs.setVisible(false);
            }
            open();
        }

        public void openCreate(Album album, Consumer<Album> action) {
            save.setText("create");
            open(album, action);
        }

        public void openUpdate(Album album, Consumer<Album> action) {
            save.setText("update");
            open(album, action);
        }


    }
}
