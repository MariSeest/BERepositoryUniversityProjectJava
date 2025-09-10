package it.unito.cloudnative.ticketing.service;

import it.unito.cloudnative.ticketing.dto.CommentDTO;
import it.unito.cloudnative.ticketing.dto.CommentNodeDTO;
import it.unito.cloudnative.ticketing.model.Comment;
import it.unito.cloudnative.ticketing.model.Ticket;
import it.unito.cloudnative.ticketing.repository.CommentRepository;
import it.unito.cloudnative.ticketing.repository.TicketRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CommentService {

    private final CommentRepository commentRepo;
    private final TicketRepository ticketRepo;

    public List<CommentNodeDTO> getTree(Long ticketId) {
        var roots = commentRepo.findByTicketIdAndParentIsNullOrderByCreatedAtAsc(ticketId);
        return roots.stream().map(this::toNode).collect(Collectors.toList());
    }

    // mapper da entity a DTO (lasciamo private)
    private CommentNodeDTO toNode(Comment c) {
        var node = CommentNodeDTO.builder()
                .id(c.getId())
                .content(c.getContent())
                .author(c.getAuthor())
                .createdAt(c.getCreatedAt())
                .build();
        var children = commentRepo.findByParentIdOrderByCreatedAtAsc(c.getId());
        node.setReplies(children.stream().map(this::toNode).collect(Collectors.toList()));
        return node;
    }

    // >>> RESTITUISCE DIRETTAMENTE CommentNodeDTO <<<
    public CommentNodeDTO create(Long ticketId, CommentDTO dto) {
        Ticket t = ticketRepo.findById(ticketId).orElseThrow();
        Comment parent = (dto.getParentId() == null) ? null :
                commentRepo.findById(dto.getParentId()).orElseThrow();

        Comment c = Comment.builder()
                .ticket(t)
                .parent(parent)
                .content(dto.getContent())
                .author(dto.getAuthor() == null ? "Anon" : dto.getAuthor())
                .build();

        Comment saved = commentRepo.save(c);
        return toNode(saved);
    }

    public void delete(Long ticketId, Long commentId) {
        // (opzionale) verificare che il comment appartenga al ticketId
        commentRepo.deleteById(commentId);
    }
}
