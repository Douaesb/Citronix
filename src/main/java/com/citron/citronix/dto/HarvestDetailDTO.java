package com.citron.citronix.dto;

import lombok.*;
import jakarta.validation.constraints.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class HarvestDetailDTO {

    private Long id;

    private Double quantity;

    @NotNull(message = "Tree ID is required")
    private Long treeId;

    @NotNull(message = "Harvest ID is required")
    private Long harvestId;
}
