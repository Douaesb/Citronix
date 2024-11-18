package com.citron.citronix.controller;

import com.citron.citronix.dto.HarvestDTO;
import com.citron.citronix.service.HarvestService;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@Validated
@RequestMapping("/api/harvests")
public class HarvestController {

    private final HarvestService harvestService;

    @Autowired
    public HarvestController(HarvestService harvestService) {
        this.harvestService = harvestService;
    }

    @ApiOperation(value = "Create a new harvest", response = HarvestDTO.class)
    @PostMapping
    public HarvestDTO createHarvest(
            @ApiParam(value = "Harvest data to be created", required = true)
            @Valid @RequestBody HarvestDTO harvestDTO) {
        return harvestService.createHarvest(harvestDTO);
    }

    @ApiOperation(value = "Get a harvest by ID", response = HarvestDTO.class)
    @GetMapping("/{id}")
    public HarvestDTO getHarvestById(
            @ApiParam(value = "ID of the harvest to be retrieved", required = true)
            @PathVariable("id") Long id) {
        return harvestService.getHarvestById(id);
    }

    @ApiOperation(value = "Update an existing harvest", response = HarvestDTO.class)
    @PutMapping("/{id}")
    public HarvestDTO updateHarvest(
            @ApiParam(value = "ID of the harvest to be updated", required = true)
            @PathVariable("id") Long id,
            @ApiParam(value = "Updated harvest data", required = true)
            @RequestBody HarvestDTO harvestDTO) {
        return harvestService.updateHarvest(id, harvestDTO);
    }

    @ApiOperation(value = "Delete a harvest by ID")
    @DeleteMapping("/{id}")
    public void deleteHarvest(
            @ApiParam(value = "ID of the harvest to be deleted", required = true)
            @PathVariable("id") Long id) {
        harvestService.deleteHarvest(id);
    }

    @ApiOperation(value = "Get all harvests", response = List.class)
    @GetMapping
    public List<HarvestDTO> getAllHarvests() {
        return harvestService.getAllHarvests();
    }

    @ApiOperation(value = "Get all harvests by sale ID", response = List.class)
    @GetMapping("/sale/{saleId}")
    public List<HarvestDTO> getHarvestsBySaleId(
            @ApiParam(value = "ID of the sale", required = true)
            @PathVariable("saleId") Long saleId) {
        return harvestService.getHarvestsBySaleId(saleId);
    }
}
