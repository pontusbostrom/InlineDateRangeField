package org.vaadin.support.pontus.daterangefield.client;

import org.vaadin.support.pontus.daterangefield.DateRangeField;

import com.vaadin.client.ApplicationConnection;
import com.vaadin.client.UIDL;
import com.vaadin.client.VConsole;
import com.vaadin.client.communication.StateChangeEvent;
import com.vaadin.client.ui.datefield.InlineDateFieldConnector;
import com.vaadin.shared.ui.Connect;

// Connector binds client-side widget class to server-side component class
// Connector lives in the client and the @Connect annotation specifies the
// corresponding server-side component
@Connect(DateRangeField.class)
public class DateRangeFieldConnector extends InlineDateFieldConnector {

    @Override
    public void updateFromUIDL(UIDL uidl, ApplicationConnection client) {
        super.updateFromUIDL(uidl, client);

        getWidget().updateVisualisation();

    }

    @Override
    public VDateRangeFieldCalendar getWidget() {
        return (VDateRangeFieldCalendar) super.getWidget();
    }

    // We must implement getState() to cast to correct type
    @Override
    public DateRangeFieldState getState() {
        return (DateRangeFieldState) super.getState();
    }

    // Whenever the state changes in the server-side, this method is
    // called
    @Override
    public void onStateChanged(StateChangeEvent stateChangeEvent) {
        super.onStateChanged(stateChangeEvent);
        VConsole.log("State changed");
        if (stateChangeEvent.hasPropertyChanged("startDate")) {
            getWidget().setStartDate(getState().startDate);
        }

        if (stateChangeEvent.hasPropertyChanged("endDate")) {
            getWidget().setEndDate(getState().endDate);
        }

    }
}
