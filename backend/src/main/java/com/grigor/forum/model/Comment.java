package com.grigor.forum.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.grigor.forum.validators.XSSValid;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Data
@Builder
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
public class Comment {

    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    @Column(name = "id")
    private Integer id;

    @Basic
    @Column(name = "comment_date")
    @JsonFormat(shape=JsonFormat.Shape.STRING, pattern="dd.MM.yyyy. HH:mm")
    private LocalDateTime date;

    @Basic
    @Column(name = "content")
    private String content;

    @ManyToOne
    @JoinColumn(name = "room_id")
    //@JsonBackReference
    //@JsonBackReference: This annotation is used on the child side of the relationship. It indicates
    // that this part of the relationship is the back reference, and it should be ignored during
    // serialization to prevent infinite recursion.
    private Room room;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;
}
