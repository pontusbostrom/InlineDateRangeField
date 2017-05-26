package org.vaadin.support.pontus.daterangefield;

import java.time.LocalDate;
import java.util.EventObject;

import com.vaadin.data.HasValue;

public class DateRangeChangeEvent extends EventObject {

    private LocalDate oldValue;
    private LocalDate value;
    private boolean userOriginated;

    public DateRangeChangeEvent(HasValue<LocalDate> hasValue,
            LocalDate oldValue, LocalDate value, boolean userOriginated) {
        super(hasValue);
        this.oldValue = oldValue;
        this.value = value;
        this.userOriginated = userOriginated;
    }

    public LocalDate getOldValue() {
        return oldValue;
    }

    public LocalDate getValue() {
        return value;
    }

    public boolean isUserOriginated() {
        return userOriginated;
    }

}
