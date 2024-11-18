package com.citron.citronix.controller;

import com.citron.citronix.dto.HarvestDetailDTO;
import com.citron.citronix.service.HarvestDetailService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/harvest-details")
@Validated
@Api(value = "Harvest Detail Management", tags = {"Harvest Details"})
public class HarvestDetailController {

    private final HarvestDetailService harvestDetailService;

    @Autowired
    public HarvestDetailController(HarvestDetailService harvestDetailService) {
        this.harvestDetailService = harvestDetailService;
    }

    @ApiOperation(value = "Create a new Harvest Detail", response = HarvestDetailDTO.class)
    @PostMapping
    public ResponseEntity<HarvestDetailDTO> createHarvestDetail(
            @ApiParam(value = "HarvestDetailDTO object to be created", required = true)
            @Valid @RequestBody HarvestDetailDTO harvestDetailDTO) {
        HarvestDetailDTO createdHarvestDetail = harvestDetailService.createHarvestDetail(harvestDetailDTO);
        return ResponseEntity.ok(createdHarvestDetail);
    }

    @ApiOperation(value = "Get Harvest Detail by ID", response = HarvestDetailDTO.class)
    @GetMapping("/{id}")
    public ResponseEntity<HarvestDetailDTO> getHarvestDetailById(
            @ApiParam(value = "ID of the HarvestDetail", required = true)
            @PathVariable("id") Long id) {
        HarvestDetailDTO harvestDetailDTO = harvestDetailService.getHarvestDetailById(id);
        return ResponseEntity.ok(harvestDetailDTO);
    }

    @ApiOperation(value = "Update an existing Harvest Detail", response = HarvestDetailDTO.class)
    @PutMapping("/{id}")
    public ResponseEntity<HarvestDetailDTO> updateHarvestDetail(
            @ApiParam(value = "ID of the HarvestDetail", required = true)
            @PathVariable("id") Long id,
            @ApiParam(value = "HarvestDetailDTO object with updated details", required = true)
            @RequestBody HarvestDetailDTO harvestDetailDTO) {
        HarvestDetailDTO updatedHarvestDetail = harvestDetailService.updateHarvestDetail(id, harvestDetailDTO);
        return ResponseEntity.ok(updatedHarvestDetail);
    }

    @ApiOperation(value = "Delete a Harvest Detail by ID")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteHarvestDetail(
            @ApiParam(value = "ID of the HarvestDetail", required = true)
            @PathVariable("id") Long id) {
        harvestDetailService.deleteHarvestDetail(id);
        return ResponseEntity.noContent().build();
    }

    @ApiOperation(value = "Get all Harvest Details", response = List.class)
    @GetMapping
    public ResponseEntity<List<HarvestDetailDTO>> getAllHarvestDetails() {
        List<HarvestDetailDTO> harvestDetails = harvestDetailService.getAllHarvestDetails();
        return ResponseEntity.ok(harvestDetails);
    }

    @ApiOperation(value = "Get Harvest Details by Tree ID", response = List.class)
    @GetMapping("/tree/{treeId}")
    public ResponseEntity<List<HarvestDetailDTO>> getHarvestDetailsByTreeId(
            @ApiParam(value = "ID of the Tree", required = true)
            @PathVariable("treeId") Long treeId) {
        List<HarvestDetailDTO> harvestDetails = harvestDetailService.getHarvestDetailsByTreeId(treeId);
        return ResponseEntity.ok(harvestDetails);
    }

    @ApiOperation(value = "Get Harvest Details by Harvest ID", response = List.class)
    @GetMapping("/harvest/{harvestId}")
    public ResponseEntity<List<HarvestDetailDTO>> getHarvestDetailsByHarvestId(
            @ApiParam(value = "ID of the Harvest", required = true)
            @PathVariable("harvestId") Long harvestId) {
        List<HarvestDetailDTO> harvestDetails = harvestDetailService.getHarvestDetailsByHarvestId(harvestId);
        return ResponseEntity.ok(harvestDetails);
    }
}
