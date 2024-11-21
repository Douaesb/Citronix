package com.citron.citronix.controller;

import com.citron.citronix.dto.FarmDTO;
import com.citron.citronix.dto.FarmSearchDTO;
import com.citron.citronix.entity.Farm;
import com.citron.citronix.service.FarmService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@Validated
@RequestMapping("/api/farms")
public class FarmController {

    private final FarmService farmService;

    @Autowired
    public FarmController(FarmService farmService) {
        this.farmService = farmService;
    }

    @Operation(summary = "Create a new farm", description = "This endpoint allows you to create a new farm in the system.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Farm created successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid input")
    })
    @PostMapping
    public ResponseEntity<FarmDTO> createFarm(@Valid @RequestBody FarmDTO farmDTO) {
        FarmDTO createdFarm = farmService.createFarm(farmDTO);
        return new ResponseEntity<>(createdFarm, HttpStatus.CREATED);
    }

    @Operation(summary = "Get a farm by ID", description = "This endpoint returns the details of a farm identified by its ID.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Farm found"),
            @ApiResponse(responseCode = "404", description = "Farm not found")
    })
    @GetMapping("/{id}")
    public ResponseEntity<FarmDTO> getFarmById(@Parameter(description = "ID of the farm to be fetched") @PathVariable Long id) {
        FarmDTO farmDTO = farmService.getFarmById(id);
        return new ResponseEntity<>(farmDTO, HttpStatus.OK);
    }

    @Operation(summary = "Update an existing farm", description = "This endpoint allows you to update an existing farm identified by its ID.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Farm updated successfully"),
            @ApiResponse(responseCode = "404", description = "Farm not found")
    })
    @PutMapping("/{id}")
    public ResponseEntity<FarmDTO> updateFarm(@Parameter(description = "ID of the farm to be updated") @PathVariable Long id,
                                              @RequestBody FarmDTO farmDTO) {
        FarmDTO updatedFarm = farmService.updateFarm(id, farmDTO);
        return new ResponseEntity<>(updatedFarm, HttpStatus.OK);
    }

    @Operation(summary = "Delete a farm", description = "This endpoint allows you to delete a farm identified by its ID.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Farm deleted successfully"),
            @ApiResponse(responseCode = "404", description = "Farm not found")
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteFarm(@Parameter(description = "ID of the farm to be deleted") @PathVariable Long id) {
        farmService.deleteFarm(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @Operation(summary = "Get all farms", description = "This endpoint returns a list of all farms in the system.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "List of farms found")
    })
    @GetMapping
    public ResponseEntity<List<FarmDTO>> getAllFarms() {
        List<FarmDTO> farms = farmService.getAllFarms();
        return new ResponseEntity<>(farms, HttpStatus.OK);
    }

    @Operation(summary = "Search farms by multiple criteria", description = "This endpoint allows you to search farms based on multiple criteria.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Farms found"),
            @ApiResponse(responseCode = "400", description = "Invalid search criteria")
    })
    @PostMapping("/search")
    public ResponseEntity<List<FarmDTO>> searchFarms(@RequestBody FarmSearchDTO criteria) {
        List<FarmDTO> farmDTOs = farmService.searchFarms(criteria);
        return ResponseEntity.ok(farmDTOs);
    }
}
