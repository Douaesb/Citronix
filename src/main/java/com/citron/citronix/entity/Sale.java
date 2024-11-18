package com.citron.citronix.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.*;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "sales")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Sale {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull(message = "Sale date is required")
    @Column(nullable = false)
    private LocalDate date;

    @NotNull(message = "Unit price is required")
    @DecimalMin(value = "0.0", inclusive = false, message = "Unit price must be greater than 0")
    @Column(nullable = false)
    private Double unitPrice;

    @NotNull(message = "Quantity is required")
    @DecimalMin(value = "0.0", inclusive = false, message = "Quantity must be greater than 0")
    @Column(nullable = false)
    private Double quantity;

    @NotNull(message = "Revenue is required")
    @DecimalMin(value = "0.0", inclusive = true, message = "Revenue must be 0 or greater")
    @Column(nullable = false)
    private Double revenue;

    @NotBlank(message = "Client name is required")
    @Column(nullable = false)
    private String client;

    @OneToMany(mappedBy = "sale", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    private List<Harvest> harvests = new ArrayList<>();
}
