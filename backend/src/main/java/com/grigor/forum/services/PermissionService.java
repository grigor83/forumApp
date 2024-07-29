package com.grigor.forum.services;

import com.grigor.forum.model.Permission;
import com.grigor.forum.repository.PermissionRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

@Service
@Transactional
public class PermissionService {
    private final PermissionRepository permissionRepository;

    public PermissionService(PermissionRepository permissionRepository) {
        this.permissionRepository = permissionRepository;
    }

    public Permission createPermission(Permission permission) {
        return permissionRepository.save(permission);
    }
}
