package com.grigor.forum.controllers;

import com.grigor.forum.model.Room;
import com.grigor.forum.repository.CommentRepository;
import com.grigor.forum.services.RoomService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin(origins = "http://localhost:4200")
@RequestMapping("/rooms")
public class RoomController {

    private final RoomService roomService;

    public RoomController(RoomService roomService) {
        this.roomService = roomService;
    }

    @GetMapping
    public ResponseEntity<List<Room>> findAll() {
        List<Room> rooms = roomService.findAll();
        return new ResponseEntity<>(rooms, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Room> findById(@PathVariable Integer id) {
        Room room = roomService.findById(id);
        return new ResponseEntity<>(room, HttpStatus.OK);
    }
}
