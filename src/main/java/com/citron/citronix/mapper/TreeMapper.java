package com.citron.citronix.mapper;

import com.citron.citronix.dto.TreeDTO;
import com.citron.citronix.entity.Tree;
import org.mapstruct.*;
import org.springframework.stereotype.Component;

@Component
@Mapper(componentModel = "spring")

public interface TreeMapper {

    @Mapping(source = "field.id", target = "fieldId")
    @Mapping(target = "age", expression = "java(tree.getAge())")
    @Mapping(target = "annualProductivity", expression = "java(tree.getAnnualProductivity())")
    TreeDTO toDTO(Tree tree);

    @Mapping(target = "field", ignore = true)
    @Mapping(target = "harvestDetails", ignore = true)

    Tree toEntity(TreeDTO treeDTO);
}
