package it.unito.cloudnative.ticketing.controller;

import it.unito.cloudnative.ticketing.model.Ticket;
import it.unito.cloudnative.ticketing.service.TicketService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@Slf4j
@RestController
@RequestMapping("/tickets")
@RequiredArgsConstructor
public class TicketController {

    private final TicketService ticketService;

    // ------------------ TICKETS ----------------

    @GetMapping
    public List<Ticket> getAllTickets() {
        log.debug("GET /tickets");
        return ticketService.getAllTickets();
    }

    @GetMapping("/ping")
    public String ping() {
        log.trace("GET /tickets/ping");
        return "pong";
    }

    @GetMapping("/{id}")
    public Optional<Ticket> getTicketById(@PathVariable Long id) {
        log.debug("GET /tickets/{}", id);
        return ticketService.getTicketById(id);
    }

    @PostMapping
    public Ticket createTicket(@RequestBody Ticket ticket) {
        log.info("POST /tickets title='{}' priority={} status={}",
                ticket.getTitle(), ticket.getPriority(), ticket.getStatus());
        return ticketService.createTicket(ticket);
    }

    @PutMapping("/{id}")
    public Ticket updateTicket(@PathVariable Long id, @RequestBody Ticket ticket) {
        log.info("PUT /tickets/{} title='{}' priority={} status={}",
                id, ticket.getTitle(), ticket.getPriority(), ticket.getStatus());
        return ticketService.updateTicket(id, ticket);
    }

    @DeleteMapping("/{id}")
    public void deleteTicket(@PathVariable Long id) {
        log.warn("DELETE /tickets/{}", id);
        ticketService.deleteTicket(id);
    }
}
