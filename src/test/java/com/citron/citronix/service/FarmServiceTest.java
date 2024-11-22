package com.citron.citronix.service;

import com.citron.citronix.dto.FarmDTO;
import com.citron.citronix.dto.FarmSearchDTO;
import com.citron.citronix.entity.Farm;
import com.citron.citronix.exception.ResourceNotFoundException;
import com.citron.citronix.mapper.FarmMapper;
import com.citron.citronix.repository.FarmRepository;
import com.citron.citronix.service.impl.FarmServiceImpl;
import com.citron.citronix.specification.FarmSpecification;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.data.jpa.domain.Specification;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class FarmServiceTest {

    @Mock
    private FarmRepository farmRepository;

    @Mock
    private FarmMapper farmMapper;

    @Mock
    private FarmSpecification farmSpecification;

    @InjectMocks
    private FarmServiceImpl farmService;

    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void createFarm_shouldReturnCreatedFarm() {
        FarmDTO farmDTO = new FarmDTO(null, "Farm A", "Location A", 100.0, LocalDate.now());
        Farm farm = new Farm();
        Farm savedFarm = new Farm();
        savedFarm.setId(1L);

        when(farmMapper.toEntity(farmDTO)).thenReturn(farm);
        when(farmRepository.save(farm)).thenReturn(savedFarm);
        when(farmMapper.toDTO(savedFarm)).thenReturn(new FarmDTO(1L, "Farm A", "Location A", 100.0, LocalDate.now()));

        FarmDTO result = farmService.createFarm(farmDTO);

        assertNotNull(result);
        assertEquals(1L, result.getId());
        verify(farmMapper).toEntity(farmDTO);
        verify(farmRepository).save(farm);
        verify(farmMapper).toDTO(savedFarm);
    }

    @Test
    void getFarmById_shouldReturnFarm_whenFarmExists() {
        // Arrange
        Farm farm = new Farm();
        farm.setId(1L);
        when(farmRepository.findById(1L)).thenReturn(Optional.of(farm));
        when(farmMapper.toDTO(farm)).thenReturn(new FarmDTO(1L, "Farm A", "Location A", 100.0, LocalDate.now()));

        // Act
        FarmDTO result = farmService.getFarmById(1L);

        // Assert
        assertNotNull(result);
        assertEquals(1L, result.getId());
        verify(farmRepository).findById(1L);
        verify(farmMapper).toDTO(farm);
    }

    @Test
    void getFarmById_shouldThrowException_whenFarmDoesNotExist() {
        // Arrange
        when(farmRepository.findById(1L)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(ResourceNotFoundException.class, () -> farmService.getFarmById(1L));
        verify(farmRepository).findById(1L);
        verifyNoInteractions(farmMapper);
    }

    @Test
    void updateFarm_shouldReturnUpdatedFarm_whenFarmExists() {
        // Arrange
        FarmDTO farmDTO = new FarmDTO(null, "Updated Farm", "Updated Location", 200.0, LocalDate.now());
        Farm existingFarm = new Farm();
        existingFarm.setId(1L);

        Farm updatedFarm = new Farm();
        updatedFarm.setId(1L);
        updatedFarm.setName("Updated Farm");

        when(farmRepository.findById(1L)).thenReturn(Optional.of(existingFarm));
        when(farmRepository.save(existingFarm)).thenReturn(updatedFarm);
        when(farmMapper.toDTO(updatedFarm)).thenReturn(new FarmDTO(1L, "Updated Farm", "Updated Location", 200.0, LocalDate.now()));

        // Act
        FarmDTO result = farmService.updateFarm(1L, farmDTO);

        // Assert
        assertNotNull(result);
        assertEquals("Updated Farm", result.getName());
        verify(farmRepository).findById(1L);
        verify(farmRepository).save(existingFarm);
        verify(farmMapper).toDTO(updatedFarm);
    }

    @Test
    void updateFarm_shouldThrowException_whenFarmDoesNotExist() {
        // Arrange
        FarmDTO farmDTO = new FarmDTO();
        when(farmRepository.findById(1L)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(ResourceNotFoundException.class, () -> farmService.updateFarm(1L, farmDTO));
        verify(farmRepository).findById(1L);
        verifyNoInteractions(farmMapper);
    }

    @Test
    void deleteFarm_shouldDeleteFarm_whenFarmExists() {
        // Arrange
        Farm farm = new Farm();
        farm.setId(1L);
        when(farmRepository.findById(1L)).thenReturn(Optional.of(farm));

        // Act
        farmService.deleteFarm(1L);

        // Assert
        verify(farmRepository).findById(1L);
        verify(farmRepository).delete(farm);
    }

    @Test
    void deleteFarm_shouldThrowException_whenFarmDoesNotExist() {
        // Arrange
        when(farmRepository.findById(1L)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(ResourceNotFoundException.class, () -> farmService.deleteFarm(1L));
        verify(farmRepository).findById(1L);
        verifyNoMoreInteractions(farmRepository);
    }


    @Test
    void searchFarms_shouldReturnFarmsMatchingCriteria() {
        // Arrange
        FarmSearchDTO criteria = new FarmSearchDTO();
        criteria.setName("Test");

        Farm mockFarm = new Farm();
        mockFarm.setId(1L);
        mockFarm.setName("Test Farm");
        mockFarm.setLocation("Test Location");
        mockFarm.setArea(10.0);
        mockFarm.setCreationDate(LocalDate.now());

        List<Farm> mockFarms = List.of(mockFarm);
        when(farmRepository.findAll(any(Specification.class))).thenReturn(mockFarms);

        when(farmMapper.toDTO(any(Farm.class))).thenAnswer(invocation -> {
            Farm farm = invocation.getArgument(0);
            FarmDTO dto = new FarmDTO();
            dto.setId(farm.getId());
            dto.setName(farm.getName());
            dto.setLocation(farm.getLocation());
            dto.setArea(farm.getArea());
            dto.setCreationDate(farm.getCreationDate());
            return dto;
        });

        // Act
        List<FarmDTO> result = farmService.searchFarms(criteria);

        // Assert
        assertEquals(1, result.size());
        assertEquals("Test Farm", result.get(0).getName());
    }



}
