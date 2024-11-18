package com.citron.citronix.service.impl;

import com.citron.citronix.dto.TreeDTO;
import com.citron.citronix.entity.Field;
import com.citron.citronix.entity.Tree;
import com.citron.citronix.exception.ResourceNotFoundException;
import com.citron.citronix.mapper.TreeMapper;
import com.citron.citronix.repository.FieldRepository;
import com.citron.citronix.repository.TreeRepository;
import com.citron.citronix.service.TreeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class TreeServiceImpl implements TreeService {

    private final TreeRepository treeRepository;
    private final FieldRepository fieldRepository;
    private final TreeMapper treeMapper;

    @Autowired
    public TreeServiceImpl(TreeRepository treeRepository, FieldRepository fieldRepository, TreeMapper treeMapper) {
        this.treeRepository = treeRepository;
        this.fieldRepository = fieldRepository;
        this.treeMapper = treeMapper;
    }

    @Override
    public TreeDTO createTree(TreeDTO treeDTO) {
        Field field = fieldRepository.findById(treeDTO.getFieldId())
                .orElseThrow(() -> new ResourceNotFoundException("Field not found with id: " + treeDTO.getFieldId()));

        Tree tree = treeMapper.toEntity(treeDTO);
        tree.setField(field);
        Tree savedTree = treeRepository.save(tree);
        return treeMapper.toDTO(savedTree);
    }

    @Override
    public TreeDTO getTreeById(Long id) {
        Tree tree = treeRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Tree not found with id: " + id));
        return treeMapper.toDTO(tree);
    }

    @Override
    public TreeDTO updateTree(Long id, TreeDTO treeDTO) {
        Tree existingTree = treeRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Tree not found with id: " + id));

        Field field = fieldRepository.findById(treeDTO.getFieldId())
                .orElseThrow(() -> new ResourceNotFoundException("Field not found with id: " + treeDTO.getFieldId()));

        existingTree.setPlantationDate(treeDTO.getPlantationDate());
        existingTree.setField(field);

        Tree updatedTree = treeRepository.save(existingTree);
        return treeMapper.toDTO(updatedTree);
    }

    @Override
    public void deleteTree(Long id) {
        Tree tree = treeRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Tree not found with id: " + id));
        treeRepository.delete(tree);
    }

    @Override
    public List<TreeDTO> getAllTrees() {
        return treeRepository.findAll()
                .stream()
                .map(treeMapper::toDTO)
                .collect(Collectors.toList());
    }

    @Override
    public List<TreeDTO> getTreesByFieldId(Long fieldId) {
        return treeRepository.findByFieldId(fieldId)
                .stream()
                .map(treeMapper::toDTO)
                .collect(Collectors.toList());
    }
}
