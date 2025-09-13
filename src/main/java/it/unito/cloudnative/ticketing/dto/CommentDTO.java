package it.unito.cloudnative.ticketing.dto;

import lombok.Data;

@Data
public class CommentDTO {
    private String content;
    private Long parentId;
    private String author;
}
