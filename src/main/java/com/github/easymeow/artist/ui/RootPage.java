package com.github.easymeow.artist.ui;

import com.github.easymeow.artist.entity.User;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.RouterLink;
import com.vaadin.flow.server.VaadinSession;

@CssImport("./styles/main.css")
public abstract class RootPage extends VerticalLayout {
    private final HorizontalLayout menu = new HorizontalLayout();

    public RootPage() {
        menu.add(new RouterLink("Songs", SongsPage.class));
        menu.add(new RouterLink("Musicians", MusicianPage.class));
        menu.add(new RouterLink("Albums", AlbumPage.class));

        User user = (User) VaadinSession.getCurrent().getAttribute("user");
        Button button = new Button(user.getName(), event -> {
            VaadinSession.getCurrent().close();
        });
        button.addThemeVariants(ButtonVariant.LUMO_TERTIARY_INLINE);
        menu.add(button);

        add(menu);
    }
}
