package com.citron.citronix.mapper;

import com.citron.citronix.dto.FarmDTO;
import com.citron.citronix.dto.FarmSearchDTO;
import com.citron.citronix.entity.Farm;
import org.mapstruct.*;
import org.springframework.stereotype.Component;


@Mapper(componentModel = "spring")
@Component
public interface FarmMapper {

    FarmDTO toDTO(Farm farm);

    @Mapping(target = "fields", ignore = true)
    Farm toEntity(FarmDTO farmDTO);

    FarmSearchDTO toSearchDTO(Farm farm);
}
