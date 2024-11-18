package com.citron.citronix.service;

import com.citron.citronix.dto.TreeDTO;

import java.util.List;

public interface TreeService {

    TreeDTO createTree(TreeDTO treeDTO);
    TreeDTO getTreeById(Long id);
    TreeDTO updateTree(Long id, TreeDTO treeDTO);
    void deleteTree(Long id);
    List<TreeDTO> getAllTrees();
    List<TreeDTO> getTreesByFieldId(Long fieldId);
}
