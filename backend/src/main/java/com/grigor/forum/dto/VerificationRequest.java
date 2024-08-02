package com.grigor.forum.dto;

import com.grigor.forum.validators.XSSValid;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class VerificationRequest {

    @NotNull
    @Min(0)
    private Integer id;

    @NotNull
    @Min(1)
    private Integer code;
}
