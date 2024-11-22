package com.citron.citronix.service;

import com.citron.citronix.dto.HarvestDetailDTO;
import com.citron.citronix.entity.Harvest;
import com.citron.citronix.entity.HarvestDetail;
import com.citron.citronix.entity.Season;
import com.citron.citronix.entity.Tree;
import com.citron.citronix.exception.ResourceNotFoundException;
import com.citron.citronix.mapper.HarvestDetailMapper;
import com.citron.citronix.repository.HarvestDetailRepository;
import com.citron.citronix.repository.HarvestRepository;
import com.citron.citronix.repository.TreeRepository;
import com.citron.citronix.service.impl.HarvestDetailServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.LocalDate;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class HarvestDetailServiceTest {

    @InjectMocks
    private HarvestDetailServiceImpl harvestDetailService;

    @Mock
    private HarvestDetailRepository harvestDetailRepository;

    @Mock
    private TreeRepository treeRepository;

    @Mock
    private HarvestRepository harvestRepository;

    @Mock
    private HarvestDetailMapper harvestDetailMapper;

    private HarvestDetailDTO harvestDetailDTO;
    private HarvestDetail harvestDetail;
    private Harvest harvest;
    private Tree tree;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        // Set up test data
        harvestDetailDTO = HarvestDetailDTO.builder()
                .id(1L)
                .quantity(100.0)
                .treeId(1L)
                .harvestId(1L)
                .build();

        tree = Tree.builder().id(1L).build();

        harvest = Harvest.builder()
                .id(1L)
                .quantity(500.0)
                .date(LocalDate.now())
                .season(Season.SUMMER)
                .build();

        harvestDetail = HarvestDetail.builder()
                .id(1L)
                .quantity(100.0)
                .tree(tree)
                .harvest(harvest)
                .build();
    }

    @Test
    void createHarvestDetail_ShouldCreateHarvestDetail() {
        // Mock repository and mapper interactions
        when(treeRepository.findById(1L)).thenReturn(Optional.of(tree));
        when(harvestRepository.findById(1L)).thenReturn(Optional.of(harvest));
        when(harvestDetailMapper.toEntity(harvestDetailDTO)).thenReturn(harvestDetail);
        when(harvestDetailRepository.save(harvestDetail)).thenReturn(harvestDetail);
        when(harvestDetailMapper.toDTO(harvestDetail)).thenReturn(harvestDetailDTO);

        // Call the service method
        HarvestDetailDTO result = harvestDetailService.createHarvestDetail(harvestDetailDTO);

        // Verify the result and interactions
        assertNotNull(result);
        assertEquals(1L, result.getId());
        assertEquals(100.0, result.getQuantity());

        verify(treeRepository).findById(1L);
        verify(harvestRepository).findById(1L);
        verify(harvestDetailRepository).save(harvestDetail);
        verify(harvestRepository).save(harvest);
    }

    @Test
    void createHarvestDetail_TreeNotFound_ShouldThrowException() {
        // Mock Tree repository to return an empty Optional
        when(treeRepository.findById(1L)).thenReturn(Optional.empty());

        // Call the service method and expect exception
        assertThrows(ResourceNotFoundException.class, () -> {
            harvestDetailService.createHarvestDetail(harvestDetailDTO);
        });

        verify(treeRepository).findById(1L);
        verifyNoInteractions(harvestRepository, harvestDetailRepository);
    }

    @Test
    void createHarvestDetail_HarvestNotFound_ShouldThrowException() {
        // Mock Tree and Harvest repositories to return existing Tree but empty Harvest
        when(treeRepository.findById(1L)).thenReturn(Optional.of(tree));
        when(harvestRepository.findById(1L)).thenReturn(Optional.empty());

        // Call the service method and expect exception
        assertThrows(ResourceNotFoundException.class, () -> {
            harvestDetailService.createHarvestDetail(harvestDetailDTO);
        });

        verify(treeRepository).findById(1L);
        verify(harvestRepository).findById(1L);
        verifyNoInteractions(harvestDetailRepository);
    }

    @Test
    void updateHarvestDetail_ShouldUpdateHarvestDetail() {
        // Mock repository and mapper interactions
        when(harvestDetailRepository.findById(1L)).thenReturn(Optional.of(harvestDetail));
        when(treeRepository.findById(1L)).thenReturn(Optional.of(tree));
        when(harvestRepository.findById(1L)).thenReturn(Optional.of(harvest));
        when(harvestDetailMapper.toEntity(harvestDetailDTO)).thenReturn(harvestDetail);
        when(harvestDetailRepository.save(harvestDetail)).thenReturn(harvestDetail);
        when(harvestDetailMapper.toDTO(harvestDetail)).thenReturn(harvestDetailDTO);

        // Call the service method
        HarvestDetailDTO result = harvestDetailService.updateHarvestDetail(1L, harvestDetailDTO);

        // Verify the result and interactions
        assertNotNull(result);
        assertEquals(1L, result.getId());
        assertEquals(100.0, result.getQuantity());

        verify(harvestDetailRepository).findById(1L);
        verify(treeRepository).findById(1L);
        verify(harvestRepository).findById(1L);
        verify(harvestDetailRepository).save(harvestDetail);
        verify(harvestRepository).save(harvest);
    }

    @Test
    void updateHarvestDetail_HarvestDetailNotFound_ShouldThrowException() {
        // Mock repository to return empty HarvestDetail
        when(harvestDetailRepository.findById(1L)).thenReturn(Optional.empty());

        // Call the service method and expect exception
        assertThrows(ResourceNotFoundException.class, () -> {
            harvestDetailService.updateHarvestDetail(1L, harvestDetailDTO);
        });

        verify(harvestDetailRepository).findById(1L);
        verifyNoInteractions(treeRepository, harvestRepository);
    }

    @Test
    void deleteHarvestDetail_ShouldDeleteHarvestDetail() {
        // Mock repository methods
        when(harvestDetailRepository.findById(1L)).thenReturn(Optional.of(harvestDetail));

        // Call service method
        harvestDetailService.deleteHarvestDetail(1L);

        // Verify interactions
        verify(harvestDetailRepository).findById(1L);
        verify(harvestDetailRepository).delete(harvestDetail);
        verify(harvestRepository).save(harvest);
    }

    @Test
    void deleteHarvestDetail_HarvestDetailNotFound_ShouldThrowException() {
        when(harvestDetailRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> {
            harvestDetailService.deleteHarvestDetail(1L);
        });
        verify(harvestDetailRepository).findById(1L);
        verify(harvestDetailRepository, never()).delete(any());
        verifyNoInteractions(harvestRepository);
    }

}
