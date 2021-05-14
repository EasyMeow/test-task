package com.github.easymeow.artist.ui;

import com.vaadin.flow.component.UI;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.server.ServiceInitEvent;
import com.vaadin.flow.server.VaadinServiceInitListener;
import com.vaadin.flow.server.VaadinSession;
import com.vaadin.flow.shared.communication.PushMode;
import com.vaadin.flow.shared.ui.Transport;
import org.springframework.stereotype.Component;

@Component
public class ServiceInitListener implements VaadinServiceInitListener {

    @Override
    public void serviceInit(ServiceInitEvent event) {
        event.addBootstrapListener(response -> {
            // BoostrapListener to change the bootstrap page
        });

        event.addDependencyFilter((dependencies, filterContext) -> {
            // DependencyFilter to add/remove/change dependencies sent to
            // the client
            return dependencies;
        });

        event.addRequestHandler((session, request, response) -> {
            // RequestHandler to change how responses are handled

            return false;
        });

        event.getSource().addUIInitListener(uiEvent -> {
            final UI ui = uiEvent.getUI();
            ui.addBeforeEnterListener(beforeEnterEvent -> {
                if (beforeEnterEvent.getNavigationTarget().getAnnotation(Route.class) == null) {
                    return;
                }

                String target = beforeEnterEvent.getNavigationTarget().getAnnotation(Route.class).value();

                if (!beforeEnterEvent.getNavigationTarget().equals(LoginPage.class)) {
                    Object user = VaadinSession.getCurrent().getAttribute("user");
                    if (user == null) {
                        beforeEnterEvent.forwardTo("login", target);
                    }
                }
            });

            ui.getPushConfiguration().setPushMode(PushMode.AUTOMATIC);
            ui.getPushConfiguration().setTransport(Transport.WEBSOCKET_XHR);
        });
    }

}
