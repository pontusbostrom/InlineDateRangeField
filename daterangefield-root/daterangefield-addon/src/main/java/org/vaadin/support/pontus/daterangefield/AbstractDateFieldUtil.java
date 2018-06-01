package org.vaadin.support.pontus.daterangefield;

import java.io.Serializable;
import java.time.format.DateTimeFormatter;
import java.time.temporal.Temporal;
import java.time.temporal.TemporalAccessor;
import java.time.temporal.TemporalAdjuster;
import java.util.Locale;

public interface AbstractDateFieldUtil<T extends Temporal & TemporalAdjuster & Serializable & Comparable<? super T>> {
    static final DateTimeFormatter RANGE_FORMATTER = DateTimeFormatter
            .ofPattern("yyyy-MM-dd[ HH:mm:ss]", Locale.ENGLISH);

    public default T convertFromDateString(String temporalStr) {
        if (temporalStr == null) {
            return null;
        }
        return toType(RANGE_FORMATTER.parse(temporalStr));
    }

    public T toType(TemporalAccessor temporalAccessor);


    public default String convertToDateString(T temporal) {
        if (temporal == null) {
            return null;
        }
        return RANGE_FORMATTER.format(temporal);
    }

    public default boolean afterDate(T value, T base) {
        if (value == null || base == null) {
            return false;
        }
        return value.compareTo(base) > 0;
    }

}
