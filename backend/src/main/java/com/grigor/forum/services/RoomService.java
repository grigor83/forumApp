package com.grigor.forum.services;

import com.grigor.forum.dto.RoomDTO;
import com.grigor.forum.model.Room;
import com.grigor.forum.repository.RoomRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class RoomService {

    private final RoomRepository roomRepository;
    private final CommentService commentService;

    public RoomService(RoomRepository roomRepository, CommentService commentService) {
        this.roomRepository = roomRepository;
        this.commentService = commentService;
    }

    public List<Room> findAll() {
        return roomRepository.findAll();
    }

    public List<RoomDTO> findAllRoomDTO() {
        List<Room> rooms = roomRepository.findAll();

        return rooms.stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    // Method to convert Room entity to RoomDTO
    private RoomDTO convertToDTO(Room room) {
        RoomDTO roomDTO = new RoomDTO();
        roomDTO.setId(room.getId());
        roomDTO.setName(room.getName());
        roomDTO.setComments(commentService.getLatestCommentsByRoomId(roomDTO.getId()));

        return roomDTO;
    }
}
