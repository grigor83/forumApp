package com.grigor.forum.repository;

import com.grigor.forum.model.Room;
import com.grigor.forum.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface UserRepository extends JpaRepository<User, Integer> {

    @Query("SELECT user.room FROM User user WHERE user.id = :id")
    Room findRoomByUserId(Integer id);
}
