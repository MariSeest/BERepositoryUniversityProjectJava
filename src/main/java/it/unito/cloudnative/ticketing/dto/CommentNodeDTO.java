package it.unito.cloudnative.ticketing.dto;

import lombok.*;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CommentNodeDTO {
    private Long id;
    private String content;
    private String author;
    private Instant createdAt;

    @Builder.Default
    private List<CommentNodeDTO> replies = new ArrayList<>();
}
