package org.vaadin.support.pontus.daterangefield.client;

import java.util.Date;

import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.user.datepicker.client.CalendarUtil;
import com.vaadin.client.ui.VDateCalendarPanel;

public class VDateRangeCalendarPanel extends VDateCalendarPanel {

    private Date startDate;
    private Date endDate;

    @Override
    public void renderCalendar() {
        super.renderCalendar();

        if (startDate != null && endDate != null) {
            updateVisualisation();
        }

    }

    public void updateVisualisation() {

        FlexTable days = getDays(this);
        int rowCount = days.getRowCount();
        for (int i = 0; i < rowCount; i++) {
            int cellCount = days.getCellCount(i);
            for (int j = 0; j < cellCount; j++) {
                Widget widget = days.getWidget(i, j);
                if (widget != null) {
                    Date curday = getDateFromDayWidget(widget);
                    if (isInInterval(curday)) {
                        widget.addStyleDependentName("selected");
                    } else {
                        widget.removeStyleDependentName("selected");
                    }

                }
            }
        }
    }

    private boolean isInInterval(Date date) {
        if (date == null || startDate == null || endDate == null) {
            return false;
        }
        Date timelessStart = (Date) startDate.clone();
        Date timelessEnd = (Date) endDate.clone();
        Date timelessDate = (Date) date.clone();

        CalendarUtil.resetTime(timelessStart);
        CalendarUtil.resetTime(timelessEnd);
        CalendarUtil.resetTime(timelessDate);
        return !timelessDate.before(timelessStart) && !date.after(timelessEnd);
    }

    private native static FlexTable getDays(VDateCalendarPanel panel)
    /*-{
        return panel.@com.vaadin.client.ui.VAbstractCalendarPanel::days;
    }-*/;

    private native static Date getDateFromDayWidget(Widget widget)
    /*-{
        return widget.@com.vaadin.client.ui.VAbstractCalendarPanel$Day::getDate()();
    }-*/;

    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public Date getEndDate() {
        return endDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

}
