package com.grigor.forum.dto;

import com.grigor.forum.model.Comment;
import lombok.*;

import java.util.List;

@Data
@Builder
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
public class RoomDTO {
    Integer id;
    String name;
    List<Comment> comments;
}
