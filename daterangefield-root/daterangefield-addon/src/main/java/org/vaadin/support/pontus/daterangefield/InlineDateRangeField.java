package org.vaadin.support.pontus.daterangefield;

import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneOffset;
import java.util.Date;
import java.util.Locale;

import org.vaadin.support.pontus.daterangefield.client.InlineDateRangeFieldServerRpc;
import org.vaadin.support.pontus.daterangefield.client.InlineDateRangeFieldState;

import com.vaadin.ui.AbstractField;

/**
 * An implementation of inline datefield component that makes it possible to
 * select date ranges.
 *
 * Some methods have been copied and modified from
 * com.vaadin.ui.AbstractDateField in Vaadin Framework version 8.
 *
 * @author pontusbostrom
 *
 */
public class InlineDateRangeField extends AbstractField<DateRange> {

    public InlineDateRangeField() {
        registerRpc(new InlineDateRangeFieldServerRpc() {

            @Override
            public void rangeUpdated(Date startDate, Date endDate) {
                setValue(new DateRange(convertFromDate(startDate),
                        convertFromDate(endDate)), true);
            }
        });

    }

    @Override
    public InlineDateRangeFieldState getState() {
        return (InlineDateRangeFieldState) super.getState();
    }

    @Override
    public InlineDateRangeFieldState getState(boolean markasDirty) {
        return (InlineDateRangeFieldState) super.getState(markasDirty);
    }

    @Override
    protected void doSetValue(DateRange value) {
        if (value == null) {
            getState().startDate = null;
            getState().endDate = null;
        } else {
            getState().startDate = convertToDate(value.getStart());
            getState().endDate = convertToDate(value.getEnd());
        }
    }

    @Override
    public DateRange getValue() {
        DateRange range = new DateRange(convertFromDate(getState().startDate),
                convertFromDate(getState().endDate));
        return range;
    }

    protected LocalDate convertFromDate(Date date) {
        if (date == null) {
            return null;
        }
        return Instant.ofEpochMilli(date.getTime()).atZone(ZoneOffset.UTC)
                .toLocalDate();
    }

    protected Date convertToDate(LocalDate date) {
        if (date == null) {
            return null;
        }
        return Date.from(date.atStartOfDay(ZoneOffset.UTC).toInstant());
    }

    @Override
    public void setLocale(Locale locale) {
        super.setLocale(locale);
        getState().locale = locale.toString();
    }

    @Override
    public void attach() {
        super.attach();
        Locale l = getLocale();
        if (l != null) {
            getState().locale = l.toString();
        }
    }

    /**
     * Sets the start range for this component. If the value is set before this
     * date (taking the resolution into account), the component will not
     * validate. If <code>startDate</code> is set to <code>null</code>, any
     * value before <code>endDate</code> will be accepted by the range
     *
     * @param startDate
     *            - the allowed range's start date
     */
    public void setRangeStart(LocalDate startDate) {
        Date date = convertToDate(startDate);
        if (date != null && getState().rangeEnd != null
                && date.after(getState().rangeEnd)) {
            throw new IllegalStateException(
                    "startDate cannot be later than endDate");
        }

        getState().rangeStart = date;
    }

    /**
     * Sets the end range for this component. If the value is set after this
     * date (taking the resolution into account), the component will not
     * validate. If <code>endDate</code> is set to <code>null</code>, any value
     * after <code>startDate</code> will be accepted by the range.
     *
     * @param endDate
     *            - the allowed range's end date (inclusive, based on the
     *            current resolution)
     */
    public void setRangeEnd(LocalDate endDate) {
        Date date = convertToDate(endDate);
        if (date != null && getState().rangeStart != null
                && getState().rangeStart.after(date)) {
            throw new IllegalStateException(
                    "endDate cannot be earlier than startDate");
        }

        getState().rangeEnd = date;
    }

    /**
     * Returns the precise rangeStart used.
     *
     * @return the precise rangeStart used, may be null.
     */
    public LocalDate getRangeStart() {
        return convertFromDate(getState(false).rangeStart);
    }

    /**
     * Returns the precise rangeEnd used.
     *
     * @return the precise rangeEnd used, may be null.
     */
    public LocalDate getRangeEnd() {
        return convertFromDate(getState(false).rangeEnd);
    }

    /**
     * Checks whether ISO 8601 week numbers are shown in the date selector.
     *
     * @return true if week numbers are shown, false otherwise.
     */
    public boolean isShowISOWeekNumbers() {
        return getState(false).showISOWeekNumbers;
    }

    /**
     * Sets the visibility of ISO 8601 week numbers in the date selector. ISO
     * 8601 defines that a week always starts with a Monday so the week numbers
     * are only shown if this is the case.
     *
     * @param showWeekNumbers
     *            true if week numbers should be shown, false otherwise.
     */
    public void setShowISOWeekNumbers(boolean showWeekNumbers) {
        getState().showISOWeekNumbers = showWeekNumbers;
    }
}
