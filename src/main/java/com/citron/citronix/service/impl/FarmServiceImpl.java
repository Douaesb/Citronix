package com.citron.citronix.service.impl;

import com.citron.citronix.dto.FarmDTO;
import com.citron.citronix.dto.FarmSearchDTO;
import com.citron.citronix.entity.Farm;
import com.citron.citronix.exception.ResourceNotFoundException;
import com.citron.citronix.mapper.FarmMapper;
import com.citron.citronix.repository.FarmRepository;
import com.citron.citronix.service.FarmService;
import com.citron.citronix.specification.FarmSpecification;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class FarmServiceImpl implements FarmService {

    private final FarmRepository farmRepository;
    private final FarmMapper farmMapper;

    @Autowired
    private FarmSpecification farmSpecification;

    @Autowired
    public FarmServiceImpl(FarmRepository farmRepository, FarmMapper farmMapper) {
        this.farmRepository = farmRepository;
        this.farmMapper = farmMapper;
    }

    @Override
    public FarmDTO createFarm(FarmDTO farmDTO) {
        Farm farm = farmMapper.toEntity(farmDTO);
        Farm savedFarm = farmRepository.save(farm);
        return farmMapper.toDTO(savedFarm);
    }

    @Override
    public FarmDTO getFarmById(Long id) {
        Farm farm = farmRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Farm not found with id: " + id));
        return farmMapper.toDTO(farm);
    }

    @Override
    public FarmDTO updateFarm(Long id, FarmDTO farmDTO) {
        Farm existingFarm = farmRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Farm not found with id: " + id));

        existingFarm.setName(farmDTO.getName());
        existingFarm.setLocation(farmDTO.getLocation());
        existingFarm.setArea(farmDTO.getArea());
        existingFarm.setCreationDate(farmDTO.getCreationDate());

        Farm updatedFarm = farmRepository.save(existingFarm);
        return farmMapper.toDTO(updatedFarm);
    }

    @Override
    public void deleteFarm(Long id) {
        Farm farm = farmRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Farm not found with id: " + id));
        farmRepository.delete(farm);
    }

    @Override
    public List<FarmDTO> getAllFarms() {
        return farmRepository.findAll()
                .stream()
                .map(farmMapper::toDTO)
                .collect(Collectors.toList());
    }

    public List<FarmDTO> searchFarms(FarmSearchDTO criteria) {
        List<Farm> farms = farmRepository.findAll(farmSpecification.search(criteria));

        return farms.stream()
                .map(farmMapper::toDTO)
                .collect(Collectors.toList());
    }
}
