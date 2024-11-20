package com.citron.citronix.repository;

import com.citron.citronix.entity.HarvestDetail;
import com.citron.citronix.entity.Season;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface HarvestDetailRepository extends JpaRepository<HarvestDetail, Long> {
    List<HarvestDetail> findByTreeId(Long treeId);
    List<HarvestDetail> findByHarvestId(Long harvestId);

    @Query("""
        SELECT COUNT(hd) > 0
        FROM HarvestDetail hd
        WHERE hd.tree.id = :treeId
        AND hd.harvest.season = :season
        AND YEAR(hd.harvest.date) = :year
    """)
    boolean isTreeAlreadyHarvested(@Param("treeId") Long treeId, @Param("season") Season season, @Param("year") int year);

}
