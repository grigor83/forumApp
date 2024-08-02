package com.grigor.forum.controllers;

import com.grigor.forum.model.Comment;
import com.grigor.forum.security.WAFService;
import com.grigor.forum.services.CommentService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin(origins = "http://localhost:4200")
@RequestMapping("/comments")
public class CommentController {

    private final CommentService commentService;
    private final WAFService wafService;

    public CommentController(CommentService commentService, WAFService wafService) {
        this.commentService = commentService;
        this.wafService = wafService;
    }

    @PostMapping
    public ResponseEntity<?> createComment(@Validated @RequestBody Comment comment, BindingResult result) {
        wafService.checkRequest(result);
        return ResponseEntity.ok().body(commentService.createComment(comment));
    }

    @PutMapping
    public ResponseEntity<?> updateComment(@Validated @RequestBody Comment comment, BindingResult result) {
        wafService.checkRequest(result);
        commentService.update(comment);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> delete(@PathVariable Integer id) {
        commentService.deleteById(id);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/{roomId}")
    public ResponseEntity<List<Comment>> findByRoomId(@PathVariable Integer roomId) {
        List<Comment> comments = commentService.getLatestCommentsByRoomId(roomId);
        return new ResponseEntity<>(comments, HttpStatus.OK);
    }

}
