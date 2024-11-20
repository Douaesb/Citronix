package com.citron.citronix.service;

import com.citron.citronix.dto.HarvestDTO;

import java.util.List;

public interface HarvestService {
    HarvestDTO createHarvest(HarvestDTO harvestDTO);

    HarvestDTO getHarvestById(Long id);

    HarvestDTO updateHarvest(Long id, HarvestDTO harvestDTO);

    void deleteHarvest(Long id);

    List<HarvestDTO> getAllHarvests();

}
