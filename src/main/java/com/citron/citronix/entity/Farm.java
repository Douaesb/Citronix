package com.citron.citronix.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.*;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "farms")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Farm {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Farm name is required")
    @Size(max = 100, message = "Farm name must not exceed 100 characters")
    @Column(nullable = false, length = 100)
    private String name;

    @NotBlank(message = "Location is required")
    @Size(max = 255, message = "Location must not exceed 255 characters")
    @Column(nullable = false, length = 255)
    private String location;

    @NotNull(message = "Area is required")
    @DecimalMin(value = "0.2", inclusive = true, message = "Area must be at least 0.2 hectares")
    @Column(nullable = false)
    private Double area;

    @NotNull(message = "Creation date is required")
    @PastOrPresent(message = "Creation date cannot be in the future")
    @Column(name = "creation_date", nullable = false)
    private LocalDate creationDate;

    @OneToMany(mappedBy = "farm", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    private List<Field> fields = new ArrayList<>();
}
