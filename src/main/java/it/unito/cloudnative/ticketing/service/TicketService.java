// src/main/java/it/unito/cloudnative/ticketing/service/TicketService.java
package it.unito.cloudnative.ticketing.service;

import it.unito.cloudnative.ticketing.model.Ticket;
import it.unito.cloudnative.ticketing.repository.TicketRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class TicketService {

    private final TicketRepository ticketRepository;
    private final MailService mailService; // iniettiamo il servizio email

    public List<Ticket> getAllTickets() {
        return ticketRepository.findAll();
    }

    public Optional<Ticket> getTicketById(Long id) {
        return ticketRepository.findById(id);
    }

    public Ticket createTicket(Ticket ticket) {
        Ticket saved = ticketRepository.save(ticket);
        log.info("Ticket creato id={} title='{}'", saved.getId(), saved.getTitle());

        // invio email di notifica
        try {
            String subject = "Nuovo ticket #" + saved.getId() + " - " + nullToEmpty(saved.getTitle());
            String body = """
                <h2>Nuovo ticket creato</h2>
                <p><b>ID:</b> %d</p>
                <p><b>Titolo:</b> %s</p>
                <p><b>Priorità:</b> %s</p>
                <p><b>Status:</b> %s</p>
                <p><b>Descrizione:</b><br/>%s</p>
                """.formatted(
                    saved.getId(),
                    nullToEmpty(saved.getTitle()),
                    nullToEmpty(saved.getPriority()),
                    nullToEmpty(saved.getStatus()),
                    nullToEmpty(saved.getDescription())
                );

            mailService.sendSimple(subject, body);
            log.info("Notifica SendGrid inviata per ticket id={}", saved.getId());
        } catch (Exception e) {
            log.error("Errore nell'invio della mail con SendGrid per ticket id={}: {}", saved.getId(), e.getMessage(), e);
        }

        return saved;
    }

    public Ticket updateTicket(Long id, Ticket updatedTicket) {
        return ticketRepository.findById(id).map(ticket -> {
            ticket.setTitle(updatedTicket.getTitle());
            ticket.setDescription(updatedTicket.getDescription());
            ticket.setStatus(updatedTicket.getStatus());
            ticket.setPriority(updatedTicket.getPriority());
            Ticket saved = ticketRepository.save(ticket);
            log.info("Ticket aggiornato id={} title='{}'", saved.getId(), saved.getTitle());
            return saved;
        }).orElse(null);
    }

    public void deleteTicket(Long id) {
        ticketRepository.deleteById(id);
        log.warn("Ticket cancellato id={}", id);
    }

    private static String nullToEmpty(String s) {
        return s == null ? "" : s;
    }
}
