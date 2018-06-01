package org.vaadin.support.pontus.daterangefield;

import java.time.LocalDate;
import java.time.temporal.TemporalAccessor;

public interface DateFieldUtil extends AbstractDateFieldUtil<LocalDate> {

    @Override
    public default LocalDate toType(TemporalAccessor temporalAccessor) {
        return temporalAccessor == null? null : LocalDate.from(temporalAccessor);
    }

}
