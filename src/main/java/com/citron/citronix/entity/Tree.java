package com.citron.citronix.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.*;

import java.time.LocalDate;
import java.time.Period;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "trees")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Tree {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull(message = "Plantation date is required")
    @Column(nullable = false)
    private LocalDate plantationDate;

    @Transient
    private Integer age;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "field_id", nullable = false)
    private Field field;

    @OneToMany(mappedBy = "tree", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    private List<HarvestDetail> harvestDetails = new ArrayList<>();

    public Integer getAge() {
        if (this.plantationDate != null) {
            return Period.between(this.plantationDate, LocalDate.now()).getYears();
        }
        return null;
    }

    public Double getAnnualProductivity() {
        Integer ageTree = getAge();
        if (ageTree != null) {
            if (ageTree < 3) {
                return 2.5; // Young tree
            } else if (ageTree <= 10) {
                return 12.0; // Mature tree
            } else if (ageTree <= 20) {
                return 20.0; // Old tree
            } else {
                return 0.0; // Non-productive
            }
        }
        return 0.0;
    }
}
