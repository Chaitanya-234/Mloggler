package com.turntoproject.socialmedia.Mlogger.service;

import com.turntoproject.socialmedia.Mlogger.model.Like;
import com.turntoproject.socialmedia.Mlogger.model.Post;
import com.turntoproject.socialmedia.Mlogger.model.User;
import com.turntoproject.socialmedia.Mlogger.repository.LikeRepository;
import com.turntoproject.socialmedia.Mlogger.repository.PostRepository;
import com.turntoproject.socialmedia.Mlogger.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class LikeService {
    
    private final LikeRepository likeRepository;
    private final PostRepository postRepository;
    private final UserRepository userRepository;
    
    public void likePost(Long postId, Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));
        
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new RuntimeException("Post not found"));
        
        if (likeRepository.existsByUserIdAndPostId(userId, postId)) {
            throw new RuntimeException("Post already liked by user");
        }
        
        Like like = Like.builder()
                .user(user)
                .post(post)
                .build();
        
        likeRepository.save(like);
    }
    
    public void unlikePost(Long postId, Long userId) {
        Like like = likeRepository.findByUserIdAndPostId(userId, postId)
                .orElseThrow(() -> new RuntimeException("Like not found"));
        
        likeRepository.delete(like);
    }
    
    public boolean isPostLikedByUser(Long postId, Long userId) {
        return likeRepository.existsByUserIdAndPostId(userId, postId);
    }
    
    public long getPostLikesCount(Long postId) {
        return likeRepository.countByPostId(postId);
    }
}
