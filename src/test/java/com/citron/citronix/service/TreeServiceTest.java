package com.citron.citronix.service;

import com.citron.citronix.dto.TreeDTO;
import com.citron.citronix.entity.Field;
import com.citron.citronix.entity.Tree;
import com.citron.citronix.exception.InvalidPlantationDateException;
import com.citron.citronix.exception.ResourceNotFoundException;
import com.citron.citronix.mapper.TreeMapper;
import com.citron.citronix.repository.FieldRepository;
import com.citron.citronix.repository.TreeRepository;
import com.citron.citronix.service.impl.TreeServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;

class TreeServiceTest {

    @InjectMocks
    private TreeServiceImpl treeService;

    @Mock
    private TreeRepository treeRepository;

    @Mock
    private FieldRepository fieldRepository;

    @Mock
    private TreeMapper treeMapper;

    private TreeDTO treeDTO;
    private Tree tree;
    private Field field;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        field = new Field();
        field.setId(1L);
        field.setName("Field 1");
        field.setArea(10.0);

        treeDTO = new TreeDTO();
        treeDTO.setId(1L);
        treeDTO.setPlantationDate(LocalDate.of(2020, 4, 15));
        treeDTO.setFieldId(1L);

        tree = new Tree();
        tree.setId(1L);
        tree.setPlantationDate(LocalDate.of(2020, 4, 15));
        tree.setField(field);
    }

    @Test
    void testCreateTree_validPlantationDate() {
        // Arrange
        Mockito.when(fieldRepository.findById(anyLong())).thenReturn(Optional.of(field));
        Mockito.when(treeRepository.countByFieldId(anyLong())).thenReturn(0);
        Mockito.when(treeMapper.toEntity(any())).thenReturn(tree);
        Mockito.when(treeRepository.save(any())).thenReturn(tree);
        Mockito.when(treeMapper.toDTO(any())).thenReturn(treeDTO);

        // Act
        TreeDTO result = treeService.createTree(treeDTO);

        // Assert
        assertNotNull(result);
        assertEquals(1L, result.getId());
        assertEquals(LocalDate.of(2020, 4, 15), result.getPlantationDate());
    }

    @Test
    void testCreateTree_fieldNotFound() {
        // Arrange
        Mockito.when(fieldRepository.findById(anyLong())).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(ResourceNotFoundException.class, () -> treeService.createTree(treeDTO));
    }

    @Test
    void testCreateTree_maxTreeDensityExceeded() {
        // Arrange
        Field field = new Field();
        field.setId(1L);
        field.setArea(0.1);

        TreeDTO treeDTO = new TreeDTO();
        treeDTO.setFieldId(1L);
        treeDTO.setPlantationDate(LocalDate.of(2024, 3, 15));

        Mockito.when(fieldRepository.findById(anyLong())).thenReturn(Optional.of(field));
        Mockito.when(treeRepository.countByFieldId(anyLong())).thenReturn(11);

        // Act & Assert
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> treeService.createTree(treeDTO));
        assertEquals("Field exceeds maximum tree density of 10 trees.", exception.getMessage());
    }


    @Test
    void testCreateTree_invalidPlantationDate() {
        // Arrange
        treeDTO.setPlantationDate(LocalDate.of(2020, 6, 1)); // Invalid plantation date

        // Act & Assert
        InvalidPlantationDateException exception = assertThrows(InvalidPlantationDateException.class, () -> treeService.createTree(treeDTO));
        assertEquals("The plantation date must be between March and May.", exception.getMessage());
    }

    @Test
    void testGetTreeById_found() {
        // Arrange
        Mockito.when(treeRepository.findById(anyLong())).thenReturn(Optional.of(tree));
        Mockito.when(treeMapper.toDTO(any())).thenReturn(treeDTO);

        // Act
        TreeDTO result = treeService.getTreeById(1L);

        // Assert
        assertNotNull(result);
        assertEquals(1L, result.getId());
    }

    @Test
    void testGetTreeById_notFound() {
        // Arrange
        Mockito.when(treeRepository.findById(anyLong())).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(ResourceNotFoundException.class, () -> treeService.getTreeById(1L));
    }

    @Test
    void testUpdateTree() {
        // Arrange
        TreeDTO updatedTreeDTO = TreeDTO.builder()
                .id(1L)
                .plantationDate(LocalDate.of(2021, 5, 10))
                .fieldId(1L)
                .build();
        Mockito.when(treeRepository.findById(anyLong())).thenReturn(Optional.of(tree));
        Mockito.when(fieldRepository.findById(anyLong())).thenReturn(Optional.of(field));
        Mockito.when(treeRepository.save(any())).thenReturn(tree);
        Mockito.when(treeMapper.toDTO(any())).thenReturn(updatedTreeDTO);

        // Act
        TreeDTO result = treeService.updateTree(1L, updatedTreeDTO);

        // Assert
        assertNotNull(result);
        assertEquals(LocalDate.of(2021, 5, 10), result.getPlantationDate());
    }

    @Test
    void testUpdateTree_fieldNotFound() {
        // Arrange
        TreeDTO updatedTreeDTO = TreeDTO.builder()
                .id(1L)
                .plantationDate(LocalDate.of(2021, 5, 10))
                .fieldId(1L)
                .build();
        Mockito.when(treeRepository.findById(anyLong())).thenReturn(Optional.of(tree));
        Mockito.when(fieldRepository.findById(anyLong())).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(ResourceNotFoundException.class, () -> treeService.updateTree(1L, updatedTreeDTO));
    }

    @Test
    void testDeleteTree() {
        // Arrange
        Mockito.when(treeRepository.findById(anyLong())).thenReturn(Optional.of(tree));

        // Act
        treeService.deleteTree(1L);

        // Assert
        Mockito.verify(treeRepository, Mockito.times(1)).delete(tree);
    }

    @Test
    void testDeleteTree_notFound() {
        // Arrange
        Mockito.when(treeRepository.findById(anyLong())).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(ResourceNotFoundException.class, () -> treeService.deleteTree(1L));
    }

    @Test
    void testGetAllTrees() {
        // Arrange
        Mockito.when(treeRepository.findAll()).thenReturn(List.of(tree));
        Mockito.when(treeMapper.toDTO(any())).thenReturn(treeDTO);

        // Act
        List<TreeDTO> result = treeService.getAllTrees();

        // Assert
        assertNotNull(result);
        assertFalse(result.isEmpty());
        assertEquals(1, result.size());
    }

    @Test
    void testGetTreesByFieldId() {
        // Arrange
        Mockito.when(treeRepository.findByFieldId(anyLong())).thenReturn(List.of(tree));
        Mockito.when(treeMapper.toDTO(any())).thenReturn(treeDTO);

        // Act
        List<TreeDTO> result = treeService.getTreesByFieldId(1L);

        // Assert
        assertNotNull(result);
        assertFalse(result.isEmpty());
        assertEquals(1, result.size());
    }
}
