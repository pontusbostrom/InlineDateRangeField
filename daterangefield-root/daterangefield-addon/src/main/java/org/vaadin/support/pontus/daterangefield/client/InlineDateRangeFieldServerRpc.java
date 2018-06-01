package org.vaadin.support.pontus.daterangefield.client;

import com.vaadin.shared.communication.ServerRpc;

@FunctionalInterface
public interface InlineDateRangeFieldServerRpc extends ServerRpc {

    public void rangeUpdated(String startDate, String endDate);

}
