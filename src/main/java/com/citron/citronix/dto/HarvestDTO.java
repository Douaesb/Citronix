package com.citron.citronix.dto;

import lombok.*;
import jakarta.validation.constraints.*;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class HarvestDTO {

    private Long id;

    @NotNull(message = "Quantity is required")
    @DecimalMin(value = "0.1", inclusive = true, message = "Quantity must be at least 0.1")
    private Double quantity;

    @NotNull(message = "Sale ID is required")
    private Long saleId;

    @NotEmpty(message = "Harvest detail IDs cannot be empty")
    private List<Long> harvestDetailIds;
}
