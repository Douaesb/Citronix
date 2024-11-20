package com.citron.citronix.mapper;

import com.citron.citronix.dto.HarvestDTO;
import com.citron.citronix.entity.Harvest;
import org.mapstruct.*;
import org.springframework.stereotype.Component;


@Mapper(componentModel = "spring")
@Component

public interface HarvestMapper {

    @Mapping(target = "saleId", source = "sale.id")
    HarvestDTO toDTO(Harvest harvest);

    @Mapping(target = "sale", ignore = true)
    @Mapping(target = "harvestDetails", ignore = true)
    Harvest toEntity(HarvestDTO harvestDTO);


}