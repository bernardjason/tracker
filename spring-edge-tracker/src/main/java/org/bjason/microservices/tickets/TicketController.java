package org.bjason.microservices.tickets;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.bjason.microservices.users.Token;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.logging.Logger;


@RestController
public class TicketController {

    protected Logger logger = Logger.getLogger(TicketController.class
            .getName());

    @Autowired
    @Lazy
    private UserControlService userControlService;

    @Autowired
    RedisTemplate redisTemplate;

    @Autowired
    TicketRepository ticketRepository;

    @Autowired
    JedisConnectionFactory jedisConnectionFactory;

    public TicketController() {
        logger.info("TicketController started");
    }

    @PostMapping("/newticket")
    Ticket newticket(@RequestBody Ticket newTicket) {

        ValueOperations<String, Object> ops = redisTemplate.opsForValue();
        Long id = ops.increment("ticketIdKey:", 1);
        newTicket.ticketId = id.intValue();
        newTicket.ticketCreated = new Date();
        Ticket ticket = ticketRepository.save(newTicket);
        return ticket;
    }

    @PutMapping("/ticket")
    Ticket putTicket(@RequestBody Ticket putTicket) {
        Ticket ticket = ticketRepository.save(putTicket);
        return ticket;
    }

    @DeleteMapping("/ticket/{ticketId}")
    void deleteTicket(
            @PathVariable("ticketId") Long ticketId,
            @RequestParam("token") String tokenIn) {


        Token token = new Token(null, tokenIn, 0);
        Integer userId = userControlService.valid(token);

        if (userId != null) {

            List<Ticket> tickets = ticketRepository.findByTicketUserId(userId);

            boolean found = false;

            for (int i = 0; !found && i < tickets.size(); i++) {

                Ticket t = tickets.get(i);
                if (t.ticketId == ticketId.longValue()) {
                    //ArchiveTicket arvhiveTicket = new ArchiveTicket(t);
                    //archiveTicketRepository.save(arvhiveTicket);

                    ObjectMapper mapper = new ObjectMapper();
                    try {
                        String json = mapper.writeValueAsString(t);
                        jedisConnectionFactory.getConnection().rPush("updatequeue".getBytes() , json.getBytes());
                    } catch (JsonProcessingException e) {
                        logger.info("Delete failed,could not archive " + ticketId);
                        throw new ResponseStatusException(
                                HttpStatus.BAD_REQUEST, "not found");
                    }



                    logger.info("Delete " + ticketId);
                    ticketRepository.delete(t);
                    logger.info("Delete done " + ticketId);
                    found = true;
                }
            }
            if (!found) {
                logger.info("Delete failed,not found " + ticketId);
                throw new ResponseStatusException(
                        HttpStatus.BAD_REQUEST, "not found");

            }
        } else {
            logger.info("Delete failed login not found " + ticketId);
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST, "not logged in");
        }
    }

    @GetMapping("/mytickets")
    List<TicketData> getMytickets(@RequestParam Integer userId) {

        List<Ticket> tickets = ticketRepository.findByTicketUserId(userId);

        String userName = getUserName(userId);

        ArrayList<TicketData> ticketData = new ArrayList<TicketData>();

        for (Ticket t : tickets) {
            TicketData td = new TicketData(t.ticketId, userName, t.getTicketName(), t.getTicketCreated(), t.getTicketDetails());
            ticketData.add(td);
        }

        return ticketData;
    }


    String getUserName(Integer id) {
        String userName = "unallocated";
        if (id != null) {
            String u = userControlService.getUserName(id);
            if (u != null) {
                userName = u;
            }
        }

        return userName;
    }

    @GetMapping("/alltickets")
    List<TicketData> getAllTickets() {

        Iterable<Ticket> tickets = ticketRepository.findAll();


        ArrayList<TicketData> ticketData = new ArrayList<TicketData>();

        for (Ticket t : tickets) {
            String userName = getUserName(t.getTicketUserId());
            TicketData td = new TicketData(t.ticketId, userName, t.getTicketName(), t.getTicketCreated(), t.getTicketDetails());
            ticketData.add(td);
        }
        return ticketData;
    }
}
