package com.citron.citronix.service;

import com.citron.citronix.dto.FarmDTO;

import java.util.List;

public interface FarmService {

    FarmDTO createFarm(FarmDTO farmDTO);

    FarmDTO getFarmById(Long id);

    FarmDTO updateFarm(Long id, FarmDTO farmDTO);

    void deleteFarm(Long id);

    List<FarmDTO> getAllFarms();
}
