package com.citron.citronix.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.*;

import java.time.LocalDate;

@Entity
@Table(name = "harvest_details")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class HarvestDetail {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull(message = "Harvest date is required")
    @Column(nullable = false)
    private LocalDate date;

    @NotBlank(message = "Season is required")
    @Size(max = 50, message = "Season must not exceed 50 characters")
    @Column(nullable = false)
    private String season;

    @NotNull(message = "Quantity is required")
    @DecimalMin(value = "0.0", inclusive = false, message = "Quantity must be greater than 0")
    @Column(nullable = false)
    private Double quantity;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "tree_id", nullable = false)
    private Tree tree;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "harvest_id", nullable = false)
    private Harvest harvest;
}
