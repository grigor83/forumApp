package com.grigor.forum.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.grigor.forum.validators.XSSValid;
import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Entity
@Data
@Builder
@EqualsAndHashCode(callSuper = false, exclude = "comments")
@ToString(exclude = "comments")
@NoArgsConstructor
@AllArgsConstructor
public class Room {

    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    @Column(name = "id")
    private Integer id;

    @Basic
    @Column(name = "name")
    @XSSValid
    private String name;


    @OneToMany(mappedBy = "room", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    // mappedBy room oznacava da je ova veza 1:M mapirana poljem room u klasi Comment
    //cascade = CascadeType.ALL znaci da kad obrisem room iz baze, brisu se i svi komentari iz baze,
    // jer je room parent a komentari su child
    @JsonBackReference // Ovdje koristim backreference zato sto komentare ucitavam naknadno
    //@JsonManagedReference
    //@JsonManagedReference: This annotation is used on the parent side of the relationship. It indicates
    // // that this part of the relationship should be serialized normally.
    private List<Comment> comments;




}
