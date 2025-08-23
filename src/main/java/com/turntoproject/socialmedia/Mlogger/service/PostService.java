package com.turntoproject.socialmedia.Mlogger.service;

import com.turntoproject.socialmedia.Mlogger.dto.CreatePostRequest;
import com.turntoproject.socialmedia.Mlogger.dto.PostDto;
import com.turntoproject.socialmedia.Mlogger.model.Post;
import com.turntoproject.socialmedia.Mlogger.model.User;
import com.turntoproject.socialmedia.Mlogger.model.Visibility;
import com.turntoproject.socialmedia.Mlogger.repository.PostRepository;
import com.turntoproject.socialmedia.Mlogger.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class PostService {
    
    private final PostRepository postRepository;
    private final UserRepository userRepository;
    private final UserService userService;
    
    public PostDto createPost(CreatePostRequest request, Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));
        
        Post post = Post.builder()
                .user(user)
                .contentType(request.getContentType())
                .title(request.getTitle())
                .description(request.getDescription())
                .tags(request.getTags())
                .hashtags(request.getHashtags())
                .visibility(request.getVisibility())
                .build();
        
        Post savedPost = postRepository.save(post);
        return convertToDto(savedPost, userId);
    }
    
    public PostDto getPostById(Long postId, Long currentUserId) {
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new RuntimeException("Post not found"));
        
        return convertToDto(post, currentUserId);
    }
    
    public Page<PostDto> getUserPosts(Long userId, Pageable pageable, Long currentUserId) {
        List<Visibility> visibilities = getVisibleVisibilities(userId, currentUserId);
        return postRepository.findByUserIdAndVisibilityIn(userId, visibilities, pageable)
                .map(post -> convertToDto(post, currentUserId));
    }
    
    public Page<PostDto> getFeedPosts(Long userId, Pageable pageable) {
        List<Visibility> visibilities = Arrays.asList(Visibility.PUBLIC, Visibility.FOLLOWERS_ONLY);
        return postRepository.findFeedPosts(userId, visibilities, pageable)
                .map(post -> convertToDto(post, userId));
    }
    
    public Page<PostDto> getTrendingPosts(Pageable pageable, Long currentUserId) {
        List<Visibility> visibilities = Arrays.asList(Visibility.PUBLIC);
        return postRepository.findTrendingPosts(visibilities, pageable)
                .map(post -> convertToDto(post, currentUserId));
    }
    
    public Page<PostDto> searchPosts(String query, Pageable pageable, Long currentUserId) {
        List<Visibility> visibilities = Arrays.asList(Visibility.PUBLIC);
        return postRepository.searchPosts(query, visibilities, pageable)
                .map(post -> convertToDto(post, currentUserId));
    }
    
    private List<Visibility> getVisibleVisibilities(Long postUserId, Long currentUserId) {
        if (postUserId.equals(currentUserId)) {
            return Arrays.asList(Visibility.PUBLIC, Visibility.PRIVATE, Visibility.FOLLOWERS_ONLY);
        }
        // Check if current user follows post user
        User currentUser = userRepository.findById(currentUserId).orElse(null);
        User postUser = userRepository.findById(postUserId).orElse(null);
        
        if (currentUser != null && postUser != null && 
            currentUser.getFollowing().contains(postUser)) {
            return Arrays.asList(Visibility.PUBLIC, Visibility.FOLLOWERS_ONLY);
        }
        
        return Arrays.asList(Visibility.PUBLIC);
    }
    
    public PostDto convertToDto(Post post, Long currentUserId) {
        return PostDto.builder()
                .id(post.getId())
                .user(userService.convertToDto(post.getUser()))
                .contentType(post.getContentType())
                .title(post.getTitle())
                .description(post.getDescription())
                .tags(post.getTags())
                .hashtags(post.getHashtags())
                .visibility(post.getVisibility())
                .createdAt(post.getCreatedAt())
                .updatedAt(post.getUpdatedAt())
                .likesCount(post.getLikes().size())
                .commentsCount(post.getComments().size())
                .isLikedByCurrentUser(post.getLikes().stream()
                        .anyMatch(like -> like.getUser().getId().equals(currentUserId)))
                .build();
    }
}
