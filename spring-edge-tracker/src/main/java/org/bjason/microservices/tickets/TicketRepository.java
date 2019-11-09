package org.bjason.microservices.tickets;

import org.bjason.microservices.users.User;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface TicketRepository extends CrudRepository<Ticket, Integer> {


    public List<Ticket> findByTicketId(Integer ticketId);
    public List<Ticket> findByTicketUserId(Integer ticketUserId);


}
