package com.citron.citronix.service;

import com.citron.citronix.dto.FieldDTO;
import com.citron.citronix.entity.Farm;
import com.citron.citronix.entity.Field;
import com.citron.citronix.exception.ResourceNotFoundException;
import com.citron.citronix.mapper.FieldMapper;
import com.citron.citronix.repository.FarmRepository;
import com.citron.citronix.repository.FieldRepository;
import com.citron.citronix.service.impl.FieldServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class FieldServiceTest {

    @Mock
    private FieldRepository fieldRepository;

    @Mock
    private FarmRepository farmRepository;

    @Mock
    private FieldMapper fieldMapper;

    @InjectMocks
    private FieldServiceImpl fieldService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void createField_shouldCreateField() {
        FieldDTO fieldDTO = new FieldDTO();
        fieldDTO.setName("Test Field");
        fieldDTO.setArea(5.0);
        fieldDTO.setFarmId(1L);

        Farm farm = new Farm();
        farm.setId(1L);
        farm.setArea(10.0);

        Field field = new Field();
        field.setName("Test Field");
        field.setArea(5.0);

        Field savedField = new Field();
        savedField.setId(1L);
        savedField.setName("Test Field");
        savedField.setArea(5.0);

        FieldDTO savedFieldDTO = new FieldDTO();
        savedFieldDTO.setId(1L);
        savedFieldDTO.setName("Test Field");
        savedFieldDTO.setArea(5.0);
        savedFieldDTO.setFarmId(1L);

        when(farmRepository.findById(1L)).thenReturn(Optional.of(farm));
        when(fieldMapper.toEntity(fieldDTO)).thenReturn(field);
        when(fieldRepository.save(field)).thenReturn(savedField);
        when(fieldMapper.toDTO(savedField)).thenReturn(savedFieldDTO);

        FieldDTO result = fieldService.createField(fieldDTO);

        // Assert
        assertNotNull(result);
        assertEquals(fieldDTO.getName(), result.getName());
        assertEquals(fieldDTO.getArea(), result.getArea());
        assertEquals(fieldDTO.getFarmId(), result.getFarmId());

        // Verify calls
        verify(farmRepository, times(2)).findById(1L);
        verify(fieldRepository).save(any(Field.class));
        verify(fieldMapper).toEntity(any(FieldDTO.class));
        verify(fieldMapper).toDTO(any(Field.class));
    }

    @Test
    void getFieldById_shouldReturnField() {
        Long fieldId = 1L;

        Field field = new Field();
        field.setId(fieldId);
        field.setName("Test Field");

        FieldDTO fieldDTO = new FieldDTO();
        fieldDTO.setId(fieldId);
        fieldDTO.setName("Test Field");

        when(fieldRepository.findById(fieldId)).thenReturn(Optional.of(field));
        when(fieldMapper.toDTO(field)).thenReturn(fieldDTO);

        FieldDTO result = fieldService.getFieldById(fieldId);

        assertNotNull(result);
        assertEquals(fieldId, result.getId());
        assertEquals("Test Field", result.getName());

        verify(fieldRepository).findById(fieldId);
        verify(fieldMapper).toDTO(field);
    }

    @Test
    void updateField_shouldUpdateField() {
        // Arrange
        Long fieldId = 1L;

        FieldDTO fieldDTO = new FieldDTO();
        fieldDTO.setId(fieldId);
        fieldDTO.setName("Updated Field");
        fieldDTO.setArea(5.0);
        fieldDTO.setFarmId(1L);

        Farm farm = new Farm();
        farm.setId(1L);
        farm.setArea(10.0);

        Field existingField = new Field();
        existingField.setId(fieldId);
        existingField.setName("Old Field");
        existingField.setArea(3.0);

        Field updatedField = new Field();
        updatedField.setId(fieldId);
        updatedField.setName("Updated Field");
        updatedField.setArea(5.0);

        FieldDTO updatedFieldDTO = new FieldDTO();
        updatedFieldDTO.setId(fieldId);
        updatedFieldDTO.setName("Updated Field");
        updatedFieldDTO.setArea(5.0);
        updatedFieldDTO.setFarmId(1L);

        // Mock interactions
        when(fieldRepository.findById(fieldId)).thenReturn(Optional.of(existingField));
        when(farmRepository.findById(1L)).thenReturn(Optional.of(farm));
        when(fieldRepository.save(any(Field.class))).thenReturn(updatedField);
        when(fieldMapper.toDTO(updatedField)).thenReturn(updatedFieldDTO);

        // Act
        FieldDTO result = fieldService.updateField(fieldId, fieldDTO);

        // Assert
        assertNotNull(result);
        assertEquals(fieldId, result.getId());
        assertEquals("Updated Field", result.getName());
        assertEquals(5.0, result.getArea());
        assertEquals(1L, result.getFarmId());

        verify(fieldRepository).findById(fieldId);
        verify(farmRepository, times(2)).findById(1L);
        verify(fieldRepository).save(any(Field.class));
        verify(fieldMapper).toDTO(updatedField);
    }


    @Test
    void deleteField_shouldDeleteField() {
        Long fieldId = 1L;

        Field field = new Field();
        field.setId(fieldId);

        when(fieldRepository.findById(fieldId)).thenReturn(Optional.of(field));

        fieldService.deleteField(fieldId);

        verify(fieldRepository).findById(fieldId);
        verify(fieldRepository).delete(field);
    }

    @Test
    void getAllFields_shouldReturnAllFields() {
        Field field1 = new Field();
        field1.setId(1L);
        Field field2 = new Field();
        field2.setId(2L);

        FieldDTO fieldDTO1 = new FieldDTO();
        fieldDTO1.setId(1L);
        FieldDTO fieldDTO2 = new FieldDTO();
        fieldDTO2.setId(2L);

        when(fieldRepository.findAll()).thenReturn(List.of(field1, field2));
        when(fieldMapper.toDTO(field1)).thenReturn(fieldDTO1);
        when(fieldMapper.toDTO(field2)).thenReturn(fieldDTO2);

        List<FieldDTO> result = fieldService.getAllFields();

        assertEquals(2, result.size());
        verify(fieldRepository).findAll();
        verify(fieldMapper).toDTO(field1);
        verify(fieldMapper).toDTO(field2);
    }

    @Test
    void getFieldsByFarmId_shouldReturnFields() {
        Long farmId = 1L;

        Field field = new Field();
        field.setId(1L);
        FieldDTO fieldDTO = new FieldDTO();
        fieldDTO.setId(1L);

        when(fieldRepository.findByFarmId(farmId)).thenReturn(List.of(field));
        when(fieldMapper.toDTO(field)).thenReturn(fieldDTO);

        List<FieldDTO> result = fieldService.getFieldsByFarmId(farmId);

        assertEquals(1, result.size());
        assertEquals(1L, result.get(0).getId());

        verify(fieldRepository).findByFarmId(farmId);
        verify(fieldMapper).toDTO(field);
    }



}
