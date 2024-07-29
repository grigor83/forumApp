package com.grigor.forum.controllers;

import com.grigor.forum.model.Permission;
import com.grigor.forum.services.PermissionService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin(origins = "http://localhost:4200")
@RequestMapping("/permissions")
public class PermissionController {

    private final PermissionService permissionService;

    public PermissionController(PermissionService permissionService) {
        this.permissionService = permissionService;
    }

    @PostMapping
    public ResponseEntity<Permission> createPermission(@RequestBody Permission permission) {
        Permission newPermission = permissionService.createPermission(permission);
        return new ResponseEntity<>(newPermission, HttpStatus.ACCEPTED);
    }

}
