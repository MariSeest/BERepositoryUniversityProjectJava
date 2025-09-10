package it.unito.cloudnative.ticketing.dto;

import lombok.Data;

@Data
public class CommentDTO {
    private String content;
    private Long parentId; // null = commento root
    private String author; // dal FE (o dal token, se proteggi con JWT)
}
