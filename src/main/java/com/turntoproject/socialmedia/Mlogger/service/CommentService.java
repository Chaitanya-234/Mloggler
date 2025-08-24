package com.turntoproject.socialmedia.Mlogger.service;

import com.turntoproject.socialmedia.Mlogger.dto.CommentDto;
import com.turntoproject.socialmedia.Mlogger.dto.CreateCommentRequest;
import com.turntoproject.socialmedia.Mlogger.model.Comment;
import com.turntoproject.socialmedia.Mlogger.model.Post;
import com.turntoproject.socialmedia.Mlogger.model.User;
import com.turntoproject.socialmedia.Mlogger.repository.CommentRepository;
import com.turntoproject.socialmedia.Mlogger.repository.PostRepository;
import com.turntoproject.socialmedia.Mlogger.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class CommentService {
    
    private final CommentRepository commentRepository;
    private final PostRepository postRepository;
    private final UserRepository userRepository;
    private final UserService userService;
    
    public CommentDto createComment(CreateCommentRequest request, Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));
        
        Post post = postRepository.findById(request.getPostId())
                .orElseThrow(() -> new RuntimeException("Post not found"));
        
        Comment comment = Comment.builder()
                .user(user)
                .post(post)
                .content(request.getContent())
                .build();
        
        Comment savedComment = commentRepository.save(comment);
        return convertToDto(savedComment);
    }
    
    public Page<CommentDto> getPostComments(Long postId, Pageable pageable) {
        return commentRepository.findByPostIdOrderByCreatedAtDesc(postId, pageable)
                .map(this::convertToDto);
    }
    
    public CommentDto convertToDto(Comment comment) {
        return CommentDto.builder()
                .id(comment.getId())
                .user(userService.convertToDto(comment.getUser()))
                .postId(comment.getPost().getId())
                .content(comment.getContent())
                .createdAt(comment.getCreatedAt())
                .updatedAt(comment.getUpdatedAt())
                .build();
    }
}
