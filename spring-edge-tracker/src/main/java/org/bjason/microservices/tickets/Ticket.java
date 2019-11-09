package org.bjason.microservices.tickets;

import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;
import org.springframework.data.redis.core.index.Indexed;

import java.io.Serializable;
import java.util.Date;

@RedisHash("Ticket")
public class Ticket implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    protected Integer ticketId;

    @Indexed
    protected Integer ticketUserId;

    protected String ticketName;

    protected java.util.Date ticketCreated;

    protected String ticketDetails;

    public Ticket(Integer ticketId, Integer ticketUserId, String ticketName, Date ticketCreated, String ticketDetails) {
        this.ticketId = ticketId;
        this.ticketUserId = ticketUserId;
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

    public Integer getTicketUserId() {
        return ticketUserId;
    }

    public void setTicketUserId(Integer ticketUserId) {
        this.ticketUserId = ticketUserId;
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
        return "Ticket{" +
                "ticketId=" + ticketId +
                ", ticketUserId=" + ticketUserId +
                ", ticketName='" + ticketName + '\'' +
                ", ticketCreated=" + ticketCreated +
                ", ticketDetails='" + ticketDetails + '\'' +
                '}';
    }
}
