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

    private Integer age;

    private Double annualProductivity;

    @NotNull(message = "Field ID is required")
    private Long fieldId;
}
