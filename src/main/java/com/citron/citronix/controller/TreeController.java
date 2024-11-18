package com.citron.citronix.controller;

import com.citron.citronix.dto.TreeDTO;
import com.citron.citronix.service.TreeService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@Validated
@RequestMapping("/api/trees")
public class TreeController {

    private final TreeService treeService;

    @Autowired
    public TreeController(TreeService treeService) {
        this.treeService = treeService;
    }

    @ApiOperation(value = "Create a new tree", response = TreeDTO.class)
    @PostMapping
    public TreeDTO createTree(
            @ApiParam(value = "Tree data to be created", required = true)
            @Valid @RequestBody TreeDTO treeDTO) {
        return treeService.createTree(treeDTO);
    }

    @ApiOperation(value = "Get a tree by ID", response = TreeDTO.class)
    @GetMapping("/{id}")
    public TreeDTO getTreeById(
            @ApiParam(value = "ID of the tree to be retrieved", required = true)
            @PathVariable("id") Long id) {
        return treeService.getTreeById(id);
    }

    @ApiOperation(value = "Update an existing tree", response = TreeDTO.class)
    @PutMapping("/{id}")
    public TreeDTO updateTree(
            @ApiParam(value = "ID of the tree to be updated", required = true)
            @PathVariable("id") Long id,
            @ApiParam(value = "Updated tree data", required = true)
            @RequestBody TreeDTO treeDTO) {
        return treeService.updateTree(id, treeDTO);
    }

    @ApiOperation(value = "Delete a tree by ID")
    @DeleteMapping("/{id}")
    public void deleteTree(
            @ApiParam(value = "ID of the tree to be deleted", required = true)
            @PathVariable("id") Long id) {
        treeService.deleteTree(id);
    }

    @ApiOperation(value = "Get all trees", response = List.class)
    @GetMapping
    public List<TreeDTO> getAllTrees() {
        return treeService.getAllTrees();
    }

    @ApiOperation(value = "Get all trees by field ID", response = List.class)
    @GetMapping("/field/{fieldId}")
    public List<TreeDTO> getTreesByFieldId(
            @ApiParam(value = "ID of the field", required = true)
            @PathVariable("fieldId") Long fieldId) {
        return treeService.getTreesByFieldId(fieldId);
    }
}
