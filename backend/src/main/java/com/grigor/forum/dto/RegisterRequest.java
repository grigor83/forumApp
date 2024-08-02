package com.grigor.forum.dto;

import com.grigor.forum.validators.XSSValid;
import jakarta.validation.constraints.Email;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import jakarta.validation.constraints.NotBlank;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RegisterRequest {
    @NotBlank
    @XSSValid
    private String username;

    @NotBlank
    @XSSValid
    private String password;

    @NotBlank
    @Email
    @XSSValid
    private String email;

    @NotBlank
    @XSSValid
    private String role;

}
