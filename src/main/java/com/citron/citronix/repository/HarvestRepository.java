package com.citron.citronix.repository;

import com.citron.citronix.entity.Harvest;
import com.citron.citronix.entity.Season;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;


@Repository
public interface HarvestRepository extends JpaRepository<Harvest, Long> {
    @Query("SELECT COUNT(h) > 0 " +
            "FROM Harvest h " +
            "JOIN h.harvestDetails hd " +
            "WHERE hd.tree.field.id = :fieldId " +
            "AND h.season = :season " +
            "AND YEAR(h.date) = :year")
    boolean isFieldAlreadyHarvested(@Param("fieldId") Long fieldId, @Param("season") Season season, @Param("year") int year);
}

