package com.grigor.forum.services;

import com.grigor.forum.exceptions.NotFoundException;
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

    public Comment createComment(Comment comment) {
        return commentRepository.save(comment);
    }

    public void update(Comment updatedComment) {
        Comment comment = commentRepository.findById(updatedComment.getId())
                .orElseThrow(NotFoundException::new);
        comment.setContent(updatedComment.getContent());
    }

    public void deleteById(Integer id) {
        if (!commentRepository.existsById(id))
            throw new NotFoundException();

        commentRepository.deleteById(id);
    }
}
