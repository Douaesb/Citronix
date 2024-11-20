package com.citron.citronix.service;

import com.citron.citronix.dto.SaleDTO;

import java.util.List;

public interface SaleService {

    SaleDTO createSale(SaleDTO saleDTO);
    SaleDTO getSaleById(Long id);
    SaleDTO updateSale(Long id, SaleDTO saleDTO);
    void deleteSale(Long id);
    List<SaleDTO> getAllSales();
    List<SaleDTO> getSalesByHarvestId(Long harvestId);
}
