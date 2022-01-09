package com.javamentor.qa.platform.models.dto;

import lombok.*;
import org.dbunit.dataset.datatype.LongDataType;


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
    private LongDataType dateAdded;
}
