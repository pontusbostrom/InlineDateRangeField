package org.vaadin.support.pontus.daterangefield.client;

import java.util.Date;

import com.vaadin.shared.communication.ServerRpc;

public interface InlineDateRangeFieldServerRpc extends ServerRpc {

    public void rangeUpdated(Date startDate, Date endDate);

}
