package com.citron.citronix.mapper;

import com.citron.citronix.dto.HarvestDTO;
import com.citron.citronix.entity.Harvest;
import org.mapstruct.*;
import org.springframework.stereotype.Component;


@Mapper(componentModel = "spring")
@Component

public interface HarvestMapper {

    HarvestDTO toDTO(Harvest harvest);

    @Mapping(target = "harvestDetails", ignore = true)
    @Mapping(target = "sales", ignore = true)
    Harvest toEntity(HarvestDTO harvestDTO);


}