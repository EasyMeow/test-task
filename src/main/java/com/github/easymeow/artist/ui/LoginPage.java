package com.github.easymeow.artist.ui;

import com.github.easymeow.artist.entity.User;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.*;
import com.vaadin.flow.server.VaadinSession;
import org.apache.logging.log4j.util.Strings;

@Route("login")
public class LoginPage extends VerticalLayout implements HasUrlParameter<String>, BeforeEnterObserver {
    private String forwardTo = "";

    public LoginPage() {
        TextField textField = new TextField();
        Button button = new Button("Login", event -> {
            String name = textField.getValue();
            if (Strings.isBlank(name)) textField.setErrorMessage("Should be not empty");
            else {
                User user = new User(name);
                VaadinSession.getCurrent().setAttribute("user", user);

                forward();
            }
        });

        add(textField, button);
    }

    private void forward() {
        if (Strings.isBlank(forwardTo)) {
            forwardTo = "songs";
        }
        getUI().ifPresent(ui -> ui.navigate(forwardTo));
    }

    @Override
    public void setParameter(BeforeEvent event, @OptionalParameter String parameter) {
        forwardTo = parameter;
    }

    @Override
    public void beforeEnter(BeforeEnterEvent event) {
        Object user = VaadinSession.getCurrent().getAttribute("user");
        if (user != null) {
            if (Strings.isBlank(forwardTo)) {
                forwardTo = "songs";
            }
            event.forwardTo(forwardTo);
        }
    }
}
