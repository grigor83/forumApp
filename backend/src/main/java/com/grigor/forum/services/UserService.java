package com.grigor.forum.services;

import com.grigor.forum.model.User;
import com.grigor.forum.repository.UserRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Transactional
public class UserService {

    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public List<User> findAll() {
        return userRepository.findAll();
    }

    public User createUser(User user) {
        return userRepository.save(user);
    }

    public User updateUser(User newUser) {
        User user = userRepository.findById(newUser.getId())
                .orElse(null);
        if (user == null)
            return null;

        user = newUser;
        return userRepository.save(user);
    }

    public User findById(Integer id) {
        return userRepository.findById(id)
                .orElse(null);
    }
}
