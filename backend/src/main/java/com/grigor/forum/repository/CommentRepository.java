package com.grigor.forum.repository;

import com.grigor.forum.model.Comment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface CommentRepository extends JpaRepository<Comment, Integer> {

    List<Comment> findCommentsByRoomId(Integer id);

    @Query(value = "SELECT * FROM comment WHERE room_id=:roomId ORDER BY comment_date ASC LIMIT 20", nativeQuery = true)
    List<Comment> findLatestComments(Integer roomId);

}
