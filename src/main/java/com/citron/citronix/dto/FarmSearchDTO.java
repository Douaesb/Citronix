package com.citron.citronix.dto;

import lombok.Data;

import java.time.LocalDate;

@Data
public class FarmSearchDTO {

        private String name;
        private String location;
        private Double minArea;
        private Double maxArea;
        private LocalDate startDate;
        private LocalDate endDate;

}
