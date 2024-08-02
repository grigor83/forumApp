package com.grigor.forum.controllers;

import com.grigor.forum.dto.RoomDTO;
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
    public ResponseEntity<List<RoomDTO>> findAll() {
        List<RoomDTO> rooms = roomService.findAllRoomDTO();
        return new ResponseEntity<>(rooms, HttpStatus.OK);
    }

}
