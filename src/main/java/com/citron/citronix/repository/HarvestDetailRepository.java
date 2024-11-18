package com.citron.citronix.repository;

import com.citron.citronix.entity.HarvestDetail;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface HarvestDetailRepository extends JpaRepository<HarvestDetail, Long> {
    List<HarvestDetail> findByTreeId(Long treeId);
    List<HarvestDetail> findByHarvestId(Long harvestId);


}
