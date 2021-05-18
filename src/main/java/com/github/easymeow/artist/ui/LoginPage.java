package com.github.easymeow.artist.ui;

import com.github.easymeow.artist.entity.User;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.PasswordField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.*;
import com.vaadin.flow.server.VaadinSession;
import org.apache.logging.log4j.util.Strings;

@PageTitle("Login")
@Route("login")
public class LoginPage extends VerticalLayout implements HasUrlParameter<String>, BeforeEnterObserver {
    private String forwardTo = "";
    private final Dialog dialog = new Dialog();
    private final VerticalLayout layout = new VerticalLayout();

    public LoginPage() {

        TextField nameField = new TextField();
        nameField.setLabel("Name");
        PasswordField passwordField = new PasswordField();
        passwordField.setLabel("Password");
        Button guest = new Button("Enter as a Guest", e -> {
            User user = new User("Guest", "password");
            VaadinSession.getCurrent().setAttribute("user", user);

            forward();
            dialog.close();
        });
        Button button = new Button("Login", event -> {
            String name = nameField.getValue();
            String password = passwordField.getValue();
            if (Strings.isBlank(name)) {
                nameField.setErrorMessage("Should be not empty");
            }
            if (Strings.isBlank(password)) {
                passwordField.setErrorMessage("Should be not empty");
            } else {
                User user = new User(name, password);
                VaadinSession.getCurrent().setAttribute("user", user);

                forward();
                dialog.close();
            }
        });

        Label text = new Label("Enter username and password");
        layout.add(text, nameField, passwordField, button, guest);
        layout.addClassName("dialog");

        dialog.setCloseOnOutsideClick(false);
        dialog.setCloseOnEsc(false);
        dialog.add(layout);
        dialog.open();
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
