package com.grigor.forum.dto;

import com.grigor.forum.validators.XSSValid;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class LoginRequest {

    @NotBlank
    @XSSValid
    private String username;

    @NotBlank
    @XSSValid
    private String password;
}
