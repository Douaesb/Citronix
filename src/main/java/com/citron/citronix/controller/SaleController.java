package com.citron.citronix.controller;

import com.citron.citronix.dto.SaleDTO;
import com.citron.citronix.service.SaleService;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@Validated
@RequestMapping("/api/sales")
public class SaleController {

    private final SaleService saleService;

    @Autowired
    public SaleController(SaleService saleService) {
        this.saleService = saleService;
    }

    @ApiOperation(value = "Create a new sale", response = SaleDTO.class)
    @PostMapping
    public SaleDTO createSale(
            @ApiParam(value = "Sale data to be created", required = true)
            @Valid @RequestBody SaleDTO saleDTO) {
        return saleService.createSale(saleDTO);
    }

    @ApiOperation(value = "Get a sale by ID", response = SaleDTO.class)
    @GetMapping("/{id}")
    public SaleDTO getSaleById(
            @ApiParam(value = "ID of the sale to be retrieved", required = true)
            @PathVariable("id") Long id) {
        return saleService.getSaleById(id);
    }

    @ApiOperation(value = "Update an existing sale", response = SaleDTO.class)
    @PutMapping("/{id}")
    public SaleDTO updateSale(
            @ApiParam(value = "ID of the sale to be updated", required = true)
            @PathVariable("id") Long id,
            @ApiParam(value = "Updated sale data", required = true)
            @RequestBody SaleDTO saleDTO) {
        return saleService.updateSale(id, saleDTO);
    }

    @ApiOperation(value = "Delete a sale by ID")
    @DeleteMapping("/{id}")
    public void deleteSale(
            @ApiParam(value = "ID of the sale to be deleted", required = true)
            @PathVariable("id") Long id) {
        saleService.deleteSale(id);
    }

    @ApiOperation(value = "Get all sales", response = List.class)
    @GetMapping
    public List<SaleDTO> getAllSales() {
        return saleService.getAllSales();
    }

    @ApiOperation(value = "Get all sales by harvest ID", response = List.class)
    @GetMapping("/harvest/{harvestId}")
    public List<SaleDTO> getSalesByHarvestId(
            @ApiParam(value = "ID of the harvest", required = true)
            @PathVariable("harvestId") Long harvestId) {
        return saleService.getSalesByHarvestId(harvestId);
    }
}
