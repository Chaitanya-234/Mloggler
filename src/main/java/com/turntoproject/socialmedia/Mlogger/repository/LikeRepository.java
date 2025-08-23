package com.turntoproject.socialmedia.Mlogger.repository;

import com.turntoproject.socialmedia.Mlogger.model.Like;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface LikeRepository extends JpaRepository<Like, Long> {
    
    Optional<Like> findByUserIdAndPostId(Long userId, Long postId);
    
    boolean existsByUserIdAndPostId(Long userId, Long postId);
    
    long countByPostId(Long postId);
    
    @Query("SELECT l FROM Like l WHERE l.user.id = :userId AND l.post.id = :postId")
    Optional<Like> findByUserAndPost(@Param("userId") Long userId, @Param("postId") Long postId);
}
