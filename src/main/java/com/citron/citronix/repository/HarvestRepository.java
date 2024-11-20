package com.citron.citronix.repository;

import com.citron.citronix.entity.Harvest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface HarvestRepository extends JpaRepository<Harvest, Long> {

}
