package org.vaadin.support.pontus.daterangefield.client;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import com.google.gwt.core.shared.GWT;
import com.google.gwt.event.dom.client.DomEvent;
import com.google.gwt.i18n.client.DateTimeFormat;
import com.google.gwt.user.datepicker.client.CalendarUtil;
import com.vaadin.client.ui.VAbstractCalendarPanel.FocusOutListener;
import com.vaadin.client.ui.VAbstractCalendarPanel.SubmitListener;
import com.vaadin.client.ui.VAbstractDateFieldCalendar;
import com.vaadin.client.ui.VDateCalendarPanel;
import com.vaadin.client.ui.VPopupCalendar;
import com.vaadin.shared.ui.datefield.DateResolution;

public class VDateRangeFieldCalendar extends
VAbstractDateFieldCalendar<VDateCalendarPanel, DateResolution> {

    @FunctionalInterface
    public interface DateRangeUpdateListener {
        public void dateUpdated(String startDate, String endDate);
    }

    private DateRangeUpdateListener updateListener;
    private Date startDate;
    private Date endDate;
    private DateTimeFormat df = DateTimeFormat.getFormat("yyyy-MM-dd");
    private boolean changed = false;

    public VDateRangeFieldCalendar() {
        super(GWT.create(VDateCalendarPanel.class), DateResolution.DAY);

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


    public void updateVisualisation() {


        Map<String, String> styleMap = new HashMap<>();
        if(startDate == null) {
            return;
        }
        Date start = (Date)startDate.clone();
        while(endDate!=null && start.before(endDate)) {
            styleMap.put(df.format(start), "v-inline-datefield-calendarpanel-day-selected");
            CalendarUtil.addDaysToDate(start, 1);
        }
        styleMap.put(df.format(start), "v-inline-datefield-calendarpanel-day-selected");
        calendarPanel.setDateStyles(styleMap);
        calendarPanel.renderCalendar();
    }

    @Override
    public void updateValueFromPanel() {
        updateBufferedValues();
        if (changed && updateListener != null) {
            updateListener.dateUpdated(df.format(startDate),
                    df.format(endDate));
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

    @Override
    public void updateBufferedValues() {
        // If field is invisible at the beginning, client can still be null when
        // this function is called.

        if (getClient() == null) {
            return;
        }

        Date date2 = calendarPanel.getDate();
        Date currentDate = getCurrentDate();
        changed =false;

        // Date range logic

        if (startDate == null) {
            startDate= (Date)date2.clone();
        } else {
            Date value = (Date) date2.clone();
            // Clicking on the edge of the range collapses the range to a single
            // day range.
            if (value != null && value.equals(startDate)) {
                startDate = value;
                endDate = (Date) value.clone();
            } else if (value != null && value.equals(endDate)) {
                startDate = endDate;
                endDate = (Date) value.clone();
            } else
                // Depending which side of the interval is clicked extend/contract
                // range
                if (startDate.before(value)) {
                    endDate = value;
                } else {
                    startDate = value;
                }
        }
        if(!date2.equals(currentDate)) {
            changed = true;
        }
        setCurrentDate((Date) date2.clone());
        updateVisualisation();

    }


    public Date getStartDate() {
        return startDate;
    }


    public void setStartDate(String startDate) {
        Date date = df.parse(startDate);
        calendarPanel.setDate((Date)date.clone());
        this.startDate = date;
    }


    public Date getEndDate() {
        return endDate;
    }


    public void setEndDate(String endDate) {
        this.endDate = df.parse(endDate);
    }

    public void setPanelDate(String date) {
        calendarPanel.setDate(df.parse(date));
    }

}
