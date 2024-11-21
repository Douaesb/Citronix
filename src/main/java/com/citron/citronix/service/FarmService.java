package com.citron.citronix.service;

import com.citron.citronix.dto.FarmDTO;
import com.citron.citronix.dto.FarmSearchDTO;
import com.citron.citronix.entity.Farm;

import java.util.List;

public interface FarmService {

    FarmDTO createFarm(FarmDTO farmDTO);

    FarmDTO getFarmById(Long id);

    FarmDTO updateFarm(Long id, FarmDTO farmDTO);

    void deleteFarm(Long id);

    List<FarmDTO> getAllFarms();

    List<FarmDTO> searchFarms(FarmSearchDTO criteria);
}
