package org.vaadin.support.pontus.daterangefield.demo;

import java.time.LocalDate;

import javax.servlet.annotation.WebServlet;

import org.vaadin.support.pontus.daterangefield.DateRange;
import org.vaadin.support.pontus.daterangefield.InlineDateRangeField;

import com.vaadin.annotations.Theme;
import com.vaadin.annotations.Title;
import com.vaadin.annotations.VaadinServletConfiguration;
import com.vaadin.server.VaadinRequest;
import com.vaadin.server.VaadinServlet;
import com.vaadin.ui.Button;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;

@Theme("demo")
@Title("DateRangeField Add-on Demo")
@SuppressWarnings("serial")
public class DemoUI extends UI {

    @WebServlet(value = "/*", asyncSupported = true)
    @VaadinServletConfiguration(productionMode = false, ui = DemoUI.class)
    public static class Servlet extends VaadinServlet {
    }

    @Override
    protected void init(VaadinRequest request) {

        // Initialize our new UI component
        final InlineDateRangeField component = new InlineDateRangeField();
        component.setCaption("Date field");

        // Show it in the middle of the screen
        final VerticalLayout layout = new VerticalLayout();
        layout.setStyleName("demoContentLayout");
        layout.setSizeFull();
        layout.setMargin(false);
        layout.setSpacing(false);
        component.addStartDateChangeListener(e -> {
            System.out.println("Start date changed from " + e.getOldValue()
                    + " to " + e.getValue());
        });

        component.addEndDateChangeListener(e -> {
            System.out.println("End date changed from " + e.getOldValue()
                    + " to " + e.getValue());
        });

        component.addValueChangeListener(e -> {
            System.out.println("Value changed from " + e.getOldValue() + " to "
                    + e.getValue());
        });

        component.setDateRange(LocalDate.now().minusDays(5), LocalDate.now()
                .plusDays(5));

        Button b = new Button("Change range");
        b.addClickListener(click -> {
            component.setValue(new DateRange(LocalDate.now().minusDays(3),
                    LocalDate.now().plusDays(3)));
        });
        layout.addComponents(component, b);
        setContent(layout);
    }
}
