package com.example.socks_warehouse.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.data.jpa.domain.Specification;

import com.example.socks_warehouse.model.Sock;

import jakarta.persistence.criteria.*;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class SockSpecification implements Specification<Sock>{

    private final Filter filter;
    private final CriteriaBuilder builder;
    private final CriteriaQuery<Long> query;
    private final Root<Sock> root;  

    @Override
    public Predicate toPredicate(Root<Sock> root, CriteriaQuery<?> query, CriteriaBuilder builder) {
        List<Predicate> predicates = new ArrayList<>();
        String color = filter.getColor();
        Integer cottonPart = filter.getCottonPart();
        Integer minCottonPart = filter.getMinCottonPart();
        Integer maxCottonPart = filter.getMaxCottonPart();
        if(color != null) 
            predicates.add(colorCriteria(color));
        if (cottonPart != null)
            predicates.add(cottonPartCriteria(cottonPart));
        if (minCottonPart != null || maxCottonPart != null)
            predicates.add(cottonPartRangeCriteria(minCottonPart, maxCottonPart));
        return builder.and(predicates.toArray(Predicate[]::new));
    }

    public CriteriaQuery<Long> toSumQuery() {
        query.select(builder.sum(root.get("quantity")));
        query.where(toPredicate(root, query, builder));
        return query;
    }

    private Predicate colorCriteria(String color){
        return builder.equal(root.get("color"), color);
    }

    private Predicate cottonPartCriteria(Integer cottonPart){
        return builder.equal(root.get("cotton_part"), cottonPart);
    }

    private Predicate cottonPartRangeCriteria(Integer minCottonPart, Integer maxCottonPart) {
        if (minCottonPart != null && maxCottonPart != null)
            return builder.between(root.get("cotton_part"), minCottonPart, maxCottonPart);
        else if (minCottonPart != null && maxCottonPart == null)
            return builder.greaterThanOrEqualTo(root.get("cotton_part"), minCottonPart);
        else return builder.lessThanOrEqualTo(root.get("cotton_part"), maxCottonPart);
    }
}
