package com.citron.citronix.service;

import com.citron.citronix.dto.HarvestDTO;
import com.citron.citronix.entity.Harvest;
import com.citron.citronix.entity.Season;
import com.citron.citronix.exception.ResourceNotFoundException;
import com.citron.citronix.mapper.HarvestMapper;
import com.citron.citronix.repository.HarvestRepository;
import com.citron.citronix.service.impl.HarvestServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class HarvestServiceTest {

    @Mock
    private HarvestRepository harvestRepository;

    @Mock
    private HarvestMapper harvestMapper;

    @InjectMocks
    private HarvestServiceImpl harvestService;

    private HarvestDTO harvestDTO;
    private Harvest harvest;

    @BeforeEach
    void setUp() {
        harvestDTO = new HarvestDTO();
        harvestDTO.setId(1L);
        harvestDTO.setQuantity(0.0);
        harvestDTO.setDate(LocalDate.of(2024, 5, 10)); // Spring season

        harvest = new Harvest();
        harvest.setId(1L);
        harvest.setQuantity(0.0);
        harvest.setDate(LocalDate.of(2024, 5, 10));
    }

    @Test
    void createHarvest_shouldReturnHarvestDTO() {
        // Arrange
        Season season = Season.SPRING;
        harvestDTO.setSeason(season);

        Harvest savedHarvest = new Harvest();
        savedHarvest.setId(1L);
        savedHarvest.setQuantity(0.0);

        HarvestDTO savedHarvestDTO = new HarvestDTO();
        savedHarvestDTO.setId(1L);
        savedHarvestDTO.setQuantity(0.0);

        // Mock dependencies
        when(harvestMapper.toEntity(harvestDTO)).thenReturn(harvest);
        when(harvestRepository.save(harvest)).thenReturn(savedHarvest);
        when(harvestMapper.toDTO(savedHarvest)).thenReturn(savedHarvestDTO);

        // Act
        HarvestDTO result = harvestService.createHarvest(harvestDTO);

        // Assert
        assertNotNull(result);
        assertEquals(savedHarvestDTO.getId(), result.getId());
        assertEquals(savedHarvestDTO.getQuantity(), result.getQuantity());

        // Verify interactions
        verify(harvestRepository).save(harvest);
        verify(harvestMapper).toEntity(harvestDTO);
        verify(harvestMapper).toDTO(savedHarvest);
    }

    @Test
    void getHarvestById_shouldReturnHarvestDTO() {
        // Arrange
        Long harvestId = 1L;
        harvest.setId(harvestId);
        harvest.setQuantity(10.0);

        HarvestDTO harvestDTO = new HarvestDTO();
        harvestDTO.setId(harvestId);
        harvestDTO.setQuantity(10.0);

        // Mock dependencies
        when(harvestRepository.findById(harvestId)).thenReturn(Optional.of(harvest));
        when(harvestMapper.toDTO(harvest)).thenReturn(harvestDTO);

        // Act
        HarvestDTO result = harvestService.getHarvestById(harvestId);

        // Assert
        assertNotNull(result);
        assertEquals(harvestId, result.getId());
        assertEquals(10.0, result.getQuantity());

        // Verify interactions
        verify(harvestRepository).findById(harvestId);
        verify(harvestMapper).toDTO(harvest);
    }

    @Test
    void getHarvestById_shouldThrowResourceNotFoundException_whenHarvestNotFound() {
        // Arrange
        Long harvestId = 1L;

        // Mock dependencies
        when(harvestRepository.findById(harvestId)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(ResourceNotFoundException.class, () -> harvestService.getHarvestById(harvestId));

        // Verify interaction
        verify(harvestRepository).findById(harvestId);
    }

    @Test
    void updateHarvest_shouldReturnUpdatedHarvestDTO() {
        // Arrange
        Long harvestId = 1L;
        harvestDTO.setDate(LocalDate.of(2024, 7, 15)); // Summer season
        harvestDTO.setQuantity(20.0);

        Harvest existingHarvest = new Harvest();
        existingHarvest.setId(harvestId);
        existingHarvest.setQuantity(10.0);

        Harvest updatedHarvest = new Harvest();
        updatedHarvest.setId(harvestId);
        updatedHarvest.setQuantity(20.0);

        HarvestDTO updatedHarvestDTO = new HarvestDTO();
        updatedHarvestDTO.setId(harvestId);
        updatedHarvestDTO.setQuantity(20.0);

        // Mock dependencies
        when(harvestRepository.findById(harvestId)).thenReturn(Optional.of(existingHarvest));
        when(harvestRepository.save(existingHarvest)).thenReturn(updatedHarvest);
        when(harvestMapper.toDTO(updatedHarvest)).thenReturn(updatedHarvestDTO);

        // Act
        HarvestDTO result = harvestService.updateHarvest(harvestId, harvestDTO);

        // Assert
        assertNotNull(result);
        assertEquals(updatedHarvestDTO.getId(), result.getId());
        assertEquals(updatedHarvestDTO.getQuantity(), result.getQuantity());

        // Verify interactions
        verify(harvestRepository).findById(harvestId);
        verify(harvestRepository).save(existingHarvest);
        verify(harvestMapper).toDTO(updatedHarvest);
    }

    @Test
    void updateHarvest_shouldThrowResourceNotFoundException_whenHarvestNotFound() {
        // Arrange
        Long harvestId = 1L;

        // Mock dependencies
        when(harvestRepository.findById(harvestId)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(ResourceNotFoundException.class, () -> harvestService.updateHarvest(harvestId, harvestDTO));

        // Verify interaction
        verify(harvestRepository).findById(harvestId);
    }

    @Test
    void deleteHarvest_shouldDeleteHarvest() {
        // Arrange
        Long harvestId = 1L;
        Harvest harvest = new Harvest();
        harvest.setId(harvestId);

        // Mock dependencies
        when(harvestRepository.findById(harvestId)).thenReturn(Optional.of(harvest));

        // Act
        harvestService.deleteHarvest(harvestId);

        // Assert
        verify(harvestRepository).findById(harvestId);
        verify(harvestRepository).delete(harvest);
    }

    @Test
    void deleteHarvest_shouldThrowResourceNotFoundException_whenHarvestNotFound() {
        // Arrange
        Long harvestId = 1L;

        // Mock dependencies
        when(harvestRepository.findById(harvestId)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(ResourceNotFoundException.class, () -> harvestService.deleteHarvest(harvestId));

        // Verify interaction
        verify(harvestRepository).findById(harvestId);
    }

    @Test
    void getAllHarvests_shouldReturnListOfHarvestDTOs() {
        // Arrange
        Harvest harvest1 = new Harvest();
        harvest1.setId(1L);
        harvest1.setQuantity(10.0);

        Harvest harvest2 = new Harvest();
        harvest2.setId(2L);
        harvest2.setQuantity(15.0);

        HarvestDTO harvestDTO1 = new HarvestDTO();
        harvestDTO1.setId(1L);
        harvestDTO1.setQuantity(10.0);

        HarvestDTO harvestDTO2 = new HarvestDTO();
        harvestDTO2.setId(2L);
        harvestDTO2.setQuantity(15.0);

        List<Harvest> harvests = List.of(harvest1, harvest2);
        List<HarvestDTO> harvestDTOs = List.of(harvestDTO1, harvestDTO2);

        // Mock dependencies
        when(harvestRepository.findAll()).thenReturn(harvests);
        when(harvestMapper.toDTO(harvest1)).thenReturn(harvestDTO1);
        when(harvestMapper.toDTO(harvest2)).thenReturn(harvestDTO2);

        // Act
        List<HarvestDTO> result = harvestService.getAllHarvests();

        // Assert
        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals(harvestDTO1.getId(), result.get(0).getId());
        assertEquals(harvestDTO2.getId(), result.get(1).getId());

        // Verify interactions
        verify(harvestRepository).findAll();
        verify(harvestMapper).toDTO(harvest1);
        verify(harvestMapper).toDTO(harvest2);
    }
}
