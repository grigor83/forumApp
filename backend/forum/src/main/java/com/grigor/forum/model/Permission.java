package com.grigor.forum.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Data
@Builder
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
public class Permission {
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    @Column(name = "id")
    private Integer id;

    @Basic
    @Column(name = "edit")
    private boolean edit;

    @Basic
    @Column(name = "post")
    private boolean post;

    @Basic
    @Column(name = "delete_comment")
    private boolean delete;

    @ManyToOne
    @JoinColumn(name = "user_id")
    @JsonBackReference("permission-user")
    private User user;

    @ManyToOne
    @JoinColumn(name = "room_id")
    private Room room;

}
