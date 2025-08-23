package com.turntoproject.socialmedia.Mlogger.repository;

import com.turntoproject.socialmedia.Mlogger.model.Comment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CommentRepository extends JpaRepository<Comment, Long> {
    
    Page<Comment> findByPostIdOrderByCreatedAtDesc(Long postId, Pageable pageable);
    
    long countByPostId(Long postId);
}
