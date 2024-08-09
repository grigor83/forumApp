package com.grigor.forum.services;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.grigor.forum.exceptions.NotFoundException;
import com.grigor.forum.model.Comment;
import com.grigor.forum.model.Room;
import com.grigor.forum.model.User;
import com.grigor.forum.repository.CommentRepository;
import com.grigor.forum.repository.UserRepository;
import com.grigor.forum.validators.XSSValid;
import jakarta.transaction.Transactional;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class CommentService {

    private final CommentRepository commentRepository;
    private final UserRepository userRepository;

    public CommentService(CommentRepository commentRepository, UserRepository userRepository) {
        this.commentRepository = commentRepository;
        this.userRepository = userRepository;
    }

    public CommentResponse createComment(CommentRequest request) {
        Comment comment = convertRequestToComment(request);
        Comment newComment = commentRepository.save(comment);

        return convertToCommentResponse(newComment);
    }

    public void update(CommentRequest request) {
        Comment comment = commentRepository.findById(request.id)
                .orElseThrow(NotFoundException::new);
        comment.setContent(request.content);
    }

    public void deleteById(Integer id) {
        if (!commentRepository.existsById(id))
            throw new NotFoundException();

        commentRepository.deleteById(id);
    }

    public List<CommentResponse> getLatestCommentsByRoomId(Integer roomId) {
        List<Comment> comments = commentRepository.findLatestComments(roomId);

        return comments.stream()
                .map(this::convertToCommentResponse)
                .collect(Collectors.toList());
    }

    private Comment convertRequestToComment(CommentRequest request){
        Comment comment = new Comment();
        LocalDateTime now = LocalDateTime.now();
        comment.setDate(now);
        comment.setContent(request.content());
        User user = userRepository.findByUsername(request.username)
                .orElseThrow(NotFoundException::new);
        comment.setUser(user);
        Room room = new Room();
        room.setId(request.roomId());
        comment.setRoom(room);
        return comment;
    }

    private CommentResponse convertToCommentResponse(Comment comment) {
        return new CommentResponse(
                comment.getId(),
                comment.getDate(),
                comment.getContent(),
                comment.getUser().getUsername()
        );
    }

    public record CommentRequest(
            @NotNull
            @Min(0)
            Integer id,
            @XSSValid
            @NotBlank
            @Size(max = 2000)
            String content,
            @XSSValid
            @NotBlank
            @Size(max = 20)
            String username,
            @NotNull
            @Min(0)
            Integer roomId
    ) {}

    public record CommentResponse (
            Integer id,
            @JsonFormat(shape=JsonFormat.Shape.STRING, pattern="dd.MM.yyyy. HH:mm")
            LocalDateTime date,
            String content,
            String username
    ){}

}
