package com.citron.citronix.repository;

import com.citron.citronix.entity.HarvestDetail;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface HarvestDetailRepository extends JpaRepository<HarvestDetail, Long> {



}
