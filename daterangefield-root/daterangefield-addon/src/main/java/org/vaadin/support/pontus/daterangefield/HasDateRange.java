package org.vaadin.support.pontus.daterangefield;

import java.time.LocalDate;

import com.vaadin.shared.Registration;

public interface HasDateRange {

    public void setDateRange(LocalDate start, LocalDate end);

    public DateRange getDateRange();

    public Registration addStartDateChangeListener(
            DateRangeChangeListener changeListener);

    public Registration addEndDateChangeListener(
            DateRangeChangeListener changeListener);
}
