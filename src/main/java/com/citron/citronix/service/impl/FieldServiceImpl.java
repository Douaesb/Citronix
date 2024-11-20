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
        validateFieldConstraints(fieldDTO);
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

        validateFieldConstraintsForUpdate(existingField, fieldDTO);

        Farm farm = farmRepository.findById(fieldDTO.getFarmId())
                .orElseThrow(() -> new ResourceNotFoundException("Farm not found with id: " + fieldDTO.getFarmId()));

        Field updatedField = Field.builder()
                .id(existingField.getId())
                .name(fieldDTO.getName())
                .area(fieldDTO.getArea())
                .farm(farm)
                .build();

        Field savedField = fieldRepository.save(updatedField);
        return fieldMapper.toDTO(savedField);
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

    private void validateFieldConstraints(FieldDTO fieldDTO) {
        Farm farm = farmRepository.findById(fieldDTO.getFarmId())
                .orElseThrow(() -> new ResourceNotFoundException("Farm not found with id: " + fieldDTO.getFarmId()));

        int existingFieldCount = farm.getFields() == null ? 0 : farm.getFields().size();
        if (existingFieldCount >= 10) {
            throw new IllegalArgumentException("A farm cannot have more than 10 fields.");
        }

        double existingFieldsArea = farm.getFields() == null
                ? 0.0
                : farm.getFields().stream()
                .mapToDouble(Field::getArea)
                .sum();

        double totalArea = existingFieldsArea + fieldDTO.getArea();

        if (totalArea > farm.getArea()) {
            throw new IllegalArgumentException("Total field area exceeds the area of the farm.");
        }

        double maxAllowedFieldArea = farm.getArea() * 0.5;
        if (fieldDTO.getArea() > maxAllowedFieldArea) {
            throw new IllegalArgumentException("A single field's area cannot exceed 50% of the farm's area.");
        }
    }

    private void validateFieldConstraintsForUpdate(Field existingField, FieldDTO fieldDTO) {
        Farm farm = farmRepository.findById(fieldDTO.getFarmId())
                .orElseThrow(() -> new ResourceNotFoundException("Farm not found with id: " + fieldDTO.getFarmId()));

        double existingFieldsArea = farm.getFields() == null
                ? 0.0
                : farm.getFields().stream()
                .filter(field -> !field.getId().equals(existingField.getId()))
                .mapToDouble(Field::getArea)
                .sum();

        double totalArea = existingFieldsArea + fieldDTO.getArea();

        if (totalArea > farm.getArea()) {
            throw new IllegalArgumentException("Total field area exceeds the area of the farm.");
        }

        double maxAllowedFieldArea = farm.getArea() * 0.5;
        if (fieldDTO.getArea() > maxAllowedFieldArea) {
            throw new IllegalArgumentException("A single field's area cannot exceed 50% of the farm's area.");
        }
    }


}
