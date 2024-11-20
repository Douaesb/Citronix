package com.citron.citronix.dto;

import com.citron.citronix.entity.Season;
import lombok.*;
import jakarta.validation.constraints.*;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class HarvestDTO {

    private Long id;


    private Double quantity;

    @NotNull(message = "Date is required")
    @PastOrPresent(message = "Date must not be in the future")
    private LocalDate date;


    private Season season;


}
