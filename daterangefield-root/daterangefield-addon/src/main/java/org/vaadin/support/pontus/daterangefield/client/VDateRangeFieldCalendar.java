package org.vaadin.support.pontus.daterangefield.client;

import java.util.Date;

import com.google.gwt.event.dom.client.DomEvent;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.user.datepicker.client.CalendarUtil;
import com.vaadin.client.VConsole;
import com.vaadin.client.ui.VAbstractCalendarPanel.FocusOutListener;
import com.vaadin.client.ui.VAbstractCalendarPanel.SubmitListener;
import com.vaadin.client.ui.VDateCalendarPanel;
import com.vaadin.client.ui.VDateFieldCalendar;
import com.vaadin.shared.ui.datefield.DateResolution;

public class VDateRangeFieldCalendar extends VDateFieldCalendar {

    Date startDate = null;
    Date endDate = null;

    public VDateRangeFieldCalendar() {
        setCurrentResolution(DateResolution.DAY);
        calendarPanel.setFocusOutListener(new FocusOutListener() {

            @Override
            public boolean onFocusOut(DomEvent<?> event) {
                startDate = null;
                updateValueFromPanel();
                return false;
            }

        });

        calendarPanel.setSubmitListener(new SubmitListener() {

            @Override
            public void onSubmit() {
                updateValueFromPanel();
                if (startDate != null && endDate != null) {
                    updateVisualisation();
                }

            }

            @Override
            public void onCancel() {
            }

        });

    }

    public void setStartDate(Date date) {
        startDate = date;
        CalendarUtil.resetTime(startDate);
    }

    public void setEndDate(Date date) {
        endDate = date;
        CalendarUtil.resetTime(endDate);
    }

    public void updateVisualisation() {
        VConsole.log("Updating visualisations");
        VDateCalendarPanel panel = calendarPanel;
        FlexTable days = getDays(panel);
        int rowCount = days.getRowCount();
        for (int i = 0; i < rowCount; i++) {
            int cellCount = days.getCellCount(i);
            for (int j = 0; j < cellCount; j++) {
                Widget widget = days.getWidget(i, j);
                VConsole.log("Got widget for cell" + i + " " + j);
                if (widget != null) {
                    Date curday = getDateFromDayWidget(widget);
                    VConsole.log("Checking day" + curday);
                    if (isInInterval(curday)) {
                        widget.addStyleDependentName("selected");
                        VConsole.log("Updating day" + curday);
                    }

                }
            }
        }
        VConsole.log("Updated visualisations");
    }

    private boolean isInInterval(Date date) {
        return date != null && startDate != null && endDate != null
                && !date.before(startDate) && !date.after(endDate);
    }

    private native static FlexTable getDays(VDateCalendarPanel panel)
    /*-{
        return panel.@com.vaadin.client.ui.VAbstractCalendarPanel::days;
    }-*/;

    private native static Date getDateFromDayWidget(Widget widget)
    /*-{
        return widget.@com.vaadin.client.ui.VAbstractCalendarPanel$Day::getDate()();
    }-*/;
}
