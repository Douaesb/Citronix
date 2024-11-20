package com.citron.citronix.dto;

import lombok.*;
import jakarta.validation.constraints.*;
import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SaleDTO {

    private Long id;

    @NotNull(message = "Date is required")
    @PastOrPresent(message = "Sale date cannot be in the future")
    private LocalDate date;

    @NotNull(message = "Unit price is required")
    @DecimalMin(value = "0.01", inclusive = true, message = "Unit price must be at least 0.01")
    private Double unitPrice;

    @NotNull(message = "Quantity is required")
    @DecimalMin(value = "0.1", inclusive = true, message = "Quantity must be at least 0.1")
    private Double quantity;

    private Double revenue;

    public void calculateRevenue() {
        if (this.quantity != null && this.unitPrice != null) {
            this.revenue = this.quantity * this.unitPrice;
        }
    }

    @NotBlank(message = "Client is required")
    @Size(max = 255, message = "Client name must not exceed 255 characters")
    private String client;

    @NotNull(message = "harvest ID is required")
    private Long harvestId;

}
