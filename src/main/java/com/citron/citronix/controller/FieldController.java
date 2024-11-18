package com.citron.citronix.controller;

import com.citron.citronix.dto.FieldDTO;
import com.citron.citronix.service.FieldService;
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
@RequestMapping("/api/fields")
public class FieldController {

    private final FieldService fieldService;

    @Autowired
    public FieldController(FieldService fieldService) {
        this.fieldService = fieldService;
    }

    @Operation(summary = "Create a new field", description = "This endpoint allows you to create a new field in the system.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Field created successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid input")
    })
    @PostMapping
    public ResponseEntity<FieldDTO> createField(@Valid @RequestBody FieldDTO fieldDTO) {
        FieldDTO createdField = fieldService.createField(fieldDTO);
        return new ResponseEntity<>(createdField, HttpStatus.CREATED);
    }

    @Operation(summary = "Get a field by ID", description = "This endpoint returns the details of a field identified by its ID.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Field found"),
            @ApiResponse(responseCode = "404", description = "Field not found")
    })
    @GetMapping("/{id}")
    public ResponseEntity<FieldDTO> getFieldById(@Parameter(description = "ID of the field to be fetched") @PathVariable Long id) {
        FieldDTO fieldDTO = fieldService.getFieldById(id);
        return new ResponseEntity<>(fieldDTO, HttpStatus.OK);
    }

    @Operation(summary = "Update an existing field", description = "This endpoint allows you to update an existing field identified by its ID.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Field updated successfully"),
            @ApiResponse(responseCode = "404", description = "Field not found")
    })
    @PutMapping("/{id}")
    public ResponseEntity<FieldDTO> updateField(@Parameter(description = "ID of the field to be updated") @PathVariable Long id,
                                                @RequestBody FieldDTO fieldDTO) {
        FieldDTO updatedField = fieldService.updateField(id, fieldDTO);
        return new ResponseEntity<>(updatedField, HttpStatus.OK);
    }

    @Operation(summary = "Delete a field", description = "This endpoint allows you to delete a field identified by its ID.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Field deleted successfully"),
            @ApiResponse(responseCode = "404", description = "Field not found")
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteField(@Parameter(description = "ID of the field to be deleted") @PathVariable Long id) {
        fieldService.deleteField(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @Operation(summary = "Get all fields", description = "This endpoint returns a list of all fields in the system.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "List of fields found")
    })
    @GetMapping
    public ResponseEntity<List<FieldDTO>> getAllFields() {
        List<FieldDTO> fields = fieldService.getAllFields();
        return new ResponseEntity<>(fields, HttpStatus.OK);
    }

    @Operation(summary = "Get fields by Farm ID", description = "This endpoint returns a list of fields associated with a specific farm.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "List of fields found"),
            @ApiResponse(responseCode = "404", description = "Farm not found")
    })
    @GetMapping("/farm/{farmId}")
    public ResponseEntity<List<FieldDTO>> getFieldsByFarmId(@Parameter(description = "ID of the farm to fetch fields for") @PathVariable Long farmId) {
        List<FieldDTO> fields = fieldService.getFieldsByFarmId(farmId);
        return new ResponseEntity<>(fields, HttpStatus.OK);
    }
}
