package com.citron.citronix.dto;

import lombok.*;
import jakarta.validation.constraints.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class FieldDTO {

    private Long id;

    @NotBlank(message = "Field name is required")
    @Size(max = 100, message = "Field name must not exceed 100 characters")
    private String name;

    @NotNull(message = "Area is required")
    @DecimalMin(value = "0.1", inclusive = true, message = "Area must be at least 0.1 hectares")
    private Double area;

    @NotNull(message = "Farm ID is required")
    private Long farmId;
}
