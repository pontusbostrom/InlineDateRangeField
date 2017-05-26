package org.vaadin.support.pontus.daterangefield;

import java.time.LocalDate;

import com.vaadin.data.HasValue;
import com.vaadin.shared.Registration;
import com.vaadin.ui.CustomComponent;

/**
 * An implementation of inline datefield component that makes it possible to
 * select date ranges. The internal implementation is based on an extension of
 * InlineDateField, DateRangeField.
 *
 *
 * @author pontusbostrom
 *
 */
public class InlineDateRangeField extends CustomComponent implements
        HasValue<DateRange>, HasDateRange {

    private DateRangeField field;
    private DateRange value;

    public InlineDateRangeField() {
        field = new DateRangeField();
        setCompositionRoot(field);
        value = field.getDateRange();
        field.addValueChangeListener(ve -> {
            setValue(value, field.getDateRange(), ve.isUserOriginated());
        });

    }

    @Override
    public void setReadOnly(boolean rdonly) {
        field.setReadOnly(rdonly);
    }

    @Override
    public boolean isReadOnly() {
        return field.isReadOnly();
    }

    @Override
    public void setValue(DateRange value) {
        DateRange old = value;
        if (value == null) {
            field.setDateRange(null, null);
        } else {
            field.setDateRange(value.getStart(), value.getEnd());
        }
        setValue(old, value, false);
    }

    public void setValue(DateRange old, DateRange value, boolean userOriginated) {
        this.value = value;
        if ((old.getStart() == null && value.getStart() != null)
                || (old.getStart() != null && !old.getStart().equals(
                        value.getStart()))) {
            fireEvent(new ValueChangeEvent<DateRange>(this, this, old,
                    userOriginated));
        } else if ((old.getEnd() == null && value.getEnd() != null)
                || (old.getEnd() != null && !old.getEnd()
                        .equals(value.getEnd()))) {
            fireEvent(new ValueChangeEvent<DateRange>(this, this, old,
                    userOriginated));
        }

    }

    @Override
    public DateRange getValue() {
        return value;
    }

    @Override
    public Registration addValueChangeListener(
            com.vaadin.data.HasValue.ValueChangeListener<DateRange> listener) {
        return addListener(ValueChangeEvent.class, listener,
                ValueChangeListener.VALUE_CHANGE_METHOD);
    }

    @Override
    public boolean isRequiredIndicatorVisible() {
        return field.isRequiredIndicatorVisible();
    }

    @Override
    public void setRequiredIndicatorVisible(boolean visible) {
        field.setRequiredIndicatorVisible(visible);
    }

    @Override
    public DateRange getEmptyValue() {
        return new DateRange(null, null);
    }

    @Override
    public void setDateRange(LocalDate start, LocalDate end) {
        field.setDateRange(start, end);

    }

    @Override
    public DateRange getDateRange() {
        return field.getDateRange();
    }

    public DateRangeField getInternalField() {
        return field;
    }

    @Override
    public Registration addStartDateChangeListener(
            DateRangeChangeListener changeListener) {
        return field.addStartDateChangeListener(changeListener);

    }

    @Override
    public Registration addEndDateChangeListener(
            DateRangeChangeListener changeListener) {
        return field.addEndDateChangeListener(changeListener);
    }
}
