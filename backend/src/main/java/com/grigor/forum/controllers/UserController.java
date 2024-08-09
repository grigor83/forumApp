package com.grigor.forum.controllers;

import com.grigor.forum.model.Permission;
import com.grigor.forum.model.User;
import com.grigor.forum.security.WAFService;
import com.grigor.forum.services.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin(origins = "http://localhost:4200")
@RequestMapping("/users")
public class UserController {

    private final UserService userService;
    private final WAFService wafService;

    public UserController(UserService userService, WAFService wafService) {
        this.userService = userService;
        this.wafService = wafService;
    }

    @GetMapping
    public ResponseEntity<List<User>> findAll() {
        List<User> users = userService.findAll();
        return new ResponseEntity<>(users, HttpStatus.OK);
    }

    @PutMapping("/verify/{id}")
    public ResponseEntity<List<Permission>> verifyUser(@PathVariable Integer id) {
        List<Permission> userPermissions = userService.verifyUser(id);
        return new ResponseEntity<>(userPermissions, HttpStatus.OK);
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateUser(@PathVariable Integer id, @Validated @RequestBody User user,
                                        BindingResult result) {
        // This method is used for changing user role, permissions and banned
        wafService.checkRequest(result);
        userService.updateUser(user);
        return ResponseEntity.ok().build();
    }
}
