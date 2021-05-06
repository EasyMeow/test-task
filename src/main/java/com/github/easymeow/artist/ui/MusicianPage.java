package com.github.easymeow.artist.ui;

import com.github.easymeow.artist.entity.Artist;
import com.github.easymeow.artist.entity.Band;
import com.github.easymeow.artist.entity.Musician;
import com.github.easymeow.artist.service.MusicianService;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
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
    private final List<Musician> musicians;


    private Button create;
    private final Grid<Musician> grid = new Grid<>();
    private final MusicianDialog dialog = new MusicianDialog();
    private final List<Musician> items = new ArrayList<>();


    public MusicianPage(MusicianService musicianService) {
        this.musicianService = musicianService;
        this.musicians = musicianService.getAll();
        initTableLayout();
        initFormLayout();
    }

    private void initTableLayout() {
        grid.addColumn(Musician::getName)
                .setHeader("Musician name")
                .setSortable(true);
        grid.addColumn(Musician::getStatus)
                .setHeader("Status")
                .setSortable(true);


        grid.addItemClickListener(event -> {
            dialog.openUpdate(event.getItem(),
                    musician -> {
                        if (musician instanceof Artist) {
                            updateArtist(musician);
                        } else if (musician instanceof Band) {
                            updateBand((Band) musician);
                        }
                    });

        });


        grid.setItems(musicians);
        add(grid);
    }

    private void initFormLayout() {
        create = new Button("create", e -> {
            dialog.openCreate((Musician) new Artist()
                    , musician -> {
                        if (dialog.status.getValue().contains("Solo Artist")) {
                            createArtist(musician.getName());
                        }
                        if (dialog.status.getValue().contains("Band")) {
                            createBand(musician.getName());
                        }

                    });
        });
        dialog.setCloseOnEsc(false);
        dialog.setCloseOnOutsideClick(false);
        add(create);
    }

    private void createArtist(String artist) {
        log.info("New Artist is created");
        musicianService.createArtist(artist);
        refreshMusicians();
    }

    private void createBand(String band) {
        log.info("New Band is created");
        musicianService.createBand(band);
        refreshMusicians();
    }

    private void updateArtist(Musician artist) {
        log.info(">> updateArtist");
        musicianService.updateArtist(artist);

        dialog.close();
        refreshMusicians();
    }

    private void updateBand(Band band) {
        log.info(">> updateBand");
        musicianService.updateBand(band);

        log.info(Arrays.toString(band.getArtists().toArray()));
        dialog.close();
        refreshMusicians();
    }

    private void refreshMusicians() {
        items.clear();
        items.addAll(musicians);
        grid.setItems(musicians);
        grid.getDataProvider().refreshAll();
    }


    public class MusicianDialog extends Dialog {
        private final TextField name = new TextField("Musician Name");
        private final ComboBox<String> status = new ComboBox<>("Status");
        private final MultiselectComboBox<Artist> artists = new MultiselectComboBox<>("Artists");
        private final Button save = new Button("Save");
        private final Button cancel = new Button("Cancel");
        private final HorizontalLayout toolbar = new HorizontalLayout(save, cancel);
        private final VerticalLayout layout = new VerticalLayout(name, status, artists, toolbar);

        private final HashSet<String> st = new HashSet<>();
        //   private final HashSet<Artist> artistList=new HashSet<>();


        private final Binder<Musician> binder = new Binder<>(Musician.class);
        private Musician buffer;
        private Consumer<Musician> action;

        public MusicianDialog() {
            add(layout);
            st.add("Solo Artist");
            st.add("Band");

            cancel.addClickListener(event -> close());

            artists.setRenderer(new ComponentRenderer<>(m -> new Label(m.getName())));
            artists.setVisible(false);

            binder.forField(name)
                    .asRequired("Name can't be null")
                    .bind(musician -> musician.getName(), (musician, str) -> musician.setName(str));

            status.setItems(st);




            save.addClickListener(event -> {
                if (binder.writeBeanIfValid(buffer)) {
                    action.accept(buffer);
                    close();
                }
            });
        }

        private void open(Musician musician, Consumer<Musician> action) {
            buffer = musician;
            this.action = action;
            binder.readBean(buffer);
            open();
        }

        public void openCreate(Musician musician, Consumer<Musician> action) {
            binder.removeBinding(artists);
            status.setVisible(true);
            save.setText("Create");
            artists.setVisible(false);
            open(musician, action);
        }

        public void openUpdate(Musician musician, Consumer<Musician> action) {
            status.setVisible(false);
            if (musician instanceof Artist) {
                artists.setVisible(false);
            }
            if (musician instanceof Band) {
                binder.forField(artists)
                        .asRequired("Band must have members")
                        .bind(m -> (new HashSet<>(((Band) m).getArtists())), (m, set) -> {
                            ((Band) m).setArtist((new ArrayList<>(set)));
                        });
                artists.setItems(musicianService.getArtists());
                artists.setVisible(true);


            }
            save.setText("Update");
            open(musician, action);
        }
    }
}
