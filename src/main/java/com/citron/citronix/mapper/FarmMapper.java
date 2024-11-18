package com.citron.citronix.mapper;

import com.citron.citronix.dto.FarmDTO;
import com.citron.citronix.entity.Farm;
import org.mapstruct.*;
import org.springframework.stereotype.Component;


@Mapper(componentModel = "spring")
@Component
public interface FarmMapper {

    @Mapping(target = "fieldIds", source = "fields")
    FarmDTO toDTO(Farm farm);

    @Mapping(target = "fields", ignore = true)
    Farm toEntity(FarmDTO farmDTO);

}
