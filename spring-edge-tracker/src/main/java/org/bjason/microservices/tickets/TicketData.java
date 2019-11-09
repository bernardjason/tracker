package org.bjason.microservices.tickets;

import java.io.Serializable;
import java.util.Date;

public class TicketData implements Serializable {

    private static final long serialVersionUID = 1L;

    protected Integer ticketId;

    protected String ticketUserName;

    protected String ticketName;

    protected Date ticketCreated;

    protected String ticketDetails;

    public TicketData(Integer ticketId, String ticketUserName, String ticketName, Date ticketCreated, String ticketDetails) {
        this.ticketId = ticketId;
        this.ticketUserName = ticketUserName;
        this.ticketName = ticketName;
        this.ticketCreated = ticketCreated;
        this.ticketDetails = ticketDetails;
    }

    public Integer getTicketId() {
        return ticketId;
    }

    public void setTicketId(Integer ticketId) {
        this.ticketId = ticketId;
    }

    public String getTicketUserName() {
        return ticketUserName;
    }

    public void setTicketUserName(String ticketUserName) {
        this.ticketUserName = ticketUserName;
    }

    public String getTicketName() {
        return ticketName;
    }

    public void setTicketName(String ticketName) {
        this.ticketName = ticketName;
    }

    public Date getTicketCreated() {
        return ticketCreated;
    }

    public void setTicketCreated(Date ticketCreated) {
        this.ticketCreated = ticketCreated;
    }

    public String getTicketDetails() {
        return ticketDetails;
    }

    public void setTicketDetails(String ticketDetails) {
        this.ticketDetails = ticketDetails;
    }

    @Override
    public String toString() {
        return "TicketData{" +
                "ticketId=" + ticketId +
                ", ticketUserName='" + ticketUserName + '\'' +
                ", ticketName='" + ticketName + '\'' +
                ", ticketCreated=" + ticketCreated +
                ", ticketDetails='" + ticketDetails + '\'' +
                '}';
    }
}
