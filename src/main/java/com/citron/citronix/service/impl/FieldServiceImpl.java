package com.citron.citronix.service.impl;

import com.citron.citronix.dto.FieldDTO;
import com.citron.citronix.entity.Farm;
import com.citron.citronix.entity.Field;
import com.citron.citronix.exception.ResourceNotFoundException;
import com.citron.citronix.mapper.FieldMapper;
import com.citron.citronix.repository.FarmRepository;
import com.citron.citronix.repository.FieldRepository;
import com.citron.citronix.service.FieldService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class FieldServiceImpl implements FieldService {

    private final FieldRepository fieldRepository;
    private final FarmRepository farmRepository;
    private final FieldMapper fieldMapper;

    @Autowired
    public FieldServiceImpl(FieldRepository fieldRepository, FarmRepository farmRepository, FieldMapper fieldMapper) {
        this.fieldRepository = fieldRepository;
        this.farmRepository = farmRepository;
        this.fieldMapper = fieldMapper;
    }

    @Override
    public FieldDTO createField(FieldDTO fieldDTO) {
        Farm farm = farmRepository.findById(fieldDTO.getFarmId())
                .orElseThrow(() -> new ResourceNotFoundException("Farm not found with id: " + fieldDTO.getFarmId()));

        Field field = fieldMapper.toEntity(fieldDTO);
        field.setFarm(farm);
        Field savedField = fieldRepository.save(field);
        return fieldMapper.toDTO(savedField);
    }

    @Override
    public FieldDTO getFieldById(Long id) {
        Field field = fieldRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Field not found with id: " + id));
        return fieldMapper.toDTO(field);
    }

    @Override
    public FieldDTO updateField(Long id, FieldDTO fieldDTO) {
        Field existingField = fieldRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Field not found with id: " + id));

        Farm farm = farmRepository.findById(fieldDTO.getFarmId())
                .orElseThrow(() -> new ResourceNotFoundException("Farm not found with id: " + fieldDTO.getFarmId()));

        existingField.setName(fieldDTO.getName());
        existingField.setArea(fieldDTO.getArea());
        existingField.setFarm(farm);

        Field updatedField = fieldRepository.save(existingField);
        return fieldMapper.toDTO(updatedField);
    }

    @Override
    public void deleteField(Long id) {
        Field field = fieldRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Field not found with id: " + id));
        fieldRepository.delete(field);
    }

    @Override
    public List<FieldDTO> getAllFields() {
        return fieldRepository.findAll()
                .stream()
                .map(fieldMapper::toDTO)
                .collect(Collectors.toList());
    }

    @Override
    public List<FieldDTO> getFieldsByFarmId(Long farmId) {
        return fieldRepository.findByFarmId(farmId)
                .stream()
                .map(fieldMapper::toDTO)
                .collect(Collectors.toList());
    }
}
