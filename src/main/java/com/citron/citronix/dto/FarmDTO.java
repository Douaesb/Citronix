package com.citron.citronix.dto;

import lombok.*;
import jakarta.validation.constraints.*;
import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class FarmDTO {

    private Long id;

    @NotBlank(message = "Farm name is required")
    @Size(max = 100, message = "Farm name must not exceed 100 characters")
    private String name;

    @NotBlank(message = "Location is required")
    @Size(max = 255, message = "Location must not exceed 255 characters")
    private String location;

    @NotNull(message = "Area is required")
    @DecimalMin(value = "0.1", inclusive = true, message = "Area must be at least 0.1 hectares")
    private Double area;

    @NotNull(message = "Creation date is required")
    @PastOrPresent(message = "Creation date cannot be in the future")
    private LocalDate creationDate;

}
