package org.vaadin.support.pontus.daterangefield;

import java.time.LocalDate;
import java.util.Locale;

import org.vaadin.support.pontus.daterangefield.client.InlineDateRangeFieldServerRpc;
import org.vaadin.support.pontus.daterangefield.client.InlineDateRangeFieldState;

import com.vaadin.data.ValidationResult;
import com.vaadin.data.ValueContext;
import com.vaadin.data.validator.DateRangeValidator;
import com.vaadin.data.validator.RangeValidator;
import com.vaadin.server.UserError;
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
@SuppressWarnings("serial")
public class InlineDateRangeField extends AbstractField<DateRange> implements DateFieldUtil{

    private String dateOutOfRangeMessage;

    public InlineDateRangeField() {
        registerRpc(
                (InlineDateRangeFieldServerRpc) ((String startDate,
                        String endDate) -> setValue(
                                new DateRange(convertFromDateString(startDate),
                                        convertFromDateString(endDate)),
                                true)));

        dateOutOfRangeMessage = "Date range not within limits";

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
            getState().startDate = convertToDateString(value.getStart());
            getState().endDate = convertToDateString(value.getEnd());
        }

        RangeValidator<LocalDate> validator = getRangeValidator();
        ValidationResult result = validator.apply(value.getStart(),
                new ValueContext(this));
        if (result.isError()) {
            setComponentError(new UserError(getDateOutOfRangeMessage()));
        } else {
            result = validator.apply(value.getEnd(), new ValueContext(this));
            if (result.isError()) {
                setComponentError(new UserError(getDateOutOfRangeMessage()));
            }
        }
    }

    protected RangeValidator<LocalDate> getRangeValidator() {
        return new DateRangeValidator(getDateOutOfRangeMessage(),
                getRangeStartLimit(), getRangeEndLimit());
    }

    @Override
    public DateRange getValue() {
        DateRange range = new DateRange(
                convertFromDateString(getState(false).startDate),
                convertFromDateString(getState(false).endDate));
        return range;
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
     * Sets the start range limit for this component. If the range is overlaps
     * this date (taking the resolution into account), the component will not
     * validate. If <code>startDate</code> is set to <code>null</code>, any
     * value before <code>endDate</code> will be accepted by the range
     *
     * @param startDate
     *            - the allowed range's start date
     */
    public void setRangeStartLimit(LocalDate startDate) {
        if (afterDate(startDate, convertFromDateString(getState().rangeEnd))) {
            throw new IllegalStateException(
                    "startDate cannot be later than endDate");
        }

        getState().rangeStart = convertToDateString(startDate);
    }

    /**
     * Sets the end range limit for this component. If the range overlaps this
     * date (taking the resolution into account), the component will not
     * validate. If <code>endDate</code> is set to <code>null</code>, any value
     * after <code>startDate</code> will be accepted by the range.
     *
     * @param endDate
     *            - the allowed range's end date (inclusive, based on the
     *            current resolution)
     */
    public void setRangeEndLimit(LocalDate endDate) {
        String date = convertToDateString(endDate);
        if (afterDate(convertFromDateString(getState().rangeStart), endDate)) {
            throw new IllegalStateException(
                    "endDate cannot be earlier than startDate");
        }

        getState().rangeEnd = date;
    }

    /**
     * Returns the precise rangeStartLimit used.
     *
     * @return the precise rangeStartLimit used, may be null.
     */
    public LocalDate getRangeStartLimit() {
        return convertFromDateString(getState(false).rangeStart);
    }

    /**
     * Returns the precise rangeEndLimit used.
     *
     * @return the precise rangeEndLimit used, may be null.
     */
    public LocalDate getRangeEndLimit() {
        return convertFromDateString(getState(false).rangeEnd);
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

    public String getDateOutOfRangeMessage() {
        return dateOutOfRangeMessage;
    }

    public void setDateOutOfRangeMessage(String dateOutOfRangeMessage) {
        this.dateOutOfRangeMessage = dateOutOfRangeMessage;
    }
}
