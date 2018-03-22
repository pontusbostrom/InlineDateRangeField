package org.vaadin.support.pontus.daterangefield.client;

import java.util.Date;
import java.util.Map;

import com.google.gwt.core.shared.GWT;
import com.google.gwt.event.dom.client.DomEvent;
import com.vaadin.client.ui.VAbstractCalendarPanel.FocusOutListener;
import com.vaadin.client.ui.VAbstractCalendarPanel.SubmitListener;
import com.vaadin.client.ui.VAbstractDateFieldCalendar;
import com.vaadin.client.ui.VPopupCalendar;
import com.vaadin.shared.ui.datefield.DateResolution;

public class VDateRangeFieldCalendar extends
        VAbstractDateFieldCalendar<VDateRangeCalendarPanel, DateResolution> {
    public interface DateRangeUpdateListener {
        public void dateUpdated(Date startDate, Date endDate);
    }

    private DateRangeUpdateListener updateListener;

    public VDateRangeFieldCalendar() {
        super(GWT.create(VDateRangeCalendarPanel.class), DateResolution.DAY);

        calendarPanel.setFocusOutListener(new FocusOutListener() {

            @Override
            public boolean onFocusOut(DomEvent<?> event) {
                updateValueFromPanel();
                return false;
            }

        });

        calendarPanel.setSubmitListener(new SubmitListener() {

            @Override
            public void onSubmit() {
                updateValueFromPanel();

            }

            @Override
            public void onCancel() {
            }

        });

    }

    public void setStartDate(Date date) {
        calendarPanel.setStartDate(date);
    }

    public void setEndDate(Date date) {
        calendarPanel.setEndDate(date);
    }

    public void updateVisualisation() {
        calendarPanel.updateVisualisation();
    }

    @Override
    @SuppressWarnings("deprecation")
    public void updateValueFromPanel() {
        // If field is invisible at the beginning, client can still be null when
        // this function is called.

        Date date2 = calendarPanel.getDate();
        Date currentDate = getCurrentDate();
        Date startDate = calendarPanel.getStartDate();
        Date endDate = calendarPanel.getEndDate();

        // Date range logic

        if (startDate == null) {
            calendarPanel.setStartDate((Date) date2.clone());
        } else {
            Date value = (Date) date2.clone();
            // Clicking on the edge of the range collapses the range to a single
            // day range.
            if (value != null && value.equals(startDate)) {
                calendarPanel.setStartDate(value);
                calendarPanel.setEndDate((Date) value.clone());
            } else if (value != null && value.equals(endDate)) {
                calendarPanel.setStartDate(endDate);
                calendarPanel.setEndDate((Date) value.clone());
            } else
            // Depending which side of the interval is clicked extend/contract
            // range
            if (startDate.before(value)) {
                calendarPanel.setEndDate(value);
            } else {
                calendarPanel.setStartDate(value);
            }
        }

        setCurrentDate((Date) date2.clone());
        calendarPanel.updateVisualisation();

        if (currentDate == null || startDate == null || endDate == null
                || !(startDate.getTime() == endDate.getTime()
                        && startDate.getTime() == currentDate.getTime()
                        && date2.getTime() == currentDate.getTime())) {

            if (updateListener != null) {
                updateListener.dateUpdated(calendarPanel.getStartDate(),
                        calendarPanel.getEndDate());
            }
        }
    }

    @Override
    public void setCurrentResolution(DateResolution resolution) {
        super.setCurrentResolution(
                resolution == null ? DateResolution.YEAR : resolution);
    }

    @Override
    public String resolutionAsString() {
        return getResolutionVariable(getCurrentResolution());
    }

    @Override
    public boolean isYear(DateResolution resolution) {
        return DateResolution.YEAR.equals(resolution);
    }

    @Override
    protected DateResolution[] doGetResolutions() {
        return DateResolution.values();
    }

    @Override
    protected Date getDate(Map<DateResolution, Integer> dateVaules) {
        return VPopupCalendar.makeDate(dateVaules);
    }

    public DateRangeUpdateListener getUpdateListener() {
        return updateListener;
    }

    public void setUpdateListener(DateRangeUpdateListener updateListener) {
        this.updateListener = updateListener;
    }

    @Override
    protected boolean supportsTime() {
        return false;
    }
}
