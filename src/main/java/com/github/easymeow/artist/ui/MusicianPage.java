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
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.vaadin.gatanaso.MultiselectComboBox;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.function.Consumer;

@PageTitle("Musicians")
@Route(value = "musicians", layout = RootLayout.class)
public class MusicianPage extends VerticalLayout {
    private final static Logger log = LoggerFactory.getLogger(MusicianPage.class);
    private final MusicianService musicianService;
    private Button createBand;
    private Button createArtist;
    private final TreeGrid<Musician> grid = new TreeGrid<>();
    private final MusicianDialog musicianDialog;

    public MusicianPage(MusicianService musicianService) {
        this.musicianService = musicianService;
        musicianDialog = new MusicianDialog(musicianService);
        setPadding(false);

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
                musicianDialog.openUpdate((Artist) event.getItem(),
                        this::updateArtist);
            }
            if (event.getItem() instanceof Band) {
                musicianDialog.openUpdate((Band) event.getItem(), m -> updateBand((Band) m));
            }

        });

        grid.addClassName("common-grid");
        add(grid);
    }

    private void initFormLayout() {
        createArtist = new Button("create Artist", e -> {
            musicianDialog.openCreate(new Artist(),
                    artist -> createArtist(artist.getName()));
        });

        createBand = new Button("create Band", e -> {
            musicianDialog.openCreate(new Band(), band -> {
                createBand(band.getName(), ((Band) band).getArtists());
            });
        });

        add(createBand, createArtist);
    }

    private void createArtist(String artist) {
        log.info("New Artist is created");
        musicianService.createArtist(artist);
        musicianDialog.close();
        refreshMusicians();
    }

    private void createBand(String band, List<Artist> artists) {
        log.info("New Band is created");

        Band m = musicianService.createBand(band, artists);
        log.info(Arrays.toString(m.getArtists().toArray()));
        musicianDialog.close();
        refreshMusicians();
    }

    private void updateArtist(Musician artist) {
        log.info(">> updateArtist");
        musicianService.updateArtist(artist);
        musicianDialog.close();
        refreshMusicians();
    }

    private void updateBand(Band band) {
        log.info(">> updateBand");
        musicianService.updateBand(band);
        log.info(Arrays.toString(band.getArtists().toArray()));
        musicianDialog.close();
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

    public static class MusicianDialog extends Dialog {
        private final TextField name = new TextField("Musician Name");
        private final MultiselectComboBox<Artist> artists = new MultiselectComboBox<>("Artists");
        private final Button save = new Button("Save");
        private final Button cancel = new Button("Cancel");
        private final HorizontalLayout toolbar = new HorizontalLayout(save, cancel);
        private final VerticalLayout layout = new VerticalLayout(name, artists, toolbar);

        private final Binder<Musician> binder = new Binder<>(Musician.class);
        private final MusicianService musicianService;
        private Musician buffer;
        private Consumer<Musician> action;


        public MusicianDialog(MusicianService musicianService) {
            add(layout);
            this.musicianService = musicianService;
            layout.addClassName("dialog");
            cancel.addClickListener(event -> close());
            artists.setRenderer(new ComponentRenderer<>(m -> new Label(m.getName())));
            binder.forField(name)
                    .asRequired("Name can't be null")
                    .bind(musician -> musician.getName(), (musician, str) -> musician.setName(str));

            save.addClickListener(event -> {
                ArrayList<Artist> before = new ArrayList<>();

                if (buffer instanceof Band) {
                    before.addAll(((Band) buffer).getArtists());
                }
                if (binder.writeBeanIfValid(buffer)) {
                    if (buffer instanceof Band) {
                        musicianService.updateAvailableArtists(before, artists.getSelectedItems());
                    }
                    action.accept(buffer);
                    close();
                }
            });
        }

        private void open(Musician musician, Consumer<Musician> action) {
            buffer = musician;
            this.action = action;
            if (musician instanceof Band) {
                artists.setItems(musicianService.getAvailableArtists());
            }
            binder.readBean(buffer);
            open();
        }

        public void openCreate(Musician musician, Consumer<Musician> action) {
            if (musician instanceof Artist) {
                unbindArtists();
            } else {
                bindArtists();
            }
            save.setText("Create");
            open(musician, action);
        }

        public void openUpdate(Musician musician, Consumer<Musician> action) {
            if (musician instanceof Band) {
                bindArtists();
            } else {
                unbindArtists();
            }
            save.setText("Update");
            open(musician, action);
        }

        private void unbindArtists() {
            binder.removeBinding(artists);
            artists.setVisible(false);
        }

        private void bindArtists() {
            artists.setVisible(true);
            binder.forField(artists)
                    .asRequired("Band must have members")
                    .bind(m -> (new HashSet<>(((Band) m).getArtists())), (m, set) -> {
                        ((Band) m).setArtist((new ArrayList<>(set)));
                    });
        }
    }
}
