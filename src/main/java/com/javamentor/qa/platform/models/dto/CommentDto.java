package com.javamentor.qa.platform.models.dto;

import lombok.*;
import java.time.LocalDateTime;


@Data
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CommentDto {
    private Long id;
    private String comment;
    private Long userId;
    private String fullName;
    private Long reputation;
    private LocalDateTime dateAdded;
}
