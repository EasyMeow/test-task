package com.github.easymeow.artist.ui;

import com.github.easymeow.artist.entity.Musician;
import com.github.easymeow.artist.entity.Song;
import com.github.easymeow.artist.service.MusicianService;
import com.github.easymeow.artist.service.Studio;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.HeaderRow;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.IntegerField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.data.binder.ValidationResult;
import com.vaadin.flow.data.provider.CallbackDataProvider;
import com.vaadin.flow.data.provider.ConfigurableFilterDataProvider;
import com.vaadin.flow.data.renderer.ComponentRenderer;
import com.vaadin.flow.data.value.ValueChangeMode;
import com.vaadin.flow.router.Route;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.vaadin.gatanaso.MultiselectComboBox;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.function.Consumer;

@Route("songs")
public class SongsPage extends VerticalLayout {
    private final static Logger log = LoggerFactory.getLogger(SongsPage.class);
    private final Studio studio;
    private final MusicianService musicianService;
    private final List<Musician> musicians;

    private final SongsFilter filter = new SongsFilter();
    private Button create;
    private final Grid<Song> grid = new Grid<>();
    private final SongDialog dialog = new SongDialog();
    private final List<Song> items = new ArrayList<>();
    private ConfigurableFilterDataProvider<Song, Void, SongsFilter> dataProvider;

    @Autowired
    public SongsPage(Studio studio, MusicianService musicianService) {
        this.studio = studio;
        this.musicianService = musicianService;
        this.musicians = musicianService.getAll();

        initSongDialog();
        initTableLayout();
        initFormLayout();

        refreshSongs();
    }

    private void initSongDialog() {
        dialog.setCloseOnEsc(true);
        dialog.setCloseOnOutsideClick(false);
    }

    private void initTableLayout() {
        initDataProvider();

        TextField filter = new TextField();
        filter.setPlaceholder("Song Name");
        filter.setValueChangeMode(ValueChangeMode.EAGER);
        filter.addValueChangeListener(event -> {
            this.filter.setName(event.getValue());
            log.info("Set filter: " + event.getValue());
            dataProvider.refreshAll();
        });

        // TODO filter by any musician name
        IntegerField countFilter = new IntegerField();
        countFilter.setPlaceholder("Count");
        countFilter.setValueChangeMode(ValueChangeMode.EAGER);
        countFilter.addValueChangeListener(event -> {
            int value = event.getValue() == null ? 0 : event.getValue();
            this.filter.setMusiciansCount(value);
            log.info("Set count filter: " + value);
            dataProvider.refreshAll();
        });

        grid.setSelectionMode(Grid.SelectionMode.SINGLE);

        Grid.Column<Song> nameColumn = grid.addColumn(Song::getName)
                .setHeader("Name")
                .setSortable(true);

        Grid.Column<Song> musiciansColumn = grid.addComponentColumn(song -> {
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
                dialog.openUpdate(event.getFirstSelectedItem().get(), musicians,
                        song -> {
                            updateSong(song);
                        });
            }
        });

        HeaderRow header = grid.appendHeaderRow();
        header.getCell(nameColumn).setComponent(filter);
        header.getCell(musiciansColumn).setComponent(countFilter);

        add(grid);
    }

    private void initDataProvider() {
        // TODO use sorting
        CallbackDataProvider<Song, SongsFilter> callbackDataProvider = new CallbackDataProvider<>(
                query -> {
                    if (query.getFilter().isPresent()) {
                        return studio.getAllSongsByName(query.getFilter().get().getName()).stream()
                                .filter(s -> (query.getFilter().get().musiciansCount == 0) || (s.getMusicians().size() == query.getFilter().get().musiciansCount))
                                .skip(Math.max(query.getOffset() - 1, 0))
                                .limit(query.getLimit());
                    } else {
                        return studio.getSongs().stream()
                                .skip(Math.max(query.getOffset() - 1, 0))
                                .limit(query.getLimit());
                    }
                },
                query -> {
                    if (query.getFilter().isPresent()) {
                        return (int) studio.getAllSongsByName(query.getFilter().get().getName())
                                .stream()
                                .filter(s -> (query.getFilter().get().musiciansCount == 0) || (s.getMusicians().size() == query.getFilter().get().musiciansCount))
                                .count();
                    } else {
                        return studio.getSongs().size();
                    }
                });

        dataProvider = callbackDataProvider
                .withConfigurableFilter();

        dataProvider.setFilter(this.filter);

        grid.setDataProvider(dataProvider);
    }

    private void initFormLayout() {
        create = new Button("create", e -> {
            dialog.openCreate(new Song(), musicians,
                    song -> {
                        createSong(song.getName(), song.getMusicians().toArray(new Musician[0]));
                    });
        });

        dialog.setCloseOnEsc(false);
        dialog.setCloseOnOutsideClick(false);
        add(create);
    }

    private void createSong(String songName, Musician... musician) {
        log.info(">> createSong");
        studio.record(songName, musician);

        dialog.close();
        refreshSongs();
    }

    private void updateSong(Song song) {
        log.info(">> updateSong");
        studio.updateSong(song);
        dialog.close();
        refreshSongs();
    }

    private void refreshSongs() {
        dataProvider.refreshAll();
    }

    public static class SongDialog extends Dialog {
        private final TextField name = new TextField("Song Name");
        private final MultiselectComboBox<Musician> musician = new MultiselectComboBox<>("Musicians");
        private final Button save = new Button("Save");
        private final Button cancel = new Button("Cancel");
        private final HorizontalLayout toolbar = new HorizontalLayout(save, cancel);
        private final VerticalLayout layout = new VerticalLayout(name, musician, toolbar);

        private final Binder<Song> binder = new Binder<>(Song.class);
        private Song buffer;
        private Consumer<Song> action;

        public SongDialog() {
            add(layout);

            cancel.addClickListener(event -> close());

            musician.setRenderer(new ComponentRenderer<>(m -> new Label(m.getName())));

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
//                    .asRequired("Song must have a performer")
                    .asRequired((p, valueContext) -> {
                        if ((p == null) || p.isEmpty()) {
                            return ValidationResult.error("Song must have a performer");
                        }
                        return ValidationResult.ok();
                    })
                    .bind(song -> new HashSet<>(song.getMusicians()), (song, set) -> song.setMusicians(new ArrayList<>(set)));

            save.addClickListener(event -> {
                if (binder.writeBeanIfValid(buffer)) {
                    action.accept(buffer);
                }
            });
        }

        private void open(Song song, List<Musician> musicians, Consumer<Song> action) {
            buffer = song;
            this.action = action;
            musician.setItems(musicians);
            binder.readBean(buffer);

            open();
        }

        public void openCreate(Song song, List<Musician> musicians, Consumer<Song> action) {
            open(song, musicians, action);
            save.setText("Create");
        }

        public void openUpdate(Song song, List<Musician> musicians, Consumer<Song> action) {
            open(song, musicians, action);
            save.setText("Update");
        }
    }

    public static class SongsFilter {
        private String name;
        private int musiciansCount;

        public int getMusiciansCount() {
            return musiciansCount;
        }

        public void setMusiciansCount(int musiciansCount) {
            this.musiciansCount = musiciansCount;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }
    }
}
