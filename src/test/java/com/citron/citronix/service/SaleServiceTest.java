package com.citron.citronix.service;

import com.citron.citronix.dto.SaleDTO;
import com.citron.citronix.entity.Harvest;
import com.citron.citronix.entity.Sale;
import com.citron.citronix.exception.ResourceNotFoundException;
import com.citron.citronix.mapper.SaleMapper;
import com.citron.citronix.repository.HarvestRepository;
import com.citron.citronix.repository.SaleRepository;
import com.citron.citronix.service.impl.SaleServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class SaleServiceTest {

    @Mock
    private SaleRepository saleRepository;

    @Mock
    private HarvestRepository harvestRepository;

    @Mock
    private SaleMapper saleMapper;

    @InjectMocks
    private SaleServiceImpl saleService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testCreateSale() {
        // Arrange
        Harvest harvest = new Harvest();
        harvest.setId(1L);

        SaleDTO saleDTO = SaleDTO.builder()
                .harvestId(1L)
                .quantity(100.0)
                .unitPrice(5.0)
                .build();

        Sale sale = new Sale();
        Sale savedSale = new Sale();
        savedSale.setRevenue(500.0);
        SaleDTO savedSaleDTO = new SaleDTO();
        savedSaleDTO.setHarvestId(1L);
        savedSaleDTO.setRevenue(500.0);

        when(harvestRepository.findById(1L)).thenReturn(Optional.of(harvest));
        when(saleMapper.toEntity(saleDTO)).thenReturn(sale);
        when(saleRepository.save(any(Sale.class))).thenReturn(savedSale);
        when(saleMapper.toDTO(savedSale)).thenReturn(savedSaleDTO);

        // Act
        SaleDTO result = saleService.createSale(saleDTO);

        // Assert
        assertNotNull(result);
        assertEquals(500.0, result.getRevenue());
        verify(harvestRepository).findById(1L);
        verify(saleRepository).save(sale);
    }

    @Test
    void testCreateSale_harvestNotFound() {
        // Arrange
        SaleDTO saleDTO = SaleDTO.builder().harvestId(1L).build();

        when(harvestRepository.findById(1L)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(ResourceNotFoundException.class, () -> saleService.createSale(saleDTO));
    }

    @Test
    void testGetSaleById() {
        // Arrange
        Sale sale = new Sale();
        SaleDTO saleDTO = new SaleDTO();

        when(saleRepository.findById(1L)).thenReturn(Optional.of(sale));
        when(saleMapper.toDTO(sale)).thenReturn(saleDTO);

        // Act
        SaleDTO result = saleService.getSaleById(1L);

        // Assert
        assertNotNull(result);
        verify(saleRepository).findById(1L);
    }

    @Test
    void testGetSaleById_notFound() {
        // Arrange
        when(saleRepository.findById(1L)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(ResourceNotFoundException.class, () -> saleService.getSaleById(1L));
    }

    @Test
    void testUpdateSale() {
        // Arrange
        Sale existingSale = new Sale();
        Harvest harvest = new Harvest();
        harvest.setId(1L);

        SaleDTO saleDTO = SaleDTO.builder()
                .date(LocalDate.now())
                .quantity(200.0)
                .unitPrice(4.0)
                .harvestId(1L)
                .client("ClientName")
                .build();

        when(saleRepository.findById(1L)).thenReturn(Optional.of(existingSale));
        when(harvestRepository.findById(1L)).thenReturn(Optional.of(harvest));
        when(saleRepository.save(existingSale)).thenReturn(existingSale);
        when(saleMapper.toDTO(existingSale)).thenReturn(saleDTO);

        // Act
        SaleDTO result = saleService.updateSale(1L, saleDTO);

        // Assert
        assertNotNull(result);
        verify(saleRepository).findById(1L);
        verify(harvestRepository).findById(1L);
    }

    @Test
    void testUpdateSale_notFound() {
        // Arrange
        SaleDTO saleDTO = SaleDTO.builder().build();

        when(saleRepository.findById(1L)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(ResourceNotFoundException.class, () -> saleService.updateSale(1L, saleDTO));
    }

    @Test
    void testDeleteSale() {
        // Arrange
        Sale sale = new Sale();
        when(saleRepository.findById(1L)).thenReturn(Optional.of(sale));

        // Act
        saleService.deleteSale(1L);

        // Assert
        verify(saleRepository).delete(sale);
    }

    @Test
    void testDeleteSale_notFound() {
        // Arrange
        when(saleRepository.findById(1L)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(ResourceNotFoundException.class, () -> saleService.deleteSale(1L));
    }

    @Test
    void testGetAllSales() {
        // Arrange
        Sale sale1 = new Sale();
        Sale sale2 = new Sale();
        SaleDTO saleDTO1 = new SaleDTO();
        SaleDTO saleDTO2 = new SaleDTO();

        when(saleRepository.findAll()).thenReturn(Arrays.asList(sale1, sale2));
        when(saleMapper.toDTO(sale1)).thenReturn(saleDTO1);
        when(saleMapper.toDTO(sale2)).thenReturn(saleDTO2);

        // Act
        List<SaleDTO> result = saleService.getAllSales();

        // Assert
        assertNotNull(result);
        assertEquals(2, result.size());
        verify(saleRepository).findAll();
    }

    @Test
    void testGetSalesByHarvestId() {
        // Arrange
        Sale sale1 = new Sale();
        Sale sale2 = new Sale();
        SaleDTO saleDTO1 = new SaleDTO();
        SaleDTO saleDTO2 = new SaleDTO();

        when(saleRepository.findByHarvestId(1L)).thenReturn(Arrays.asList(sale1, sale2));
        when(saleMapper.toDTO(sale1)).thenReturn(saleDTO1);
        when(saleMapper.toDTO(sale2)).thenReturn(saleDTO2);

        // Act
        List<SaleDTO> result = saleService.getSalesByHarvestId(1L);

        // Assert
        assertNotNull(result);
        assertEquals(2, result.size());
        verify(saleRepository).findByHarvestId(1L);
    }

    @Test
    void testGetSalesByHarvestId_noSalesFound() {
        // Arrange
        when(saleRepository.findByHarvestId(1L)).thenReturn(Arrays.asList());

        // Act & Assert
        assertThrows(ResourceNotFoundException.class, () -> saleService.getSalesByHarvestId(1L));
    }
}
