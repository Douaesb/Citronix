package com.citron.citronix.service.impl;

import com.citron.citronix.dto.SaleDTO;
import com.citron.citronix.entity.Sale;
import com.citron.citronix.entity.Harvest;
import com.citron.citronix.exception.ResourceNotFoundException;
import com.citron.citronix.mapper.SaleMapper;
import com.citron.citronix.repository.SaleRepository;
import com.citron.citronix.repository.HarvestRepository;
import com.citron.citronix.service.SaleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class SaleServiceImpl implements SaleService {

    private final SaleRepository saleRepository;
    private final HarvestRepository harvestRepository;  // Added HarvestRepository
    private final SaleMapper saleMapper;

    @Autowired
    public SaleServiceImpl(SaleRepository saleRepository, HarvestRepository harvestRepository, SaleMapper saleMapper) {
        this.saleRepository = saleRepository;
        this.harvestRepository = harvestRepository;
        this.saleMapper = saleMapper;
    }

    @Override
    public SaleDTO createSale(SaleDTO saleDTO) {
        // Fetch Harvest by harvestId
        Harvest harvest = harvestRepository.findById(saleDTO.getHarvestId())
                .orElseThrow(() -> new ResourceNotFoundException("Harvest not found with id: " + saleDTO.getHarvestId()));

        // Map SaleDTO to Sale entity
        Sale sale = saleMapper.toEntity(saleDTO);

        // Set the Harvest entity in the Sale
        sale.setHarvest(harvest);

        // Calculate revenue
        double revenue = saleDTO.getQuantity() * saleDTO.getUnitPrice();
        sale.setRevenue(revenue);

        // Save and return the SaleDTO
        Sale savedSale = saleRepository.save(sale);
        SaleDTO savedSaleDTO = saleMapper.toDTO(savedSale);

        savedSaleDTO.calculateRevenue();

        return savedSaleDTO;
    }

    @Override
    public SaleDTO getSaleById(Long id) {
        Sale sale = saleRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Sale not found with id: " + id));

        SaleDTO saleDTO = saleMapper.toDTO(sale);
        saleDTO.calculateRevenue();

        return saleDTO;
    }

    @Override
    public SaleDTO updateSale(Long id, SaleDTO saleDTO) {
        Sale existingSale = saleRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Sale not found with id: " + id));

        // Update the Sale fields
        existingSale.setDate(saleDTO.getDate());
        existingSale.setUnitPrice(saleDTO.getUnitPrice());
        existingSale.setQuantity(saleDTO.getQuantity());
        existingSale.setClient(saleDTO.getClient());

        // Fetch the Harvest using the provided harvestId
        Harvest harvest = harvestRepository.findById(saleDTO.getHarvestId())
                .orElseThrow(() -> new ResourceNotFoundException("Harvest not found with id: " + saleDTO.getHarvestId()));
        existingSale.setHarvest(harvest);

        // Recalculate revenue
        double revenue = saleDTO.getQuantity() * saleDTO.getUnitPrice();
        existingSale.setRevenue(revenue);

        Sale updatedSale = saleRepository.save(existingSale);
        SaleDTO updatedSaleDTO = saleMapper.toDTO(updatedSale);

        updatedSaleDTO.calculateRevenue();

        return updatedSaleDTO;
    }

    @Override
    public void deleteSale(Long id) {
        Sale sale = saleRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Sale not found with id: " + id));
        saleRepository.delete(sale);
    }

    @Override
    public List<SaleDTO> getAllSales() {
        return saleRepository.findAll()
                .stream()
                .map(saleMapper::toDTO)
                .map(saleDTO -> {
                    saleDTO.calculateRevenue();
                    return saleDTO;
                })
                .collect(Collectors.toList());
    }

    @Override
    public List<SaleDTO> getSalesByHarvestId(Long harvestId) {
        List<Sale> sales = saleRepository.findByHarvestId(harvestId);
        if (sales.isEmpty()) {
            throw new ResourceNotFoundException("No sales found for harvest with id: " + harvestId);
        }

        return sales.stream()
                .map(saleMapper::toDTO)
                .map(saleDTO -> {
                    saleDTO.calculateRevenue();
                    return saleDTO;
                })
                .collect(Collectors.toList());
    }
}
