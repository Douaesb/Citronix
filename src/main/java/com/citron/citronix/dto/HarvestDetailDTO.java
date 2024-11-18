package com.citron.citronix.dto;

import lombok.*;
import jakarta.validation.constraints.*;
import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class HarvestDetailDTO {

    private Long id;

    @NotNull(message = "Date is required")
    @PastOrPresent(message = "Date must not be in the future")
    private LocalDate date;

    @NotBlank(message = "Season is required")
    @Size(max = 50, message = "Season name must not exceed 50 characters")
    private String season;

    @NotNull(message = "Quantity is required")
    @DecimalMin(value = "0.1", inclusive = true, message = "Quantity must be at least 0.1")
    private Double quantity;

    @NotNull(message = "Tree ID is required")
    private Long treeId;

    @NotNull(message = "Harvest ID is required")
    private Long harvestId;
}
