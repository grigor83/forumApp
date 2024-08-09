package com.grigor.forum.services;

import com.grigor.forum.exceptions.*;
import com.grigor.forum.model.Permission;
import com.grigor.forum.model.User;
import com.grigor.forum.enums.UserRole;
import com.grigor.forum.repository.UserRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@Transactional
public class UserService {

    private final UserRepository userRepository;
    private final EmailService emailService;
    private final RoomService roomService;
    private final PermissionService permissionService;

    public UserService(UserRepository userRepository, EmailService emailService,
                       RoomService roomService, PermissionService permissionService) {
        this.userRepository = userRepository;
        this.emailService = emailService;
        this.roomService = roomService;
        this.permissionService = permissionService;
    }

    public List<User> findAll() {
        return userRepository.findAll();
    }

    public void updateUser(User user) {
        User u = userRepository.findById(user.getId())
                .orElseThrow(NotFoundException::new);

        u.setUsername(user.getUsername());
        u.setPassword(user.getPassword());
        u.setBanned(user.isBanned());
        u.setEmail(user.getEmail());
        u.setRole(user.getRole());

        List<Permission> newPermissions = new ArrayList<>();
        user.getPermissions().forEach(perm -> {
                    newPermissions.add(permissionService.updatePermission(perm));
                }
        );

        u.setPermissions(newPermissions);
        //return userRepository.save(user);
    }

    public List<Permission> verifyUser(Integer id) {
        User user = userRepository.findById(id)
                .orElseThrow(NotFoundException::new);

        // postavi mu sve permisije u skladu sa rolom
        roomService.findAll().forEach(room -> {
            if (user.getRole().equals(UserRole.ADMIN.toString().toLowerCase())
                    || user.getRole().equals(UserRole.MODER.toString().toLowerCase())) {
                Permission perm = new Permission(true, true, true, room, user);
                permissionService.createPermission(perm);
                user.getPermissions().add(perm);
            }
            else {
                Permission perm = new Permission(true, false, false, room, user);
                permissionService.createPermission(perm);
                user.getPermissions().add(perm);
            }
        });
        user.setVerified(true);
        emailService.sendVerificationEmail(user.getEmail());

        return user.getPermissions();
    }
}
