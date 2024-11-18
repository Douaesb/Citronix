package com.citron.citronix.repository;

import com.citron.citronix.entity.Field;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;



@Repository
public interface FieldRepository extends JpaRepository<Field, Long> {

}
