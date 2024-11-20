package com.citron.citronix.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.*;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "harvests")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Harvest {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull(message = "Harvest qte is required")

    @Column(nullable = false)
    private Double quantity;

    @NotNull(message = "Harvest date is required")
    @Column(nullable = false)
    private LocalDate date;

    @NotNull(message = "Season is required")
    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 50)
    private Season season;

    @OneToMany(mappedBy = "harvest", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    private List<HarvestDetail> harvestDetails = new ArrayList<>();


    @OneToMany(mappedBy = "harvest", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    private List<Sale> sales = new ArrayList<>();

    public void updateQuantity() {
        this.quantity = harvestDetails.stream()
                .mapToDouble(HarvestDetail::getQuantity)
                .sum();
    }
}
