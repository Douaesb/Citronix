package com.citron.citronix.specification;

import com.citron.citronix.dto.FarmSearchDTO;
import com.citron.citronix.entity.Farm;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class FarmSpecification {

    public static Specification<Farm> search(FarmSearchDTO criteria) {
        return (Root<Farm> root, CriteriaQuery<?> query, CriteriaBuilder cb) -> {
            List<Predicate> predicates = new ArrayList<>();

            if (criteria.getName() != null) {
                predicates.add(cb.like(cb.lower(root.get("name")), "%" + criteria.getName().toLowerCase() + "%"));
            }
            if (criteria.getLocation() != null) {
                predicates.add(cb.like(cb.lower(root.get("location")), "%" + criteria.getLocation().toLowerCase() + "%"));
            }
            if (criteria.getMinArea() != null) {
                predicates.add(cb.greaterThanOrEqualTo(root.get("area"), criteria.getMinArea()));
            }
            if (criteria.getMaxArea() != null) {
                predicates.add(cb.lessThanOrEqualTo(root.get("area"), criteria.getMaxArea()));
            }
            if (criteria.getStartDate() != null) {
                predicates.add(cb.greaterThanOrEqualTo(root.get("creationDate"), criteria.getStartDate()));
            }
            if (criteria.getEndDate() != null) {
                predicates.add(cb.lessThanOrEqualTo(root.get("creationDate"), criteria.getEndDate()));
            }

            return cb.and(predicates.toArray(new Predicate[0]));
        };
    }
}
