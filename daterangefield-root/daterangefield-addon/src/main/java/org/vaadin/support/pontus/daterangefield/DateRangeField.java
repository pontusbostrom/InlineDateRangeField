package org.vaadin.support.pontus.daterangefield;

import java.time.LocalDate;
import java.util.Date;

import org.vaadin.support.pontus.daterangefield.client.DateRangeFieldState;

import com.vaadin.shared.Registration;
import com.vaadin.ui.InlineDateField;

/**
 * This class implements the basics for handling date range on the server side.
 * However it extends InlineDateField and it therefore contains a LocalDate.
 * This means that it is not usable with e.g. Binder.
 *
 * Please use the class InlineDateRangeField instead. It provides a better API
 * to be used in applications. In particular getValue/setValue,
 * ValueChangeEvents works with date ranges there. This means it can be used
 * with Binder to do data binding.
 *
 * @author pontusbostrom
 *
 */
public class DateRangeField extends InlineDateField implements HasDateRange {

    public DateRangeField() {
        addStyleName("daterange-field");
    }

    // We must override getState() to cast the state to DateRangeFieldState
    @Override
    protected DateRangeFieldState getState() {
        return (DateRangeFieldState) super.getState();
    }

    @Override
    public void setDateRange(LocalDate start, LocalDate end) {
        LocalDate oldStart = convertFromDate(getState().startDate);
        getState().startDate = convertToDate(start);
        setValue(end);
        // Since startDate is set before setValue is entered, an event will not
        // be sent there. Need to send it aftewards.
        if ((oldStart == null && start != null)
                || (oldStart != null && !oldStart.equals(start))) {
            fireEvent(new DateRangeStartChangeEvent(this, oldStart, start,
                    false));
        }
    }

    @Override
    protected boolean setValue(LocalDate value, boolean userOriginated) {
        Date oldStart = getState().startDate;
        Date oldEnd = getState().endDate;
        boolean changed = super.setValue(value, userOriginated);

        Date newStart = getState().startDate;
        Date newEnd = getState().endDate;
        if ((oldStart == null && newStart != null)
                || (oldStart != null && !oldStart.equals(newStart))) {
            fireEvent(new DateRangeStartChangeEvent(this,
                    convertFromDate(oldStart), convertFromDate(newStart),
                    userOriginated));
        }

        if ((oldEnd == null && newEnd != null)
                || (oldEnd != null && !oldEnd.equals(newEnd))) {
            fireEvent(new DateRangeEndChangeEvent(this,
                    convertFromDate(oldEnd), convertFromDate(newEnd),
                    userOriginated));
        }

        return changed;

    }

    @Override
    public void doSetValue(LocalDate date) {
        if (getState().startDate == null) {
            getState().startDate = convertToDate(date);
        } else {
            Date value = convertToDate(date);
            if (getState().startDate.before(value)) {
                getState().endDate = value;
            } else {
                getState().endDate = getState().startDate;
                getState().startDate = value;
            }
        }
        super.doSetValue(date);
    }

    @Override
    public DateRange getDateRange() {
        return new DateRange(convertFromDate(getState().startDate),
                convertFromDate(getState().endDate));
    }

    @Override
    public Registration addStartDateChangeListener(
            DateRangeChangeListener changeListener) {
        return addListener(DateRangeStartChangeEvent.class, changeListener,
                DateRangeChangeListener.CHANGE_METHOD);

    }

    @Override
    public Registration addEndDateChangeListener(
            DateRangeChangeListener changeListener) {
        return addListener(DateRangeEndChangeEvent.class, changeListener,
                DateRangeChangeListener.CHANGE_METHOD);

    }
}
