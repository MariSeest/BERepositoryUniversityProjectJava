// src/main/java/it/unito/cloudnative/ticketing/repository/CommentRepository.java
package it.unito.cloudnative.ticketing.repository;

import it.unito.cloudnative.ticketing.model.Comment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CommentRepository extends JpaRepository<Comment, Long> {
  List<Comment> findByTicketIdAndParentIsNullOrderByCreatedAtAsc(Long ticketId);
  List<Comment> findByParentIdOrderByCreatedAtAsc(Long parentId);
}
