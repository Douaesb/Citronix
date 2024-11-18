package com.citron.citronix.service;

import com.citron.citronix.dto.FieldDTO;

import java.util.List;

public interface FieldService {

    FieldDTO createField(FieldDTO fieldDTO);

    FieldDTO getFieldById(Long id);
    FieldDTO updateField(Long id, FieldDTO fieldDTO);

    void deleteField(Long id);
    List<FieldDTO> getAllFields();

    List<FieldDTO> getFieldsByFarmId(Long farmId);
}
