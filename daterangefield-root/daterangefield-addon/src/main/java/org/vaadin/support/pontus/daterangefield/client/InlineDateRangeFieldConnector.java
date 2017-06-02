package org.vaadin.support.pontus.daterangefield.client;

import java.util.Date;

import org.vaadin.support.pontus.daterangefield.InlineDateRangeField;
import org.vaadin.support.pontus.daterangefield.client.VDateRangeFieldCalendar.DateRangeUpdateListener;

import com.vaadin.client.LocaleNotLoadedException;
import com.vaadin.client.VConsole;
import com.vaadin.client.communication.StateChangeEvent;
import com.vaadin.client.ui.AbstractFieldConnector;
import com.vaadin.shared.ui.Connect;

@Connect(InlineDateRangeField.class)
public class InlineDateRangeFieldConnector extends AbstractFieldConnector {

    public InlineDateRangeFieldConnector() {
        InlineDateRangeFieldServerRpc proxy = getRpcProxy(InlineDateRangeFieldServerRpc.class);
        VDateRangeFieldCalendar cal = getWidget();
        cal.setUpdateListener(new DateRangeUpdateListener() {

            @Override
            public void dateUpdated(Date startDate, Date endDate) {
                proxy.rangeUpdated(startDate, endDate);
            }

        });
    }

    @Override
    public VDateRangeFieldCalendar getWidget() {
        return (VDateRangeFieldCalendar) super.getWidget();
    }

    // We must implement getState() to cast to correct type
    @Override
    public InlineDateRangeFieldState getState() {
        return (InlineDateRangeFieldState) super.getState();
    }

    // Whenever the state changes in the server-side, this method is
    // called
    @Override
    public void onStateChanged(StateChangeEvent stateChangeEvent) {
        super.onStateChanged(stateChangeEvent);
        VConsole.log("State changed");
        if (stateChangeEvent.hasPropertyChanged("enabled")) {
            getWidget().setEnabled(getState().enabled);
        }

        if (stateChangeEvent.hasPropertyChanged("readOnly")) {
            getWidget().setReadonly(getState().readOnly);
        }

        if (stateChangeEvent.hasPropertyChanged("startDate")) {
            getWidget().setStartDate(getState().startDate);
        }

        if (stateChangeEvent.hasPropertyChanged("endDate")) {
            getWidget().setEndDate(getState().endDate);
        }

        getWidget().setShowISOWeekNumbers(
                getState().showISOWeekNumbers
                        && getWidget().dts.getFirstDayOfWeek() == 1);
        getWidget().calendarPanel.setDateTimeService(getWidget()
                .getDateTimeService());
        getWidget().calendarPanel.setResolution(getWidget()
                .getCurrentResolution());
        String locale = getState().locale;
        if (locale != null) {

            try {
                getWidget().dts.setLocale(locale);
                getWidget().setCurrentLocale(locale);
            } catch (LocaleNotLoadedException e) {
                getWidget().setCurrentLocale(locale);
                VConsole.error(e);

            }
        }
        Date currentDate = getWidget().getCurrentDate();
        if (currentDate != null) {
            getWidget().calendarPanel.setDate(new Date(currentDate.getTime()));
        } else {
            if (getState().startDate != null) {
                getWidget().calendarPanel.setDate((Date) getState().startDate
                        .clone());
            } else {
                getWidget().calendarPanel.setDate(null);
            }
        }

        getWidget().calendarPanel.renderCalendar();

    }
}
