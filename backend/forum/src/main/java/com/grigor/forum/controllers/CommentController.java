package com.grigor.forum.controllers;

import com.grigor.forum.model.Comment;
import com.grigor.forum.services.CommentService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin(origins = "http://localhost:4200")
@RequestMapping("/comments")
public class CommentController {

    private final CommentService commentService;

    public CommentController(CommentService commentService) {
        this.commentService = commentService;
    }

    @GetMapping("/{roomId}")
    public ResponseEntity<List<Comment>> findByRoomId(@PathVariable Integer roomId) {
        List<Comment> comments = commentService.getLatestCommentsByRoomId(roomId);
        return new ResponseEntity<>(comments, HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<Comment> createComment(@RequestBody Comment comment) {
        Comment newComment = commentService.createComment(comment);
        return new ResponseEntity<>(newComment, HttpStatus.ACCEPTED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<String> updateComment(@PathVariable Integer id, @RequestBody Comment comment) {
        Comment updatedComment = commentService.update(comment);
        if (updatedComment != null)
            return ResponseEntity.ok().build();
        else
            return ResponseEntity.notFound().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> delete(@PathVariable Integer id) {
        if (commentService.deleteById(id))
            return ResponseEntity.ok().build();
        else
            return ResponseEntity.notFound().build();
    }
}
