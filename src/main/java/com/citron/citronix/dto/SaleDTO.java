package com.citron.citronix.dto;

import lombok.*;
import jakarta.validation.constraints.*;
import java.time.LocalDate;
import java.util.List;

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

    @NotNull(message = "Revenue is required")
    @DecimalMin(value = "0.01", inclusive = true, message = "Revenue must be at least 0.01")
    private Double revenue;

    @NotBlank(message = "Client is required")
    @Size(max = 255, message = "Client name must not exceed 255 characters")
    private String client;

}
