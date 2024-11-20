package com.citron.citronix.service.impl;

import com.citron.citronix.dto.HarvestDetailDTO;
import com.citron.citronix.entity.Harvest;
import com.citron.citronix.entity.HarvestDetail;
import com.citron.citronix.entity.Season;
import com.citron.citronix.entity.Tree;
import com.citron.citronix.exception.ResourceNotFoundException;
import com.citron.citronix.mapper.HarvestDetailMapper;
import com.citron.citronix.repository.HarvestDetailRepository;
import com.citron.citronix.repository.HarvestRepository;
import com.citron.citronix.repository.TreeRepository;
import com.citron.citronix.service.HarvestDetailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class HarvestDetailServiceImpl implements HarvestDetailService {

    private final HarvestDetailRepository harvestDetailRepository;
    private final TreeRepository treeRepository;
    private final HarvestRepository harvestRepository;
    private final HarvestDetailMapper harvestDetailMapper;

    @Autowired
    public HarvestDetailServiceImpl(
            HarvestDetailRepository harvestDetailRepository,
            TreeRepository treeRepository,
            HarvestRepository harvestRepository,
            HarvestDetailMapper harvestDetailMapper) {
        this.harvestDetailRepository = harvestDetailRepository;
        this.treeRepository = treeRepository;
        this.harvestRepository = harvestRepository;
        this.harvestDetailMapper = harvestDetailMapper;
    }

    @Override
    public HarvestDetailDTO createHarvestDetail(HarvestDetailDTO harvestDetailDTO) {
        Tree tree = treeRepository.findById(harvestDetailDTO.getTreeId())
                .orElseThrow(() -> new ResourceNotFoundException("Tree not found with id: " + harvestDetailDTO.getTreeId()));

        Harvest harvest = harvestRepository.findById(harvestDetailDTO.getHarvestId())
                .orElseThrow(() -> new ResourceNotFoundException("Harvest not found with id: " + harvestDetailDTO.getHarvestId()));

        Season season = harvest.getSeason();
        int year = harvest.getDate().getYear();

        boolean isTreeHarvested = harvestDetailRepository.isTreeAlreadyHarvested(tree.getId(), season, year);
        if (isTreeHarvested) {
            throw new IllegalStateException("Tree with ID " + tree.getId() + " has already been harvested in " + season + " " + year);
        }

        HarvestDetail harvestDetail = harvestDetailMapper.toEntity(harvestDetailDTO);
        harvestDetail.setTree(tree);
        harvestDetail.setHarvest(harvest);

        HarvestDetail savedHarvestDetail = harvestDetailRepository.save(harvestDetail);

        harvest.updateQuantity();
        harvestRepository.save(harvest);

        return harvestDetailMapper.toDTO(savedHarvestDetail);
    }

    @Override
    public HarvestDetailDTO getHarvestDetailById(Long id) {
        HarvestDetail harvestDetail = harvestDetailRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("HarvestDetail not found with id: " + id));
        return harvestDetailMapper.toDTO(harvestDetail);
    }

    @Override
    public HarvestDetailDTO updateHarvestDetail(Long id, HarvestDetailDTO harvestDetailDTO) {
        HarvestDetail existingHarvestDetail = harvestDetailRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("HarvestDetail not found with id: " + id));

        Tree tree = treeRepository.findById(harvestDetailDTO.getTreeId())
                .orElseThrow(() -> new ResourceNotFoundException("Tree not found with id: " + harvestDetailDTO.getTreeId()));

        Harvest harvest = harvestRepository.findById(harvestDetailDTO.getHarvestId())
                .orElseThrow(() -> new ResourceNotFoundException("Harvest not found with id: " + harvestDetailDTO.getHarvestId()));


        existingHarvestDetail.setQuantity(harvestDetailDTO.getQuantity());
        existingHarvestDetail.setTree(tree);
        existingHarvestDetail.setHarvest(harvest);

        HarvestDetail updatedHarvestDetail = harvestDetailRepository.save(existingHarvestDetail);

        harvest.updateQuantity();
        harvestRepository.save(harvest);

        return harvestDetailMapper.toDTO(updatedHarvestDetail);
    }

    @Override
    public void deleteHarvestDetail(Long id) {
        HarvestDetail harvestDetail = harvestDetailRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("HarvestDetail not found with id: " + id));

        Harvest harvest = harvestDetail.getHarvest();

        harvestDetailRepository.delete(harvestDetail);

        harvest.updateQuantity();
        harvestRepository.save(harvest);
    }

    @Override
    public List<HarvestDetailDTO> getAllHarvestDetails() {
        return harvestDetailRepository.findAll()
                .stream()
                .map(harvestDetailMapper::toDTO)
                .collect(Collectors.toList());
    }

    @Override
    public List<HarvestDetailDTO> getHarvestDetailsByTreeId(Long treeId) {
        return harvestDetailRepository.findByTreeId(treeId)
                .stream()
                .map(harvestDetailMapper::toDTO)
                .collect(Collectors.toList());
    }

    @Override
    public List<HarvestDetailDTO> getHarvestDetailsByHarvestId(Long harvestId) {
        return harvestDetailRepository.findByHarvestId(harvestId)
                .stream()
                .map(harvestDetailMapper::toDTO)
                .collect(Collectors.toList());
    }
}
