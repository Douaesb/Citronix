package com.citron.citronix.service.impl;

import com.citron.citronix.dto.SaleDTO;
import com.citron.citronix.entity.Sale;
import com.citron.citronix.exception.ResourceNotFoundException;
import com.citron.citronix.mapper.SaleMapper;
import com.citron.citronix.repository.SaleRepository;
import com.citron.citronix.service.SaleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class SaleServiceImpl implements SaleService {

    private final SaleRepository saleRepository;
    private final SaleMapper saleMapper;

    @Autowired
    public SaleServiceImpl(SaleRepository saleRepository, SaleMapper saleMapper) {
        this.saleRepository = saleRepository;
        this.saleMapper = saleMapper;
    }

    @Override
    public SaleDTO createSale(SaleDTO saleDTO) {
        Sale sale = saleMapper.toEntity(saleDTO);
        Sale savedSale = saleRepository.save(sale);
        return saleMapper.toDTO(savedSale);
    }

    @Override
    public SaleDTO getSaleById(Long id) {
        Sale sale = saleRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Sale not found with id: " + id));
        return saleMapper.toDTO(sale);
    }

    @Override
    public SaleDTO updateSale(Long id, SaleDTO saleDTO) {
        Sale existingSale = saleRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Sale not found with id: " + id));

        existingSale.setDate(saleDTO.getDate());
        existingSale.setUnitPrice(saleDTO.getUnitPrice());
        existingSale.setQuantity(saleDTO.getQuantity());
        existingSale.setRevenue(saleDTO.getRevenue());
        existingSale.setClient(saleDTO.getClient());

        Sale updatedSale = saleRepository.save(existingSale);
        return saleMapper.toDTO(updatedSale);
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
                .collect(Collectors.toList());
    }
}
