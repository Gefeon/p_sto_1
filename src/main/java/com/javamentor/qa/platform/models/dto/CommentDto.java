package com.javamentor.qa.platform.models.dto;

import lombok.*;

import java.time.LocalDateTime;

@Data
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CommentDto {
    Long id;
    String comment;
    Long userId;
    String fullName;
    Long reputation;
    LocalDateTime dateAdded;
}