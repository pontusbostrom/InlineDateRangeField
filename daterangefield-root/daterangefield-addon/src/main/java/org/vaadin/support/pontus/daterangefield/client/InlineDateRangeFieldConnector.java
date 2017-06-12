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

        getWidget().calendarPanel.setDateTimeService(getWidget()
                .getDateTimeService());
        getWidget().calendarPanel.setResolution(getWidget()
                .getCurrentResolution());
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
        boolean visualsChanged = false;
        if (stateChangeEvent.hasPropertyChanged("enabled")) {
            getWidget().setEnabled(getState().enabled);
            visualsChanged = true;
        }

        if (stateChangeEvent.hasPropertyChanged("readOnly")) {
            getWidget().setReadonly(getState().readOnly);
            visualsChanged = true;
        }

        if (stateChangeEvent.hasPropertyChanged("locale")) {
            String locale = getState().locale;
            if (locale != null) {

                try {
                    getWidget().dts.setLocale(locale);
                    getWidget().setCurrentLocale(locale);
                } catch (LocaleNotLoadedException e) {
                    getWidget().setCurrentLocale(locale);
                    VConsole.error(e);

                }
                visualsChanged = true;
            }
        }

        if (stateChangeEvent.hasPropertyChanged("startDate")) {

            Date currentStart = getWidget().calendarPanel.getStartDate();
            if (currentStart == null && getState().startDate == null) {
                // No change, No need to update
            } else if ((currentStart == null && getState().startDate != null)
                    || !currentStart.equals(getState().startDate)) {
                getWidget().setStartDate(getState().startDate);
                visualsChanged = true;
            }

            Date currentDate = getWidget().calendarPanel.getDate();
            if (currentDate == null) {
                if (getState().startDate != null) {
                    getWidget().calendarPanel
                            .setDate((Date) getState().startDate.clone());
                } else {
                    getWidget().calendarPanel.setDate(null);
                }
            }
        }

        if (stateChangeEvent.hasPropertyChanged("endDate")) {
            Date currentEnd = getWidget().calendarPanel.getEndDate();
            if (currentEnd == null && getState().endDate == null) {
                // No change, No need to update
            } else if ((currentEnd == null && getState().endDate != null)
                    || !currentEnd.equals(getState().endDate)) {
                getWidget().setEndDate(getState().endDate);

                visualsChanged = true;
            }

            Date currentDate = getWidget().calendarPanel.getDate();
            if (currentDate == null) {
                if (getState().startDate != null) {
                    getWidget().calendarPanel
                            .setDate((Date) getState().startDate.clone());
                } else {
                    getWidget().calendarPanel.setDate(null);
                }
            }

        }

        if (stateChangeEvent.hasPropertyChanged("showISOWeekNumbers")) {
            getWidget().setShowISOWeekNumbers(
                    getState().showISOWeekNumbers
                            && getWidget().dts.getFirstDayOfWeek() == 1);
            visualsChanged = true;
        }

        if (stateChangeEvent.hasPropertyChanged("rangeStart")) {
            getWidget().calendarPanel.setRangeStart(getState().rangeStart);
            visualsChanged = true;
        }

        if (stateChangeEvent.hasPropertyChanged("rangeEnd")) {
            getWidget().calendarPanel.setRangeEnd(getState().rangeEnd);
            visualsChanged = true;
        }

        if (visualsChanged) {
            getWidget().calendarPanel.renderCalendar();
        }

    }
}
