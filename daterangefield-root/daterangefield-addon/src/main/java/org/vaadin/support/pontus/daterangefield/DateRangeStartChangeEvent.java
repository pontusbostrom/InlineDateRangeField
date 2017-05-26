package org.vaadin.support.pontus.daterangefield;

import java.time.LocalDate;

import com.vaadin.data.HasValue;

class DateRangeStartChangeEvent extends DateRangeChangeEvent {

    public DateRangeStartChangeEvent(HasValue<LocalDate> hasValue,
            LocalDate oldValue, LocalDate value, boolean userOriginated) {
        super(hasValue, oldValue, value, userOriginated);
    }
}
