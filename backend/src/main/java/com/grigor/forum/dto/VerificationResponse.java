package com.grigor.forum.dto;

import com.grigor.forum.model.Permission;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Data;

import java.util.List;

@Data
public class VerificationResponse {
    @NotNull
    @Min(0)
    private Integer id;

    @NotBlank
    private String username;

    @NotNull
    private List<Permission> permissions;

    @NotNull
    private boolean verified;

    @NotNull
    private boolean banned;

    @NotBlank
    @Pattern(regexp = "^(admin|moder|regular)")
    private String role;

    @NotBlank
    private String token;
}
