package com.citron.citronix.mapper;

import com.citron.citronix.dto.HarvestDetailDTO;
import com.citron.citronix.entity.HarvestDetail;
import org.mapstruct.*;
import org.springframework.stereotype.Component;

@Mapper(componentModel = "spring")
@Component

public interface HarvestDetailMapper {

    @Mapping(target = "treeId", source = "tree.id")
    @Mapping(target = "harvestId", source = "harvest.id")
    HarvestDetailDTO toDTO(HarvestDetail harvestDetail);

    @Mapping(target = "tree", ignore = true)
    @Mapping(target = "harvest", ignore = true)
    HarvestDetail toEntity(HarvestDetailDTO harvestDetailDTO);
}
