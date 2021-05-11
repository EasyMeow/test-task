package com.github.easymeow.artist.ui;

import com.github.easymeow.artist.entity.Artist;
import com.github.easymeow.artist.entity.Band;
import com.github.easymeow.artist.entity.Musician;
import com.github.easymeow.artist.service.MusicianService;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.component.treegrid.TreeGrid;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.data.renderer.ComponentRenderer;
import com.vaadin.flow.router.Route;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.vaadin.gatanaso.MultiselectComboBox;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.function.Consumer;

@Route("musicians")
public class MusicianPage extends VerticalLayout {
    private final static Logger log = LoggerFactory.getLogger(MusicianPage.class);
    private final MusicianService musicianService;

    private Button createBand;
    private Button createArtist;
    private final TreeGrid<Musician> grid = new TreeGrid<>();
    private final ArtistDialog artistDialog = new ArtistDialog();
    private final BandDialog bandDialog = new BandDialog();

    public MusicianPage(MusicianService musicianService) {
        this.musicianService = musicianService;
        initTableLayout();
        initFormLayout();

        refreshMusicians();
    }

    private void initTableLayout() {
        grid.addHierarchyColumn(Musician::getName)
                .setHeader("Musician name")
                .setSortable(true);
        grid.addColumn(Musician::getStatus)
                .setHeader("Status")
                .setSortable(true);


        grid.addItemDoubleClickListener(event -> {
            if (event.getItem() instanceof Artist) {
                artistDialog.openUpdate((Artist) event.getItem(),
                        this::updateArtist);
            }
            if (event.getItem() instanceof Band) {
                bandDialog.openUpdate((Band) event.getItem(), this::updateBand);
            }

        });
        add(grid);
    }

    private void initFormLayout() {
        createArtist = new Button("create Artist", e -> {
            artistDialog.openCreate(new Artist(),
                    artist -> createArtist(artist.getName()));
        });

        createBand = new Button("create Band", e -> {
            bandDialog.openCreate(new Band(), band -> {
                createBand(band.getName(), band.getArtists());
            });
        });

        add(createBand, createArtist);
    }

    private void createArtist(String artist) {
        log.info("New Artist is created");
        musicianService.createArtist(artist);
        artistDialog.close();
        refreshMusicians();
    }

    private void createBand(String band, List<Artist> artists) {
        log.info("New Band is created");

        Band m = musicianService.createBand(band, artists);
        log.info(Arrays.toString(m.getArtists().toArray()));
        artistDialog.close();
        refreshMusicians();
    }

    private void updateArtist(Musician artist) {
        log.info(">> updateArtist");
        musicianService.updateArtist(artist);
        artistDialog.close();
        refreshMusicians();
    }

    private void updateBand(Band band) {
        log.info(">> updateBand");
        musicianService.updateBand(band);
        log.info(Arrays.toString(band.getArtists().toArray()));
        bandDialog.close();
        refreshMusicians();
    }

    private void refreshMusicians() {
        grid.setItems(new ArrayList<>(musicianService.getHierarchy()), musician -> {
            if (musician instanceof Band) {
                return new ArrayList<>(((Band) musician).getArtists());
            } else
                return new ArrayList<>();
        });
    }


    public static class ArtistDialog extends Dialog {
        private final TextField name = new TextField("Musician Name");
        private final Button save = new Button("Save");
        private final Button cancel = new Button("Cancel");
        private final HorizontalLayout toolbar = new HorizontalLayout(save, cancel);
        private final VerticalLayout layout = new VerticalLayout(name, toolbar);


        private final Binder<Artist> binder = new Binder<>(Artist.class);
        private Artist buffer;
        private Consumer<Artist> action;

        public ArtistDialog() {
            add(layout);
            cancel.addClickListener(event -> close());

            binder.forField(name)
                    .asRequired("Name can't be null")
                    .bind(musician -> musician.getName(), (musician, str) -> musician.setName(str));

            save.addClickListener(event -> {
                if (binder.writeBeanIfValid(buffer)) {
                    action.accept(buffer);
                    close();
                }
            });
        }

        private void open(Artist musician, Consumer<Artist> action) {
            buffer = musician;
            this.action = action;
            binder.readBean(buffer);
            open();
        }

        public void openCreate(Artist musician, Consumer<Artist> action) {
            save.setText("Create");
            open(musician, action);
        }

        public void openUpdate(Artist musician, Consumer<Artist> action) {
            save.setText("Update");
            open(musician, action);
        }
    }

    // TODO update all entities on service layer
    // TODO mode common code to base class
    public class BandDialog extends Dialog {
        private final TextField name = new TextField("Musician Name");
        private final MultiselectComboBox<Artist> artists = new MultiselectComboBox<>("Artists");
        private final Button save = new Button("Save");
        private final Button cancel = new Button("Cancel");
        private final HorizontalLayout toolbar = new HorizontalLayout(save, cancel);
        private final VerticalLayout layout = new VerticalLayout(name, artists, toolbar);

        private final Binder<Band> binder = new Binder<>(Band.class);
        private Band buffer;
        private Consumer<Band> action;


        public BandDialog() {
            add(layout);

            cancel.addClickListener(event -> close());
            artists.setRenderer(new ComponentRenderer<>(m -> new Label(m.getName())));
            binder.forField(name)
                    .asRequired("Name can't be null")
                    .bind(musician -> musician.getName(), (musician, str) -> musician.setName(str));

            binder.forField(artists)
                    .asRequired("Band must have members")
                    .bind(m -> (new HashSet<>(m.getArtists())), (m, set) -> {
                        m.setArtist((new ArrayList<>(set)));
                    });

            save.addClickListener(event -> {
                ArrayList<Artist> before = new ArrayList<>(buffer.getArtists());
                if (binder.writeBeanIfValid(buffer)) {
                    for (Artist a : before) {
                        if (!artists.getSelectedItems().contains(a)) {
                            a.setMember(false);
                        }
                    }

                    for (Artist a : artists.getSelectedItems()) {
                        a.setMember(true);
                    }

                    action.accept(buffer);
                    close();
                }
            });
        }

        private void open(Band band, Consumer<Band> action) {
            buffer = band;
            this.action = action;
            artists.setItems(musicianService.getAvailableOrUnavailableArtists(false));
            binder.readBean(buffer);
            open();
        }

        public void openCreate(Band band, Consumer<Band> action) {
            save.setText("Create");
            open(band, action);
        }

        public void openUpdate(Band band, Consumer<Band> action) {
            save.setText("Update");
            open(band, action);
        }
    }
}
