package com.citron.citronix.service.impl;

import com.citron.citronix.dto.HarvestDTO;
import com.citron.citronix.entity.Harvest;
import com.citron.citronix.entity.HarvestDetail;
import com.citron.citronix.entity.Season;
import com.citron.citronix.exception.ResourceNotFoundException;
import com.citron.citronix.mapper.HarvestMapper;
import com.citron.citronix.repository.HarvestRepository;
import com.citron.citronix.service.HarvestService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class HarvestServiceImpl implements HarvestService {

    private final HarvestRepository harvestRepository;
    private final HarvestMapper harvestMapper;

    @Autowired
    public HarvestServiceImpl(HarvestRepository harvestRepository, HarvestMapper harvestMapper) {
        this.harvestRepository = harvestRepository;
        this.harvestMapper = harvestMapper;
    }

    @Override
    public HarvestDTO createHarvest(HarvestDTO harvestDTO) {
        Season season = determineSeason(harvestDTO.getDate());
        harvestDTO.setSeason(season);

        Harvest harvest = harvestMapper.toEntity(harvestDTO);
        harvest.setQuantity(0.0);
        Harvest savedHarvest = harvestRepository.save(harvest);
        return harvestMapper.toDTO(savedHarvest);
    }

    @Override
    public HarvestDTO getHarvestById(Long id) {
        Harvest harvest = harvestRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Harvest not found with id: " + id));
        return harvestMapper.toDTO(harvest);
    }

    @Override
    public HarvestDTO updateHarvest(Long id, HarvestDTO harvestDTO) {
        Harvest existingHarvest = harvestRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Harvest not found with id: " + id));
        Season season = determineSeason(harvestDTO.getDate());
        existingHarvest.setSeason(season);
        existingHarvest.setDate(harvestDTO.getDate());
        double totalQuantity = existingHarvest.getHarvestDetails().stream()
                .mapToDouble(HarvestDetail::getQuantity)
                .sum();
        existingHarvest.setQuantity(totalQuantity);

        Harvest updatedHarvest = harvestRepository.save(existingHarvest);
        return harvestMapper.toDTO(updatedHarvest);
    }

    @Override
    public void deleteHarvest(Long id) {
        Harvest harvest = harvestRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Harvest not found with id: " + id));
        harvestRepository.delete(harvest);
    }

    @Override
    public List<HarvestDTO> getAllHarvests() {
        return harvestRepository.findAll()
                .stream()
                .map(harvestMapper::toDTO)
                .collect(Collectors.toList());
    }

    public Season determineSeason(LocalDate date) {
        int month = date.getMonthValue();
        if (month >= 3 && month <= 5) {
            return Season.SPRING;
        } else if (month >= 6 && month <= 8) {
            return Season.SUMMER;
        } else if (month >= 9 && month <= 11) {
            return Season.FALL;
        } else {
            return Season.WINTER;
        }
    }


}
