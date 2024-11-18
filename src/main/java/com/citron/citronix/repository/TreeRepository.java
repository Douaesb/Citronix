package com.citron.citronix.repository;

import com.citron.citronix.entity.Tree;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface TreeRepository extends JpaRepository<Tree, Long> {

}
