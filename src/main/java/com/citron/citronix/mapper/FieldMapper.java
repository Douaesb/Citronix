package com.citron.citronix.mapper;

import com.citron.citronix.dto.FieldDTO;
import com.citron.citronix.entity.Field;
import org.mapstruct.*;
import org.springframework.stereotype.Component;

@Mapper(componentModel = "spring")
@Component

public interface FieldMapper {

    @Mapping(target = "farmId", source = "farm.id")
    FieldDTO toDTO(Field field);

    @Mapping(target = "farm", ignore = true)
    Field toEntity(FieldDTO fieldDTO);
}
