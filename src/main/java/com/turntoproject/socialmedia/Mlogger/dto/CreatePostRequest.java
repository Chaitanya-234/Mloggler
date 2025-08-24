package com.turntoproject.socialmedia.Mlogger.dto;

import com.turntoproject.socialmedia.Mlogger.model.ContentType;
import com.turntoproject.socialmedia.Mlogger.model.Visibility;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CreatePostRequest {
    
    @NotNull(message = "Content type is required")
    private ContentType contentType;
    
    @NotBlank(message = "Title is required")
    @Size(max = 255, message = "Title cannot exceed 255 characters")
    private String title;
    
    @Size(max = 1000, message = "Description cannot exceed 1000 characters")
    private String description;
    
    private Set<String> tags;
    private Set<String> hashtags;
    
    @NotNull(message = "Visibility is required")
    private Visibility visibility;
}
