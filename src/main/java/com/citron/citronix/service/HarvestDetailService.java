package com.citron.citronix.service;

import com.citron.citronix.dto.HarvestDetailDTO;

import java.util.List;

public interface HarvestDetailService {

    HarvestDetailDTO createHarvestDetail(HarvestDetailDTO harvestDetailDTO);
    HarvestDetailDTO getHarvestDetailById(Long id);
    HarvestDetailDTO updateHarvestDetail(Long id, HarvestDetailDTO harvestDetailDTO);
    void deleteHarvestDetail(Long id);
    List<HarvestDetailDTO> getAllHarvestDetails();
    List<HarvestDetailDTO> getHarvestDetailsByTreeId(Long treeId);
    List<HarvestDetailDTO> getHarvestDetailsByHarvestId(Long harvestId);
}
