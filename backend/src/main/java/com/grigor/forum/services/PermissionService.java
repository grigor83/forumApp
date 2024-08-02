package com.grigor.forum.services;

import com.grigor.forum.exceptions.NotFoundException;
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

    public void createPermission(Permission permission) {
        permissionRepository.save(permission);
    }

    public Permission updatePermission(Permission permission){
        Permission perm = permissionRepository.findById(permission.getId())
                                .orElseThrow(NotFoundException::new);

        perm.setPost(permission.isPost());
        perm.setEdit(permission.isEdit());
        perm.setDelete(permission.isDelete());

        return permissionRepository.save(perm);
    }
}
