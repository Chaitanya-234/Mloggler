package com.turntoproject.socialmedia.Mlogger.repository;

import com.turntoproject.socialmedia.Mlogger.model.Post;
import com.turntoproject.socialmedia.Mlogger.model.Visibility;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PostRepository extends JpaRepository<Post, Long> {
    
    Page<Post> findByUserIdAndVisibilityIn(Long userId, List<Visibility> visibilities, Pageable pageable);
    
    Page<Post> findByVisibilityIn(List<Visibility> visibilities, Pageable pageable);
    
    @Query("SELECT p FROM Post p WHERE p.user.id IN " +
           "(SELECT f.id FROM User u JOIN u.following f WHERE u.id = :userId) " +
           "AND p.visibility IN :visibilities " +
           "ORDER BY p.createdAt DESC")
    Page<Post> findFeedPosts(@Param("userId") Long userId, 
                             @Param("visibilities") List<Visibility> visibilities, 
                             Pageable pageable);
    
    @Query("SELECT p FROM Post p WHERE p.visibility IN :visibilities " +
           "ORDER BY (SELECT COUNT(l) FROM Like l WHERE l.post = p) DESC, p.createdAt DESC")
    Page<Post> findTrendingPosts(@Param("visibilities") List<Visibility> visibilities, Pageable pageable);
    
    @Query("SELECT p FROM Post p WHERE " +
           "(p.title LIKE %:query% OR p.description LIKE %:query% OR " +
           "EXISTS (SELECT t FROM p.tags t WHERE t LIKE %:query%) OR " +
           "EXISTS (SELECT h FROM p.hashtags h WHERE h LIKE %:query%)) " +
           "AND p.visibility IN :visibilities")
    Page<Post> searchPosts(@Param("query") String query, 
                           @Param("visibilities") List<Visibility> visibilities, 
                           Pageable pageable);
    
    @Query("SELECT p FROM Post p WHERE p.contentType = :contentType " +
           "AND p.visibility IN :visibilities " +
           "ORDER BY p.createdAt DESC")
    Page<Post> findByContentType(@Param("contentType") String contentType, 
                                 @Param("visibilities") List<Visibility> visibilities, 
                                 Pageable pageable);
}
