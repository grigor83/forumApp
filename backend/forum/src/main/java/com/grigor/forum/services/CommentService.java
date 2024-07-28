package com.grigor.forum.services;

import com.grigor.forum.model.Comment;
import com.grigor.forum.repository.CommentRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class CommentService {

    private final CommentRepository commentRepository;

    public CommentService(CommentRepository commentRepository) {
        this.commentRepository = commentRepository;
    }

    public List<Comment> getLatestCommentsByRoomId(Integer roomId) {
        return commentRepository.findLatestComments(roomId);
    }

    public List<Comment> findByRoomId(Integer roomId) {
        return commentRepository.findCommentsByRoomId(roomId);
    }

    public Comment createComment(Comment comment) {
        return commentRepository.save(comment);
    }

    public Comment update(Comment updatedComment) {
        Comment comment = commentRepository.findById(updatedComment.getId())
                .orElse(null);
        if (comment == null)
            return null;

        comment = updatedComment;
        return commentRepository.save(comment);
    }

    public boolean deleteById(Integer id) {
        Optional<Comment> comment = commentRepository.findById(id);
        if (comment.isEmpty())
            return false;
        else{
            commentRepository.deleteById(id);
            return true;
        }
    }
}
