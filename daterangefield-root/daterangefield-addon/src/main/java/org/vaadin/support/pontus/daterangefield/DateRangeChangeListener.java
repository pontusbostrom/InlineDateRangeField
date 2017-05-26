package org.vaadin.support.pontus.daterangefield;

import java.lang.reflect.Method;

import com.vaadin.util.ReflectTools;

@FunctionalInterface
public interface DateRangeChangeListener {

    public static Method CHANGE_METHOD = ReflectTools.findMethod(
            DateRangeChangeListener.class, "rangeChanged",
            DateRangeChangeEvent.class);

    public void rangeChanged(DateRangeChangeEvent e);

}
