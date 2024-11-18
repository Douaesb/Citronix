package com.citron.citronix.dto;

import lombok.*;
import jakarta.validation.constraints.*;
import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TreeDTO {

    private Long id;

    @NotNull(message = "Plantation date is required")
    @PastOrPresent(message = "Plantation date cannot be in the future")
    private LocalDate plantationDate;

    @NotNull(message = "Age is required")
    @Min(value = 0, message = "Age cannot be less than 0")
    @Max(value = 200, message = "Age cannot be more than 200")
    private Integer age;

    @NotNull(message = "Field ID is required")
    private Long fieldId;
}
