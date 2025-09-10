package it.unito.cloudnative.ticketing.controller;

import it.unito.cloudnative.ticketing.dto.CommentDTO;
import it.unito.cloudnative.ticketing.dto.CommentNodeDTO;
import it.unito.cloudnative.ticketing.service.CommentService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/tickets/{ticketId}/comments")
@RequiredArgsConstructor
@CrossOrigin(origins = "http://localhost:3000")
public class CommentController {

    private final CommentService commentService;

    @GetMapping
    public List<CommentNodeDTO> list(@PathVariable Long ticketId) {
        return commentService.getTree(ticketId);
    }

    @PostMapping
    public CommentNodeDTO create(@PathVariable Long ticketId, @RequestBody CommentDTO dto) {
        return commentService.create(ticketId, dto); // ora il service restituisce DTO
    }

    @DeleteMapping("/{commentId}")
    public void delete(@PathVariable Long ticketId, @PathVariable Long commentId) {
        commentService.delete(ticketId, commentId);
    }
}
